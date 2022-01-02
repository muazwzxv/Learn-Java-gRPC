package com.muazwzxv.client.loadBalancing;

import com.muazwzxv.models.Balance;
import com.muazwzxv.models.BalanceCheckRequest;
import com.muazwzxv.models.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NginxTestClient {
    private BankServiceGrpc.BankServiceBlockingStub bankServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8585)
                .usePlaintext()
                .build();
        this.bankServiceStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void balanceTest() {

        for (int i = 1; i < 10; i++) {
            BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();
            // This line is blocking
            Balance balance = this.bankServiceStub.getBalance(balanceCheckRequest);
            System.out.println("Received: RM " + balance.getAmount());
        }
    }
}
