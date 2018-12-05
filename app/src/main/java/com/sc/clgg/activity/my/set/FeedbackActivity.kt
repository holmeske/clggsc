package com.sc.clgg.activity.my.set

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.sc.clgg.R
import com.sc.clgg.activity.TakePhotoActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.setTextChangeListener
import com.sc.clgg.util.showTakePhoto
import kotlinx.android.synthetic.main.activity_feedback.*
import org.devio.takephoto.model.TResult
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response
import java.io.File


class FeedbackActivity : TakePhotoActivity() {
    private var pathList = arrayListOf<String>()

    override fun takeSuccess(result: TResult?) {
        super.takeSuccess(result)
        iv.visibility = View.GONE
        result?.images?.forEach { c ->
            LogHelper.e("image = " + c.compressPath + "     " + File(c.compressPath).length())
            addImageView(c.compressPath)
            pathList.add(c.compressPath)
        }
    }



    private fun addImageView(path: String) {
        val imageView = ImageView(this@FeedbackActivity)
        imageView.setImageBitmap(BitmapFactory.decodeFile(path))
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setBackgroundColor(ContextCompat.getColor(application, R.color.gray_aaa))

        val layoutParams = LinearLayout.LayoutParams(
                MeasureHelper.dp2px(this@FeedbackActivity, 60f),
                MeasureHelper.dp2px(this@FeedbackActivity, 60f))
        layoutParams.setMargins(0, 0, 4, 0)
        imageView.layoutParams = layoutParams

        imageView.setOnClickListener {
            ll_images.removeView(imageView);if (ll_images.childCount == 0) {
            iv.visibility = View.VISIBLE
        }
        }

        ll_images.addView(imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        initTitle("意见反馈")

        et.setTextChangeListener {
            tv_number.text = it.length.toString()
        }

        tv_commit.setOnClickListener {
            feedBack(pathList)
        }

        iv.setOnClickListener {
            showTakePhoto(takePhoto,5)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        http?.cancel()
    }

    private var http: Call<Check>? = null
    private fun feedBack(files: List<String>) {
        if (files.isEmpty() && et?.text?.toString()?.isBlank()!!) {
            toast("请留下您宝贵的建议")
            return
        }
        http = RetrofitHelper().feedBack(files, et?.text?.toString())

        showProgressDialog()
        http?.enqueue(object : retrofit2.Callback<Check> {
            override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                response?.body().let {
                    if (it?.success!!) {
                        toast("上传成功")
                    } else {
                        toast("上传失败")
                    }
                }
                hideProgressDialog()
                finish()
            }

            override fun onFailure(call: Call<Check>?, t: Throwable?) {
                hideProgressDialog()
            }
        })
    }
}
