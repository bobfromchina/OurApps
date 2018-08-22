package com.example.wangbo.ourapp.activity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View

import com.example.wangbo.ourapp.R
import com.jackmar.jframelibray.base.JBaseAct

import butterknife.OnClick
import java.lang.reflect.Type

/**
 * Created by wangbo on 2018/8/3.
 *
 * 设置页面
 */
class SetAct : JBaseAct() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.activity_set, true, "设置")
    }

    override fun initView() {

    }

    override fun initData() {

    }

    @OnClick(R.id.login_pwd, R.id.note_pwd, R.id.clear_cache)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.login_pwd -> aboutPwd(1)
            R.id.note_pwd -> aboutPwd(2)
            R.id.clear_cache -> showToast("清除成功！")
        }
    }

    private fun aboutPwd(type: Int) {
        launchActivity(ModifyPwdAct::class.java, ModifyPwdAct.EXTRA_TYPE, type)
    }
}
