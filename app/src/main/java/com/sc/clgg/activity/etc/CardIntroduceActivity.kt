package com.sc.clgg.activity.etc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.activity.etc.opencard.UserInfoActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.bean.CertificationInfo
import com.sc.clgg.util.Tools
import kotlinx.android.synthetic.main.activity_card_introduce.*
import kotlinx.android.synthetic.main.view_titlebar.*
import org.jetbrains.anko.toast

class CardIntroduceActivity : BaseImmersionActivity() {
    private var certificationInfo = CertificationInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_introduce)
        titlebar_title.text = "鲁通A卡和B卡的区别"
        tv_apply_a.setOnClickListener {
            certificationInfo.cardType = "2"
            next()
        }
        tv_apply_b.setOnClickListener {
            certificationInfo.cardType = "3"
            next()
        }
        tv_tel.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Tools.callPhone("400-888-1122", this)
                } else {
                    toast("电话权限被禁止，请打开设置-应用权限页面开通")
                }
            } else {
                Tools.callPhone("400-888-1122", this)
            }
        }
    }

    private fun next() {
//        startActivity(Intent(this, IdentityCertificationActivity::class.java).putExtra("info", certificationInfo))
        startActivity(Intent(this, UserInfoActivity::class.java))
    }

}
