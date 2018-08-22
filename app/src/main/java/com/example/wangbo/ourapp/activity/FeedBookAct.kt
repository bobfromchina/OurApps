package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.http.HttpHelper
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import butterknife.BindView
import butterknife.OnClick

/**
 * Created by wangbo on 2018/8/6.
 *
 *
 * 留言板页面
 */

class FeedBookAct : JBaseAct(), TextWatcher {

    @BindView(R.id.et_back_book)
    lateinit var etBackBook: EditText

    @BindView(R.id.remaining)
    lateinit var remaining: TextView

    @BindView(R.id.phone_or_email)
    lateinit var phoneOrEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.actvity_feed_book, true, "留言板")
    }

    override fun initView() {
        etBackBook.addTextChangedListener(this)
    }

    override fun initData() {

    }

    /**
     * 提交按钮
     */
    @OnClick(R.id.submit_book_buffer)
    internal fun onClicked() {
        val backPhone = phoneOrEmail.text.toString().trim()
        val backBook = etBackBook.text.toString().trim()
        if (backBook == "" || backPhone == "") {
            showToast(getString(R.string.please_complate_info))
        } else {
            //submitBookBuffer.setEnabled(false);
            HttpHelper.getInstance(context).feedBook(backBook, backPhone, ProgressSubscriber(context, IOnNextListener<String> { o ->
                if (o == "1") {
                    showToast("非常感谢你反馈的问题，我们将及时做出处理，谢谢！")
                    finish()
                }
            }))
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        val num = s.length
        remaining.text = String.format(getString(R.string.two_oo_zi), (MAX_LENGTH - num).toString())
    }

    companion object {
        private const val MAX_LENGTH = 140
    }
}
