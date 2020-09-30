package com.sc.clgg.activity.my.set

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import kotlinx.android.synthetic.main.activity_about_we.*

class AboutUsActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_we)

        initTitle("关于我们")
        tv_version.text = packageManager?.getPackageInfo(packageName, 0)?.versionName
    }

}
