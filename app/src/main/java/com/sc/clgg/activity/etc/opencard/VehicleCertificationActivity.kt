package com.sc.clgg.activity.etc.opencard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.activity.TakePhotoActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.logcat
import com.sc.clgg.util.randomId
import com.sc.clgg.util.showTakePhoto
import com.sc.clgg.widget.VehicleImageView
import kotlinx.android.synthetic.main.activity_vehicle_certification.*
import kotlinx.android.synthetic.main.activity_vehicle_certification.view.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.devio.takephoto.model.TResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class VehicleCertificationActivity : TakePhotoActivity() {
    private var certificationInfo: CertificationInfo? = null
    private var currentVehicle: VehicleImageView? = null
    private var currentImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_certification)
        certificationInfo = intent.getParcelableExtra("info")
        LogHelper.e("“车辆认证”页面接收的数据 = ${Gson().toJson(certificationInfo)}")

        init()
    }

    /**
     * 初始化车辆选中状态
     */
    private fun initVehicleSelectState() {
        currentVehicle = null
        currentImageView = null
    }

    override fun takeCancel() {
        super.takeCancel()
        initVehicleSelectState()
    }

    override fun takeFail(result: TResult?, msg: String?) {
        super.takeFail(result, msg)
        initVehicleSelectState()
    }

    override fun takeSuccess(result: TResult?) {
        super.takeSuccess(result)
        LogHelper.e("图片路径 = " + result?.image?.compressPath)
        when (currentImageView?.id) {
            R.id.iv_vehicle_license -> {
                LogHelper.e("设置行驶证")
                currentVehicle?.car?.vehicleLicenseImg = result?.image?.compressPath
                currentVehicle?.car?.vehicleImageId = randomId()
                currentVehicle?.hideLicenseHint()
                Glide.with(this).load(File(currentVehicle?.car?.vehicleLicenseImg)).into(currentImageView!!)
                scanVehicleLicense(result?.image?.compressPath)
            }
            R.id.iv_vehicle_front -> {
                LogHelper.e("设置正面照")
                currentVehicle?.car?.vehicleFrontImg = result?.image?.compressPath
                currentVehicle?.car?.carNoImageId = randomId()
                currentVehicle?.hideFrontHint()
                Glide.with(this).load(File(currentVehicle?.car?.vehicleFrontImg)).into(currentImageView!!)
                scanVehicleFront(result?.image?.compressPath)
            }
            else -> {
                LogHelper.e("图片异常 = " + result?.image?.compressPath)
            }
        }
        LogHelper.e("上传照片后的数据 = ${Gson().toJson(certificationInfo)}")
    }

    private fun init() {
        titlebar_title.text = getString(R.string.vehicle_certification)
        certificationProgressView.setProress(2)

        //添加第一辆车
        ll_vehicle_container.addView(creatVehicleImageView().apply { setFirstVehicle();setTag(0) })

        tv_add_vehicle.visibility = if (certificationInfo?.userType == "2") View.VISIBLE else View.GONE

        //添加车辆
        tv_add_vehicle.setOnClickListener {
            ll_vehicle_container.addView(creatVehicleImageView())
            resetVehicleTag(ll_vehicle_container)
        }

        //点击下一步
        tv_next.setOnClickListener {
            LogHelper.e("childCount =  ${ll_vehicle_container.childCount}")
            val lastIndex = ll_vehicle_container.childCount - 1
            certificationInfo?.etcCardApplyVehicleVoList?.clear()
            ll_vehicle_container?.takeIf { it.childCount > 0 }?.run {
                for (i in 0 until ll_vehicle_container.childCount) {
                    LogHelper.e("i =  $i")
                    getChildAt(i).apply {
                        this as VehicleImageView
                        certificationInfo?.etcCardApplyVehicleVoList?.add(car)

                        if (checkThrough(applicationContext) && i == lastIndex) {
                            LogHelper.e("childCount =  ${ll_vehicle_container?.childCount}")
                            LogHelper.e("size =  ${certificationInfo?.etcCardApplyVehicleVoList?.size}")
                            LogHelper.e("将要传递到下个页面的数据 =  ${Gson().toJson(certificationInfo)}")
                            startActivity(Intent(this@VehicleCertificationActivity, InfoCertificationActivity::class.java)
                                    .putExtra("info", certificationInfo))
                        }
                    }
                }
            }
        }
    }

    /**
     * 重新为所有车辆打标记
     */
    private fun resetVehicleTag(container: LinearLayout) {
        container?.childCount?.takeIf { it > 0 }?.let {
            for (index in 0 until it) {
                container.get(index).run {
                    this as VehicleImageView
                    setTag(index)
                    if (index != it - 1) {
                        setVehicleTitle("车辆${index + 1}")
                    }
                }
            }
        }
    }

    /**
     * 创建自定义车辆控件
     */
    private fun creatVehicleImageView(): VehicleImageView {
        return VehicleImageView(this).apply {
            setVehicleLicense {
                currentImageView = it as ImageView
                currentVehicle = this
                showTakePhoto(takePhoto)
            }
            setVehicleFront {
                currentImageView = it as ImageView
                currentVehicle = this
                showTakePhoto(takePhoto)
            }
            OnDeleteListener {
                ll_vehicle_container.removeView(this)
                resetVehicleTag(ll_vehicle_container)
            }
            certificationInfo?.etcCardApplyVehicleVoList?.add(car)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vehicleLicenseInfoHttp?.cancel()
        vehicleFrontHttp?.cancel()
    }

    private var vehicleLicenseInfoHttp: Call<Map<String, Any>>? = null
    /**
     * 识别行驶证
     */
    private fun scanVehicleLicense(filePath: String?) {
        vehicleLicenseInfoHttp = RetrofitHelper().scan(File(filePath))
        vehicleLicenseInfoHttp?.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                hideProgressDialog()
                try {
                    val allMap = response.body()
                    if (allMap!!.containsKey("success") && allMap["success"] as Boolean) {
                        val identifyMap = allMap["identify"] as Map<String, Any>

                        if (identifyMap.containsKey("words_result")) {

                            val resultMap = identifyMap["words_result"] as Map<String, Any>

                            currentVehicle?.car?.let {
                                it.carNo = (resultMap["号牌号码"] as Map<String, String>)["words"]
                                it.carOwner = (resultMap["所有人"] as Map<String, String>)["words"]
                                it.address = (resultMap["住址"] as Map<String, String>)["words"]

                                it.vehicleType = (resultMap["车辆类型"] as Map<String, String>)["words"]
                                it.model = (resultMap["品牌型号"] as Map<String, String>)["words"]
                                it.vinCode = (resultMap["车辆识别代号"] as Map<String, String>)["words"]
                                it.engineNumber = (resultMap["发动机号码"] as Map<String, String>)["words"]
                            }
                        }

                    }
                } catch (e: Exception) {
                    LogHelper.e(e)
                }
                logcat("识别结果 = ", currentVehicle?.car)
                initVehicleSelectState()
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                hideProgressDialog()
                initVehicleSelectState()
            }
        })
    }

    private var vehicleFrontHttp: Call<Map<String, Any>>? = null
    /**
     * 识别行驶证
     */
    private fun scanVehicleFront(filePath: String?) {
        vehicleFrontHttp = RetrofitHelper().licensePlate(File(filePath))
        vehicleFrontHttp?.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                hideProgressDialog()
                try {
                    val allMap = response.body()
                    if (allMap!!.containsKey("success") && allMap["success"] as Boolean) {
                        val identifyMap = allMap["identify"] as Map<String, Any>

                        if (identifyMap.containsKey("words_result")) {

                            val resultMap = identifyMap["words_result"] as Map<String, String>

                            currentVehicle?.car?.let {
                                it.carNo = resultMap["number"]
                            }
                        }

                    }
                } catch (e: Exception) {
                    LogHelper.e(e)
                }

                initVehicleSelectState()
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                hideProgressDialog()
                initVehicleSelectState()
            }
        })
    }
}
