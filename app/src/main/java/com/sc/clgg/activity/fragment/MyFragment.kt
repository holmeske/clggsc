package com.sc.clgg.activity.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sc.clgg.R
import com.sc.clgg.activity.MyMessageActivity
import com.sc.clgg.activity.PersonalDataActivity
import com.sc.clgg.activity.SetActivity
import com.sc.clgg.activity.basic.WebActivity
import com.sc.clgg.activity.vehiclemanager.myvehicle.MyVehicleActivity
import com.sc.clgg.bean.IsNotReadInfo
import com.sc.clgg.bean.PersonalData
import com.sc.clgg.config.ConstantValue
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.setDefaultRoundedCornerPicture
import com.sc.clgg.util.setRoundedCornerPicture
import com.sc.clgg.util.statusBarHeight
import kotlinx.android.synthetic.main.fragment_my.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * @author：lvke
 * @date：2018/5/29 11:51
 */
class MyFragment : BaseFragment() {
    private var DriverCircleType: Boolean = false
    private var News: Boolean = false
    private var Activities: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            title.setPadding(0, 0, 0, 0)
            title.layoutParams.height = MeasureHelper.dp2px(activity, 64f) - activity!!.statusBarHeight()
        }
        v_1.onClick {
            startActivity(Intent(activity, MyMessageActivity::class.java)
                    .putExtra("DriverCircleType", DriverCircleType)
                    .putExtra("News", News)
                    .putExtra("Activities", Activities))
        }
        item_my_car.onClick { startActivity(Intent(activity, MyVehicleActivity::class.java)) }
        item_real_name.onClick { WebActivity.start(activity, "实名认证", ConstantValue.REAL_NAME_AUTHENTICATION) }
        item_wallet.onClick { WebActivity.start(activity, "我的钱包", ConstantValue.WALLET_ENTRANCE) }
        tv_operation.onClick { WebActivity.start(activity, "运营", ConstantValue.OPERATING) }
        tv_member_info.onClick { WebActivity.start(activity, "会员信息", ConstantValue.MEMBER_INFORMATION) }
        item_personal_data.onClick {
            startActivity(Intent(activity, PersonalDataActivity::class.java))
        }
        item_set.onClick {
            startActivity(Intent(activity, SetActivity::class.java))
        }
    }


    private var isActivityCreated: Boolean = false
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isActivityCreated = true
    }

    override fun onLazyFetchData() {
        super.onLazyFetchData()
        getUserInfo()
        isNotReadInfo()
    }

    override fun onResume() {
        LogHelper.e("onResume() --->我的")
        super.onResume()
        if (!ConfigUtil().userid.isEmpty()) {
            getUserInfo()
        }
        if (userVisibleHint) {
            isNotReadInfo()
        }
    }

    private var call: Call<PersonalData>? = null

    private fun getUserInfo() {
        call = RetrofitHelper().personalData()
        call?.enqueue(object : Callback<PersonalData> {
            override fun onFailure(call: Call<PersonalData>?, t: Throwable?) {
                toast(R.string.network_anomaly)
                iv_head.setDefaultRoundedCornerPicture(activity!!, R.drawable.ic_launcher)
            }

            override fun onResponse(call: Call<PersonalData>?, response: Response<PersonalData>?) {
                if (!response?.body()?.success!!) {
                    toast(R.string.network_anomaly)
                    return
                }
                response.body()?.data?.let {

                    iv_head.setRoundedCornerPicture(activity!!, it.headImg)

                    ConfigUtil().userid = it.userCode!!
                    ConfigUtil().username = it.userName!!
                    ConfigUtil().realName = it.realName!!
                    ConfigUtil().mobile = it.userName!!

                    ConfigUtil().nickName = it.nickName!!


                    tv_nickname.text = it.nickName
                    tv_describe.text = it.clientSign
                }


            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
        callIsNotReadInfo?.cancel()
    }

    private var callIsNotReadInfo: Call<IsNotReadInfo>? = null
    private fun isNotReadInfo() {
        callIsNotReadInfo = RetrofitHelper().isNotReadInfo
        callIsNotReadInfo?.enqueue(object : Callback<IsNotReadInfo> {
            override fun onFailure(call: Call<IsNotReadInfo>?, t: Throwable?) {
                v_point.visibility = View.GONE
                toast(R.string.network_anomaly)

            }

            override fun onResponse(call: Call<IsNotReadInfo>?, response: Response<IsNotReadInfo>?) {
                response?.body()?.let {
                    DriverCircleType = it.DriverCircleType
                    News = it.News
                    Activities = it.Activities

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