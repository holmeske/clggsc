package com.sc.clgg.activity.my.userinfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.activity.TakePhotoActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.bean.PersonalData
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.util.setRoundedCornerPicture
import com.sc.clgg.util.showTakePhotoWithCrop
import kotlinx.android.synthetic.main.activity_personal_data.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.devio.takephoto.model.TResult
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class PersonalDataActivity : TakePhotoActivity() {

    companion object {
        var DATA: PersonalData.Data? = null
    }

    private var uploadHttp: Call<Check>? = null
    private var getUserInfoHttp: Call<PersonalData>? = null
    private var updateInfoHttp: Call<Check>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_data)

        initTitle("个人资料")

        titlebar_right?.let { it ->
            it.text = "保存"

            it.setOnClickListener {
                updatePersonalData()
            }
        }

        tv_head.setOnClickListener {
            showTakePhotoWithCrop(takePhoto)
        }

        tv_phone.setOnClickListener {
            startActivity(Intent(this@PersonalDataActivity, ModifyAccountActivity::class.java))
        }
        tv_sex.setOnClickListener {
            startActivity(Intent(this@PersonalDataActivity, GenderActivity::class.java).putExtra("gender", DATA?.gender))
        }

        tv_name.setOnClickListener {
            startActivity(Intent(this@PersonalDataActivity, NickNameActivity::class.java)
                    .putExtra("type", "name").putExtra("name", DATA?.realName)
                    .putExtra("maxLength", 20))
        }
        tv_nickname.setOnClickListener {
            startActivity(Intent(this@PersonalDataActivity, NickNameActivity::class.java)
                    .putExtra("type", "nickname").putExtra("nickname", DATA?.nickName).putExtra("maxLength", 20))
        }
        tv_signature.setOnClickListener {
            startActivity(Intent(this@PersonalDataActivity, NickNameActivity::class.java)
                    .putExtra("type", "signature").putExtra("signature", DATA?.clientSign).putExtra("maxLength", 50))
        }

        tv_invite_code.setOnClickListener {
            startActivity(Intent(this@PersonalDataActivity, NickNameActivity::class.java)
                    .putExtra("type", "invite").putExtra("invite", DATA?.inviteCode).putExtra("maxLength", 15))
        }

        getPersonalInfo()
    }

    override fun takeSuccess(result: TResult?) {
        super.takeSuccess(result)
        uploadHttp = upload(File(result?.image?.compressPath))
    }

    /**
     * 上传文件
     */
    private fun upload(file: File?): Call<Check>? {
        return RetrofitHelper().upload(file)?.apply {
            enqueue(object : Callback<Check> {
                override fun onFailure(call: Call<Check>, t: Throwable) {
                }

                override fun onResponse(call: Call<Check>, response: Response<Check>) {
                    response.body()?.let {
                        Log.v("Upload", "success" + "\n" + Gson().toJson(it))
                        iv_head.setRoundedCornerPicture(this@PersonalDataActivity, it.url)
                    }
                }
            })
        }

    }

    /**
     * 获取个人信息
     */
    private fun getPersonalInfo() {
        getUserInfoHttp = RetrofitHelper().personalData()
        getUserInfoHttp?.enqueue(object : Callback<PersonalData> {
            override fun onFailure(call: Call<PersonalData>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<PersonalData>?, response: Response<PersonalData>?) {
                response?.body()?.data.let {
                    DATA = it

                    iv_head.setRoundedCornerPicture(this@PersonalDataActivity,it?.headImg)

                    it?.bindView()
                }
            }

        })
    }

    /**
     * 控件赋值
     */
    private fun PersonalData.Data.bindView() {
        tv_signature.text = clientSign
        tv_phone.text = userName
        tv_nickname.text = nickName
        tv_name.text = realName
        tv_invite_code.text = inviteCode

        when (gender) {
            "1" -> tv_sex.text = "男"
            "0" -> tv_sex.text = "女"
            else -> tv_sex.text = ""
        }
    }

    override fun onResume() {
        super.onResume()
        DATA?.bindView()
    }

    /**
     * 更新个人信息
     */
    private fun updatePersonalData() {
        if (DATA?.gender!!.isEmpty()) {
            toast("请设置性别")
            return
        }
        if (DATA?.nickName!!.isEmpty()) {
            toast("昵称不能为空")
            return
        }
        updateInfoHttp = RetrofitHelper().update(DATA?.gender, DATA?.nickName, DATA?.realName, DATA?.clientSign, DATA?.inviteCode)
        updateInfoHttp?.enqueue(object : Callback<Check> {
            override fun onFailure(call: Call<Check>?, t: Throwable?) {
                toast("保存失败")
            }

            override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                response?.body()?.let {
                    if (it.success) {
                        toast("保存成功")
                        finish()
                    } else {
                        toast("${it.msg}")
                    }
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        uploadHttp?.cancel()
        getUserInfoHttp?.cancel()
        updateInfoHttp?.cancel()
    }
}
