package com.sc.clgg.activity.etc.opencard

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.activity.TakePhotoActivity
import com.sc.clgg.bean.CertificationInfo
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
                    certificationInfo?.idcardImgFront = it
                    LogHelper.e("客户正面")
                    scanIdCard(it, false)
                }
                choosingBehind -> {
                    idcardImgBehind = it
                    Glide.with(this).load(File(it)).into(iv_idcard_reverse)
                    tv_idcard_reverse_hint.visibility = View.INVISIBLE
                    certificationInfo?.idcardImgBehind = it
                    LogHelper.e("客户反面")
                }
                choosingAgentFront -> {
                    agentIdcardFront = it
                    Glide.with(this).load(File(it)).into(iv_agent_idcard_front)
                    tv_agent_idcard_front_hint.visibility = View.INVISIBLE
                    certificationInfo?.agentIdcardImgFront = it
                    LogHelper.e("经办人正面")
                    scanIdCard(it, true)
                }
                choosingAgentReverse -> {
                    agentIdcardReverse = it
                    Glide.with(this).load(File(it)).into(iv_agent_idcard_reverse)
                    tv_agent_idcard_reverse_hint.visibility = View.INVISIBLE
                    certificationInfo?.agentIdcardImgBehind = it
                    LogHelper.e("经办人反面")
                }
                else -> {
                    businessLicenseImg = it
                    Glide.with(this).load(File(it)).into(iv_enterprise)
                    tv_enterprise_hint.visibility = View.INVISIBLE
                    certificationInfo?.businessLicenseImg = it
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
                        ll_enterprise.visibility = View.GONE

                        certificationInfo?.userType = "1"
                    } else {
                        ll_personal.visibility = View.GONE
                        ll_agent.visibility = View.VISIBLE
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
            if (certificationInfo?.userType!!.isBlank()) {
                toast("请选择用户类型")
                return@setOnClickListener
            }
            when (certificationInfo?.userType) {
                "1" -> {
                    if (certificationInfo?.idcardImgFront!!.isBlank()) {
                        toast("请上传身份证正面图片")
                        return@setOnClickListener
                    }
                    if (certificationInfo?.idcardImgBehind!!.isBlank()) {
                        toast("请上传身份证反面图片")
                        return@setOnClickListener
                    }
                }
                "2" -> {
                    if (certificationInfo?.agentIdcardImgFront!!.isBlank()) {
                        toast("请上传经办人身份证正面图片")
                        return@setOnClickListener
                    }
                    if (certificationInfo?.agentIdcardImgBehind!!.isBlank()) {
                        toast("请上传经办人身份证反面图片")
                        return@setOnClickListener
                    }
                    if (certificationInfo?.userType == "2" && certificationInfo?.businessLicenseImg!!.isBlank()) {
                        toast("请上传企业营业执照图片")
                        return@setOnClickListener
                    }
                }
            }

            certificationInfo?.invitationCode = et_invite_code.text.toString()

            startActivity(Intent(this@IdentityCertificationActivity, VehicleCertificationActivity::class.java)
                    .putExtra("info", certificationInfo))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scanIdCardHttp?.cancel()
        scanPassportHttp?.cancel()
    }

    private var scanPassportHttp: Call<Map<String, Any>>? = null
    /**
     * 识别营业执照
     */
    private fun scanPassport(filePath: String?) {
        scanPassportHttp = RetrofitHelper().passport(File(filePath))
        scanPassportHttp?.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                hideProgressDialog()
                try {
                    response.body()?.let {
                        if (it.containsKey("success") && it["success"] as Boolean) {

                            if (it.containsKey("identify")) {
                                it["identify"]?.let { it as Map<String, Any> }?.let {
                                    if (it.containsKey("words_result")) {
                                        it["words_result"]?.let { it as Map<String, Any> }?.let {
                                            it["社会信用代码"]?.let { it as Map<String, String> }?.let {
                                                certificationInfo?.certSn = if (it["words"] == "无") "" else it["words"]
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                } catch (e: Exception) {
                    LogHelper.e(e)
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                hideProgressDialog()
                toast(R.string.network_anomaly)
            }
        })
    }

    private var scanIdCardHttp: Call<Map<String, Any>>? = null
    /**
     * 识别身份证
     */
    private fun scanIdCard(filePath: String?, isAgent: Boolean) {
        scanIdCardHttp = RetrofitHelper().idCard(File(filePath))
        scanIdCardHttp?.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                hideProgressDialog()
                try {
                    val allMap = response.body()
                    if (allMap!!.containsKey("success") && allMap["success"] as Boolean) {
                        val identifyMap = allMap["identify"] as Map<String, Any>

                        if (identifyMap.containsKey("words_result")) {

                            val resultMap = identifyMap["words_result"] as Map<String, Any>

                            if (isAgent) certificationInfo?.agentName = (resultMap["姓名"] as Map<String, String>)["words"]
                            else certificationInfo?.certSn = (resultMap["公民身份号码"] as Map<String, String>)["words"]
                        }

                    }
                } catch (e: Exception) {
                    LogHelper.e(e)
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                hideProgressDialog()
                toast(R.string.network_anomaly)
            }
        })
    }
}
