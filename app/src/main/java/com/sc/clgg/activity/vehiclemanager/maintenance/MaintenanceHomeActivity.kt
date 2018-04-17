package com.sc.clgg.activity.vehiclemanager.maintenance

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sc.clgg.R
import kotlinx.android.synthetic.main.activity_maintenance_home_constaint.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.ActivityHelper

/**
 * @author：lvke
 * @date：2018/2/26 15:01
 */
class MaintenanceHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        title = getString(R.string.maintenance)
        setContentView(R.layout.activity_maintenance_home_constaint)
        super.onCreate(savedInstanceState)

        weiXiuLayout.onClick { ActivityHelper.startAcMove(this@MaintenanceHomeActivity, MaintenanceActivity::class.java) }
        jiayouLayout.onClick { ActivityHelper.startAcMove(this@MaintenanceHomeActivity, FuelGasActivity::class.java) }
        yuanQuLayout.onClick { ActivityHelper.startAcMove(this@MaintenanceHomeActivity, LogisticsParkActivity::class.java) }
    }
}