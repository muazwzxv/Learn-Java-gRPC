package com.muazwzxv.client.observer;

import com.muazwzxv.models.TransferResponse;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class TestTransferStreamResponse implements StreamObserver<TransferResponse> {

    private final CountDownLatch latch;

    public TestTransferStreamResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(TransferResponse transferResponse) {
        System.out.println("Status : " + transferResponse.getStatus());
        transferResponse.getAccountsList()
                .stream()
                .map(account -> account.getAccountNumber() + ":" + account.getAmount())
                .forEach(System.out::println);
        System.out.println("-----------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        this.latch.countDown();
    }
}
