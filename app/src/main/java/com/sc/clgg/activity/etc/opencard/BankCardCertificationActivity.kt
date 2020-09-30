package com.sc.clgg.activity.etc.opencard

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.bean.CertificationInfoBean
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.widget.PickerViewHelper
import kotlinx.android.synthetic.main.activity_bank_card_certification.*
import kotlinx.android.synthetic.main.activity_bank_card_certification.tv_next
import kotlinx.android.synthetic.main.activity_info_certification.*
import kotlinx.android.synthetic.main.activity_info_certification.certificationProgressView
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast

class BankCardCertificationActivity : AppCompatActivity() {
    private var certificationInfo: CertificationInfoBean? = null

    //用户类型
    private val bankCardTypes = arrayListOf("借记卡", "信用卡")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_card_certification)

        certificationInfo = intent.getParcelableExtra("info")
        LogHelper.e("“银行卡认证”页面接收的数据 = ${Gson().toJson(certificationInfo)}")
        init()
    }

    private fun init() {
        titlebar_title.text = "银行卡认证"
        certificationProgressView.setProress(2)

        if (certificationInfo?.companyflag == "1") {
            ll_bank_no.visibility = View.VISIBLE
        }

        cl_bank_card_type.setOnClickListener {
            PickerViewHelper().creat(this@BankCardCertificationActivity, bankCardTypes) { options1, _, _, _ ->
                bankCardTypes[options1].run {
                    tv_select_bank_type.text = this
                    if (this == "借记卡") {
                        certificationInfo?.bankCardNoType = "1"
                    } else {
                        certificationInfo?.bankCardNoType = "0"
                    }
                }
            }
        }
        tv_next.setOnClickListener {
            if (canNext()) {
                certificationInfo?.bankCardNo = et_bank_card?.text.toString()
                certificationInfo?.bankCardNoCreatBrno = et_bank_branches?.text.toString()
                certificationInfo?.companyAgentSerialNo = et_bank_unit_number?.text.toString()

                startActivity(Intent(this@BankCardCertificationActivity, InfoCertificationNewActivity::class.java)
                        .putExtra("info", certificationInfo))
            }
        }
    }

    /**
     * 输入校验
     */
    private fun canNext(): Boolean {
        if (et_bank_card.text.isBlank()) {
            toast("请输入银行卡卡号")
            return false
        }
        if (tv_select_bank_type.text.isBlank()) {
            toast("请选择银行卡类型")
            return false
        }
        if (et_bank_branches.text.isBlank()) {
            toast("请输入银行卡开户网点")
            return false
        }
        if (certificationInfo?.companyflag == "1") {
            if (et_bank_unit_number.text.isBlank()) {
                toast("请输入银行方单位编号")
                return false
            }
        }
        return true
    }
}
