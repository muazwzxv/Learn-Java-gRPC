package com.muazwzxv.protobuf;

import com.google.protobuf.Int32Value;
import com.muazwzxv.models.Address;
import com.muazwzxv.models.Car;
import com.muazwzxv.models.Person;

import java.util.ArrayList;
import java.util.List;

public class CompositionDemo {
    public static void main(String[] args) {
        Address city = Address.newBuilder()
                .setPostbox(124)
                .setStreet("Main street")
                .setCity("Puchong")
                .build();

        Car accord = Car.newBuilder()
                .setMake("Honda")
                .setModel("Accord")
                .setYear(2020)
                .build();

        Car civic = Car.newBuilder()
                .setMake("Honda")
                .setModel("civic")
                .setYear(2005)
                .build();

        Car camry = Car.newBuilder()
                .setMake("Toyota")
                .setModel("Camry")
                .setYear(2020)
                .build();

        List<Car> cars = new ArrayList<Car>();
        cars.add(camry);
        cars.add(civic);
        cars.add(accord);


        Person muaz = Person.newBuilder()
                .setName("Muaz")
                .setAge(Int32Value.newBuilder().setValue(22).build())
                .addAllCar(cars)
                .setAddress(city)
                .build();

        System.out.println(muaz.hasAge());
    }
}
