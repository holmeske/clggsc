package com.sc.clgg;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2018/9/14 14:55
 */
public class a {

    public static void main(String[] args) {

        List<Float> data = new ArrayList<>();
        data.add(115.5f);

        StringBuilder sb=new StringBuilder(",123456");
        if (sb.substring(0).equals(",")){
        }

        System.out.print(sb.substring(1,sb.length()) );
    }


}
