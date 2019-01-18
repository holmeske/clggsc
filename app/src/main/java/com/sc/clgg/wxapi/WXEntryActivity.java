package com.sc.clgg.wxapi;


import android.app.Activity;
import android.os.Bundle;

import com.sc.clgg.tool.helper.LogHelper;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * @author：lvke
 * @date：2018/6/19 15:26
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.d("WXEntryActivity.onCreate()");
        //IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, ConstantValue.WX_APP_ID);
        //iwxapi.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
        //finish();
        /*WeChatOrder order = getIntent().getParcelableExtra("order");
        PayReq request = new PayReq();
        request.appId = order.getData().getAppid();
        request.partnerId = WX_PARTNER_ID;
        request.prepayId = order.getData().getPrepay_id();
        request.packageValue = BuildConfig.APPLICATION_ID;
        request.nonceStr = order.getData().getNonce_str();
        request.timeStamp = System.currentTimeMillis() + "";
        request.sign = order.getData().getSign();
        new WeChatPayUtil(getApplicationContext()).toPay(request);*/
    }


    @Override
    public void onReq(BaseReq arg0) {
        LogHelper.d("WXEntryActivity.onReq()");
    }

    @Override
    public void onResp(BaseResp resp) {
        LogHelper.d("WXEntryActivity.onResp()");
        /*if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //发送成功
//            EventBus.getDefault().post(new EventBusBean(Constants.APP_SHARE_SUCCESS));
            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
        } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            //发送取消
//            EventBus.getDefault().post(new EventBusBean(Constants.APP_SHARE_CANCEL));
            Toast.makeText(getApplicationContext(), "发送取消", Toast.LENGTH_SHORT).show();
        } else {
            //发送失败
//            EventBus.getDefault().post(new EventBusBean(Constants.APP_SHARE_FAILURE));
            Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
        }*/
    }
}
