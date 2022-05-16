package com.muazwzxv.client;


import com.muazwzxv.client.observer.TestTransferStreamResponse;
import com.muazwzxv.models.TransferRequest;
import com.muazwzxv.models.TransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {

    private TransferServiceGrpc.TransferServiceStub stub;

    @BeforeAll
    public void setup() {

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8000)
                .usePlaintext()
                .build();

        this.stub = TransferServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void transfer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        TestTransferStreamResponse res = new TestTransferStreamResponse(latch);

        // The server side observer
        StreamObserver<TransferRequest> requestStreamObserver = this.stub.transfer(res);

        for (int i = 0; i < 100; i++) {
            TransferRequest transferRequest = TransferRequest.newBuilder()
                    .setFromAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setToAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setAmount(10)
                    .build();
            requestStreamObserver.onNext(transferRequest);
        }
        requestStreamObserver.onCompleted();
        latch.await();
    }
}
