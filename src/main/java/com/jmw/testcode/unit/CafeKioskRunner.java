package com.jmw.testcode.unit;

import com.jmw.testcode.unit.beverage.Americano;
import com.jmw.testcode.unit.beverage.Latte;

public class CafeKioskRunner {

    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println("add americano");
        cafeKiosk.add(new Latte());
        System.out.println("add latte");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("totalPrice = " + totalPrice);
    }

}
