package com.sc.clgg.activity.etc.opencard

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.activity.TakePhotoActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.bean.Passport
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.logcat
import com.sc.clgg.util.showTakePhoto
import com.sc.clgg.widget.PickerViewHelper
import kotlinx.android.synthetic.main.activity_identity_certification.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.devio.takephoto.model.TResult
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class IdentityCertificationActivity : TakePhotoActivity() {
    //开卡申请表单
    private var certificationInfo: CertificationInfo? = null
    //开卡客户身份证正面
    private var idcardImgFront: String = ""
    //开卡客户身份证反面
    private var idcardImgBehind: String = ""
    //经办人身份证正面
    private var agentIdcardFront: String = ""
    //经办人身份证反面
    private var agentIdcardReverse: String = ""
    //企业营业执照
    private var businessLicenseImg: String = ""

    //是否正在选择 - 身份证正面
    private var choosingFront: Boolean = false
    //是否正在选择 - 身份证反面
    private var choosingBehind: Boolean = false
    //是否正在选择 - 经办人身份证反面
    private var choosingAgentFront: Boolean = false
    //是否正在选择 - 经办人身份证反面
    private var choosingAgentReverse: Boolean = false

    //用户类型
    private val userTypes = arrayListOf("个人", "企业")

    private fun initChoosingState() {
        choosingFront = false
        choosingBehind = false
        choosingAgentFront = false
        choosingAgentReverse = false
        LogHelper.e("初始化图片选择状态")

        logcat(certificationInfo)
    }

    override fun takeCancel() {
        super.takeCancel()
        LogHelper.e("takeCancel")
        initChoosingState()
    }

    override fun takeFail(result: TResult?, msg: String?) {
        super.takeFail(result, msg)
        LogHelper.e("takeFail")
        initChoosingState()
    }

    override fun takeSuccess(result: TResult?) {
        super.takeSuccess(result)
        LogHelper.e("takeSuccess")
        result?.image?.compressPath?.let {
            when {
                choosingFront -> {
                    idcardImgFront = it
                    Glide.with(this).load(File(it)).into(iv_idcard_front)
                    tv_idcard_front_hint.visibility = View.INVISIBLE
                    certificationInfo?.idcardImgFront = result.image?.originalPath
                    LogHelper.e("客户正面")
                    scanIdCard(it, false)
                }
                choosingBehind -> {
                    idcardImgBehind = it
                    Glide.with(this).load(File(it)).into(iv_idcard_reverse)
                    tv_idcard_reverse_hint.visibility = View.INVISIBLE
                    certificationInfo?.idcardImgBehind = result.image?.originalPath
                    LogHelper.e("客户反面")
                }
                choosingAgentFront -> {
                    agentIdcardFront = it
                    Glide.with(this).load(File(it)).into(iv_agent_idcard_front)
                    tv_agent_idcard_front_hint.visibility = View.INVISIBLE
                    certificationInfo?.agentIdcardImgFront = result.image?.originalPath
                    LogHelper.e("经办人正面")
                    scanIdCard(it, true)
                }
                choosingAgentReverse -> {
                    agentIdcardReverse = it
                    Glide.with(this).load(File(it)).into(iv_agent_idcard_reverse)
                    tv_agent_idcard_reverse_hint.visibility = View.INVISIBLE
                    certificationInfo?.agentIdcardImgBehind = result.image?.originalPath
                    LogHelper.e("经办人反面")
                }
                else -> {
                    businessLicenseImg = it
                    Glide.with(this).load(File(it)).into(iv_enterprise)
                    tv_enterprise_hint.visibility = View.INVISIBLE
                    certificationInfo?.businessLicenseImg = result.image?.originalPath
                    LogHelper.e("营业执照")
                    scanPassport(it)
                }
            }
        }
        initChoosingState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity_certification)

        certificationInfo = intent.getParcelableExtra("info")
        LogHelper.e("“身份认证”页面接收的数据 = ${Gson().toJson(certificationInfo)}")
        init()
    }

    private fun init() {
        titlebar_title.text = getString(R.string.identity_certification)
        certificationProgressView.setProress(1)

        ll_user_type.setOnClickListener {

            PickerViewHelper().creat(this@IdentityCertificationActivity, userTypes) { options1, _, _, _ ->
                userTypes[options1].run {
                    tv_user_type.text = this
                    if (this == "个人") {
                        ll_personal.visibility = View.VISIBLE
                        ll_agent.visibility = View.VISIBLE
                        tv_agent.text = "上传经办人身份证（本人可不填）"
                        ll_enterprise.visibility = View.GONE

                        certificationInfo?.userType = "1"
                    } else {
                        ll_personal.visibility = View.GONE
                        ll_agent.visibility = View.VISIBLE
                        tv_agent.text = "上传经办人身份证"
                        ll_enterprise.visibility = View.VISIBLE
                        certificationInfo?.userType = "2"
                    }
                }
            }
        }

        iv_idcard_front.setOnClickListener { showTakePhoto(takePhoto) { choosingFront = true } }
        iv_idcard_reverse.setOnClickListener { showTakePhoto(takePhoto) { choosingBehind = true } }
        iv_agent_idcard_front.setOnClickListener { showTakePhoto(takePhoto) { choosingAgentFront = true } }
        iv_agent_idcard_reverse.setOnClickListener { showTakePhoto(takePhoto) { choosingAgentReverse = true } }
        iv_enterprise.setOnClickListener { showTakePhoto(takePhoto) }

        tv_next.setOnClickListener {
            /*startActivity(Intent(this@IdentityCertificationActivity, VehicleCertificationActivity::class.java)
                    .putExtra("info", certificationInfo))
            return@setOnClickListener*/
            if (certificationInfo?.userType!!.isBlank()) {
                toast("请选择用户类型")
                return@setOnClickListener
            }
            with(et_invite_code.text.toString()) {
                if (isNotBlank()) {
                    RetrofitHelper().invitationCode(this).enqueue(object : Callback<StatusBean> {
                        override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                            check()
                        }

                        override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                            if (response.body()?.number == 0) {
                                toast("邀请码不存在")
                                return
                            }
                            check()
                        }
                    })
                } else {
                    check()
                }
            }
        }
    }

    /**
     * 输入校验
     */
    private fun check() {
        when (certificationInfo?.userType) {
            "1" -> {
                if (certificationInfo?.idcardImgFront!!.isBlank()) {
                    toast("请上传身份证正面图片")
                    return
                }
                if (certificationInfo?.idcardImgBehind!!.isBlank()) {
                    toast("请上传身份证反面图片")
                    return
                }
            }
            "2" -> {
                if (certificationInfo?.agentIdcardImgFront!!.isBlank()) {
                    toast("请上传经办人身份证正面图片")
                    return
                }
                if (certificationInfo?.agentIdcardImgBehind!!.isBlank()) {
                    toast("请上传经办人身份证反面图片")
                    return
                }
                if (certificationInfo?.userType == "2" && certificationInfo?.businessLicenseImg!!.isBlank()) {
                    toast("请上传企业营业执照图片")
                    return
                }
            }
        }

        certificationInfo?.invitationCode = et_invite_code.text.toString()

        startActivity(Intent(this@IdentityCertificationActivity, VehicleCertificationActivity::class.java)
                .putExtra("info", certificationInfo))
    }

    override fun onDestroy() {
        super.onDestroy()
        scanIdCardHttp?.cancel()
        scanPassportHttp?.cancel()
    }

    private var scanPassportHttp: Call<Passport>? = null
    /**
     * 识别营业执照
     */
    private fun scanPassport(filePath: String?) {
        showProgressDialog(false)
        scanPassportHttp = RetrofitHelper().passport(File(filePath))
        scanPassportHttp?.enqueue(object : Callback<Passport> {
            override fun onResponse(call: Call<Passport>, response: Response<Passport>) {
                hideProgressDialog()
                response.body()?.identify?.words_result?.社会信用代码?.words?.takeUnless { it == "无" }?.let { certificationInfo?.certSn = it }
            }

            override fun onFailure(call: Call<Passport>, t: Throwable) {
                hideProgressDialog()
                toast(R.string.network_anomaly)
            }
        })
    }

    private var scanIdCardHttp: Call<Passport>? = null
    /**
     * 识别身份证
     */
    private fun scanIdCard(filePath: String?, isAgent: Boolean) {
        showProgressDialog(false)
        scanIdCardHttp = RetrofitHelper().idCard(File(filePath))
        scanIdCardHttp?.enqueue(object : Callback<Passport> {
            override fun onResponse(call: Call<Passport>, response: Response<Passport>) {
                hideProgressDialog()

                response.body()?.identify?.words_result?.let { it ->
                    if (isAgent) it.姓名?.words?.takeUnless { it == "无" }?.let { certificationInfo?.agentName = it }
                    else it.公民身份号码?.words?.takeUnless { it == "无" }?.let { certificationInfo?.certSn = it }
                }
            }

            override fun onFailure(call: Call<Passport>, t: Throwable) {
                hideProgressDialog()
                toast(R.string.network_anomaly)
            }
        })
    }
}
