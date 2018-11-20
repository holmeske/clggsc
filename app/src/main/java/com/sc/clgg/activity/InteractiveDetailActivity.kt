package com.sc.clgg.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.lzy.ninegrid.ImageInfo
import com.lzy.ninegrid.NineGridView
import com.lzy.ninegrid.NineGridViewAdapter
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.Check
import com.sc.clgg.bean.InteractiveDetail
import com.sc.clgg.dialog.AlertDialogHelper
import com.sc.clgg.retrofit.RetrofitHelper
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.ConfigUtil
import com.sc.clgg.util.setRoundedCornerPicture
import kotlinx.android.synthetic.main.activity_interactive_detail.*
import kotlinx.android.synthetic.main.item_interactive_detail.view.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class InteractiveDetailActivity : BaseImmersionActivity() {
    private var data: InteractiveDetail.A? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interactive_detail)
        titlebar_title.text = "详情"
        titlebar_left.setOnClickListener { finish() }
        loadData()

        iv_commen.setOnClickListener { cs_input.visibility = View.VISIBLE }
        cs_input.setOnClickListener {
            cs_input.visibility = View.GONE
            hideSoftInput(et_input)
        }
    }

    private var call: Call<InteractiveDetail>? = null
    private fun loadData() {

        call = RetrofitHelper().getSingleDetail(intent.getStringExtra("messageId"))
        call?.enqueue(object : Callback<InteractiveDetail> {
            override fun onFailure(call: Call<InteractiveDetail>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<InteractiveDetail>?, response: Response<InteractiveDetail>?) {
                response?.body()?.detail?.let {
                    data = it
                    send(data?.id!!, data?.userId!!)

                    iv_head.setRoundedCornerPicture(this@InteractiveDetailActivity, it.headImg)

                    tv_nickname.text = it.nickName
                    tv_describe.text = it.clientSign
                    tv_message.text = it.message
                    tv_time.text = it.dealTime
                    tv_official.visibility = if (it.type == 1) View.VISIBLE else View.INVISIBLE

                    it.driverCircleCommentList?.size?.let {
                        if (it > 99) {
                            tv_comment_count.text = "99+"
                        } else {
                            tv_comment_count.text = it.toString()
                        }
                    }
                    it.driverCircleLaudList?.size?.let {
                        if (it > 99) {
                            tv_laud_count.text = "99+"
                        } else {
                            tv_laud_count.text = it.toString()
                        }
                    }
                    it.driverCircleImagesList?.size?.run {
                        if (this > 0) {
                            nineGridView.visibility = View.VISIBLE
                            setImages(it, nineGridView)
                        } else {
                            nineGridView.visibility = View.GONE
                        }
                    }
                    if (isLike(it)) {
                        it.isLike = true
                        iv_laud.setImageResource(R.drawable.ico_like_yes)
                    } else {
                        it.isLike = false
                        iv_laud.setImageResource(R.drawable.ico_like_no)
                    }

                    iv_laud.setOnClickListener {
                        if (data?.isLike!!) {
                            removeLike(data!!, tv_laud_count, iv_laud, tv_lauds)
                        } else {
                            like(data!!, tv_laud_count, iv_laud, tv_lauds)
                        }
                    }

                    setLaudsLimit(setLauds(it, tv_lauds, iv_lauds), it)
                    setCommens(it, ll_commen)
                    setDelete(it.userId, it.id)
                }
            }

        })
    }

    private fun setLauds(bean: InteractiveDetail.A, tv: TextView, iv_lauds: ImageView): String {
        if (bean.driverCircleLaudList != null && bean.driverCircleLaudList!!.size > 0) {
            tv.visibility = View.VISIBLE
            iv_lauds.visibility = View.VISIBLE

            val sb = StringBuilder()
            for ((_, _, _, _, _, _, _, nickName) in bean.driverCircleLaudList!!) {
                sb.append(",").append(nickName
                        ?: "")
            }
            tv.text = sb.substring(1, sb.length)
        } else {
            tv.visibility = View.GONE
            iv_lauds.visibility = View.GONE
        }
        return tv.text.toString()
    }

    private fun setMessageLimit(bean: InteractiveDetail.A) {
        tv_message.post {
            val lineCount = tv_message.lineCount//行数
            //val maxLineCount = tv_message.maxLines
            //LogHelper.e("消息行数：" + lineCount + "   最大行数：" + maxLineCount);

            if (lineCount > 1) {
                tv_all_show.visibility = View.VISIBLE
                tv_all_show.setOnClickListener({
                    if (tv_all_show.text == "全文") {
                        tv_message.maxLines = 10
                        tv_all_show.text = "收起"
                    } else {
                        tv_message.maxLines = 6
                        tv_all_show.text = "全文"
                    }
                    tv_message.text = if (bean.message == null) "" else bean.message
                })
            } else {
                tv_all_show.visibility = View.GONE
            }
        }
    }

    private fun setTextViewMaxLinesEllipsize(context: String, tv_lauds: TextView) {
        tv_lauds.viewTreeObserver.addOnGlobalLayoutListener {
            tv_lauds.text = context
            if (tv_lauds.lineCount > tv_lauds.maxLines) {
                var lineEndIndex = tv_lauds.layout.getLineEnd(4 - 1)
                tv_lauds.text = "${context.subSequence(0, lineEndIndex - 1)}…"
            }
        }
    }

    private fun setLaudsLimit(context: String, bean: InteractiveDetail.A) {
        tv_lauds.post(Runnable {
            val lineCount = tv_lauds.lineCount//行数
            val maxLineCount = tv_lauds.maxLines
            LogHelper.e("消息行数：" + lineCount + "   最大行数：" + maxLineCount)

            if (lineCount > 4) {
                with(tv_all_show_commen) {
                    visibility = View.VISIBLE
                    setOnClickListener(View.OnClickListener {
                        if (tv_all_show_commen.text == "显示全部") {
                            tv_lauds.maxLines = lineCount

                            tv_all_show_commen.text = "收起"

                            tv_lauds.text = context
                        } else {
                            tv_lauds.maxLines = 4

                            tv_all_show_commen.text = "显示全部"

                            setTextViewMaxLinesEllipsize(context, tv_lauds)
                        }
                        LogHelper.e("消息行数：" + tv_lauds.lineCount + "   最大行数：" + tv_lauds.maxLines)
                        setLauds(bean, tv_lauds, iv_lauds)

                    })
                }
            } else {
                tv_all_show_commen.visibility = View.GONE
            }
        })
    }

    private fun setCommens(bean: InteractiveDetail.A, ll: LinearLayout) {

        ll.removeAllViews()
        if (bean.driverCircleCommentList != null && bean.driverCircleCommentList?.size!! > 0) {
            ll.addView(View.inflate(this, R.layout.view_line, null))

            for ((id, _, _, userId, createTime, _, comment, _, nickName, headImg) in bean.driverCircleCommentList!!) {
                var itemView = View.inflate(this, R.layout.item_interactive_detail, null)

                itemView.iv_head.setRoundedCornerPicture(this@InteractiveDetailActivity, headImg)

                itemView.tv_name.text = nickName
                itemView.tv_des.text = comment
                itemView.tv_time.text = createTime

                ll.addView(itemView)
                if (ConfigUtil().userid == userId.toString()) {
                    itemView.setOnClickListener {
                        AlertDialogHelper().show(this, "确定删除?") { _, _ ->
                            RetrofitHelper().removeOpinion(id.toString() + "").enqueue(object : Callback<Check> {
                                override fun onResponse(call: Call<Check>, response: Response<Check>) {
                                    if (response.body()!!.success) {
                                        toast("删除成功")
                                        ll.removeView(itemView)
                                    } else {
                                        toast("删除失败")
                                    }
                                }

                                override fun onFailure(call: Call<Check>, t: Throwable) {
                                    toast("删除失败")
                                }
                            })
                        }
                    }
                }
            }
        }
    }

    private fun like(bean: InteractiveDetail.A, tv: TextView, iv: ImageView, tv_lauds: TextView) {
        iv.isEnabled = false
        RetrofitHelper().like(bean.id, bean.userId).enqueue(object : Callback<Check> {
            override fun onResponse(call: Call<Check>, response: Response<Check>) {
                iv.isEnabled = true
                if (response.body()!!.success) {
                    bean.isLike = true
                    Toast.makeText(applicationContext, "点赞成功", Toast.LENGTH_SHORT).show()

                    tv_lauds.text = StringBuilder().append(tv_lauds.text).append(",").append(ConfigUtil().nickName)

                    iv.setImageResource(R.drawable.ico_like_yes)
                    if (tv.text != "99+") {
                        tv.text = (Integer.parseInt(tv.text.toString()) + 1).toString()
                    }
                } else {
                    Toast.makeText(applicationContext, "点赞失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Check>, t: Throwable) {
                iv.isEnabled = true
                Toast.makeText(applicationContext, "点赞失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeLike(bean: InteractiveDetail.A, tv: TextView, iv: ImageView, tv_lauds: TextView) {
        iv.isEnabled = false
        RetrofitHelper().removeLike(bean.id).enqueue(object : Callback<Check> {
            override fun onResponse(call: Call<Check>, response: Response<Check>) {
                iv.isEnabled = true
                if (response.body()!!.success) {
                    bean.isLike = false
                    Toast.makeText(applicationContext, "取消点赞成功", Toast.LENGTH_SHORT).show()

                    setLaudsFilte(bean, tv_lauds)

                    iv.setImageResource(R.drawable.ico_like_no)
                    if (tv.text != "99+") {
                        tv.text = (Integer.parseInt(tv.text.toString()) - 1).toString()
                    }
                } else {
                    Toast.makeText(applicationContext, "取消点赞失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Check>, t: Throwable) {
                iv.isEnabled = true
                Toast.makeText(applicationContext, "取消点赞失败", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setDelete(userId: Int, id: Int) {
        if (ConfigUtil().userid == userId.toString()) {
            tv_delete.visibility = View.VISIBLE
            tv_delete.setOnClickListener({
                AlertDialogHelper().show(this, "确定删除?") { _, _ ->
                    RetrofitHelper().removeMessage(id).enqueue(object : Callback<Check> {
                        override fun onResponse(call: Call<Check>, response: Response<Check>) {
                            if (response.body()!!.success) {
                                toast("删除成功")
                                finish()
                            } else {
                                toast("删除失败")
                            }
                        }

                        override fun onFailure(call: Call<Check>, t: Throwable) {
                            toast("删除失败")
                        }
                    })
                }
            })
        } else {
            tv_delete.visibility = View.GONE
        }
    }

    private fun setLaudsFilte(bean: InteractiveDetail.A, tv_lauds: TextView) {
        val sb = StringBuilder()
        for ((_, _, _, userId, _, _, _, nickName) in bean.driverCircleLaudList!!) {
            if (userId.toString() != ConfigUtil().userid) {
                sb.append(",").append(nickName ?: "")
            }
            if (sb.length > 0) {
                tv_lauds.visibility = View.VISIBLE
            } else {
                tv_lauds.visibility = View.GONE
            }
        }
        tv_lauds.text = sb.substring(1, sb.length)
    }

    private fun isLike(bean: InteractiveDetail.A): Boolean {
        for ((_, _, _, userId) in bean.driverCircleLaudList!!) {
            if (ConfigUtil().userid == userId.toString()) {
                return true
            }
        }
        return false
    }

    private fun setImages(bean: InteractiveDetail.A?, nineGridView: NineGridView) {
        val imageInfo = ArrayList<ImageInfo>()
        if (bean != null) {
            for ((_, _, _, imgUrl) in bean.driverCircleImagesList!!) {
                val info = ImageInfo()
                info.setThumbnailUrl(imgUrl)
                info.setBigImageUrl(imgUrl)
                imageInfo.add(info)
            }
        }
        nineGridView.setAdapter(ClickNineGridViewAdapter(this@InteractiveDetailActivity, imageInfo))
    }

    internal inner class ClickNineGridViewAdapter(context: Context, imageInfo: List<ImageInfo>) : NineGridViewAdapter(context, imageInfo) {

        override fun onImageItemClick(context: Context?, nineGridView: NineGridView?, index: Int, imageInfo: List<ImageInfo>?) {
            super.onImageItemClick(context, nineGridView, index, imageInfo)
            val urls = ArrayList<String>()
            for (info in imageInfo!!) {
                urls.add(info.getBigImageUrl())
            }
            startActivity(Intent(this@InteractiveDetailActivity, PictureActivity::class.java)
                    .putExtra("url", imageInfo[index].getBigImageUrl())
                    .putStringArrayListExtra("urls", urls)
            )
        }
    }


    private var sendCall: Call<Check>? = null
    private fun send(circleMessageId: Int, commentUserId: Int) {
        tv_send.setOnClickListener {
            var comment = et_input?.text?.toString()

            if (!comment.isNullOrEmpty())

                sendCall = RetrofitHelper().sendOpinion(circleMessageId, commentUserId, comment)
            sendCall?.enqueue(object : retrofit2.Callback<Check> {
                override fun onFailure(call: Call<Check>?, t: Throwable?) {
                    cs_input.visibility = View.GONE
                    toast("评论失败")
                }

                override fun onResponse(call: Call<Check>?, response: Response<Check>?) {
                    cs_input.visibility = View.GONE
                    response?.body()?.success?.let {
                        if (it) {
                            toast("评论成功")
                            loadData()
                            hideSoftInput(et_input)
                        } else {
                            toast("评论失败")
                        }
                    }
                }
            })


        }
    }

    private fun hideSoftInput(v: View) {
        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED)
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        call?.cancel()
        sendCall?.cancel()
    }
}
