package com.sc.clgg.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.loader.ImageLoader
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.bean.PersonalData
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.setRoundedCornerPicture
import kotlinx.android.synthetic.main.activity_personal_data.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*


class PersonalDataActivity : BaseImmersionActivity() {

    companion object {
        var DATA: PersonalData.Data? = null
    }

    var imagePicker: ImagePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        initTitle("个人资料")
        imagePicker = ImagePicker.getInstance()

        titlebar_right?.let {
            it.text = "保存"

            it.onClick {
                updatePersonalData()
            }
        }

        iv_head.onClick {

            imagePicker?.imageLoader = GlideImageLoader()   //设置图片加载器
            imagePicker?.isShowCamera = true  //显示拍照按钮
            imagePicker?.isCrop = true        //允许裁剪（单选才有效）
            imagePicker?.isSaveRectangle = true //是否按矩形区域保存
            imagePicker?.selectLimit = 5    //选中数量限制
            imagePicker?.isMultiMode = false
            imagePicker?.style = CropImageView.Style.RECTANGLE  //裁剪框的形状
            imagePicker?.focusWidth = 280   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
            imagePicker?.focusHeight = 280  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
            imagePicker?.outPutX = 800//保存文件的宽度。单位像素
            imagePicker?.outPutY = 800//保存文件的高度。单位像素

            var intent = Intent(this@PersonalDataActivity, ImageGridActivity::class.java)
            intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images)
            startActivityForResult(intent, 100)
        }
        tv_phone.onClick {
            startActivity(Intent(this@PersonalDataActivity, ModifyAccountActivity::class.java))
        }
        tv_sex.onClick {
            startActivity(Intent(this@PersonalDataActivity, GenderActivity::class.java).putExtra("gender", DATA?.gender))
        }

        tv_name.onClick {
            startActivity(Intent(this@PersonalDataActivity, NickNameActivity::class.java)
                    .putExtra("type", "name").putExtra("name", DATA?.realName))
        }
        tv_nickname.onClick {
            startActivity(Intent(this@PersonalDataActivity, NickNameActivity::class.java)
                    .putExtra("type", "nickname").putExtra("nickname", DATA?.nickName))
        }
        tv_signature.onClick {
            startActivity(Intent(this@PersonalDataActivity, NickNameActivity::class.java)
                    .putExtra("type", "signature").putExtra("signature", DATA?.clientSign))
        }

        getPersonalData()
    }

    internal var images: ArrayList<ImageItem>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            try {
                if (data != null && requestCode == 100) {
                    images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<ImageItem>
                    LogHelper.e("" + images!![0].path)
//                    imagePicker?.getImageLoader()?.displayImage(this, images!![0].path, iv_head, 100, 100);

                    val call = RetrofitHelper().upload(File(images!![0].path))

                    call.enqueue(object : Callback<Check> {
                        override fun onResponse(call: Call<Check>?, response: Response<Check>?) {

                            response?.body().let {
                                Log.v("Upload", "success" + "\n" + Gson().toJson(it))
                                iv_head.setRoundedCornerPicture(this@PersonalDataActivity,it?.url)
                            }

                        }

                        override fun onFailure(call: Call<Check>?, t: Throwable?) {
                            Log.v("Upload", "onFailure")
                        }
                    })
                } else {
                    Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                LogHelper.e(e)
            }

        }
    }

    class GlideImageLoader : ImageLoader {

        override fun displayImage(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
            Glide.with(activity).load(Uri.fromFile(File(path))).into(imageView)

        }

        override fun displayImagePreview(activity: Activity, path: String, imageView: ImageView, width: Int, height: Int) {
            Glide.with(activity)                             //配置上下文
                    .load(Uri.fromFile(File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                    .into(imageView)
        }

        override fun clearMemoryCache() {}
    }

    private var call_getData: Call<PersonalData>? = null

    private fun getPersonalData() {
        call_getData = RetrofitHelper().personalData()
        call_getData?.enqueue(object : Callback<PersonalData> {
            override fun onFailure(call: Call<PersonalData>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<PersonalData>?, response: Response<PersonalData>?) {
                response?.body()?.data.let {
                    DATA = it

                    iv_head.setRoundedCornerPicture(this@PersonalDataActivity,it?.headImg)

                    tv_signature.text = it?.clientSign
                    tv_phone.text = it?.userName
                    tv_nickname.text = it?.nickName
                    tv_name.text = it?.realName

                    if (it?.gender == "1") {
                        tv_sex.text = "男"
                    } else if (it?.gender == "0") {
                        tv_sex.text = "女"
                    } else {
                        tv_sex.text = ""
                    }
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()

        tv_signature.text = DATA?.clientSign
        tv_phone.text = DATA?.userName
        tv_nickname.text = DATA?.nickName
        tv_name.text = DATA?.realName

        if (DATA?.gender == "1") {
            tv_sex.text = "男"
        } else if (DATA?.gender == "0") {
            tv_sex.text = "女"
        } else {
            tv_sex.text = ""
        }
    }

    private fun updatePersonalData() {
        if (DATA?.gender!!.isEmpty()) {
            toast("请设置性别")
            return
        }
        call = RetrofitHelper().update(DATA?.gender, DATA?.nickName, DATA?.realName, DATA?.clientSign)
        call?.enqueue(object : Callback<Check> {
            override fun onFailure(call: Call<Check>?, t: Throwable?) {
                toast("保存失败")
            }

            override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                response?.body()?.let {
                    if (it.success) {
                        toast("保存成功")
                        finish()
                    } else {
                        toast("保存失败")
                    }
                }
            }

        })
    }

    private var call: Call<Check>? = null

    override fun onDestroy() {
        super.onDestroy()
        call_getData?.cancel()
        call?.cancel()

    }
}
