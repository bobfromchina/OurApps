package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.OnClick
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

/**
 * Created by wangbo on 2018/8/21.
 *
 * 修改密码
 */
class ModifyPwdAct : JBaseAct() {

    companion object {
        var EXTRA_TYPE = "extra_type"
    }

    @BindView(R.id.old_pwd)
    lateinit var oldPwd: EditText

    @BindView(R.id.new_pwd)
    lateinit var newPwd: EditText


    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = intent.getIntExtra(EXTRA_TYPE, 0)
        setLyContent(R.layout.activity_modify_pwd, true, if (type == 3) "设置密码" else "修改密码")
    }

    override fun initView() {
        if (type == 3) {
            oldPwd.hint = "请输入登录密码"
            newPwd.hint = "请设置记事本密码"
        }
    }

    override fun initData() {

    }

    @OnClick(R.id.tv_submit)
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_submit -> {

                val old = oldPwd.text.toString()
                val news = newPwd.text.toString()
                if (old == "" || news == "") {
                    showToast("请输入完善信息，谢谢")
                    return
                }

                if (type == 3 && newPwd.length() != 6) {
                    showToast("请输入6位数字的登录密码")
                    return
                }

                HttpHelper.getInstance(context).modifyPwd(old, news, type, UserManager.getInstance().userId,
                        ProgressSubscriber(context, IOnNextListener<String> {
                    showToast("设置成功！")
                    finish()
                }))
            }
        }
    }
}