package com.sc.clgg.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.sc.clgg.R
import com.sc.clgg.adapter.PublishDynamicAdapter
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_publish_dynamic.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*
import java.util.regex.Pattern


class PublishDynamicActivity : BaseImmersionActivity() {
    var imagePicker: ImagePicker? = null
    private var handler: Handler? = null
    private var compressedFiles = arrayListOf<String>()
    private var paths = arrayListOf<String>()
    private var adapter: PublishDynamicAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_dynamic)
        initTitle("发布动态")

        titlebar_right?.text = "发布"

        et?.filters = arrayOf(inputFilter, InputFilter.LengthFilter(140))

        titlebar_right.onClick {

            if (compressedFiles.size < 2 && et.text.toString().equals("")) {
                toast("请添加图片或文字")
                return@onClick
            }

            if (compressedFiles.size >= paths.size) {
                adapter?.publishImageList?.forEach {
                    LogHelper.e("图片路径:" + it)
                }
                publishDynamic(adapter?.publishImageList!!)
            }

        }

        imagePicker = ImagePicker.getInstance()
        imagePicker?.imageLoader = PersonalDataActivity.GlideImageLoader()   //设置图片加载器
        imagePicker?.isShowCamera = true  //显示拍照按钮
        imagePicker?.isCrop = false        //允许裁剪（单选才有效）
        imagePicker?.isSaveRectangle = true //是否按矩形区域保存
        imagePicker?.selectLimit = 9    //选中数量限制
        imagePicker?.isMultiMode = true
        imagePicker?.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
        imagePicker?.focusWidth = 480   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker?.focusHeight = 480  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker?.outPutX = 800//保存文件的宽度。单位像素
        imagePicker?.outPutY = 800//保存文件的高度。单位像素

        adapter = PublishDynamicAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter?.refresh(arrayListOf(""))
        adapter?.setCallbackListener { JumpImageGrid() }

        handler = object : Handler() {

            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    0 -> {

                        images?.forEach {
                            LogHelper.e("" + it.path)
                            if (paths.size < 9) {
                                paths.add(it.path)
                            } else {
                                toast("最多可选9张图片")
                            }
                        }

                        Luban.with(this@PublishDynamicActivity).load(paths)
                                .ignoreBy(100)
                                .setCompressListener(object : OnCompressListener {
                                    override fun onStart() {
                                        Log.e("tag", "onStart ")
                                    }

                                    override fun onSuccess(file: File) {
                                        Log.e("tag", "file " + file.name + " " + file.length() / 1024)

                                        compressedFiles.add(file.absolutePath)

                                        if (compressedFiles.size == paths.size) {
                                            if (compressedFiles.size < 9) {
                                                compressedFiles.add("")
                                            }
                                            adapter?.refresh(compressedFiles)
                                        }

                                    }

                                    override fun onError(e: Throwable) {
                                        Log.e("tag", "onError ")
                                    }
                                }).launch()


                    }
                }
            }
        }
    }

    private var emojiList = listOf(
            ":-D", ":-(", "O:-)", ":-P",
            "=-O", ":-*", "o_O", "B-)",
            ":-$", ":-\\", ":-[", ":O",
            "T_T", ":-X", ":-)", "@_@")
    internal var inputFilter: InputFilter = object : InputFilter {
        //`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？
        var emoji = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_`~!@#\$^&*()=|{}':;',\\\\[\\\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]")

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            LogHelper.e("" + source)
            if (emojiList.contains(source)) {
                return ""
            } else {
                var emojiMatcher = emoji.matcher(source)
                if (emojiMatcher.find()) {
                    toast("请输入合法字符")
                    return ""
                } else {
                    return null
                }

            }
        }

    }


    private fun JumpImageGrid() {
        val intent = Intent(this@PublishDynamicActivity, ImageGridActivity::class.java)
//        intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images)
        startActivityForResult(intent, 100)
    }

    internal var images: ArrayList<ImageItem>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            try {
                if (data != null && requestCode == 100) {
                    images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>

                    LogHelper.e("cacheDir = " + cacheDir)

//                    paths.clear()
                    compressedFiles.clear()
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

    private fun publishDynamic(files: List<String>) {
        val call = RetrofitHelper().publishDynamic(files, et?.text?.toString())

        showProgressDialog()
        call.enqueue(object : retrofit2.Callback<Check> {
            override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                response?.body().let {
                    if (it?.success!!) {
                        toast("发布动态成功")
                    } else {
                        toast("发布动态失败")
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
