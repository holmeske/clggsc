package com.sc.clgg.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.sc.clgg.R
import com.sc.clgg.bean.Check
import com.sc.clgg.http.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_comment.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response

class CommentActivity : Activity(), View.OnLayoutChangeListener {
    private var circleMessageId: Int = 0
    private var commentUserId: Int = 0

    override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        LogHelper.e("left = " + left + "  top = " + top + "  right = " + right + "  bottom = " + bottom + "\n"
                + "oldLeft = " + oldLeft + "  oldTop = " + oldTop + "  oldRight = " + oldRight + "  oldBottom = " + oldBottom)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        circleMessageId = intent.getIntExtra("circleMessageId", 0)
        commentUserId = intent.getIntExtra("commentUserId", 0)

        root?.onClick {
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_input, InputMethodManager.SHOW_FORCED)
            imm.hideSoftInputFromWindow(et_input.windowToken, 0)
            finish()
        }
        root?.addOnLayoutChangeListener(this)

        tv_send.onClick {
            var comment = et_input?.text?.toString()

            if (!comment.isNullOrEmpty())

                RetrofitHelper().sendOpinion(circleMessageId, commentUserId, comment)
                        .enqueue(object : retrofit2.Callback<Check> {
                            override fun onFailure(call: Call<Check>?, t: Throwable?) {
                                toast("评论失败")
                            }

                            override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                                response?.body()?.success?.let {
                                    if (it) toast("评论成功") else toast("评论失败")
                                }
                            }
                        })

            finish()
        }
    }

}
