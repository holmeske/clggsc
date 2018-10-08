package com.sc.clgg.activity

import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.dialog.ConfirmCircleDialog
import com.sc.clgg.util.startActivity
import kotlinx.android.synthetic.main.activity_write_card.*
import kotlinx.android.synthetic.main.view_read_card.*
import kotlinx.android.synthetic.main.view_titlebar_blue.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class WriteCardActivity : BaseImmersionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_card)

        titlebar_title.text = getString(R.string.recharge_write_card)
        iv_nav.setImageResource(R.drawable.pay_success_nav_step_icon)


        cl_go_recharge.onClick {
            tv_recharge_circle.text = "充值"
            cl_go_circle.isSelected = false
            cl_go_recharge.isSelected = true
        }
        cl_go_circle.onClick {
            tv_recharge_circle.text = "圈存"
            cl_go_circle.isSelected = true
            cl_go_recharge.isSelected = false
        }

        tv_recharge_circle.onClick {
            if (tv_recharge_circle.text == "圈存") {
                ConfirmCircleDialog(this@WriteCardActivity).run {
                    show()
                    setData(100.00)
                    setConfirmListener { toast("confirm");dismiss();startActivity(RechargeSuccessActivity::class.java) }
                    setCancelListener { toast("cancel");dismiss() }
                }
            }
        }
    }
}
