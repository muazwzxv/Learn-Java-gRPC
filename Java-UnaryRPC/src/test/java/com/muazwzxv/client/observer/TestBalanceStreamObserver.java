package com.muazwzxv.client.observer;

import com.muazwzxv.models.Balance;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class TestBalanceStreamObserver implements StreamObserver<Balance> {

    private final CountDownLatch latch;

    public TestBalanceStreamObserver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Balance balance) {
        System.out.println("Final Balance : RM " + balance.getAmount());
        this.latch.countDown();
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is done");
        this.latch.countDown();
    }
}
