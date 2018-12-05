package com.sc.clgg.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.TrafficDetailAdapter
import com.sc.clgg.bean.TrafficDetail
import kotlinx.android.synthetic.main.fragment_traffic_detail.*
import java.util.*

/**
 * @author：lvke
 * @date：2018/11/20 14:27
 */
class TrafficDetailFragment : Fragment() {
    private val mTrafficDetailAdapter = TrafficDetailAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_traffic_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = mTrafficDetailAdapter

        var bean = TrafficDetail("消费时间:  2018-01-21 13:45:21",
                "消费金额:  ￥28.00",
                "卡内余额:  ￥100.00",
                "通行区间:  上海-杭州",
                "入口通行时间:  2018-01-21 13:45:21",
                "出口通行时间:  2018-01-21 13:45:21",
                "结算省份:  杭州")

        var list= ArrayList<TrafficDetail>()

        for (i in 0..3){
            list.add(bean)
        }
        mTrafficDetailAdapter.notifyItemInserted(list)
    }
}