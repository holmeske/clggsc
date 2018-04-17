package com.sc.clgg.http;

import com.alibaba.fastjson.JSON;
import com.sc.clgg.util.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

import tool.helper.LogHelper;

/**
 * @author：lvke
 * @date：2018/3/9 13:53
 */
public class ParamsHelper {

    public static Object drivingScore(String timeLine) {
        Map<String, String> params = new HashMap<>(2);
        params.put("corp", new ConfigUtil().getUserid());
        params.put("timeLine", timeLine);
        Object o = JSON.toJSON(params);
        LogHelper.e("params = " + o);
        return o;
    }

}
