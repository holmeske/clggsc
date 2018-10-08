package com.sc.clgg.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sc.clgg.R
import com.sc.clgg.adapter.ViewPagerAdapter
import com.sc.clgg.tool.helper.LogHelper
import kotlinx.android.synthetic.main.activity_picture.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*



class PictureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        iv_back.onClick { finish() }
//        Glide.with(this).load(intent.getStringExtra("url")).into(iv)

        val  urls=intent.getStringArrayListExtra("urls")
        LogHelper.e("urls = "+Gson().toJson(urls))
        val viewList = ArrayList<View>()
        urls?.forEach {
            LogHelper.e(it)
            val iv = ImageView(this)
            iv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            iv.scaleType = ImageView.ScaleType.FIT_CENTER
            iv.onClick { finish() }
            Glide.with(this).load(it).into(iv)
            viewList.add(iv)
        }

        viewPager.adapter=ViewPagerAdapter(viewList)
        viewPager.offscreenPageLimit=viewList.size
        viewPager.currentItem=0
    }
}
