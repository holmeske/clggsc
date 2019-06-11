package com.sc.clgg.activity.friendscircle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_picture.*


class PictureActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        iv_back.setOnClickListener { finish() }

        val urls = intent.getStringArrayListExtra("urls")

        viewpager2.adapter = object : RecyclerView.Adapter<MyHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, parent, false)
                return MyHolder(view)
            }

            override fun onBindViewHolder(holder: MyHolder, position: Int) {
                Glide.with(this@PictureActivity).load(urls[position]).into(holder.iv_picture)
                holder.iv_picture.setOnClickListener { finish() }
            }

            override fun getItemCount(): Int {
                return urls.size
            }
        }
        viewpager2.setCurrentItem(intent.getIntExtra("index", 0), false)
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_picture: ImageView = itemView.findViewById(R.id.iv_picture)
    }

}
