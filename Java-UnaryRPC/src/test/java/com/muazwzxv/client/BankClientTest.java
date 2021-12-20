package com.muazwzxv.client;

import com.google.protobuf.Int32Value;
import com.muazwzxv.models.Balance;
import com.muazwzxv.models.BalanceCheckRequest;
import com.muazwzxv.models.BankServiceGrpc;
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
                .setAccountNumber(Int32Value.newBuilder().setValue(3000).build())
                .build();
        // This line is blocking
        Balance balance = this.blockingStub.getBalance(balanceCheckRequest);
        System.out.println("Received: RM " + balance.getAmount());
    }
}
