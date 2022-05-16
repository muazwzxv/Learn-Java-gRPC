package com.muazwzxv.protobuf;

import com.muazwzxv.models.BodyStyle;
import com.muazwzxv.models.Car;
import com.muazwzxv.models.Dealer;

public class MapDemo {

    public static void main(String[] args) {

        Car accord = Car.newBuilder()
                .setMake("Honda")
                .setModel("Accord")
                .setYear(2020)
                .setBodyStyle(BodyStyle.COUPE)
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

        Dealer dealer = Dealer.newBuilder()
                .putModel(accord.getYear(), accord)
                .putModel(camry.getYear(), camry)
                .putModel(civic.getYear(), civic)
                .build();

        System.out.println(dealer.getModelOrThrow(2005).getBodyStyle());
    }
}
