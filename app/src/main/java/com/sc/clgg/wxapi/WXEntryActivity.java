package com.sc.clgg.wxapi;


import android.app.Activity;
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
 * @date：2018/6/19 15:26
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(this, ConstantValue.WX_APP_ID);
        iwxapi.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
        finish();
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            //发送成功
//            EventBus.getDefault().post(new EventBusBean(Constants.APP_SHARE_SUCCESS));
            Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
        } else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            //发送取消
//            EventBus.getDefault().post(new EventBusBean(Constants.APP_SHARE_CANCEL));
            Toast.makeText(this, "发送取消", Toast.LENGTH_SHORT).show();
        } else {
            //发送失败
//            EventBus.getDefault().post(new EventBusBean(Constants.APP_SHARE_FAILURE));
            Toast.makeText(this, "发送失败", Toast.LENGTH_SHORT).show();
        }
    }
}
