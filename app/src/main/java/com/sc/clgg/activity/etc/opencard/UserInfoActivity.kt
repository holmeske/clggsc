package com.sc.clgg.activity.etc.opencard

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfoBean
import com.sc.clgg.bean.PickerBean
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast


class UserInfoActivity : BaseImmersionActivity() {
    private val certifiTypeList = arrayListOf(PickerBean("0", "身份证"), PickerBean("1", "军官证"),
            PickerBean("2", "护照"), PickerBean("3", "入境证(限港澳居民)"), PickerBean("4", "临时身份证"), PickerBean("5", "武警警察身份证"), PickerBean("6", "台湾居民来往大陆通行证"), PickerBean("101", "组织机构代码"), PickerBean("102", "统一社会信用代码证书"), PickerBean("103", "营业执照"), PickerBean("104", "事业单位法人证书"), PickerBean("105", "社会团体法人登记证书"), PickerBean("106", "律师事务所执业许可证"))

    //用户类型
    private val userTypes = arrayListOf("个人", "企业")
    private var certificationInfo = CertificationInfoBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        LogHelper.e("“用户信息”页面接收的数据 = ${Gson().toJson(certificationInfo)}")
        init()
    }


    private fun init() {
        titlebar_title.text = "用户信息"

        tv_user_type.text = "企业"
        ll_user_type.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creat(this@UserInfoActivity, userTypes) { options1, _, _, _ ->
                userTypes[options1].run {
                    tv_user_type.text = this
                    if (this == "个人") {
                        ll_view_container.visibility = View.GONE
                        certificationInfo.companyflag = "0"
                    } else {
                        ll_view_container.visibility = View.VISIBLE
                        certificationInfo.companyflag = "1"
                    }
                }
            }
        }
        tv_zjlx.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@UserInfoActivity, certifiTypeList)
            { options1, _, _, _ ->
                certifiTypeList[options1].run {
                    tv_zjlx.text = this.name
                    certificationInfo.certifiType = this.id
                }
            }
        }

        tv_certificate_type.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@UserInfoActivity, certifiTypeList)
            { options1, _, _, _ ->
                certifiTypeList[options1].run {
                    tv_certificate_type.text = this.name
                    certificationInfo.agentIdType = this.id
                }
            }
        }

        tv_next.setOnClickListener {

            //收件人
            //certificationInfo?.companyflag = tv_user_type?.text.toString()
            //certificationInfo?.certifiType = tv_zjlx?.text.toString()
            certificationInfo.companyLic = et_zjhm?.text.toString()
            certificationInfo.relationName = et_agent_name?.text.toString()
            certificationInfo.phoneNo = et_agent_tel?.text.toString()
            certificationInfo.department = et_agent_idno?.text.toString()
            certificationInfo.accountName = et_entity_name?.text.toString()
            //certificationInfo?.agentIdType = tv_certificate_type?.text.toString()
            certificationInfo.agentIdNum = et_certificate_number?.text.toString()

            if (canNext()) {
                startActivity(Intent(this@UserInfoActivity, BankCardCertificationActivity::class.java)
                        .putExtra("info", certificationInfo))
            }
        }

    }

    /**
     * 输入校验
     */
    private fun canNext(): Boolean {
        if (certificationInfo.companyflag?.isBlank()!!) {
            toast("请选择用户类型")
            return false
        }
        if (certificationInfo.certifiType?.isBlank()!!) {
            toast("请选择证件类型")
            return false
        }
        if (certificationInfo.companyLic?.isBlank()!!) {
            toast("请输入证件号码")
            return false
        }
        if (certificationInfo.companyflag == "1") {
            if (certificationInfo.relationName?.isBlank()!!) {
                toast("请输入经办人姓名")
                return false
            }
            if (certificationInfo.phoneNo?.isBlank()!!) {
                toast("请输入经办人联系电话")
                return false
            }
            if (certificationInfo.department?.isBlank()!!) {
                toast("请输入经办人所在部门")
                return false
            }
            if (certificationInfo.accountName?.isBlank()!!) {
                toast("请输入经办人单位名称")
                return false
            }
            if (certificationInfo.agentIdType?.isBlank()!!) {
                toast("请输入经办人证件类型")
                return false
            }
            if (certificationInfo.agentIdNum?.isBlank()!!) {
                toast("请输入经办人证件号码")
                return false
            }
        }
        return true
    }

}
