package com.sc.clgg.activity.basic

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.lvke.tools.widget.PopupWindowHelper
import com.sc.clgg.R
import com.sc.clgg.activity.contact.LoginContact
import com.sc.clgg.activity.forgetpassword.ForgetPasswordActivity
import com.sc.clgg.activity.presenter.LoginPresenter
import com.sc.clgg.activity.register.RegisterStartActivity
import com.sc.clgg.adapter.RecyclerAdapter
import com.sc.clgg.application.App
import com.sc.clgg.util.Tools
import com.sc.clgg.util.setTextChangeListener
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import tool.helper.LogHelper
import tool.helper.SharedPreferencesHelper
import java.util.*

class LoginActivity : AppCompatActivity(), LoginContact {

    private var mLoginPresenter: LoginPresenter? = null
    private val mPopupWindowHelper = PopupWindowHelper()
    private var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)
        LogHelper.e("onCreate()")

        mLoginPresenter = LoginPresenter(this)

        val account = SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("account", "")

        if (!TextUtils.isEmpty(account)) {
            ll_head?.visibility = View.VISIBLE
            tv_switch_account?.visibility = View.VISIBLE
            ll_account_number?.visibility = View.GONE
            findViewById<View>(R.id.space).visibility = View.GONE
            tv_name?.text = account
            et_account?.setText(account)
        } else {
            ll_head?.visibility = View.GONE
            tv_switch_account?.visibility = View.GONE
            findViewById<View>(R.id.space).visibility = View.VISIBLE
            ll_account_number?.visibility = View.VISIBLE
            et_account?.setText("")
        }
        initPopupWindow()
        initListener()
    }

    override fun onRestart() {
        super.onRestart()
        et_pwd?.setText("")
    }

    private fun initPopupWindow() {
        adapter = RecyclerAdapter()
        adapter?.setStringContact { s1 ->
            et_account?.setText(s1)
            et_account?.setSelection(s1.length)
            mPopupWindowHelper.dismiss()
        }

        val view = mPopupWindowHelper.init(this@LoginActivity, R.layout.pop_input_associate, null)
        val rv = view.find<RecyclerView>(R.id.rv)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this@LoginActivity)
    }

    private fun initListener() {
        et_pwd.setTextChangeListener { iv_delete_password?.visibility = if (it.isNotEmpty()) View.VISIBLE else View.INVISIBLE }
        iv_delete_account.onClick { et_account?.setText("") }
        iv_delete_password.onClick { et_pwd?.setText("") }
        tv_register.onClick { startActivity(Intent(this@LoginActivity, RegisterStartActivity::class.java)) }
        tv_forget_password.onClick { startActivity(Intent(this@LoginActivity, ForgetPasswordActivity::class.java)) }

        btn_login.onClick {
            val userName = et_account?.text.toString().trim()
            val password = et_pwd?.text.toString().trim()
            if (TextUtils.isEmpty(userName)) {
                Tools.Toast(this@LoginActivity, getString(R.string.username_cannot_empty))
                return@onClick
            }
            if (TextUtils.isEmpty(password)) {
                Tools.Toast(this@LoginActivity, getString(R.string.password_cannot_empty))
                return@onClick
            }
            mLoginPresenter?.loginToTXJ(userName, password)
//            onStartLoading()
//            Handler().postDelayed(Runnable { this@LoginActivity.setButtonSuccess() }, 5000)
        }

        tv_switch_account.onClick {
            ll_head?.visibility = View.GONE
            tv_switch_account?.visibility = View.GONE
            ll_account_number?.visibility = View.VISIBLE
            space.visibility = View.VISIBLE
            et_account?.setText("")
            et_account?.requestFocus()
            et_pwd?.setText("")

            et_account.setTextChangeListener {
                iv_delete_account.visibility = if (it.isNotEmpty()) View.VISIBLE else View.INVISIBLE

                val accounts = SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("history_account", "")

                LogHelper.e("history_account = $accounts")

                val list = Arrays.asList(*accounts.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())

                LogHelper.e("list = $list")

                val newList = ArrayList<String>()
                val length = it.length

                for (s in list) {

                    if (length > 0 && length < s.length && TextUtils.equals(it, s.substring(0, length))) {
                        newList.add(s)
                    }
                }

                adapter?.refresh(newList)
                LogHelper.e("newList = $newList")

                mPopupWindowHelper.showAsDropDown(et_account, 0, 0)
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
    }

    override fun onSuccess(body: String) {}

    override fun onError(msg: String) {}

    override fun onToast(msg: String) {
        Tools.Toast(this, msg)
    }

    override fun onStartLoading() {
        btn_login?.startLoading()
    }

    override fun setButtonSuccess() {
        btn_login?.endLoading()
    }

    override fun jumpOtherActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(0, R.anim.scale_out)
    }

}
