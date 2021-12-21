package com.muazwzxv.client.observer;

import com.muazwzxv.models.Money;
import io.grpc.stub.StreamObserver;

public class MoneyStreamObserver implements StreamObserver<Money> {

    @Override
    public void onNext(Money money) {
        System.out.println("Received : RM " + money.getValue());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Completed");
    }
}
