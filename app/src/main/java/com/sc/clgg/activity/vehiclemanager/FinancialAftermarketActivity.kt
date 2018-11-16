package com.sc.clgg.activity.vehiclemanager

import android.content.Intent
import android.os.Bundle
import com.sc.clgg.R
import com.sc.clgg.activity.basic.WebActivity
import com.sc.clgg.base.BaseImmersionActivity
import com.sc.clgg.tool.helper.ActivityHelper
import kotlinx.android.synthetic.main.activity_financial_aftermarket.*

/**
 * @author：lvke
 * @date：2018/2/26 15:57
 */
class FinancialAftermarketActivity : BaseImmersionActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        title = getString(R.string.financial_aftermarket)
        setContentView(R.layout.activity_financial_aftermarket)
        super.onCreate(savedInstanceState)

        relat_bft.setOnClickListener {
            ActivityHelper.startActivityScale(this@FinancialAftermarketActivity,
                    Intent(this@FinancialAftermarketActivity, WebActivity::class.java)
                            .putExtra("name", "保险分期付款")
                            .putExtra("url", "http://www.clgg.com/truckApp/bft.html"))
        }
        relat_kczl.setOnClickListener {
            ActivityHelper.startActivityScale(this@FinancialAftermarketActivity,
                    Intent(this@FinancialAftermarketActivity, WebActivity::class.java)
                            .putExtra("name", "卡车租赁")
                            .putExtra("url", "http://wxsp.kachego.com:3000/#/productList"))
        }
        relat_yp.setOnClickListener {
            ActivityHelper.startActivityScale(this@FinancialAftermarketActivity,
                    Intent(this@FinancialAftermarketActivity, WebActivity::class.java)
                            .putExtra("name", "油品")
                            .putExtra("url", "http://www.clgg.com/truckApp/rhy.html"))
        }
        relat_luntai.setOnClickListener {
            ActivityHelper.startActivityScale(this@FinancialAftermarketActivity,
                    Intent(this@FinancialAftermarketActivity, WebActivity::class.java)
                            .putExtra("name", "轮胎")
                            .putExtra("url", "http://www.clgg.com/truckApp/tyre.html"))
        }
    }
}