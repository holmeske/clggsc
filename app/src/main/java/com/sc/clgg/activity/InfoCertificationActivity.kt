package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.util.PickerViewHelper
import kotlinx.android.synthetic.main.activity_info_certification.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast


class InfoCertificationActivity : BaseImmersionActivity() {

    private var certificationInfo: CertificationInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_certification)
        init()
        certificationProgressView.setProress(3)
        certificationInfo = intent.getParcelableExtra("info")
    }

    val pickerViewHelper = PickerViewHelper()

    private fun init() {
        titlebar_title.text = "信息认证"

        pickerViewHelper.initJsonData(this)

        iv_select_vehicle_type.setOnClickListener {
            pickerViewHelper.showPickerView(this, { s1, s2, s3 -> tv_region.text = "${s1} ${s2} ${s3}" })
        }

        tv_next.setOnClickListener {

            if (isCanNext()) {
                certificationInfo?.recipientsName = et_consignee_name?.text.toString()
                certificationInfo?.recipientsPhone = et_consignee_phone?.text.toString()
                certificationInfo?.recipientsAddress = "${tv_region.text}${et_detailed_address?.text.toString()}"
                startActivity(Intent(this, SubmitApplicationActivity::class.java).putExtra("info", certificationInfo))
            }
        }

    }

    private fun isCanNext(): Boolean {
        if (et_consignee_name.text.isEmpty()) {
            toast("请输入收货人姓名")
            return false
        }
        if (et_consignee_phone.text.isEmpty()) {
            toast("请输入收货人手机号")
            return false
        }
        if (tv_region.text.isBlank()) {
            toast("请选择所在地区")
            return false
        }
        if (et_detailed_address.text.isEmpty()) {
            toast("请输入收货人详细地址")
            return false
        }
        return true
    }

}
