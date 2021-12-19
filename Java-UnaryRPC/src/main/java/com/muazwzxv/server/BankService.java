package com.muazwzxv.server;

import com.google.protobuf.Int32Value;
import com.muazwzxv.models.Balance;
import com.muazwzxv.models.BalanceCheckRequest;
import com.muazwzxv.models.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        // Method return type returns void as we are
        // returning data using the StreamObserver.
        Int32Value accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(Int32Value.newBuilder().setValue(3000).build())
                .build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }
}
