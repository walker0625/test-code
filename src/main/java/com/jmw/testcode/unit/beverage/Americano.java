package com.jmw.testcode.unit.beverage;

public class Americano implements Beverage{
    @Override
    public String getName() {
        return "Americano";
    }

    @Override
    public int getPrice() {
        return 4000;
    }
}
