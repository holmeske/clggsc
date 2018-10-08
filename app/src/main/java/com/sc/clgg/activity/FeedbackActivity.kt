package com.sc.clgg.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.activity_feedback.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*


class FeedbackActivity : BaseImmersionActivity() {
    var imagePicker: ImagePicker? = null
    private var handler: Handler? = null
    private var compressedFiles = arrayListOf<String>()
    private var paths = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        initTitle("意见反馈")

        et.setTextChangeListener {
            tv_number.text = it.length.toString()
        }

        tv_commit.onClick {
//            if (ll_images?.childCount == images?.size) {
                feedBack(compressedFiles)
//            }
        }

        imagePicker = ImagePicker.getInstance()
        imagePicker?.imageLoader = PersonalDataActivity.GlideImageLoader()   //设置图片加载器
        imagePicker?.isShowCamera = false  //显示拍照按钮
        imagePicker?.isCrop = false        //允许裁剪（单选才有效）
        imagePicker?.isSaveRectangle = true //是否按矩形区域保存
        imagePicker?.selectLimit = 5    //选中数量限制
        imagePicker?.isMultiMode = true
        imagePicker?.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
        imagePicker?.focusWidth = 280   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker?.focusHeight = 280  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker?.outPutX = 800//保存文件的宽度。单位像素
        imagePicker?.outPutY = 800//保存文件的高度。单位像素

        iv.onClick {
            val intent = Intent(this@FeedbackActivity, ImageGridActivity::class.java)
            intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images)
            startActivityForResult(intent, 100)
        }


        handler = object : Handler() {

            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    0 -> {

                        iv.alpha = 0f
                        images?.forEach {
                            LogHelper.e("" + it.path)
                            paths.add(it.path)
                        }

                        Luban.with(this@FeedbackActivity).load(paths)
                                .ignoreBy(100)
                                .setCompressListener(object : OnCompressListener {
                                    override fun onStart() {
                                        Log.e("tag", "onStart ")
                                    }

                                    override fun onSuccess(file: File) {
                                        Log.e("tag", "file " + file.name + " " + file.length() / 1024)

                                        compressedFiles.add(file.absolutePath)

                                        var imageView = ImageView(this@FeedbackActivity)
                                        imageView.setImageBitmap(BitmapFactory.decodeFile(file.absolutePath))
                                        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                                        imageView.setBackgroundColor(resources.getColor(R.color.gray_aaa))

                                        val layoutParams = LinearLayout.LayoutParams(
                                                MeasureHelper.dp2px(this@FeedbackActivity, 60f),
                                                MeasureHelper.dp2px(this@FeedbackActivity, 60f))
                                        layoutParams.setMargins(0, 0, 4, 0)
                                        imageView.layoutParams = layoutParams

                                        ll_images.addView(imageView)

                                        hideProgressDialog()
                                    }

                                    override fun onError(e: Throwable) {
                                        Log.e("tag", "onError ")
                                        hideProgressDialog()
                                    }
                                }).launch()


                    }
                }
            }
        }
    }

    internal var images: ArrayList<ImageItem>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            try {
                if (data != null && requestCode == 100) {
                    images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>

                    LogHelper.e("cacheDir = " + cacheDir)

                    paths.clear()
                    compressedFiles.clear()
                    ll_images.removeAllViews()
                    showProgressDialog("正在压缩...")
                    handler?.sendEmptyMessage(0)
                }
            } catch (e: Exception) {
                LogHelper.e(e)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun feedBack(files: List<String>) {
        if (files.isEmpty() && et?.text?.toString()?.isBlank()!!) {
            toast("请留下您宝贵的建议")
            return
        }
        val call = RetrofitHelper().feedBack(files, et?.text?.toString())

        showProgressDialog()
        call.enqueue(object : retrofit2.Callback<Check> {
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
