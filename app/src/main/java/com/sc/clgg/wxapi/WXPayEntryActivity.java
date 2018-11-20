package com.sc.clgg.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sc.clgg.config.ConstantValue;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author：lvke
 * @date：2018/10/10 14:52
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI iwxapi;// IWXAPI 是第三方app和微信通信的openapi接口

    @Override
    public void onCreate(Bundle savedInstanceState) {
        iwxapi = WXAPIFactory.createWXAPI(this, ConstantValue.WX_APP_ID);
        iwxapi.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //支付成功
            //EventBus.getDefault().post(new EventBusBean(Constants.APP_PAY_SUCCESS));
            Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
        } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            //支付取消
            //EventBus.getDefault().post(new EventBusBean(Constants.APP_PAY_CANCEL));
            Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
        } else {
            //支付失败
            //EventBus.getDefault().post(new EventBusBean(Constants.APP_PAY_FAILURE));
            Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}