package com.bank.server;

import com.bank.models.Balance;
import com.bank.models.BalanceCheckRequest;
import com.bank.models.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {
    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder().setAmount(AccountDatabase.getBalance(accountNumber)).build();

        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }
}
