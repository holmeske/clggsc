package com.sc.clgg;

import com.google.gson.Gson;
import com.sc.clgg.bean.EtcCardInfo;

/**
 * @author：lvke
 * @date：2018/9/14 14:55
 */
public class a {
    public static void main(String[] args) {
         System.out.print(System.currentTimeMillis());
        EtcCardInfo etcCardInfo;
        etcCardInfo  =new Gson().fromJson("",EtcCardInfo.class);
    }
}
