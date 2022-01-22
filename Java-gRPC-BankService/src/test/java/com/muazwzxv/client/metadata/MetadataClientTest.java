package com.muazwzxv.client.metadata;

import com.google.common.util.concurrent.Uninterruptibles;
import com.muazwzxv.client.observer.TestMoneyStreamObserver;
import com.muazwzxv.models.Balance;
import com.muazwzxv.models.BalanceCheckRequest;
import com.muazwzxv.models.BankServiceGrpc;
import com.muazwzxv.models.WithdrawRequest;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                // Interceptor added, all logics prior to request can be coded to an interceptor
//                .intercept(new DeadlineInterceptor())
                .intercept(MetadataUtils.newAttachHeadersInterceptor(ClientConstant.getClientToken()))
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

        try {
            // This line is blocking
            Balance balance = this.blockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .getBalance(balanceCheckRequest);
            System.out.println("Received: RM " + balance.getAmount());
        } catch (StatusRuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void withdrawalTestBlocking() {
        WithdrawRequest request = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(70).build();

        try {
            this.blockingStub
                    .withdraw(request)
                    .forEachRemaining(money -> {
                        System.out.println("Received: RM " + money.getValue());
                    });
        } catch (StatusRuntimeException e) {
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

        //Sleep the program to see the output of the test
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
    }
}
