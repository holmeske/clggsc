package com.sc.clgg.activity.etc.opencard

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.PickerViewHelper
import com.sc.clgg.widget.VehicleInfoView
import kotlinx.android.synthetic.main.activity_info_certification.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast


class InfoCertificationActivity : BaseImmersionActivity() {

    private var certificationInfo: CertificationInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_certification)

        certificationInfo = intent.getParcelableExtra("info")
        LogHelper.e("“信息认证”页面接收的数据 = ${Gson().toJson(certificationInfo)}")
        init()
    }

    private val pickerViewHelper = PickerViewHelper()

    private fun init() {
        if (certificationInfo?.userType == "2") {
            tv_certificate_type?.text = "营业执照"
            et_certificate_number?.setText(certificationInfo?.certSn)
        }

        titlebar_title.text = "信息认证"
        certificationProgressView.setProress(3)

        pickerViewHelper.initJsonData(this)

        tv_region.setOnClickListener {
            pickerViewHelper.showPickerView(this) { s1, s2, s3 -> tv_region.text = "${s1} ${s2} ${s3}" }
        }

        tv_certificate_type.setOnClickListener {
            //"身份证含临时身份证", "护照", "港澳居民来往内地通行证", "台湾居民来往大陆通行证"
            //"统一社会信用代码证书", "组织机构代码证", "营业执照"
            val data =
                    if (certificationInfo?.userType == "1") arrayOf("身份证含临时身份证", "护照", "港澳居民来往内地通行证", "台湾居民来往大陆通行证").toList()
                    else arrayOf("统一社会信用代码证书", "组织机构代码证", "营业执照").toList()
            com.sc.clgg.widget.PickerViewHelper().creat(this, data)
            { options1, _, _, _ -> tv_certificate_type.text = data[options1] }
        }

        var index = 1
        certificationInfo?.etcCardApplyVehicleVoList?.forEach {
            ll_view_container.addView(VehicleInfoView(this).apply { car = it;setVehicleName("车辆${index++}:") })
        }

        tv_next.setOnClickListener {

            if (canNext()) {
                //收件人
                certificationInfo?.recipientsName = et_consignee_name?.text.toString()
                certificationInfo?.recipientsPhone = et_consignee_phone?.text.toString()
                certificationInfo?.recipientsAddress = "${tv_region.text}${et_detailed_address?.text.toString()}"
                certificationInfo?.agentName = et_agent_name?.text.toString()
                certificationInfo?.agentPhone = et_agent_tel?.text.toString()
                certificationInfo?.certType = tv_certificate_type?.text.toString()
                certificationInfo?.certSn = et_certificate_number?.text.toString()

                certificationInfo?.etcCardApplyVehicleVoList?.clear()
                ll_view_container?.takeIf { it.childCount > 0 }?.run {
                    for (i in 0 until ll_view_container.childCount) {
                        getChildAt(i).apply {
                            this as VehicleInfoView
                            certificationInfo?.etcCardApplyVehicleVoList?.add(car)
                            if (checkThrough(applicationContext)) {
                                if (i == ll_view_container.childCount - 1) {
                                    startActivity(Intent(this@InfoCertificationActivity, SubmitApplyActivity::class.java)
                                            .putExtra("info", certificationInfo))
                                }
                            } else {
                                return@run
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 输入校验
     */
    private fun canNext(): Boolean {
        if (et_consignee_name.text.isEmpty()) {
            toast("请输入收货人姓名")
            return false
        }
        if (et_consignee_name.text.length < 2) {
            toast("收货人姓名字符长度2~15（包含）")
            return false
        }
        if (et_consignee_phone.text.isEmpty()) {
            toast("请输入收货人手机号")
            return false
        }
        if (et_consignee_phone.text.length != 11) {
            toast("收货人手机号请输入11位数字")
            return false
        }
        if (tv_region.text.isEmpty()) {
            toast("请选择所在地区")
            return false
        }
        if (et_detailed_address.text.isEmpty()) {
            toast("请输入详细地址")
            return false
        }
        if (et_detailed_address.text.length < 5) {
            toast("详细地址字符长度5~120之间（包含）")
            return false
        }
        if (certificationInfo?.userType == "2") {
            if (et_agent_name.text.isEmpty()) {
                toast("请输入经办人姓名")
                return false
            }
            if (et_agent_name.text.length < 2) {
                toast("经办人姓名字符长度2~150（包含）")
                return false
            }
        }
        if (et_agent_tel.text.isEmpty()) {
            toast("请输入联系电话")
            return false
        }
        if (tv_certificate_type.text.isEmpty()) {
            toast("请选择所有人证件类型")
            return false
        }
        if (et_certificate_number.text.isEmpty()) {
            toast("请输入所有人证件号码")
            return false
        }
        return true
    }

}
