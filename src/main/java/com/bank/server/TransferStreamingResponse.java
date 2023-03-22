package com.bank.server;

import com.bank.models.Account;
import com.bank.models.TransferRequest;
import com.bank.models.TransferResponse;
import com.bank.models.TransferStatus;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class TransferStreamingResponse implements StreamObserver<TransferResponse> {

    private CountDownLatch latch;

    public TransferStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(TransferResponse transferResponse) {

        System.out.println("Status : " + transferResponse.getStatus());

        transferResponse.getAccountsList().forEach(System.out::println);
        System.out.println("-----------");

    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("All done!!");
        latch.countDown();
    }
}
