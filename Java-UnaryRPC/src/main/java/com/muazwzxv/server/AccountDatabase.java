package com.muazwzxv.server;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountDatabase {

    /**
     * Class for mock database
     */

    private static final Map<Integer, Integer> STORE = IntStream
            .rangeClosed(1, 10)
            .boxed()
            .collect(Collectors.toMap(
                    Function.identity(),
                    v -> v * 10)
            );

    public static int getBalance(int accountId) {
        return STORE.get(accountId);
    }

    public static int addBalance(int accountId, int amount) {
        return STORE.computeIfPresent(accountId, (key, value) -> value + amount);
    }

    public static int deductBalance(int accountId, int amount) {
        return STORE.computeIfPresent(accountId, (key, value) -> value - amount);

    }
}
