package com.muazwzxv.client.observer;

import com.muazwzxv.models.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class TestMoneyStreamObserver implements StreamObserver<Money> {

    private final CountDownLatch latch;

    public TestMoneyStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println("Received : RM " + money.getValue());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Completed");
        latch.countDown();
    }
}
