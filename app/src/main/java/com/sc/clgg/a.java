package com.sc.clgg;

import com.google.gson.Gson;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.tool.helper.RandomHelper;

/**
 * @author：lvke
 * @date：2018/9/14 14:55
 */
public class a {
  public static String a="";
    public static void main(String[] args) {
         System.out.print(RandomHelper.two());

    }

    private void log(Object o){
        LogHelper.e(new Gson().toJson(o));
    }
}
