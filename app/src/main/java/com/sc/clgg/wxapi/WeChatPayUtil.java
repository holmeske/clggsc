package com.sc.clgg.wxapi;

import android.content.Context;
import android.widget.Toast;

import androidx.collection.ArrayMap;

import com.sc.clgg.config.ConstantValue;
import com.sc.clgg.tool.helper.LogHelper;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * @author lvke
 */
public class WeChatPayUtil {
    private Context context;
    /**
     * IWXAPI 是第三方app和微信通信的openapi接口
     */
    private IWXAPI wxApi;

    public WeChatPayUtil(Context context) {
        this.context = context;
        //通过WXAPIFactory工厂，获取IWXAPI的实例
        wxApi = WXAPIFactory.createWXAPI(context, ConstantValue.WX_APP_ID, true);
        //将应用的appId注册到微信
        wxApi.registerApp(ConstantValue.WX_APP_ID);
    }

    // 支付
    public void toPay(PayReq req) {
        if (!wxApi.isWXAppInstalled()) {
            Toast.makeText(context, "您尚未安装微信", Toast.LENGTH_SHORT).show();
            payErr();
            return;
        }

        if (!wxApi.isWXAppInstalled()) {
            Toast.makeText(context, "当前版本不支持支付功能", Toast.LENGTH_SHORT).show();
            payErr();
            return;
        }

        //对参数重新拼接签名
        ArrayMap<String, String> paramsMap = new ArrayMap<>();
        paramsMap.put("appid", req.appId);
        paramsMap.put("noncestr", req.nonceStr);
        paramsMap.put("package", req.packageValue);
        paramsMap.put("partnerid", req.partnerId);
        paramsMap.put("prepayid", req.prepayId);
        paramsMap.put("timestamp", req.timeStamp);
        req.sign = getAppSign(paramsMap);

        boolean sendResult = wxApi.sendReq(req);
        LogHelper.e("支付状态:" + sendResult);
        if (!sendResult) {
            Toast.makeText(context, "支付失败,请重试", Toast.LENGTH_SHORT).show();
            payErr();
        }
    }

    //重新对后台返回的sign签名
    private String getAppSign(ArrayMap<String, String> paramsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append('=');
            stringBuilder.append(entry.getValue());
            stringBuilder.append('&');
        }

        String appSign =  byteArrayToHex(stringBuilder.toString().getBytes()).toUpperCase();
        LogHelper.e(appSign);
        return appSign;
    }

    private void payErr() {
        LogHelper.e("payErr()");
//        EventBus.getDefault().post(new EventBusBean(Constants.APP_PAY_NO_TOAST));
    }

    public static String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

}