package com.sc.clgg.activity.usersettings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sc.clgg.R
import kotlinx.android.synthetic.main.activity_about_we.*

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_we)

        tv_version.text = packageManager?.getPackageInfo(packageName, 0)?.versionName
    }

}
