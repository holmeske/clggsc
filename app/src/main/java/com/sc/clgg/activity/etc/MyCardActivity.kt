package com.sc.clgg.activity.etc

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.MyCardAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CardList
import com.sc.clgg.bean.StatusBean
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.widget.PickerViewHelper
import kotlinx.android.synthetic.main.activity_my_card.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCardActivity : BaseImmersionActivity() {
    private var adapter = MyCardAdapter()
    private var cardListHttp: Call<CardList>? = null
    //卡列表
    private var mCardList: ArrayList<CardList.Card>? = null
    //车牌列表
    private var mCarNoList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)

        initView()
        initListener()
        getCardList()
    }

    private fun initView() {
        intent.getBooleanExtra("click", false).let {
            adapter.setItemEnable(it)
            if (it) {
                tv_add_card.visibility = View.GONE
                tv_add.visibility = View.GONE
            }
        }

        titlebar_title.text = getString(R.string.my_etc_card)
        titlebar_right?.run {
            visibility = View.VISIBLE
            text = "全部"
            setOnClickListener {
                tv_car_number.text = ""
                tv_car_type.text = ""
                adapter.refresh(mCardList)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initListener() {
        tv_add_card.setOnClickListener { startActivity(Intent(this, ReadCardActivity::class.java).putExtra("type", "add")) }

        iv_car_type.setOnClickListener {
            val data = arrayListOf("鲁通A卡", "鲁通B卡")
            PickerViewHelper().creat(this@MyCardActivity, data) { options1, _, _, _ ->
                tv_car_type.text = data[options1]

                when (options1) {
                    0 -> {
                        adapter.refresh(ArrayList())
                    }
                    1 -> {
                        if (tv_car_number.text.isNotBlank()) {
                            adapter.refresh(mCardList?.filter { tv_car_number.text == it.carNo })
                        } else {
                            adapter.refresh(mCardList)
                        }
                    }
                }
            }
        }

        iv_car_number.setOnClickListener {
            PickerViewHelper().creat(this@MyCardActivity, mCarNoList) { options1, _, _, _ ->
                tv_car_number.text = mCarNoList[options1]
                if (tv_car_type.text == "鲁通A卡") {
                    adapter.refresh(ArrayList())
                } else {
                    adapter.refresh(mCardList?.filter { mCarNoList[options1] == it.carNo })
                }
            }
        }

        adapter.setOnDelListener { c, pos ->
            val builder = AlertDialog.Builder(this)
                    .setMessage("确认解绑此卡？")
                    .setPositiveButton("确定") { _, _ ->
                        RetrofitHelper().unBindingCard(c.id!!).enqueue(object : Callback<StatusBean> {
                            override fun onFailure(call: Call<StatusBean>, t: Throwable) {
                                toast(R.string.network_anomaly)
                            }

                            override fun onResponse(call: Call<StatusBean>, response: Response<StatusBean>) {
                                response.body()?.run {
                                    if (success) {
                                        toast("解绑成功")
                                        adapter.remove(pos)
                                        mCardList = mCardList?.filter { it.cardNo != c.cardNo } as ArrayList<CardList.Card>
                                        mCarNoList = mCarNoList.filter { it != c.carNo } as ArrayList<String>
                                        tv_car_number.text = "";tv_car_type.text = ""
                                        adapter.refresh(mCardList)
                                    } else {
                                        toast("$msg")
                                    }
                                }
                            }
                        })

                    }
                    .setNegativeButton("取消") { _, _ -> }
            builder.show()
        }
    }

    private fun getCardList() {
        showProgressDialog()
        cardListHttp = RetrofitHelper().cardList.apply {
            enqueue(object : Callback<CardList> {
                override fun onFailure(call: Call<CardList>, t: Throwable) {
                    hideProgressDialog()
                    toast(R.string.network_anomaly)
                }

                override fun onResponse(call: Call<CardList>, response: Response<CardList>) {
                    hideProgressDialog()
                    response.body()?.run {
                        if (!success) {
                            toast("$msg")
                            return
                        }
                        mCardList = etcCardApplyList
                        adapter.refresh(mCardList)
                        mCardList?.forEach { mCarNoList.add(it.carNo!!) }
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cardListHttp?.cancel()
    }
}
