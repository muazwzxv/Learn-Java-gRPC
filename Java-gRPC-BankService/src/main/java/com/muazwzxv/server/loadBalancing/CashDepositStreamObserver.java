package com.muazwzxv.server.loadBalancing;

import com.google.protobuf.Int32Value;
import com.muazwzxv.models.Balance;
import com.muazwzxv.models.DepositRequest;
import com.muazwzxv.server.store.AccountDatabase;
import io.grpc.stub.StreamObserver;

public class CashDepositStreamObserver implements StreamObserver<DepositRequest> {

    private final StreamObserver<Balance> balanceStreamObserver;
    private int accountBalance;

    public CashDepositStreamObserver(StreamObserver<Balance> balance) {
        this.balanceStreamObserver = balance;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        int accountNumber = depositRequest.getAccountNumber();
        int amountToDeposit = depositRequest.getAmount();

        System.out.println(
                "Received cash deposit for " + accountNumber
        );
        this.accountBalance = AccountDatabase.addBalance(accountNumber, amountToDeposit);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        Balance balance = Balance.newBuilder()
                .setAmount(Int32Value.of(this.accountBalance))
                .build();

        balanceStreamObserver.onNext(balance);
        balanceStreamObserver.onCompleted();
    }
}
