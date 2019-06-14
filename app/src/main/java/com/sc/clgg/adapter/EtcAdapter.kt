package com.sc.clgg.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sc.clgg.R
import com.sc.clgg.activity.MainActivity
import com.sc.clgg.activity.etc.*
import com.sc.clgg.adapter.EtcAdapter.MyHolder
import com.sc.clgg.bean.CarNetEvent
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.ConfigUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author：lvke
 * @date：2018/9/12 19:29
 */
class EtcAdapter : RecyclerView.Adapter<MyHolder>() {
    private var mContext: Context? = null
    private val names = arrayOf("申请ETC卡", "充值 · 圈存", "预充值", "订单查询", "余额查询", "我的ETC卡", "我的车队", "开卡审核")

    private val drawables = intArrayOf(R.drawable.etc_icon1, R.drawable.etc_icon2, R.drawable.etc_icon3, R.drawable.etc_icon4, R.drawable.etc_icon5, R.drawable.etc_icon6, R.drawable.etc_icon7, R.drawable.etc_icon8)

    private val activitys = arrayOf<Class<*>>(CardIntroduceActivity::class.java, RechargeActivity::class.java, MyCardActivity::class.java, RechargeOrderActivity::class.java, BalanceQueryPreActivity::class.java, MyCardActivity::class.java, MainActivity::class.java, ApplyStateActivity::class.java)

    private val mItemListener = View.OnClickListener { v ->
        val pos = v.tag as Int
        if (pos == 6) {
            mContext?.startActivity(Intent(mContext, MainActivity::class.java))
            EventBus.getDefault().postSticky(CarNetEvent(0))
        }
        if (ConfigUtil().loggedIn(mContext)) {
            when (pos) {
                2 -> mContext?.startActivity(Intent(mContext, activitys[pos]).putExtra("click", true))
                5 -> mContext?.startActivity(Intent(mContext, activitys[pos]).putExtra("click", false))
                else -> mContext?.startActivity(Intent(mContext, activitys[pos]))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        mContext = parent.context
        return MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_etc, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        with(holder.name) {
            text = names[holder.adapterPosition]
            setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    mContext?.let { ContextCompat.getDrawable(it, drawables[holder.adapterPosition]) }, null, null)
            tag = holder.adapterPosition
            setOnClickListener(mItemListener)
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)

        init {
            name.width = MeasureHelper.getScreenWidth(itemView.context) / 4
        }

    }

}
