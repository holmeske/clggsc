package com.sc.clgg.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.FaultSelectVehicleAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Fault
import com.sc.clgg.bean.FaultDetail
import com.sc.clgg.bean.Vehicle
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.activity_fault_diagnosis.*
import kotlinx.android.synthetic.main.dialog_select_vehicle.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaultDiagnosisActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fault_diagnosis)
        initTitle("故障诊断")
        titlebar_right.text = "选择车辆"
        v_line.visibility = View.VISIBLE

        val a1 = TranslateAnimation(0f, 300f, 0f, 0f)
        a1.duration = 1500
        a1.repeatCount = 0//动画的重复次数
        a1.fillAfter = true//设置为true，动画转化结束后被应用

        val a2 = TranslateAnimation(300f, 0f, 0f, 0f)
        a2.duration = 3000
        a2.repeatCount = 0//动画的重复次数
        a2.fillAfter = true//设置为true，动画转化结束后被应用
        a2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                iv_scan.startAnimation(a1)//开始动画
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        a1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                iv_scan.startAnimation(a2)//开始动画
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

        val animation = TranslateAnimation(-200f, 600f, 0f, 0f)
        animation.duration = 2000
        animation.repeatCount = -1//动画的重复次数
        animation.fillAfter = true//设置为true，动画转化结束后被应用
        //animation.interpolator= DecelerateInterpolator()

        diagnosis.setOnClickListener {
            //            iv_scan.startAnimation(a1)
//            iv_scan.visibility = View.VISIBLE
//            iv_scan.startAnimation(animation)

            if (mSelectVehicleAdapter?.selectedList!!.isNotEmpty()) {
                faults.clear()
                diagnosis.text = "诊断中..."
                diagnosis(mSelectVehicleAdapter?.selectedList!![0].vinNumber!!, 0)
            }
        }

        initSelectVehicleView()

        loadVehicleList()
    }

    private var isAllSelected: Boolean = false

    private fun initSelectVehicleView() {

        /*点击选择车辆*/
        titlebar_right?.setOnClickListener {
            if(vehicleList.isNullOrEmpty()){
                toast("正在加载车辆列表")
                return@setOnClickListener
            }

            select_root?.visibility = View.VISIBLE
            clt_all_select?.visibility = View.VISIBLE
            tv_sure?.visibility = View.VISIBLE
            isAllSelected = false
            clt_all_select_check?.setImageResource(R.drawable.checkbox_vain)
            et_select_vehicle?.setText("")

            /*初始化数据，全部设置未选中*/
            for (b in vehicleList!!) {
                b.isChecked = false
            }
            mSelectVehicleAdapter?.refresh(vehicleList)
        }

        select_root?.setOnClickListener {
            select_root.visibility = View.GONE
        }

        clt_all_select?.setOnClickListener {
            isAllSelected = !isAllSelected
            if (isAllSelected) {
                /*全选*/
                clt_all_select_check?.setImageResource(R.drawable.checkbox_chosen)
                mSelectVehicleAdapter?.allSelected(vehicleList)
            } else {
                /*全不选*/
                clt_all_select_check?.setImageResource(R.drawable.checkbox_vain)
                mSelectVehicleAdapter?.allUnSelected(vehicleList)
            }
        }

        /*没有全选*/
        mSelectVehicleAdapter?.setSelectAllListener {
            clt_all_select_check.setImageResource(R.drawable.checkbox_vain)
        }

        /*确定按钮*/
        tv_sure.setOnClickListener {
            LogHelper.e(Gson().toJson(mSelectVehicleAdapter?.selectedList))
            select_root.visibility = View.GONE

            val stringBuilder = StringBuilder()

            stringBuilder.append("已选择")
            if (mSelectVehicleAdapter?.selectedList!!.size < 4) {
                mSelectVehicleAdapter?.selectedList?.forEach {

                    if ((mSelectVehicleAdapter?.selectedList!!.size - 1) == (mSelectVehicleAdapter?.selectedList!!.indexOf(it))) {
                        stringBuilder.append(it?.carNumber)
                    } else {
                        stringBuilder.append(it?.carNumber).append("、")
                    }
                }
                stringBuilder.append("等${mSelectVehicleAdapter?.selectedList!!.size}辆车")
            } else {

                stringBuilder.append(mSelectVehicleAdapter?.selectedList!![0].carNumber).append("、")
                        .append(mSelectVehicleAdapter?.selectedList!![1].carNumber).append("、")
                        .append(mSelectVehicleAdapter?.selectedList!![2].carNumber)
                        .append("等${mSelectVehicleAdapter?.selectedList!!.size}辆车")
            }

            tv_des.text = stringBuilder
            tv_des.textColor = Color.parseColor("#666666")

        }

        et_select_vehicle.setTextChangeListener {
            if (it.length == 0) {
                mSelectVehicleAdapter?.refresh(vehicleList)
            }
            var searchList = ArrayList<Vehicle.Bean>()
            for (data in vehicleList!!) {
                if (data.carNumber!!.contains(it)) {
                    searchList.add(data)
                }
            }
            mSelectVehicleAdapter?.refresh(searchList)
        }

        recyclerView?.let {
            mSelectVehicleAdapter = FaultSelectVehicleAdapter()
            it.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            it.adapter = mSelectVehicleAdapter
        }
    }

    private var mSelectVehicleAdapter: FaultSelectVehicleAdapter? = null

    private var call: Call<Vehicle>? = null

    private var vehicleList: List<Vehicle.Bean>? = null

    private fun loadVehicleList() {
        call = RetrofitHelper().myVehicle()
        call?.enqueue(object : retrofit2.Callback<Vehicle> {
            override fun onFailure(call: Call<Vehicle>?, t: Throwable?) {

            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Vehicle>?, response: Response<Vehicle>?) {
                response?.body()?.vehicleInfoList?.let {
                    vehicleList = it

                    mSelectVehicleAdapter?.refresh(vehicleList)
                    if (it.size == 1) {
                        faults.clear()
                        diagnosis.text = "诊断中..."
                        tv_des.text = "已选择${it[0].vinNumber}等${1}辆车"
                        diagnosisSingleVehicle(it[0])
                    }
                }
            }
        })

    }

    private var faults = ArrayList<FaultDetail>()
    private var calls: MutableList<Call<Fault>>? = null

    private var callSingleVehicle: Call<Fault>? = null
    private fun diagnosisSingleVehicle(vehicle: Vehicle.Bean) {

        tv_des.text = "共诊断1辆车，正在诊断第1辆车"

        callSingleVehicle = RetrofitHelper().faultDiagnose(vehicle.vinNumber)
        callSingleVehicle?.enqueue(object : Callback<Fault> {
            override fun onFailure(call: Call<Fault>?, t: Throwable?) {
                faults.add(FaultDetail(vehicle.carNumber, null))
                diagnosis.text = "开始诊断"
                tv_des.text = "请先选择您要诊断的车辆"
                tv_des.textColor = Color.parseColor("#ff3434")
            }

            override fun onResponse(call: Call<Fault>?, response: Response<Fault>?) {
                if (response?.body()?.success!! && response.body()?.data != null && response.body()?.data?.isNotEmpty()!!) {
                    faults.add(FaultDetail(vehicle.carNumber, response.body()?.data))
                }

                diagnosis.text = "开始诊断"
                tv_des.text = "请先选择您要诊断的车辆"
                tv_des.textColor = Color.parseColor("#ff3434")
                faults.filter { it.data != null && it.data?.size!! > 0 }.let {
                    LogHelper.e("过滤后的数据 = " + it.size)
                    if (it.size == 0) {
                        toast("您的爱车未检测出有故障")
                        return@let
                    }
                    startActivity(Intent(this@FaultDiagnosisActivity, FaultDetailActivity::class.java)
                            .putParcelableArrayListExtra("data", faults))
                }

            }
        })
    }

    private fun diagnosis(vin: String, position: Int) {

        var vehicle = mSelectVehicleAdapter?.selectedList!![position]
        var size = mSelectVehicleAdapter?.selectedList!!.size

        tv_des.text = "共诊断${mSelectVehicleAdapter?.selectedList?.size}辆车，正在诊断第${position + 1}辆车"

        var call = RetrofitHelper().faultDiagnose(vin)
        calls?.add(call)
        call.enqueue(object : Callback<Fault> {
            override fun onFailure(call: Call<Fault>?, t: Throwable?) {
                //faults.add(FaultDetail(vehicle.carNumber, null))
                if ((size - 1) == position) {
                    diagnosis.text = "开始诊断"
                    tv_des.text = "请先选择您要诊断的车辆"
                    tv_des.textColor = Color.parseColor("#ff3434")
                    startActivity(Intent(this@FaultDiagnosisActivity, FaultDetailActivity::class.java)
                            .putParcelableArrayListExtra("data", faults))
                }

                if (position < size - 1) {
                    diagnosis(mSelectVehicleAdapter?.selectedList!![position + 1].vinNumber!!, position + 1)
                }
            }

            override fun onResponse(call: Call<Fault>?, response: Response<Fault>?) {
                LogHelper.e("response = "+Gson().toJson(response?.body()))
                if (response?.body()?.success!! && response.body()?.data != null && response.body()?.data?.isNotEmpty()!!) {
                    faults.add(FaultDetail(vehicle.carNumber, response.body()?.data))
                }

                if ((mSelectVehicleAdapter?.selectedList?.size!! - 1) == position) {
                    diagnosis.text = "开始诊断"
                    tv_des.text = "请先选择您要诊断的车辆"
                    tv_des.textColor = Color.parseColor("#ff3434")
                    faults.filter { it.data != null && it.data?.size!! > 0 }.let {
                        LogHelper.e("过滤后的数据 = " + it.size)
//                        if (it.size == 0) {
                            //toast("您的爱车未检测出有故障")
                            //return@let
//                        }
                        startActivity(Intent(this@FaultDiagnosisActivity, FaultDetailActivity::class.java)
                                .putParcelableArrayListExtra("data", faults))
                    }

                }
                if (position < size - 1) {
                    diagnosis(mSelectVehicleAdapter?.selectedList!![position + 1].vinNumber!!, position + 1)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        callSingleVehicle?.cancel()

        call?.cancel()
        iv_scan.clearAnimation()

        calls?.forEach {
            it.cancel()
        }
    }

    override fun onBackPressed() {
        if (select_root != null && select_root.visibility == View.VISIBLE) {
            select_root.visibility = View.GONE
            return
        } else {
            super.onBackPressed()
        }
    }
}
