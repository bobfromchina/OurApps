package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.baoyz.actionsheet.ActionSheet
import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.PersonInfo
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.ImageDownloader
import com.example.wangbo.ourapp.utils.LoadPic
import com.example.wangbo.ourapp.utils.RoundedImageView
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.jackmar.jframelibray.utils.GlideImageLoadUtil
import com.lzy.imagepicker.bean.ImageItem
import kotlinx.android.synthetic.main.item_rank_list.*

/**
 * Created by wangbo on 2018/8/21.
 *
 *  个人中心
 */
class UserInfoAct : JBaseAct(), ActionSheet.ActionSheetListener {

    companion object {
        val EXTRA_USER_INFO = "extra_user_info"
    }

    @BindView(R.id.img_head)
    lateinit var imgHeader: RoundedImageView

    /**
     * 昵称
     */
    @BindView(R.id.tv_username)
    lateinit var tvUserName: EditText

    /**
     * 性别
     */
    @BindView(R.id.tv_sex)
    lateinit var tvSex: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.activity_user_info, true, "个人资料")
        mJTitleView.moreText.setText(R.string.save);
        mJTitleView.moreText.setOnClickListener {
            val name = tvUserName.text.toString()
            val sex = if (tvSex.text.toString() == "男") "1" else "0"
            val token = UserManager.getInstance().userId
//            val list = ArrayList<String>()
//            list.add(path)
//            var pic = LoadPic.upLoadPic(list, UserManager.getInstance().getUserId());
//            showToast(pic)
            HttpHelper.getInstance(context).modifyUserInfo(name, sex, token, ProgressSubscriber(context, IOnNextListener<String> { showToast("修改成功！") }))
        }
    }

    lateinit var path: String

    override fun OnImageSelected(images: java.util.ArrayList<ImageItem>?) {
        super.OnImageSelected(images)
        if (images != null && images.size > 0) {
            path = images[0].path
            GlideImageLoadUtil.loadFileImage(context, images[0].path, imgHeader)
        }
    }


    override fun initView() {

        val p = intent.getParcelableExtra<PersonInfo>(EXTRA_USER_INFO)
        if (p != null) {
            tvUserName.setText(p.username)
            GlideImageLoadUtil.loadImage(context, p.headImg, imgHeader)
            tvSex.text = if (p.sex == "") "男" else "女"
        } else {
            HttpHelper.getInstance(context).getUserInfo(UserManager.getInstance().userId.toString(), ProgressSubscriber(context, IOnNextListener<PersonInfo> { o ->
                tvUserName.setText(o.username)
                GlideImageLoadUtil.loadImage(context, o.headImg, imgHeader)
                tvSex.text = if (o.sex == "") "男" else "女"
            }))
        }
    }

    override fun initData() {

    }

    @OnClick(R.id.tv_sex, R.id.img_head)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.tv_sex -> ActionSheet.createBuilder(this, supportFragmentManager)
                    .setCancelButtonTitle(getString(R.string.cancel))
                    .setOtherButtonTitles(getString(R.string.man), getString(R.string.women))
                    .setCancelableOnTouchOutside(true)
                    .setListener(this).show()
            R.id.img_head -> chooseImage(1)
        }
    }

    override fun onDismiss(actionSheet: ActionSheet?, isCancel: Boolean) {

    }

    override fun onOtherButtonClick(actionSheet: ActionSheet?, index: Int) {
        when (index) {
            0 -> tvSex.text = getString(R.string.man)
            1 -> tvSex.text = getString(R.string.women)
        }
    }
}