package com.example.wangbo.ourapp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.activity.NoteBookDetailsAct
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.adapter.ThreeAdapter
import com.example.wangbo.ourapp.base.PLEFragment
import com.example.wangbo.ourapp.bean.ThreeBean
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.CommonUtils
import com.example.wangbo.ourapp.utils.ViewUtils
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

import butterknife.BindView
import com.example.wangbo.ourapp.utils.OurAnimation

/**
 * Created by wangbo on 2018/7/3.
 *
 *
 * 板块3
 */
class ThreeFragment : PLEFragment(), RecyclerListAdapter.OnItemClickedListener<ThreeBean>, OnLoadMoreListener, OnRefreshListener {

    private lateinit var threeAdapter: ThreeAdapter

    @BindView(R.id.tv_right)
    lateinit var tvRight: TextView

    @BindView(R.id.tv_title)
    lateinit var tvTitle: TextView

    @BindView(R.id.view_line)
    lateinit var viewLine: View

    @BindView(R.id.recycle_view)
    lateinit var recyclerViewHeaderAndFooter: RecyclerViewHeaderAndFooter

    @BindView(R.id.refreshLayout)
    lateinit var mRefreshLayout: RefreshLayout

    private var pageIndex = 0

    private var listData: ArrayList<ThreeBean> = ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.fg_three
    }

    override fun initView() {

        val params = viewLine.layoutParams
        params.height = CommonUtils.getStatusBarHeight(context)
        viewLine.layoutParams = params

        tvTitle.text = "记事本"

        threeAdapter = ThreeAdapter(listData, context)
        recyclerViewHeaderAndFooter.adapter = threeAdapter
        threeAdapter.setOnItemClickedListener(this)

        tvRight.text = "添加"
        tvRight.visibility = View.VISIBLE
        tvRight.setOnClickListener {
            if (UserManager.getInstance().checkLogin(context, true)) {
                val intent = Intent(context, NoteBookDetailsAct::class.java)
                intent.putExtra(NoteBookDetailsAct.EXTRA_TYPE, 1)
                startActivityForResult(intent, 10000)
            }
        }
        mRefreshLayout.setOnRefreshListener(this)
        mRefreshLayout.setOnLoadMoreListener(this)
    }

    override fun initData() {
        loadPage(true)
    }

    private fun loadPage(isFirst: Boolean) {
        HttpHelper.getInstance(context).getNoteBook(pageIndex, UserManager.getInstance().userId, ProgressSubscriber(context, IOnNextListener<List<ThreeBean>> { o ->
            if (isFirst) {
                threeAdapter.setData(o)
                mRefreshLayout.finishRefresh()
            } else {
                threeAdapter.addDataToLast(o)
                mRefreshLayout.finishLoadMore()
            }

            OurAnimation.runLayoutAnimation(recyclerViewHeaderAndFooter)
        }))
    }

    override fun onClicked(position: Int, threeBean: ThreeBean) {
        if (threeBean.status == 6) {
            showAlert(threeBean)
        } else {
            val intent = Intent(context, NoteBookDetailsAct::class.java)
            intent.putExtra(NoteBookDetailsAct.EXTRA_DATA_BASE, threeBean)
            startActivityForResult(intent, 10000)
        }
    }

    private fun showAlert(threeBean: ThreeBean) {
        val marginLeftAndRight = ViewUtils.dp2pxF(30f)
        val width = ViewUtils.getWidth()

        val alertDialog = android.support.v7.app.AlertDialog.Builder(context, R.style.TRANSPARENT).setView(R.layout.dailog_input_pwd).create()
        alertDialog.window!!.setLayout(width - marginLeftAndRight * 2, ViewGroup.LayoutParams.WRAP_CONTENT)
        alertDialog.show()

        alertDialog.findViewById<View>(R.id.tv_save)!!.setOnClickListener(View.OnClickListener {
            val pwd = (alertDialog.findViewById<View>(R.id.pwd) as TextView?)!!
            if (pwd.text.length != 6) {
                showToast("请输入6位数的密码")
                return@OnClickListener
            }

            HttpHelper.getInstance(context).validePwd(pwd.text, UserManager.getInstance().userId, ProgressSubscriber(context, IOnNextListener<String> {
                fun onNext(o: Any) {
                    alertDialog.dismiss()
                    val intent = Intent(context, NoteBookDetailsAct::class.java)
                    intent.putExtra(NoteBookDetailsAct.EXTRA_DATA_BASE, threeBean)
                    startActivityForResult(intent, 10000)
                }
            }))
        })

        alertDialog.findViewById<View>(R.id.tv_cancel)!!.setOnClickListener { alertDialog.dismiss() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000 && resultCode == RESULT_OK) {
            pageIndex = 0
            loadPage(true)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        pageIndex = 0
        loadPage(true)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        pageIndex++
        loadPage(false)
    }
}