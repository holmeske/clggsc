package com.sc.clgg.activity.etc.opencard

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfoBean
import com.sc.clgg.bean.PickerBean
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.PickerViewHelper
import com.sc.clgg.util.hideSoftInputFromWindow
import kotlinx.android.synthetic.main.activity_info_certification_new.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast


class InfoCertificationNewActivity : BaseImmersionActivity() {
    private val sexTypes = arrayListOf("男", "女")
    private val certifiTypeList = arrayListOf(PickerBean("0", "身份证"), PickerBean("1", "军官证"),
            PickerBean("2", "护照"), PickerBean("3", "入境证(限港澳居民)"), PickerBean("4", "临时身份证"), PickerBean("5", "武警警察身份证"), PickerBean("6", "台湾居民来往大陆通行证"), PickerBean("101", "组织机构代码"), PickerBean("102", "统一社会信用代码证书"), PickerBean("103", "营业执照"), PickerBean("104", "事业单位法人证书"), PickerBean("105", "社会团体法人登记证书"), PickerBean("106", "律师事务所执业许可证"))

    private val vehicleColorList = arrayListOf(PickerBean("0", "蓝色"), PickerBean("1", "黄色"),
            PickerBean("2", "黑色"), PickerBean("3", "白色"), PickerBean("4", "渐变绿色"), PickerBean("5", "黄绿双拼色"), PickerBean("6", "蓝白渐变色"), PickerBean("9", "未确定"))

    private val vehicleTypeList = arrayListOf(PickerBean("1", "客一"), PickerBean("2", "客二"),
            PickerBean("3", "客三"), PickerBean("4", "客四"), PickerBean("11", "货一"), PickerBean("12", "货二"), PickerBean("13", "货三"), PickerBean("14", "货四"), PickerBean("15", "货五"), PickerBean("21", "一类专项作业车"), PickerBean("22", "二类专项作业车"), PickerBean("23", "三类专项作业车"), PickerBean("24", "四类专项作业车"), PickerBean("25", "五类专项作业车"), PickerBean("26", "六类专项作业车"))
    private val isPassengerList = arrayListOf(PickerBean("1", "客车"), PickerBean("2", "货车"))

    private val useCharacterList = arrayListOf(PickerBean("1", "家庭自用"), PickerBean("2", "非营业客车"),
            PickerBean("3", "营业客车"), PickerBean("4", "非营业货车"), PickerBean("5", "营业货车"), PickerBean("6", "特种车"), PickerBean("7", "特种车"))

    private val pickerViewHelper = PickerViewHelper()
    private var certificationInfo: CertificationInfoBean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_certification_new)

        certificationInfo = intent.getParcelableExtra("info")
        LogHelper.e("“信息认证”页面接收的数据 = ${Gson().toJson(certificationInfo)}")
        init()
    }

    private fun init() {
        titlebar_title.text = "信息认证"
        certificationProgressView.setProress(3)

        pickerViewHelper.initJsonData(this)

        tv_region.setOnClickListener {
            hideSoftInputFromWindow(it)
            pickerViewHelper.showPickerView(this) { s1, s2, s3 -> tv_region.text = "$s1 $s2 $s3" }
        }

        //持卡人性别
        tv_ckr_sex.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creat(this@InfoCertificationNewActivity, sexTypes)
            { options1, _, _, _ ->
                sexTypes[options1].run {
                    tv_ckr_sex.text = this
                    certificationInfo?.sex = this
                }
            }
        }
        //持卡人证件类型
        tv_ckr_zjlx.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@InfoCertificationNewActivity, certifiTypeList)
            { options1, _, _, _ ->
                certifiTypeList[options1].run {
                    tv_ckr_zjlx.text = this.name
                    certificationInfo?.ecardNoCertifiType = this.id
                }
            }
        }
        //所有人证件类型
        tv_syr_zjlx.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@InfoCertificationNewActivity, certifiTypeList)
            { options1, _, _, _ ->
                certifiTypeList[options1].run {
                    tv_syr_zjlx.text = this.name
                    certificationInfo?.ownerIdType = this.id
                }
            }
        }
        //车牌颜色
        tv_vehicle_brand_color.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@InfoCertificationNewActivity, vehicleColorList)
            { options1, _, _, _ ->
                vehicleColorList[options1].run {
                    tv_vehicle_brand_color.text = this.name
                    certificationInfo?.vehicleColor = this.id
                }
            }
        }
        //车型
        tv_vehicle_type.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@InfoCertificationNewActivity, vehicleTypeList)
            { options1, _, _, _ ->
                vehicleTypeList[options1].run {
                    tv_vehicle_type.text = this.name
                    certificationInfo?.vehicleType = this.id
                }
            }
        }
        //是否为客车
        tv_passenger_car.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@InfoCertificationNewActivity, isPassengerList)
            { options1, _, _, _ ->
                isPassengerList[options1].run {
                    tv_passenger_car.text = this.name
                    certificationInfo?.vehicleFlag = this.id
                }
            }
        }

        //行驶证车辆类型
        tv_xsz_cllx.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@InfoCertificationNewActivity, vehicleTypeList)
            { options1, _, _, _ ->
                vehicleTypeList[options1].run {
                    tv_xsz_cllx.text = this.name
                    certificationInfo?.certifiVehType = this.id
                }
            }
        }
        //车辆使用性质
        tv_xsz_syxz.setOnClickListener {
            com.sc.clgg.widget.PickerViewHelper().creatBean(this@InfoCertificationNewActivity, useCharacterList)
            { options1, _, _, _ ->
                useCharacterList[options1].run {
                    tv_xsz_syxz.text = this.name
                    certificationInfo?.useCharacter = this.id
                }
            }
        }
        tv_next.setOnClickListener {
            certificationInfo?.contact = et_consignee_name?.text.toString()
            certificationInfo?.ownerTel = et_consignee_phone?.text.toString()
            certificationInfo?.address = "${tv_region.text}${et_detailed_address?.text.toString()}"
            certificationInfo?.name = et_ckr_name?.text.toString()
            certificationInfo?.ecardNoPhoneNo = et_ckr_tel?.text.toString()
            certificationInfo?.birthday = et_ckr_csrq?.text.toString()
            //certificationInfo?.sex = tv_ckr_sex?.text.toString()
            //certificationInfo?.ecardNoCertifiType = tv_ckr_zjlx?.text.toString()
            certificationInfo?.certifiNo = et_ckr_zjhm?.text.toString()

            //certificationInfo?.ownerIdType = tv_syr_zjlx?.text.toString()
            certificationInfo?.ownerIdNum = et_syr_zjhm?.text.toString()

            //certificationInfo?.vehicleColor = tv_vehicle_brand_color?.text.toString()
            certificationInfo?.vehiclePlate = et_vehicle_no?.text.toString()
            certificationInfo?.ownerName = et_vehicle_master?.text.toString()
            //certificationInfo?.vehicleType = tv_vehicle_type?.text.toString()
            //certificationInfo?.vehicleFlag = tv_passenger_car?.text.toString()

            //certificationInfo?.certifiVehType = tv_xsz_cllx?.text.toString()
            certificationInfo?.model = et_tv_xsz_ppxh?.text.toString()
            //certificationInfo?.useCharacter = tv_xsz_syxz?.text.toString()
            certificationInfo?.vehiclelicNo = et_tv_xsz_sbdm?.text.toString()
            certificationInfo?.vehicleEngineNo = et_tv_xsz_fdjh?.text.toString()

            if (canNext()) {
                startActivity(Intent(this@InfoCertificationNewActivity, SubmitApplyNewActivity::class.java)
                        .putExtra("info", certificationInfo))
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
        if (certificationInfo?.name?.isBlank()!!) {
            toast("请输入持卡人姓名")
            return false
        }
        if (certificationInfo?.ecardNoPhoneNo?.isBlank()!!) {
            toast("请输入联系电话")
            return false
        }
        if (certificationInfo?.birthday?.isBlank()!!) {
            toast("请输入出生日期如：1992-01-01")
            return false
        }
        if (certificationInfo?.sex?.isBlank()!!) {
            toast("请选择性别")
            return false
        }
        if (certificationInfo?.ecardNoCertifiType?.isBlank()!!) {
            toast("请选择持卡人证件类型")
            return false
        }
        if (certificationInfo?.certifiNo?.isBlank()!!) {
            toast("请输入持卡人证件号码")
            return false
        }
        if (certificationInfo?.ownerIdType?.isBlank()!!) {
            toast("请选择所有人证件类型")
            return false
        }
        if (certificationInfo?.ownerIdNum?.isBlank()!!) {
            toast("请输入所有人证件号码")
            return false
        }

        if (certificationInfo?.vehicleColor?.isBlank()!!) {
            toast("请选择车牌颜色")
            return false
        }

        if (certificationInfo?.vehiclePlate?.isBlank()!!) {
            toast("请输入车牌号码")
            return false
        }
        if (certificationInfo?.ownerName?.isBlank()!!) {
            toast("请输入所有人（行驶证）")
            return false
        }
        if (certificationInfo?.vehicleType?.isBlank()!!) {
            toast("请选择车型")
            return false
        }
        if (certificationInfo?.vehicleFlag?.isBlank()!!) {
            toast("请选择是否是客车")
            return false
        }
        if (certificationInfo?.certifiVehType?.isBlank()!!) {
            toast("请选择行驶证车辆类型")
            return false
        }
        if (certificationInfo?.model?.isBlank()!!) {
            toast("请输入行驶证品牌型号")
            return false
        }
        if (certificationInfo?.useCharacter?.isBlank()!!) {
            toast("车辆使用性质")
            return false
        }
        if (certificationInfo?.vehiclelicNo?.isBlank()!!) {
            toast("请输入车辆识别代码")
            return false
        }
        if (certificationInfo?.vehicleEngineNo?.isBlank()!!) {
            toast("请输入车辆发动机号")
            return false
        }
        return true
    }

}
