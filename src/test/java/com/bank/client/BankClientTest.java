package com.bank.client;

import com.bank.models.*;
import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    // async
    private BankServiceGrpc.BankServiceStub bankServiceStub;
    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
        bankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void balanceTest() {

        Balance balance = blockingStub.getBalance(
                BalanceCheckRequest.newBuilder().setAccountNumber(7).build()
        );

        System.out.println(balance);
    }

    @Test
    public void withdrawTest() {
        WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(40).build();
        blockingStub.withdraw(request).forEachRemaining(
                money -> System.out.println("Received : " + money.getValue())
        );
    }

    @Test
    public void withdrawAsyncTest() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(8).setAmount(40).build();
        bankServiceStub.withdraw(request, new MoneyStreamingResponse(latch));
        latch.await();
    }

    @Test
    public void cashStreamingRequest() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<DepositRequest> streamObserver = bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));

        for(int i = 0; i < 10; i++) {
            DepositRequest request = DepositRequest.newBuilder().setAmount(10).setAccountNumber(8).build();
            streamObserver.onNext(request);
        }

        streamObserver.onCompleted();

        latch.await();
    }


}
