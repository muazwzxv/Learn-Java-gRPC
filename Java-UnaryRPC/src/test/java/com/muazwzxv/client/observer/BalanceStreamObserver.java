package com.muazwzxv.client.observer;

import com.muazwzxv.models.Balance;
import io.grpc.stub.StreamObserver;

public class BalanceStreamObserver implements StreamObserver<Balance> {

    @Override
    public void onNext(Balance balance) {
        System.out.println("Final Balance : RM " + balance.getAmount());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is done");

    }
}
