package com.sc.clgg.activity.etc

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sc.clgg.R
import com.sc.clgg.adapter.MyCardAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CarNumberList
import com.sc.clgg.bean.CardList
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.startActivity
import com.sc.clgg.widget.PickerViewHelper
import kotlinx.android.synthetic.main.activity_my_card.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCardActivity : BaseImmersionActivity() {
    private var adapter = MyCardAdapter()
    private var carNumberListHttp: Call<CarNumberList>? = null
    private var cardListHttp: Call<CardList>? = null
    private var currentCarNumber = ""
    private var currentCardType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_card)

        adapter?.setItemEnable(intent.getBooleanExtra("click",false))
        initView()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        cardListHttp?.cancel()
        carNumberListHttp?.cancel()
    }

    private fun initHttp() {
        getCardList("", "")
        carNumberListHttp = RetrofitHelper().carNumberList.apply {
            enqueue(object : Callback<CarNumberList> {
                override fun onFailure(call: Call<CarNumberList>, t: Throwable) {
                    toast(R.string.network_anomaly)
                }

                override fun onResponse(call: Call<CarNumberList>, response: Response<CarNumberList>) {
                    response.body()?.run {
                        if (success) {
                            etcCardApplyList?.run {
                                //tv_car_number.text = this[0]
                                if (this.isNotEmpty()) {
                                    iv_car_number.setOnClickListener {
                                        PickerViewHelper().creat(this@MyCardActivity, this)
                                        { options1, _, _, _ ->
                                            tv_car_number.text = this[options1]
                                            currentCarNumber = this[options1]
                                            getCardList(currentCardType, currentCarNumber)
                                        }
                                    }
                                }
                            }

                        } else {
                            toast("$msg")
                        }
                    }
                }
            })
        }
    }

    private fun getCardList(cardType: String?, carNo: String?) {
        showProgressDialog()
        cardListHttp = RetrofitHelper().getCardList(cardType, carNo).apply {
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
                        }
                        adapter?.refresh(etcCardApplyList)
                    }
                }
            })
        }
    }

    private fun initView() {
        initHttp()

        titlebar_title.text = getString(R.string.my_etc_card)

        iv_car_number.setOnClickListener { }
        iv_car_type.setOnClickListener { }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        tv_apply_state.setOnClickListener { startActivity(ApplyStateActivity::class.java) }

        iv_car_type.setOnClickListener {
            val data = arrayListOf("鲁通A卡", "鲁通B卡")
            PickerViewHelper().creat(this@MyCardActivity, data)
            { options1, _, _, _ ->
                tv_car_type.text = data[options1]

                when (options1) {
                    0 -> {
                        currentCardType = "2"
                        getCardList(currentCardType, currentCarNumber)
                    }
                    1 -> {
                        currentCardType = "3"
                        getCardList(currentCardType, currentCarNumber)
                    }
                }
            }
        }
    }
}
