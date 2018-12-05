package com.sc.clgg.mvvm.m

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.bean.Banner
import com.sc.clgg.retrofit.RetrofitHelper
import kotlinx.android.synthetic.main.activity_live_data.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveDataActivity : AppCompatActivity() {
    // Declaring it
    val liveDataA = MutableLiveData<Banner>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)

        // Optionally, one could use liveDataA.postValue(value)
        // to get it set on the UI thread
        liveDataA.observe(this, changeObserver)

        btn.setOnClickListener {
            RetrofitHelper().bannerList.enqueue(object : Callback<Banner> {
                override fun onFailure(call: Call<Banner>, t: Throwable) {

                }

                override fun onResponse(call: Call<Banner>, response: Response<Banner>) {
                    // Trigger the value change
                    liveDataA.value = response.body()
                }
            })
        }
    }

    private val changeObserver = Observer<Banner> { value ->
        value?.let {
            toast(Gson().toJson(it))
            btn.text = it.data?.banner?.get(0)?.name
        }
    }

}
