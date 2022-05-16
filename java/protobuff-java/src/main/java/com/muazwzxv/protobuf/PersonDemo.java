package com.muazwzxv.protobuf;

import com.muazwzxv.models.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersonDemo {
    public static void main(String [] args) throws IOException {
//        Person muaz = Person.newBuilder()
//                .setName("Muaz Bin Wazir")
//                .setAge(22)
//                .build();

        Path path = Paths.get("muaz.serialize");
//        Files.write(path, muaz.toByteArray());

        byte[] bytes = Files.readAllBytes(path);
        Person newMuaz = Person.parseFrom(bytes);
        System.out.println(newMuaz);
    }
}
