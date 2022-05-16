package com.muazwzxv.client.observer;

import com.muazwzxv.client.metadata.ClientConstant;
import com.muazwzxv.models.TransferResponse;
import com.muazwzxv.models.WithdrawalError;
import io.grpc.Metadata;
import io.grpc.Status;
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

        /**
         * Fetch (Status | Metadata) from throwable
         */
        //Status status = Status.fromThrowable(throwable);
        //Metadata metadata = Status.trailersFromThrowable(throwable);

        Metadata metadata = Status.trailersFromThrowable(throwable);
        WithdrawalError error = metadata.get(ClientConstant.WITHDRAWAL_ERROR_KEY);

        System.out.println(error.getAmount() + " : " + error.getErrorMessage());
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        this.latch.countDown();
    }
}
