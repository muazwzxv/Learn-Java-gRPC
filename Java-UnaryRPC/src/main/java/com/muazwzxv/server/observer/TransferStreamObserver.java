package com.muazwzxv.server.observer;

import com.muazwzxv.models.TransferRequest;
import com.muazwzxv.models.TransferResponse;
import io.grpc.stub.StreamObserver;

public class TransferStreamObserver implements StreamObserver<TransferRequest> {

    private final StreamObserver<TransferResponse> response;

    public TransferStreamObserver(StreamObserver<TransferResponse> response) {
        this.response = response;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
