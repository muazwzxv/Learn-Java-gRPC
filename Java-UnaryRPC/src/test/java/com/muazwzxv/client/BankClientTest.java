package com.muazwzxv.client;

import com.muazwzxv.client.observer.TestBalanceStreamObserver;
import com.muazwzxv.client.observer.TestMoneyStreamObserver;
import com.muazwzxv.models.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8000)
                .usePlaintext()
                .build();

        // Blocking stub provides a blocking line of code as it waits for the response
        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        this.bankServiceStub = BankServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(2)
                .build();
        // This line is blocking
        Balance balance = this.blockingStub.getBalance(balanceCheckRequest);
        System.out.println("Received: RM " + balance.getAmount());
    }

    @Test
    public void withdrawalTestBlocking() {
        WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(70).build();

        try {
            this.blockingStub.withdraw(request)
                    .forEachRemaining(money -> {
                        System.out.println("Received: RM " + money.getValue());
                    });
        } catch (io.grpc.StatusRuntimeException e) {
            System.out.println("Withdraw Amount: RM " + request.getAmount());
            System.out.println(e.getStatus().getDescription());
        }
    }

    @Test
    public void withdrawalTestAsync() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(70).build();
        this.bankServiceStub.withdraw(request, new TestMoneyStreamObserver(latch));
        latch.await();

        // Sleep the program to see the output of the test
//        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
    }

    @Test
    public void cashStreamingTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> depositStream = this.bankServiceStub.deposit(new TestBalanceStreamObserver(latch));

        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest = DepositRequest.newBuilder().setAccountNumber(8).setAmount(10).build();
            depositStream.onNext(depositRequest);
        }

        depositStream.onCompleted();
        latch.await();
    }
}
