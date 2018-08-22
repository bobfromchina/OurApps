package com.example.wangbo.ourapp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.UserEntity
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

/**
 * Created by wangbo on 2018/7/20.
 *
 *
 * 登录页面
 */

class LoginAct : JBaseAct() {

    @BindView(R.id.et_pwd)
    lateinit var pwd: EditText

    @BindView(R.id.phone)
    lateinit var phone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.act_login, true)
    }

    override fun initView() {
        ButterKnife.bind(this)
    }

    override fun initData() {

    }

    @OnClick(R.id.tv_forgot_password, R.id.tv_login, R.id.tv_back, R.id.tv_register_now)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.tv_login -> {
                val pwds = pwd.text.toString().trim { it <= ' ' }
                val phones = phone.text.toString().trim { it <= ' ' }
                if (phones.length != 11) {
                    showToast("请输入正确的账号")
                    return
                }

                if (pwds == "" || pwds.length < 6) {
                    showToast("请输入正确的密码")
                    return
                }
                login()
            }

            R.id.tv_back -> finish()

            R.id.tv_register_now -> launchActivity(RegisterOrForgotPwdAct::class.java, RegisterOrForgotPwdAct.EXTRA_TYPE_NAME, 1)

            R.id.tv_forgot_password -> launchActivity(RegisterOrForgotPwdAct::class.java, RegisterOrForgotPwdAct.EXTRA_TYPE_NAME, 2)
        }
    }

    private fun login() {
        val key = phone.text.toString() + "_" + pwd.text.toString()
        HttpHelper.getInstance(context).login(key, ProgressSubscriber(context, IOnNextListener<UserEntity> { userEntity ->
            launch(MainActivity::class.java)
            val bean = UserEntity.UserBean()
            bean.tokenId = userEntity.tokenId
            userEntity.user = bean
            UserManager.getInstance().updateUser(context as Activity, userEntity, true)
        }))
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginAct::class.java)
            context.startActivity(intent)
        }
    }
}
