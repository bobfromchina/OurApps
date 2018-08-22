package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.VerifyBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.jackmar.jframelibray.utils.StrUtil
import com.jackmar.jframelibray.utils.TimeUtils

import butterknife.BindView
import butterknife.OnClick

/**
 * Created by JackMar on 2018/3/28.
 *
 *
 * 注册或者忘记密码页面
 */
class RegisterOrForgotPwdAct : JBaseAct() {

    @BindView(R.id.et_mobile)
    lateinit var mEtMobile: EditText

    @BindView(R.id.et_code)
    lateinit var mEtCode: EditText

    @BindView(R.id.et_pwd)
    lateinit var mEtPwd: EditText

    @BindView(R.id.tv_get_code)
    lateinit var mTvGetCode: TextView

    private lateinit var timeUtils: TimeUtils

    private lateinit var mobile: String

    private lateinit var code: String

    private lateinit var pwd: String

    internal var type = 0

    private var ver: VerifyBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = intent.getIntExtra(EXTRA_TYPE_NAME, 1)
        setLyContent(R.layout.act_register, true, if (type == 1) "注册" else "忘记密码")
    }

    override fun initView() {
        timeUtils = TimeUtils(60000, 1000, mTvGetCode)
    }

    override fun initData() {

    }

    private fun check() {
        mobile = mEtMobile.text.toString()
        code = mEtCode.text.toString()
        pwd = mEtPwd.text.toString()
        if (StrUtil.isEmpty(mobile) || mobile.length != 11) {
            showToast("请输入手机号")
            return
        }

        if (StrUtil.isEmpty(code)) {
            showToast("请输入验证码")
            return
        }

        if (StrUtil.isEmpty(pwd)) {
            showToast("请输入密码")
            return
        }

        if (type == 1) {
            register()
        } else {
            modifyPwd()
        }
    }

    private fun modifyPwd() {
        HttpHelper.getInstance(context).forgotPwd(mobile, code, pwd, ver!!.messageId, ProgressSubscriber(context, IOnNextListener<String> {
            showToast("密码修改成功")
            finish()
        }))
    }

    private fun register() {
        HttpHelper.getInstance(context).register(mobile, code, pwd, ver!!.messageId, ProgressSubscriber(context, IOnNextListener<String> {
            showToast("注册成功")
            finish()
        }))
    }

    private fun sendCode() {
        mobile = mEtMobile.text.toString()
        if (StrUtil.isEmpty(mobile) || mobile.length != 11) {
            showToast("请输入正确手机号")
            return
        }

        HttpHelper.getInstance(context).getLoginCode(mobile, type, ProgressSubscriber(context, false, IOnNextListener<VerifyBean> { s ->
            ver = s
            timeUtils.start()
            showToast("短信已发送至手机:" + mobile)
        }))
    }

    @OnClick(R.id.tv_get_code, R.id.tv_regist)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_get_code -> sendCode()
            R.id.tv_regist -> check()
        }
    }

    companion object {

       const val EXTRA_TYPE_NAME = "extra_type_name"
    }
}
