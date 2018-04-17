package com.sc.clgg.activity.fragment

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bigkoo.pickerview.TimePickerView
import com.sc.clgg.R
import com.sc.clgg.activity.basic.MainActivity
import com.sc.clgg.activity.contact.TransportManageContact
import com.sc.clgg.activity.presenter.TransportManagePresenter
import com.sc.clgg.adapter.TransportManageAdapter
import com.sc.clgg.bean.MessageEvent
import com.sc.clgg.bean.TransportManageBean
import com.sc.clgg.dialog.LoadingDialogHelper
import kotlinx.android.synthetic.main.fragment_transport_manage.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.CalendarHelper
import java.text.SimpleDateFormat
import java.util.*

class TransportManageFragment : Fragment(), TransportManageContact {

    private var mTransportManageAdapter: TransportManageAdapter? = null
    private var presenter: TransportManagePresenter? = null
    private var mLoadingDialogHelper: LoadingDialogHelper? = null
    private var waybillStatus: String? = "10"
    private var mTimePickerView: TimePickerView? = null
    private var mBuilder: TimePickerView.Builder? = null
    private val startCalendar = Calendar.getInstance()//开始日期
    private val mSelectedCalendar = Calendar.getInstance()//系统当前时间

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_transport_manage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTransportManageAdapter = TransportManageAdapter()
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mTransportManageAdapter

        mTransportManageAdapter?.setOperationCallBack { presenter?.getOrderList(waybillStatus, null, null) }

        refreshlayout.setOnRefreshListener { presenter?.getOrderList(waybillStatus, null, null) }

        presenter = TransportManagePresenter(this)
        mLoadingDialogHelper = LoadingDialogHelper(activity)
        presenter?.getOrderList(waybillStatus, null, null)

        initDatePicker()
        initListener()
    }

    private fun initListener() {
        ll_wait_pickup_goods.onClick {
            titlebar_right_text?.visibility = View.GONE

            activity?.applicationContext?.let { it ->
                ll_wait_pickup_goods?.setSolid(getColor(it, R.color.red))
                ll_wait_sign_for?.setSolid(getColor(it, R.color.white))
                tv_all?.setSolid(getColor(it, R.color.white))

                tv_wait_pickup_goods?.setTextColor(getColor(it, R.color.white))
                tv_wait_sign_for?.setTextColor(getColor(it, R.color.black))
                tv_all?.setTextColor(getColor(it, R.color.black))

                tv_wait_pickup_goods_number?.setTextColor(getColor(it, R.color.red))
                tv_wait_pickup_goods_number?.setSolid(getColor(it, R.color.white))

                tv_wait_sign_for_number?.setTextColor(getColor(it, R.color.white))
                tv_wait_sign_for_number?.setSolid(getColor(it, R.color.red))
            }

            waybillStatus = "10"
            presenter?.getOrderList(waybillStatus, null, null)
        }

        ll_wait_sign_for.onClick {
            titlebar_right_text?.visibility = View.GONE

            activity?.applicationContext?.let {
                ll_wait_pickup_goods?.setSolid(getColor(it, R.color.white))
                ll_wait_sign_for?.setSolid(getColor(it, R.color.red))
                tv_all?.setSolid(getColor(it, R.color.white))

                tv_wait_pickup_goods?.setTextColor(getColor(it, R.color.black))
                tv_wait_sign_for?.setTextColor(getColor(it, R.color.white))
                tv_all?.setTextColor(getColor(it, R.color.black))

                tv_wait_pickup_goods_number?.setTextColor(getColor(it, R.color.white))
                tv_wait_pickup_goods_number?.setSolid(getColor(it, R.color.red))
                tv_wait_sign_for_number?.setTextColor(getColor(it, R.color.red))
                tv_wait_sign_for_number?.setSolid(getColor(it, R.color.white))
            }

            waybillStatus = "20"
            presenter?.getOrderList(waybillStatus, null, null)
        }

        tv_all.onClick {
            titlebar_right_text?.visibility = View.VISIBLE
            titlebar_right_text?.text = getString(R.string.all)

            activity?.applicationContext?.let {
                ll_wait_pickup_goods?.setSolid(getColor(it, R.color.white))
                ll_wait_sign_for?.setSolid(getColor(it, R.color.white))
                tv_all?.setSolid(getColor(it, R.color.red))
                tv_wait_pickup_goods_number?.setSolid(getColor(it, R.color.red))
                tv_wait_sign_for_number?.setSolid(getColor(it, R.color.red))

                tv_wait_pickup_goods?.setTextColor(getColor(it, R.color.black))
                tv_wait_sign_for?.setTextColor(getColor(it, R.color.black))
                tv_all?.setTextColor(getColor(it, R.color.white))
                tv_wait_pickup_goods_number?.setTextColor(getColor(it, R.color.white))
                tv_wait_sign_for_number?.setTextColor(getColor(it, R.color.white))
            }

            waybillStatus = null
            presenter?.getOrderList(waybillStatus, null, null)
        }

        titlebar_right_text.onClick { mTimePickerView?.show() }
    }

    override fun onResume() {
        super.onResume()
        if ((activity as MainActivity).currenMainTabIndex == 1) presenter?.getOrderList(waybillStatus, null, null)
    }

    private fun initDatePicker() {
        startCalendar.set(2013, 5, 18)
        mBuilder = TimePickerView.Builder(activity) { date, _ ->
            mSelectedCalendar.time = date
            titlebar_right_text?.text = SimpleDateFormat("yyyy年MM月", Locale.getDefault()).format(date)
            presenter?.getOrderList(waybillStatus, CalendarHelper.getFirstDayOfMonth(mSelectedCalendar), CalendarHelper.getLastDayOfMonth(mSelectedCalendar))
        }.setType(booleanArrayOf(true, true, false, false, false, false))
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(mSelectedCalendar)
                .setRangDate(startCalendar, Calendar.getInstance())
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null)
        mTimePickerView = mBuilder?.build()
    }

    override fun requestStart() {
        if ((activity as MainActivity).currenMainTabIndex == 1) mLoadingDialogHelper?.show()
    }

    override fun requestSuccess(bean: TransportManageBean?) {
        tv_wait_sign_for_number?.text = bean?.waybillCountProgress.toString()
        tv_wait_pickup_goods_number?.text = bean?.waybillCountWaiting.toString()

        EventBus.getDefault().post(MessageEvent(bean?.waybillCountProgress!!.plus(bean.waybillCountWaiting)))

        mTransportManageAdapter?.refresh(bean.waybillList, waybillStatus == null)
    }

    override fun requestFail(msg: String) {}

    override fun requestFinish() {
        mLoadingDialogHelper?.dismiss()
        if (refreshlayout.isRefreshing) refreshlayout.isRefreshing = false
    }

}
