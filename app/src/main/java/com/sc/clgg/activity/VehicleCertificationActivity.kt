package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.showTakePhoto
import com.sc.clgg.widget.VehicleInfoView
import kotlinx.android.synthetic.main.activity_vehicle_certification.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.devio.takephoto.model.TResult

class VehicleCertificationActivity : TakePhotoActivity() {
    private var certificationInfo : CertificationInfo?=null
    private var currentVehicle:VehicleInfoView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_certification)

        init()
        certificationInfo = intent.getParcelableExtra("info")

        LogHelper.e("身份认证 = ${Gson().toJson(certificationInfo)}")
    }

    override fun takeCancel() {
        super.takeCancel()
        currentVehicle=null
    }

    override fun takeFail(result: TResult?, msg: String?) {
        super.takeFail(result, msg)
        currentVehicle=null
    }

    override fun takeSuccess(result: TResult?) {
        super.takeSuccess(result)
        currentVehicle?.let {
            it.car?.vehicleLicenseImg=result?.image?.compressPath
            it.setVehicleLicense(result?.image?.compressPath)
        }
//        vehicle_input?.car?.vehicleLicenseImg=result?.image?.compressPath
//        vehicle_input?.setVehicleLicense(result?.image?.compressPath)
    }

    private fun init() {
        titlebar_title.text = getString(R.string.vehicle_certification)
        certificationProgressView.setProress(2)

        vehicle_input.fold()
        tv_next.setOnClickListener {
            if (vehicle_input.canSubmit(this@VehicleCertificationActivity)) {
                ll_vehicle_container?.run {
                    if (childCount>0){
                        for (i in 0 until childCount){
                            val v = this.getChildAt(i) as VehicleInfoView
                            certificationInfo?.etcCardApplyVehicleVoList?.add(v.car)
                        }
                    }
                }
                startActivity(Intent(this, InfoCertificationActivity::class.java).putExtra("info", certificationInfo))
            }
        }
        vehicle_input.onUploadListener { currentVehicle=vehicle_input;showTakePhoto(takePhoto) }

        tv_add_vehicle.setOnClickListener {
            if (vehicle_input.canSubmit(this@VehicleCertificationActivity)) {
                LogHelper.e("Car = " + Gson().toJson(vehicle_input.getCar()))
                var vehicle = VehicleInfoView(this)
                vehicle.unFold()
                vehicle.setCar(vehicle_input.car)

                vehicle.setOnShowOrHideListener {
                    vehicle.showOrHide()
                    vehicle.setTag(0)
                    if (ll_vehicle_container.childCount > 0) {
                        for (i in 0 until ll_vehicle_container.childCount) {
                            ll_vehicle_container.getChildAt(i).let {
                                it as VehicleInfoView
                                if (it.getTag() != 0) {
                                    it.unFold()
                                }
                            }
                        }
                    }
                    vehicle_input.unFold()
                }
                vehicle.onUploadListener { currentVehicle=vehicle;showTakePhoto(takePhoto) }
                ll_vehicle_container.addView(vehicle, 0)
            }
        }

    }
}
