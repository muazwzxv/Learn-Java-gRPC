package com.muazwzxv.server;

import com.google.protobuf.Int32Value;
import com.muazwzxv.models.*;
import com.muazwzxv.server.observer.CashDepositStreamObserver;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        // Method return type returns void as we are
        // returning data using the StreamObserver.
        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(Int32Value.newBuilder()
                        .setValue(AccountDatabase.getBalance(accountNumber))
                        .build())
                .build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        // We assume all amount request are multiple of 10
        // 10 - 20 - 30 - 40
        int amount = request.getAmount();
        int balance = AccountDatabase.getBalance(accountNumber);

        // Error handling gRPC
        if (amount > balance) {
            responseObserver.onError(Status.FAILED_PRECONDITION
                    .withDescription("Not Enough Money, you only have RM " + balance)
                    .asRuntimeException());
            return;
        }


        // Server will stream 10 dollar for each iteration of the loop
        for (int i = 0; i < (amount / 10); i++) {
            Money money = Money.newBuilder().setValue(10).build();
            responseObserver.onNext(money);
            AccountDatabase.deductBalance(accountNumber, 10);

            // wait 1 second after every loop
            // Just to demo how streaming works
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        // Server completed the request
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> deposit(StreamObserver<Balance> responseObserver) {
        return new CashDepositStreamObserver(responseObserver);
    }
}
