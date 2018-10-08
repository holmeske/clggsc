package com.sc.clgg.activity

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.util.DialogUtil
import kotlinx.android.synthetic.main.activity_repair.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.sdk25.coroutines.onClick


class RepairActivity : BaseImmersionActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repair)
        test()
        initTitle("一键报修")
        call_hotline.onClick {
            var list = arrayListOf("029-86955159", "029-86955858")
            DialogUtil.showTelDialog(this@RepairActivity, list)


        }
    }



    fun test() {


        launch(CommonPool) {

        }
        launch {
            // launch new coroutine in background and continue
            delay(5000L) // non-blocking delay for 1 second (default time unit is ms)
            LogHelper.d("logcat", "World!") // print after delay
            launch(UI) {
            }

        }
        LogHelper.d("logcat", "Hello!")  // main thread continues while coroutine is delayed
    }
}
