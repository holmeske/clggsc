package com.sc.clgg.activity.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sc.clgg.R
import com.sc.clgg.activity.WebActivity
import com.sc.clgg.activity.my.MyMessageActivity
import com.sc.clgg.activity.my.MyVehicleActivity
import com.sc.clgg.activity.my.SetActivity
import com.sc.clgg.activity.my.userinfo.PersonalDataActivity
import com.sc.clgg.base.BaseFragment
import com.sc.clgg.bean.IsNotReadInfo
import com.sc.clgg.bean.PersonalData
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.setDefaultRoundedCornerPicture
import com.sc.clgg.util.setRoundedCornerPicture
import com.sc.clgg.util.statusBarHeight
import kotlinx.android.synthetic.main.fragment_my.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @author：lvke
 * @date：2018/5/29 11:51
 */
class MyFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            title.setPadding(0, 0, 0, 0)
            title.layoutParams.height = MeasureHelper.dp2px(activity, 64f) - activity!!.statusBarHeight()
        }

        v_0.setOnClickListener { ConfigUtil().loggedIn(activity) }
        v_1.setOnClickListener { if (ConfigUtil().loggedIn(activity)) startActivity<MyMessageActivity>() }
        item_my_car.setOnClickListener { if (ConfigUtil().loggedIn(activity)) startActivity<MyVehicleActivity>() }
        item_real_name.setOnClickListener { if (ConfigUtil().loggedIn(activity)) WebActivity.start(activity, "实名认证", ConstantValue.REAL_NAME_AUTHENTICATION) }
        item_wallet.setOnClickListener { if (ConfigUtil().loggedIn(activity)) WebActivity.start(activity, "我的钱包", ConstantValue.WALLET_ENTRANCE) }
        tv_operation.setOnClickListener { if (ConfigUtil().loggedIn(activity)) WebActivity.start(activity, "运营", ConstantValue.OPERATING) }
        tv_member_info.setOnClickListener { if (ConfigUtil().loggedIn(activity)) WebActivity.start(activity, "会员信息", ConstantValue.MEMBER_INFORMATION) }
        item_personal_data.setOnClickListener { if (ConfigUtil().loggedIn(activity)) startActivity<PersonalDataActivity>() }
        item_set.setOnClickListener { if (ConfigUtil().loggedIn(activity)) startActivity<SetActivity>() }


        /*group_no_car.visibility = View.VISIBLE
        item_personal_data.setTopDivider(false)*/
    }

    override fun onResume() {
        super.onResume()
        if (ConfigUtil().userid.isNotEmpty()) {
            getUserInfo()
            isNotReadInfo()
        } else {
            iv_head.setImageResource(R.drawable.ic_launcher)
            tv_nickname.text = "请登录"
            tv_describe.text = ""
        }
    }

    private var personalDataHttp: Call<PersonalData>? = null

    private fun getUserInfo() {
        personalDataHttp = RetrofitHelper().personalData()
        personalDataHttp?.enqueue(object : Callback<PersonalData> {
            override fun onFailure(call: Call<PersonalData>, t: Throwable?) {
                toast(R.string.network_anomaly)
                activity?.let { iv_head.setDefaultRoundedCornerPicture(it, R.drawable.ic_launcher) }
            }

            override fun onResponse(call: Call<PersonalData>, response: Response<PersonalData>) {
                if (!response.isSuccessful) {
                    toast(R.string.network_anomaly)
                    return
                }
                response.body()?.success?.run {
                    if (!this) {
                        toast(R.string.network_anomaly)
                        return
                    }
                }
                response.body()?.data?.let {

                    iv_head.setRoundedCornerPicture(activity, it.headImg)

                    ConfigUtil().icon = it.headImg

                    ConfigUtil().userid = it.userCode!!
                    ConfigUtil().username = it.userName
                    ConfigUtil().realName = it.realName
                    ConfigUtil().mobile = it.userName

                    ConfigUtil().nickName = it.nickName


                    tv_nickname.text = it.nickName
                    tv_describe.text = it.clientSign
                }


            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        personalDataHttp?.cancel()
        notReadInfoHttp?.cancel()
    }

    private var notReadInfoHttp: Call<IsNotReadInfo>? = null
    private fun isNotReadInfo() {
        notReadInfoHttp = RetrofitHelper().isNotReadInfo
        notReadInfoHttp?.enqueue(object : Callback<IsNotReadInfo> {
            override fun onFailure(call: Call<IsNotReadInfo>?, t: Throwable?) {
                v_point.visibility = View.GONE
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<IsNotReadInfo>?, response: Response<IsNotReadInfo>?) {
                response?.body()?.let {
                    if (it.DriverCircleType || it.News || it.Activities) {
                        v_point.visibility = View.VISIBLE
                    } else {
                        v_point.visibility = View.GONE
                    }
                }
            }
        })
    }


}