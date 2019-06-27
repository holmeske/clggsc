package com.sc.clgg.util

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.sc.clgg.R
import com.sc.clgg.application.App
import com.sc.clgg.tool.helper.LogHelper
import com.sc.clgg.tool.helper.MeasureHelper
import com.sc.clgg.tool.helper.TakePhotoHelper
import org.devio.takephoto.app.TakePhoto
import java.util.*

fun Context.dp2px(dipValue: Float): Int {
    val scale = resources.displayMetrics.density
    return (dipValue * scale + 0.5f).toInt()
}

fun Context.getArea(location: Location?): String? {
    if (location == null) {
        return ""
    }
    return Geocoder(this).getFromLocation(location.latitude, location.longitude, 1)?.get(0)?.adminArea
}

@SuppressLint("MissingPermission")
fun getLocationInfo(): Location? {
    val locationProvider: String?
    val location: Location?
    val locationManager = App.app.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    /*val providers = locationManager.getProviders(true)
    if (providers.contains(LocationManager.GPS_PROVIDER)) {
        locationProvider = LocationManager.GPS_PROVIDER
    } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
        locationProvider = LocationManager.NETWORK_PROVIDER
    } else {
        locationProvider = LocationManager.PASSIVE_PROVIDER
    }*/

    val criteria = Criteria()
    //高度
    criteria.isAltitudeRequired = false
    //方向
    criteria.isBearingRequired = false
    criteria.isCostAllowed = false
    criteria.powerRequirement = Criteria.POWER_HIGH
    //精确度
    criteria.accuracy = Criteria.ACCURACY_FINE
    locationProvider = locationManager.getBestProvider(criteria, true)

    location = locationManager.getLastKnownLocation(locationProvider)

    LogHelper.e(location?.latitude?.toString() + "," + location?.longitude)
    return location
}

fun Context.hideSoftInputFromWindow(v: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(v, InputMethodManager.SHOW_FORCED)
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun Activity.showAlertDialog(message: String, ok: () -> Unit) {
    var dialog: AlertDialog? = null
    AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("确定") { _, _ -> ok();dialog?.dismiss() }
            .setNegativeButton("取消", null)
            .show().apply { dialog = this }
}

/**
 * 是否打开蓝牙
 */
fun isOpenBlueTooth(): Boolean {
    return BluetoothAdapter.getDefaultAdapter().isEnabled
}

fun randomId(): String {
    return UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.getDefault())
}

fun EditText.setTextChangeListener(body: (key: String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            body(s.toString())
        }
    })
}

@SuppressLint("MissingPermission")
fun Context.makeDIAL(number: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.startActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

fun Context.statusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    //LogHelper.e("状态栏高 = ${result}")
    return result
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun ImageView.setDefaultRoundedCornerPicture(context: Context, @DrawableRes resourceId: Int) {
    Glide.with(context).load(resourceId).apply(
            RequestOptions.bitmapTransform(RoundedCorners(MeasureHelper.dp2px(context.applicationContext, 5f)))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .override(if (measuredWidth == 0) 100 else measuredWidth, if (measuredHeight == 0) 100 else measuredHeight)
    ).into(this)
}

fun ImageView.setRoundedCornerPicture(context: Context?, url: String? = "") {
    //if (!TextUtils.isEmpty(url)) {
    //LogHelper.e("ImageView 宽高：" + measuredWidth + "," + measuredHeight)
    context?.let {
        Glide.with(it).load(url).apply(
                RequestOptions.bitmapTransform(RoundedCorners(MeasureHelper.dp2px(context.applicationContext, 5f)))
                        .placeholder(R.drawable.ic_launcher)
                        .error(R.drawable.ic_launcher)
                        .override(if (measuredWidth == 0) 100 else measuredWidth, if (measuredHeight == 0) 100 else measuredHeight)
        )
                /*.listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
            LogHelper.e("onLoadFailed")
            return false
        }

        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
            LogHelper.e("onResourceReady")
            return false
        }
    })*/
                .into(this)
    }
    //}
}

fun ImageView.setImage(url: String? = "") {
    Glide.with(context).load(url).apply(RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)).into(this)
}

/*fun Activity.showTakePhoto(n: () -> Unit, p: () -> Unit) {
    AlertDialog.Builder(this)
            .setMessage("选择")
            .setNegativeButton("相机") { _, _ -> n }
            .setPositiveButton("图库") { _, _ -> p }
            .show()
}*/


fun Activity.showTakePhoto(t: TakePhoto, limit: Int = 1, v: () -> Unit) {
    AlertDialog.Builder(this)
            .setMessage("选择")
            .setNegativeButton("相机") { _, _ -> TakePhotoHelper().onPikeByTake(t); v() }
            .setPositiveButton("图库") { _, _ -> TakePhotoHelper().onPickBySelect(t, limit);v() }
            .show()
}

fun Activity.showTakePhoto(t: TakePhoto, limit: Int = 1) {
    AlertDialog.Builder(this)
            .setMessage("选择")
            .setNegativeButton("相机") { _, _ -> TakePhotoHelper().onPikeByTake(t) }
            .setPositiveButton("图库") { _, _ -> TakePhotoHelper().onPickBySelect(t, limit) }
            .show()
}

fun Activity.showTakePhotoWithCrop(t: TakePhoto, limit: Int = 1) {
    AlertDialog.Builder(this)
            .setMessage("选择")
            .setNegativeButton("相机") { _, _ -> TakePhotoHelper().onPickFromCaptureWithCrop(t) }
            .setPositiveButton("图库") { _, _ -> TakePhotoHelper().onPickMultipleWithCrop(t, limit) }
            .show()
}






