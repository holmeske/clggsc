//package com.sc.clgg.adapter
//
//import android.app.Activity
//import android.content.Intent
//import android.view.View
//import android.widget.TextView
//import com.sc.clgg.R
//import com.sc.clgg.activity.transportmanager.WaybillDetailActivity
//import com.sc.clgg.base.BaseAdNet
//import com.sc.clgg.bean.IncomeBean
//import org.jetbrains.anko.sdk25.coroutines.onClick
//
///**
// * 选择车辆 适配器
// *
// * @author ZhangYi 2014-12-4 14:08:39
// */
//class IncomeAdapter(activity: Activity, list: List<IncomeBean.DataBeanX.DataBean>) : BaseAdNet<IncomeBean.DataBeanX.DataBean>(activity) {
//
//    init {
//        mDatas = list
//    }
//
//    override fun setConvertView(convertView: View, position: Int): View {
//        convertView = mInflater.inflate(R.layout.item_income_list, null)
//        val tv_data = convertView.findViewById<TextView>(R.id.tv_data)
//        val tv_addresss = convertView.findViewById<TextView>(R.id.tv_addresss)
//        val tv_price = convertView.findViewById<TextView>(R.id.tv_price)
//
//        val bean = mDatas[position]
//
//        tv_data.text = bean.day
//        tv_addresss.text = bean?.address
//        tv_price.text = "+${bean?.amount}"
//
//        convertView.onClick {
//            mActivity.startActivity(Intent(mActivity, WaybillDetailActivity::class.java).putExtra("order_no", bean?.waybillNo)
//                    .putExtra("order_status", bean?.waybillStatus))
//        }
//        return convertView
//    }
//
//}
