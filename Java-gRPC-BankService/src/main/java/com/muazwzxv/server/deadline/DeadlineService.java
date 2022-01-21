package com.muazwzxv.server.deadline;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Int32Value;
import com.muazwzxv.models.*;
import com.muazwzxv.server.observer.CashDepositStreamObserver;
import com.muazwzxv.server.store.AccountDatabase;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class DeadlineService extends BankServiceGrpc.BankServiceImplBase {

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

        // Simulate time consuming call
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);

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

            Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);

            // Checks the current context whether channel
            // to a client is still open
            if (Context.current().isCancelled()) {
                responseObserver.onNext(money);
                System.out.println(
                        "Delivered 10 "
                );
                AccountDatabase.deductBalance(accountNumber, 10);
                continue;
            }
            break;
        }
        // Server completed the request
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> deposit(StreamObserver<Balance> responseObserver) {
        return new CashDepositStreamObserver(responseObserver);
    }
}
