package com.sc.clgg.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sc.clgg.bean.MessageEvent;
import com.sc.clgg.config.ConstantValue;
import com.sc.clgg.tool.helper.LogHelper;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * @author：lvke
 * @date：2018/10/10 14:52
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    /**
     * 第三方app和微信通信的openapi接口
     */
    private IWXAPI iwxapi;

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
        LogHelper.d("WXPayEntryActivity.onReq()");
    }

    @Override
    public void onResp(BaseResp resp) {
        LogHelper.d("WXPayEntryActivity.onResp()");
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            EventBus.getDefault().post(new MessageEvent(4));
            //EventBus.getDefault().post(new EventBusBean(Constants.APP_PAY_SUCCESS));
            //Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
        } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            //支付取消
            //EventBus.getDefault().post(new EventBusBean(Constants.APP_PAY_CANCEL));
            Toast.makeText(getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();
        } else {
            //支付失败
            //EventBus.getDefault().post(new EventBusBean(Constants.APP_PAY_FAILURE));
            Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

}
