package com.sc.clgg.activity

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sc.clgg.R
import com.sc.clgg.adapter.PublishDynamicAdapter
import com.sc.clgg.bean.Check
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.showTakePhoto
import kotlinx.android.synthetic.main.activity_publish_dynamic.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.devio.takephoto.model.TResult
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response


class PublishDynamicActivity : TakePhotoActivity() {
    private var adapter: PublishDynamicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_dynamic)
        initTitle("发布动态")

        titlebar_right?.text = "发布"

        titlebar_right.setOnClickListener {
            if (adapter?.publishImageList!!.size < 2 && et.text.toString() == "") {
                toast("请添加图片或文字")
                return@setOnClickListener
            }

            publishDynamic(adapter?.publishImageList!!)
        }

        adapter = PublishDynamicAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter?.refresh(arrayListOf(""))
        adapter?.setCallbackListener { showTakePhoto(takePhoto, 9) }
    }

    override fun takeSuccess(result: TResult?) {
        super.takeSuccess(result)

        val paths = adapter?.publishImageList as ArrayList<String>

        result?.images?.forEach {
            if (paths.size < 9) {
                paths.add(it.compressPath)
            }
        }
        if (paths.size < 9 && !paths.contains("")) {
            paths.add("")
        }
        adapter?.refresh(paths)
    }

    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }

    private var http: Call<Check>? = null

    /**
     * 发布动态
     */
    private fun publishDynamic(files: List<String>) {
        showProgressDialog()
        http = RetrofitHelper().publishDynamic(files, et?.text?.toString()).apply {
            enqueue(object : retrofit2.Callback<Check> {
                override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                    hideProgressDialog()
                    response?.body()?.let {
                        if (it.success) {
                            toast("发布动态成功")
                        } else {
                            toast("${it.msg}")
                        }
                    }
                    finish()
                }

                override fun onFailure(call: Call<Check>?, t: Throwable?) {
                    hideProgressDialog()
                }
            })
        }
    }
}
