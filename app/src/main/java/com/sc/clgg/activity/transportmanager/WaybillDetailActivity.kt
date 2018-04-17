package com.sc.clgg.activity.transportmanager

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.base.BaseAppCompatActivity
import com.sc.clgg.bean.OrderBean
import com.sc.clgg.bean.WayBillBean
import com.sc.clgg.bean.WaybillDetailBean
import com.sc.clgg.dialog.HintSelectDialog
import com.sc.clgg.http.HttpCallBack
import com.sc.clgg.http.HttpRequestHelper
import com.sc.clgg.util.Tools
import kotlinx.android.synthetic.main.activity_waybill_detail.*

/**
 * Author：lvke
 * CreateDate：2017/10/9
 */
class WaybillDetailActivity : BaseAppCompatActivity() {

    private var mHintSelectDialog: HintSelectDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waybill_detail)
        loadData()
    }

    private fun loadData() {
        HttpRequestHelper.getWaybillDetail(intent.getStringExtra("order_no"), intent.getStringExtra("order_status"), object : HttpCallBack() {
            override fun onStart() {
                super.onStart()
                showProgressDialog()
            }

            override fun onSuccess(body: String) {
                val mWaybillDetail = Gson().fromJson(body, WaybillDetailBean::class.java)
                val mWayBill = mWaybillDetail.wayBill

                tv_waybill_amount?.text = "${getString(R.string.rmb)} ${mWayBill?.orderAmount}"
                tv_waybill_amount_bottom?.text = "${getString(R.string.rmb)} ${mWayBill?.orderAmount}"

                tv_with_car_time?.text = if (TextUtils.isEmpty(mWaybillDetail.timeWait))
                    "调度时间: ---------------------"
                else
                    "调度时间: " + mWaybillDetail.timeWait
                tv_pickup_goods_time?.text = if (TextUtils.isEmpty(mWaybillDetail.timeProgress))
                    "提货时间: ---------------------"
                else
                    "提货时间: " + mWaybillDetail.timeProgress
                tv_sign_for_time?.text = if (TextUtils.isEmpty(mWaybillDetail.timeDeliveried))
                    "签收时间: ---------------------"
                else
                    "签收时间: " + mWaybillDetail.timeDeliveried

                when {
                    mWayBill?.waybillStatus == "10" -> {
                        tv_operation?.setText(R.string.pick_up_goods)
                        fl_operation?.visibility = View.VISIBLE
                        tv_waybill_amount?.visibility = View.GONE

                        tv_operation?.setOnClickListener(OperationListener(mWayBill.waybillStatus, mWayBill.waybillNo))
                    }
                    mWayBill?.waybillStatus == "20" -> {
                        tv_operation?.setText(R.string.sign_for)
                        fl_operation?.visibility = View.VISIBLE
                        tv_waybill_amount?.visibility = View.GONE
                        tv_operation?.setOnClickListener(OperationListener(mWayBill.waybillStatus, mWayBill.waybillNo))
                    }
                    else -> {
                        fl_operation?.visibility = View.GONE
                        tv_waybill_amount?.visibility = View.VISIBLE
                    }
                }

                tv_waybill_number?.text = "运单号:${mWayBill?.waybillNo}"

                setOrder(mWaybillDetail.order, mWayBill)
            }

            override fun onFinish() {
                super.onFinish()
                hideProgressDialog()
            }
        })
    }

    private fun setOrder(orderList: List<OrderBean>?, wayBillBean: WayBillBean?) {

        if (orderList?.isEmpty()!!) return

        tv_order_amount?.text = "订单列表(${orderList.size})"

        for (order in orderList) {
            val view = View.inflate(this, R.layout.view_waybill_detail_order, null)

            view.findViewById<View>(R.id.v_line).visibility = if (orderList.indexOf(order) == 0) View.GONE else View.VISIBLE

            val tvOrderNo = view.findViewById<View>(R.id.tv_order_no) as TextView
            tvOrderNo.text = order.orderNo.toString()

            val tv_order_addresser = view.findViewById<View>(R.id.tv_order_addresser) as TextView
            tv_order_addresser.text = "${getString(R.string.fa)}${if (order.dispatchContact == null) "" else
                order.dispatchContact + "\t\t" + wayBillBean?.dispatchProvince + "-" + wayBillBean?.dispatchCity + "-" + order.dispatchDistrict + " " + order.dispatchAddress}"

            val tv_order_consignee = view.findViewById<View>(R.id.tv_order_consignee) as TextView
            tv_order_consignee.text = "${getString(R.string.shou)}${
            if (order.consigneeContact == null) ""
            else order.consigneeContact + "\t\t" + wayBillBean?.consigneeProvince + "-" + wayBillBean?.consigneeCity + "-" + order.consigneeDistrict + " " + order.consigneeAddress}"

            root?.addView(view)

            val mDetailList = order.listDetail

            if (mDetailList == null || mDetailList.isEmpty()) {
                return
            }

            val llCargo = view.findViewById<View>(R.id.ll_cargo) as LinearLayout
            llCargo.removeAllViews()

            for (detail in mDetailList) {
                val v = View.inflate(this, R.layout.view_cargo_detail, null)
                (v.findViewById<View>(R.id.tv_order_name) as TextView).text = detail.detailCargoName.toString()
                if (mDetailList.indexOf(detail) == 0) {
                    v.findViewById<View>(R.id.dashline).visibility = View.GONE
                } else {
                    v.findViewById<View>(R.id.dashline).visibility = View.VISIBLE
                }

                if (detail.detailCargoCubic == 0f) {
                    (v.findViewById<View>(R.id.tv_order_size) as TextView).text = detail.detailCargoQuantity.toString() + "件/" + detail.detailCargoWeight + "吨"
                } else {
                    (v.findViewById<View>(R.id.tv_order_size) as TextView).text = (detail.detailCargoQuantity.toString() + "件/" + detail.detailCargoWeight + "吨/"
                            + detail.detailCargoCubic + "方")
                }
                (v.findViewById<View>(R.id.tv_order_type) as TextView).text = getDetailCargoName(detail.detailCargoType)
                llCargo.addView(v)
            }
        }
    }

    private fun getDetailCargoName(type: String?): String {
        var s = ""
        when (type) {
            "90" -> s = "电子\n产品"
            "92" -> s = "商品\n汽车"
            "93" -> s = "冷藏\n货物"
            "94" -> s = "大宗\n货物"
            "95" -> s = "快速\n消费品"
            "96" -> s = "农\n产品"
            "999" -> s = "其他"
        }
        return s
    }

    internal inner class OperationListener(private val waybillStatus: String?, private val waybillNo: String?) : View.OnClickListener {

        override fun onClick(view: View) {
            mHintSelectDialog = HintSelectDialog(this@WaybillDetailActivity, View.OnClickListener {
                if (waybillStatus == "10") {
                    HttpRequestHelper.pickUpGoods(waybillNo, object : HttpCallBack() {
                        override fun onStart() {
                            super.onStart()
                            showProgressDialog()
                        }

                        override fun onFinish() {
                            super.onFinish()
                            hideProgressDialog()
                        }

                        override fun onSuccess(body: String) {
                            mHintSelectDialog?.dismiss()
                            Tools.Toast("提货成功")
                            view.visibility = View.GONE
                        }
                    })
                } else if (waybillStatus == "20") {
                    HttpRequestHelper.signFor(waybillNo, object : HttpCallBack() {
                        override fun onStart() {
                            super.onStart()
                            showProgressDialog()
                        }

                        override fun onFinish() {
                            super.onFinish()
                            hideProgressDialog()
                        }

                        override fun onSuccess(body: String) {
                            mHintSelectDialog?.dismiss()
                            Tools.Toast("签收成功")
                            view.visibility = View.GONE
                        }
                    })
                }
            })
            if (waybillStatus == "10") {
                mHintSelectDialog?.show()
                mHintSelectDialog?.setHint("是否提货？")
            } else if (waybillStatus == "20") {
                mHintSelectDialog?.show()
                mHintSelectDialog?.setHint("是否签收？")
            }

        }
    }
}
