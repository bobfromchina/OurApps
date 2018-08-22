package com.example.wangbo.ourapp.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.ThreeBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.FontTextView
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import butterknife.BindView
import butterknife.OnClick

/**
 * Created by wangbo on 2018/7/30.
 *
 * 记事本详情
 */
class NoteBookDetailsAct : JBaseAct() {

    internal var bean = ThreeBean()

    @BindView(R.id.type)
    lateinit var type: FontTextView

    @BindView(R.id.time)
    lateinit var time: FontTextView

    @BindView(R.id.title)
    lateinit var title: EditText

    @BindView(R.id.content)
    lateinit var content: EditText

    @BindView(R.id.submit)
    lateinit var submit: TextView

    @BindView(R.id.delete)
    lateinit var delete: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.activity_note_book_details, true, "记事本")

        mJTitleView.setMoreText("心情")

        mJTitleView.moreText.setOnClickListener { launchActivityForResult(SingleChooseAct::class.java, 10000, SingleChooseAct.EXTRA_SINGLE_TYPE, 1) }

        val statusType = intent.getIntExtra(EXTRA_TYPE, 0)
        if (statusType == 1) {
            delete.visibility = View.GONE
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
            val times = simpleDateFormat.format(Date())
            time.text = times.substring(5, times.length - 3)
        } else {
            bean = intent.getParcelableExtra(EXTRA_DATA_BASE)

            when (bean.status) {
                1 -> {
                    type.text = "心情"
                    mJTitleView.setMoreText("心情")
                }
                2 -> {
                    type.text = "记事"
                    mJTitleView.setMoreText("记事")
                }
                3 -> {
                    type.text = "提醒"
                    mJTitleView.setMoreText("提醒")
                }
                4 -> {
                    type.text = "日程"
                    mJTitleView.setMoreText("日程")
                }
                5 -> {
                    type.text = "日记"
                    mJTitleView.setMoreText("日记")
                }
                6 -> {
                    type.text = "私密日记"
                    mJTitleView.setMoreText("私密日记")
                }
            }

            time.text = bean.lastTime.substring(5, bean.lastTime.length - 3)

            title.setText(bean.descss)

            content.setText(bean.content)
        }
    }

    override fun initView() {
        val typeface = Typeface.createFromAsset(context.assets, "fonts/text_font.OTF")
        title.typeface = typeface
        content.typeface = typeface
        submit.typeface = typeface
        mJTitleView.moreText.typeface = typeface
        mJTitleView.geTitleText().typeface = typeface
    }

    override fun initData() {

    }

    /**
     * 页面监听处理
     */
    @OnClick(R.id.type, R.id.submit, R.id.delete)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.type -> launchActivityForResult(SingleChooseAct::class.java, 10000, SingleChooseAct.EXTRA_SINGLE_TYPE, 1)
            R.id.submit -> {
                var types = 0
                when {
                    type.text == "心情" -> types = 1
                    type.text == "记事" -> types = 2
                    type.text == "提醒" -> types = 3
                    type.text == "日程" -> types = 4
                    type.text == "日记" -> types = 5
                    type.text == "私密日记" -> types = 6
                }

                val titles = title.text.toString()

                val contents = content.text.toString()

                HttpHelper.getInstance(context).addNoteBook(titles, contents, bean.id, UserManager.getInstance().userId, types, ProgressSubscriber(context, IOnNextListener<String> { o ->
                    if (o == "1") {
                        showToast("为了您的隐私安全,请设置记事本密码！")
                        launchActivity(ModifyPwdAct::class.java, "extra_type", 3)
                    } else {
                        showToast("操作成功！")
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }))
            }
            R.id.delete -> {
                AlertDialog.Builder(context).setTitle("提示").setMessage("您确定要删除该条数据吗？").setPositiveButton("确定") { dialogInterface, i ->

                    HttpHelper.getInstance(context).deleteNotebook(bean.id, UserManager.getInstance().userId,
                            ProgressSubscriber(context, IOnNextListener<String> {
                                showToast("删除成功！")
                                setResult(Activity.RESULT_OK)
                                finish()
                            }))
                }.setNegativeButton("取消", null).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000 && resultCode == Activity.RESULT_OK) {
            val cityName = data.getStringExtra("name")
            type.text = cityName
            mJTitleView.setMoreText(cityName)
        }
    }

    companion object {

        const val EXTRA_DATA_BASE = "extra_data_base"

        const val EXTRA_TYPE = "EXTRA_TYPE"
    }
}
