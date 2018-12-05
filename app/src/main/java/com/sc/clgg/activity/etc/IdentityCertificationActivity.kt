package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Patterns
import android.view.View
import com.bumptech.glide.Glide
import com.sc.clgg.R
import com.sc.clgg.activity.TakePhotoActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
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
import java.util.*

class IdentityCertificationActivity : TakePhotoActivity() {
    private var certificationInfo: CertificationInfo? = null
    private var idcardImgFront: String = ""
    private var idcardImgBehind: String = ""
    private var businessLicenseImg: String = ""

    private var choosingFront: Boolean = false
    private var choosingBehind: Boolean = false

    private var checkCode: String? = ""

    private fun initChoosingState() {
        LogHelper.e("initChoosingState")
        choosingFront = false
        choosingBehind = false
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
                    Glide.with(this).load(File(it)).into(iv_id_crad_positive)
                    LogHelper.e("正面")
                }
                choosingBehind -> {
                    idcardImgBehind = it
                    Glide.with(this).load(File(it)).into(iv_id_crad_reverse)
                    LogHelper.e("反面")
                }
                else -> {
                    businessLicenseImg = it
                    Glide.with(this).load(File(it)).into(iv_enterprise)
                    LogHelper.e("营业")
                }
            }
        }
        initChoosingState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identity_certification)

        certificationInfo = intent.getParcelableExtra("info")
        init()
    }

    private fun init() {
        titlebar_title.text = getString(R.string.identity_certification)
        certificationProgressView.setProress(1)

        ll_user_type.setOnClickListener {
            val data = ArrayList<String>()
            data.add("个人")
            data.add("企业")
            PickerViewHelper().creat(this@IdentityCertificationActivity, data) { options1, _, _, _ ->
                data[options1].run {
                    tv_user_type.text = this
                    tv_info_hint.visibility = View.VISIBLE
                    if (this == "个人") {
                        ll_personal.visibility = View.VISIBLE
                        ll_enterprise.visibility = View.GONE
                    } else {
                        ll_personal.visibility = View.GONE
                        ll_enterprise.visibility = View.VISIBLE
                    }
                }
            }
        }
        tv_get_verification_code.setOnClickListener {
            et_mobile_phone.text?.toString()?.run {
                if (length == 11) {
                    tv_get_verification_code.isEnabled = false
                    send(this)
                }
            }
        }

        iv_id_crad_positive.setOnClickListener { showTakePhoto(takePhoto) { choosingFront = true } }
        iv_id_crad_reverse.setOnClickListener { showTakePhoto(takePhoto) { choosingBehind = true } }
        iv_enterprise.setOnClickListener { showTakePhoto(takePhoto) }

        tv_next.setOnClickListener {
            if (tv_user_type.text.length != 2) {
                toast("请选择用户类型")
                return@setOnClickListener
            }
            if (et_name.text.isBlank()) {
                toast("请输入持卡人姓名")
                return@setOnClickListener
            }
            if (et_id_number.text.isBlank()) {
                toast("请输入持卡人身份证号")
                return@setOnClickListener
            }
            if (et_mobile_phone.text.isBlank()) {
                toast("请输入持卡人手机号")
                return@setOnClickListener
            }
            if (!Patterns.PHONE.matcher(et_mobile_phone.text.toString()).matches()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (et_verification_code.text.isBlank()) {
                toast("请输入验证码")
                return@setOnClickListener
            }
            if (checkCode != et_verification_code.text.toString().trim()) {
                toast("请输入正确的验证码")
                return@setOnClickListener
            }
            when (tv_user_type.text) {
                "个人" -> {
                    if (idcardImgFront.isBlank()) {
                        toast("请上传身份证正面图片")
                        return@setOnClickListener
                    }
                    if (idcardImgBehind.isBlank()) {
                        toast("请上传身份证反面图片")
                        return@setOnClickListener
                    }
                }
                "企业" -> {
                    if (businessLicenseImg.isBlank()) {
                        toast("请上传企业营业执照图片")
                        return@setOnClickListener
                    }
                }
            }

            certificationInfo?.userType = if (tv_user_type.text.toString() == "个人") "1" else "2"
            certificationInfo?.userName = et_name.text.toString()
            certificationInfo?.certSn = et_id_number.text.toString()
            certificationInfo?.linkMobile = et_mobile_phone.text.toString()
            certificationInfo?.verificationCode = et_verification_code.text.toString()
            certificationInfo?.invitationCode = et_invite_code.text.toString()

            if (tv_user_type.text == "个人") {
                certificationInfo?.idcardImgFront = idcardImgFront
                certificationInfo?.idcardImgBehind = idcardImgBehind
            } else {
                certificationInfo?.businessLicenseImg = businessLicenseImg
            }

            mCountDownTimer.cancel()
            tv_get_verification_code.isEnabled = true
            startActivity(Intent(this@IdentityCertificationActivity, VehicleCertificationActivity::class.java)
                    .putExtra("info", certificationInfo))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCountDownTimer.cancel()
    }

    private val mCountDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            tv_get_verification_code.text = String.format("%ds", millisUntilFinished / 1000)
        }

        override fun onFinish() {
            tv_get_verification_code.text = "发送验证码"
            tv_get_verification_code.isEnabled = true
        }
    }

    private fun send(phone: String) {
        RetrofitHelper().identityCertification(phone).enqueue(object : Callback<StatusBean> {
            override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                toast(R.string.network_anomaly)
            }

            override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                response.body()?.let {
                    if (it.success) {
                        mCountDownTimer.start()
                        checkCode = it.checkCode
                    } else {
                        toast("${it.msg}")
                    }
                }
            }

        })
    }
}
