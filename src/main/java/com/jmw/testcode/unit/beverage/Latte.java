package com.jmw.testcode.unit.beverage;

public class Latte implements Beverage{
    @Override
    public String getName() {
        return "Latte";
    }

    @Override
    public int getPrice() {
        return 5000;
    }
}
