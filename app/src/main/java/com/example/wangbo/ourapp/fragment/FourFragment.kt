package com.example.wangbo.ourapp.fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.activity.AboutUsAct
import com.example.wangbo.ourapp.activity.FeedBookAct
import com.example.wangbo.ourapp.activity.LoginAct
import com.example.wangbo.ourapp.activity.MineNewsAct
import com.example.wangbo.ourapp.activity.MineNoteAct
import com.example.wangbo.ourapp.activity.MineSongAct
import com.example.wangbo.ourapp.activity.SetAct
import com.example.wangbo.ourapp.activity.UserInfoAct
import com.example.wangbo.ourapp.bean.PersonInfo
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.RoundedImageView
import com.jackmar.jframelibray.base.JBaseFg
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.jackmar.jframelibray.utils.GlideImageLoadUtil
import com.jackmar.jframelibray.utils.PreHelper
import com.jackmar.jframelibray.utils.PreferenceKey

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

/**
 * Created by wangbo on 2018/7/16.
 *
 * 板块4
 */
class FourFragment : JBaseFg() {

    @BindView(R.id.img_head)
    lateinit var headImg: RoundedImageView

    @BindView(R.id.tv_name)
    lateinit var name: TextView

    @BindView(R.id.tv_new_and_note)
    lateinit var newAndNote: TextView

    @BindView(R.id.go_login)
    lateinit var goLogin: TextView

    lateinit var personInfo: PersonInfo

    override fun initView() {
        ButterKnife.bind(context)
    }

    override fun initData() {
        getUserInfo()
    }

    private fun getUserInfo() {
        goLogin.text = "退出登录"
        if (UserManager.getInstance().checkLogin(context, false)) {
            HttpHelper.getInstance(context).getUserInfo(UserManager.getInstance().userId.toString(), ProgressSubscriber(context, IOnNextListener<PersonInfo> { o ->
                personInfo = o
                name.text = o.username
                newAndNote.text = "新闻 " + o.newNum + "  记事 " + o.noteNum
                GlideImageLoadUtil.loadImage(context, o.headImg, headImg)
            }))
        } else {
            goLogin.text = "登录"
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fg_four
    }

    @OnClick(R.id.tv_edit, R.id.go_login, R.id.tv_set, R.id.tv_new, R.id.tv_note, R.id.tv_aboutUs, R.id.tv_message, R.id.tv_song)
    internal fun onClicked(view: View) {
        when (view.id) {

            R.id.tv_edit -> if (UserManager.getInstance().checkLogin(context, true))
                launchActivity(UserInfoAct::class.java, UserInfoAct.EXTRA_USER_INFO, personInfo)

            R.id.go_login -> {
                LoginAct.start(context)
                PreHelper.defaultCenter(context).setData(PreferenceKey.LOGIN_STATE, false)
            }

            R.id.tv_set -> launchActivity(SetAct::class.java)

            R.id.tv_new -> if (UserManager.getInstance().checkLogin(context, true))
                launchActivity(MineNewsAct::class.java)

            R.id.tv_note -> if (UserManager.getInstance().checkLogin(context, true))
                launchActivity(MineNoteAct::class.java)

            R.id.tv_aboutUs -> launchActivity(AboutUsAct::class.java)

            R.id.tv_song -> if (UserManager.getInstance().checkLogin(context, true))
                launchActivity(MineSongAct::class.java)

            R.id.tv_message -> showAlert("提示", "我们有什么地方做的不好的，请及时联系我们，谢谢！", "QQ联系", "留言联系", { _, _ ->
                val url = "mqqwpa://im/chat?chat_type=wpa&uin=601211498"
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    showToast("未安装手Q或安装的版本不支持")
                }
            }) { _, _ -> launchActivity(FeedBookAct::class.java) }.show()
        }
    }
}
