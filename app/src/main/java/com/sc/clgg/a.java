package com.sc.clgg;

/**
 * @author：lvke
 * @date：2018/9/14 14:55
 */
public class a {

    public static void main(String[] args) {
        int s = 720;
        int price = 720;
        for (int i = 0; i < 35; i++) {
            price = price - 21;

            s = s + price;
            System.out.print(price + "  ");
        }
        System.out.print("\ns = " + s);
    }
}
