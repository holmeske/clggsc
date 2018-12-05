package com.sc.clgg.activity.etc

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.ApplyStateAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.ApplyState
import kotlinx.android.synthetic.main.activity_apply_state.*
import kotlinx.android.synthetic.main.view_titlebar.*
import java.util.*

class ApplyStateActivity : BaseImmersionActivity() {
    private var mApplyStateAdapter=ApplyStateAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_state)

        titlebar_title.text="ETC 申请状态查询"


        val dataList = ArrayList<ApplyState>()

        dataList.add(ApplyState("2018-10-11 13:18:00","沪A13246","6122 3909 7930 390","鲁通B卡","身份证不清晰，请重新上传，谢谢！",1))
        dataList.add(ApplyState("2018-10-11 13:18:00","沪A13246","6122 3909 7930 390","鲁通B卡","身份证不清晰，请重新上传，谢谢！",2))
        dataList.add(ApplyState("2018-10-11 13:18:00","沪A13246","6122 3909 7930 390","鲁通B卡","身份证不清晰，请重新上传，谢谢！",3))
        dataList.add(ApplyState("2018-10-11 13:18:00","沪A13246","6122 3909 7930 390","鲁通B卡","身份证不清晰，请重新上传，谢谢！",4))
        dataList.add(ApplyState("2018-10-11 13:18:00","沪A13246","6122 3909 7930 390","鲁通B卡","身份证不清晰，请重新上传，谢谢！",5))

        rv.layoutManager=LinearLayoutManager(this)
        rv.adapter=mApplyStateAdapter

        mApplyStateAdapter.notifyItemInserted(dataList)
    }
}
