package com.muazwzxv.server.observer;

import com.muazwzxv.models.Account;
import com.muazwzxv.models.TransferRequest;
import com.muazwzxv.models.TransferResponse;
import com.muazwzxv.models.TransferStatus;
import com.muazwzxv.server.store.AccountDatabase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class TransferStreamObserver implements StreamObserver<TransferRequest> {

    private final StreamObserver<TransferResponse> response;

    public TransferStreamObserver(StreamObserver<TransferResponse> response) {
        this.response = response;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int balance = AccountDatabase.getBalance(transferRequest.getFromAccount());
        int fromAcc = transferRequest.getFromAccount();
        int toAcc = transferRequest.getToAccount();

        /*
         * If an error happens, notify the client
         */
        if (transferRequest.getAmount() > balance)
            this.response.onError(Status.FAILED_PRECONDITION
                    .withDescription("Not enough Balance: RM " + balance)
                    .asRuntimeException());

        if (fromAcc == toAcc)
            this.response.onError(Status.FAILED_PRECONDITION
                    .withDescription("From account and To account cannot be the same")
                    .asRuntimeException());

        AccountDatabase.addBalance(toAcc, transferRequest.getAmount());
        AccountDatabase.deductBalance(fromAcc, transferRequest.getAmount());

        TransferResponse response = TransferResponse.newBuilder()
                .addAccounts(Account.newBuilder()
                        .setAccountNumber(fromAcc)
                        .setAmount(AccountDatabase.getBalance(fromAcc))
                        .build())
                .addAccounts(Account.newBuilder()
                        .setAccountNumber(toAcc)
                        .setAmount(AccountDatabase.getBalance(toAcc))
                        .build())
                .setStatus(TransferStatus.SUCCESS)
                .build();

        this.response.onNext(response);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        // Notify the client that the process is completed
        AccountDatabase.printAccount();
        this.response.onCompleted();
    }
}
