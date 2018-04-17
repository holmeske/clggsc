package com.sc.clgg.activity.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sc.clgg.R
import com.sc.clgg.R.string.network_anomaly
import com.sc.clgg.activity.basic.LoginActivity
import com.sc.clgg.activity.contact.UserSettingContact
import com.sc.clgg.activity.presenter.UserSettingPresenter
import com.sc.clgg.activity.usersettings.AboutUsActivity
import com.sc.clgg.activity.usersettings.ModifyPasswordActivity
import com.sc.clgg.bean.VersionInfoBean
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.dialog.ExitDialog
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.Tools
import com.sc.clgg.util.UpdateApkUtil
import kotlinx.android.synthetic.main.fragment_user_settings.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import tool.helper.ActivityHelper

/**
 * @author：lvke
 * @date：2018/2/27 13:47
 */
class UserSettingsFragment : Fragment(), UserSettingContact {
    private var presenter: UserSettingPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_user_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = UserSettingPresenter(this)

        val mConfigUtil = ConfigUtil()
        if (mConfigUtil.mobile.isNotEmpty()) tv_name.text = mConfigUtil.mobile
        else if (mConfigUtil.username.isNotEmpty()) tv_name.text = mConfigUtil.username

        tv_change_password.onClick { ActivityHelper.startAcMove(activity, Intent(activity, ModifyPasswordActivity::class.java)) }
        tv_custom_service.onClick { Tools.callPhone(ConstantValue.SERVICE_TEL, activity) }
        tv_about.onClick { ActivityHelper.startAcMove(activity, Intent(activity, AboutUsActivity::class.java)) }
        tv_check_apk_update.onClick { presenter?.checkUpdate() }

        exitTxt.onClick {
            ExitDialog(activity).show("温馨提示", "确定退出登录？") { _, _ ->
                ConfigUtil().reset()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity!!.finish()
            }
        }


    }

    override fun getVersionInfo(bean: VersionInfoBean?) {
        if (bean == null) toast(network_anomaly)
        else UpdateApkUtil().checkUpdateInfo(activity, bean.single?.code, bean.single!!.type, bean.single?.url, true)
    }

    override fun onError(msg: String?) {
        toast(network_anomaly)
    }

}