package com.muazwzxv.client.loadBalancing;

import com.muazwzxv.client.observer.TestBalanceStreamObserver;
import com.muazwzxv.models.Balance;
import com.muazwzxv.models.BalanceCheckRequest;
import com.muazwzxv.models.BankServiceGrpc;
import com.muazwzxv.models.DepositRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NginxTestClient {
    private BankServiceGrpc.BankServiceBlockingStub bankServiceStub;
    private BankServiceGrpc.BankServiceStub asyncBankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8585)
                .usePlaintext()
                .build();
        this.bankServiceStub = BankServiceGrpc.newBlockingStub(channel);
        this.asyncBankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void balanceTest() {

        for (int i = 0; i < 100; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(ThreadLocalRandom.current().nextInt(1, 11))
                    .build();
            // This line is blocking
            Balance balance = this.bankServiceStub.getBalance(balanceCheckRequest);
            System.out.println("Received: RM " + balance.getAmount());
        }
    }

    @Test
    public void cashStreamingTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> depositStream = this.asyncBankServiceStub.deposit(new TestBalanceStreamObserver(latch));

        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest = DepositRequest.newBuilder().setAccountNumber(8).setAmount(10).build();
            depositStream.onNext(depositRequest);
        }

        depositStream.onCompleted();
        latch.await();
    }
}
