package com.muazwzxv.server.metadata;

import com.google.protobuf.Int32Value;
import com.muazwzxv.models.*;
import com.muazwzxv.server.observer.CashDepositStreamObserver;
import com.muazwzxv.server.store.AccountDatabase;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

public class MetadataService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        // Method return type returns void as we are
        // returning data using the StreamObserver.
        int accountNumber = request.getAccountNumber();

        UserRole role = ServerConstant.getCtxUserRole().get();

        if (UserRole.PRIME_USER.equals(role)) {
            System.out.println("User is a Prime user");
        } else {
            System.out.println("User is Regular user");
        }

        Balance balance = Balance.newBuilder()
                .setAmount(Int32Value.newBuilder()
                        .setValue(AccountDatabase.getBalance(accountNumber))
                        .build())
                .build();

        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        int accountNumber = request.getAccountNumber();

        // We assume all amount request are multiple of 10
        // 10 - 20 - 30 - 40
        int amount = request.getAmount();
        int balance = AccountDatabase.getBalance(accountNumber);

        Metadata metadata = new Metadata();
        Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
        // Error handling gRPC
        if (amount < 10 || amount % 10 != 0) {
            WithdrawalError error = WithdrawalError.newBuilder()
                    .setAmount(balance)
                    .setErrorMessage(ErrorMessage.ONLY_TEN_MULTIPLE)
                    .build();

            metadata.put(errorKey, error);
            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata));
            return;
        }

        if (amount > balance) {
            WithdrawalError error = WithdrawalError.newBuilder()
                    .setAmount(balance)
                    .setErrorMessage(ErrorMessage.INSUFFICIENT_BALANCE)
                    .build();

            metadata.put(errorKey, error);
            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException(metadata));
            return;
        }


        // Server will stream 10 dollar for each iteration of the loop
        for (int i = 0; i < (amount / 10); i++) {
            Money money = Money.newBuilder().setValue(10).build();
            responseObserver.onNext(money);
            AccountDatabase.deductBalance(accountNumber, 10);
        }
        // Server completed the request
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<DepositRequest> deposit(StreamObserver<Balance> responseObserver) {
        return new CashDepositStreamObserver(responseObserver);
    }
}
