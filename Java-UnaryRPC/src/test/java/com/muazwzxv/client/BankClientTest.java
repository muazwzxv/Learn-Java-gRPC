package com.muazwzxv.client;

import com.muazwzxv.models.Balance;
import com.muazwzxv.models.BalanceCheckRequest;
import com.muazwzxv.models.BankServiceGrpc;
import com.muazwzxv.models.WithdrawRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup() {

        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8000)
                .usePlaintext()
                .build();

        // Blocking stub provides a blocking line of code as it waits for the response
        this.blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
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
    public void withdrawalTest() {
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
}
