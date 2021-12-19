package com.muazwzxv.protobuf;

import com.muazwzxv.models.Television;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VersionCompatibilityDemo {
    public static void main(String[] args) throws IOException {

//        Television samsung = Television.newBuilder()
//                .setBrand("samsung")
//                .setYear(2020)
//                .build();
//
//        Path pathV1 = Paths.get("tv-v1");
//        Files.write(pathV1, samsung.toByteArray());

        Path pathV1 = Paths.get("tv-v1");
        byte[] bytes = Files.readAllBytes(pathV1);
        System.out.println(Television.parseFrom(bytes));
    }

}
