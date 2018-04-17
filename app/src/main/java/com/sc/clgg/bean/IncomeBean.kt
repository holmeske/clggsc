package com.sc.clgg.bean

/**
 * @author：lvke
 * @date：2018/2/24 11:34
 */
data class IncomeBean(var success: Boolean = false, var msg: Any? = null, var data: DataBeanX? = null) {

    data class DataBeanX(var totalAmount: Double = 0.toDouble(), var data: List<DataBean>? = null) {

        data class DataBean(var amount: Double = 0.toDouble(),
                            var address: String? = null,
                            var day: String? = null,
                            var waybillStatus: String? = null,
                            var waybillNo: String? = null)
    }
}
