package com.example.wangbo.ourapp.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.base.IndicatorPagerAdapter
import com.example.wangbo.ourapp.adapter.RecyclerListAdapter
import com.example.wangbo.ourapp.bean.OneHeaderBean
import com.example.wangbo.ourapp.bean.PersonInfo
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.manager.UserManager
import com.example.wangbo.ourapp.utils.PagerSlidingTabStrip
import com.example.wangbo.ourapp.utils.RoundedImageView
import com.example.wangbo.ourapp.utils.StatusBarUtil
import com.example.wangbo.ourapp.utils.UpDownTextView
import com.example.wangbo.ourapp.utils.ViewUtils
import com.jackmar.jframelibray.base.JBaseFg
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber
import com.jackmar.jframelibray.utils.GlideImageLoadUtil

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.wangbo.ourapp.activity.*

/**
 * Created by wangbo on 2018/7/3.
 *
 * 页面1
 */
class OneFragment : JBaseFg(), RecyclerListAdapter.OnItemClickedListener<OneHeaderBean>,
        View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.siv_activity_task_details_indicator)
    lateinit var psts: PagerSlidingTabStrip

    @BindView(R.id.vp_activity_task_details_pager)
    lateinit var viewPager: ViewPager

    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.search_view)
    lateinit var searchView: UpDownTextView

    @BindView(R.id.nav_view)
    lateinit var navView: NavigationView

    private lateinit var headImg: RoundedImageView

    private lateinit var name: TextView

    internal lateinit var adapter: IndicatorPagerAdapter

    private var position: Int = 0

    private val data: List<OneHeaderBean>
        get() {
            val listName = ArrayList<OneHeaderBean>()
            listName.add(OneHeaderBean(1, "头条", 1))
            listName.add(OneHeaderBean(2, "娱乐", 1))
            listName.add(OneHeaderBean(3, "NBA", 1))
            listName.add(OneHeaderBean(4, "军事", 1))
            listName.add(OneHeaderBean(5, "社会", 1))
            listName.add(OneHeaderBean(6, "情感", 1))
            listName.add(OneHeaderBean(7, "科技", 1))
            listName.add(OneHeaderBean(8, "数码", 1))
            listName.add(OneHeaderBean(9, "娱乐", 1))
            listName.add(OneHeaderBean(10, "财金", 1))
            listName.add(OneHeaderBean(11, "时尚", 1))
            listName.add(OneHeaderBean(12, "健康", 1))
            listName.add(OneHeaderBean(13, "运动", 1))
            listName.add(OneHeaderBean(14, "更多", 1))
            return listName
        }

    override fun getLayoutId(): Int {
        return R.layout.fg_one
    }

    override fun initView() {
        ButterKnife.bind(this, rootView)
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(context, drawerLayout,
                ContextCompat.getColor(context, R.color.wangyi_red))
        initDrawerLayout()

        val res = arrayOf("大家都在看 爸爸去哪儿了", "可爱的小瓶子 你还不买吗")
        searchView.setResources(res)
        searchView.setTextStillTime(3000)
        searchView.setOnClickListener { showToast(searchView.nowText) }

        initViewPager()
    }

    private fun initViewPager() {

        val listFragment = ArrayList<Fragment>()

        val listName = ArrayList<String>()

        for (bean in data) {
            listName.add(bean.getName())
        }

        for (i in 1..listName.size) {
            listFragment.add(NewFragment.getInstance(i.toString()))
        }

        //设置对应的适配器
        adapter = IndicatorPagerAdapter(childFragmentManager, listFragment, listName)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 2

        //配置指示器
        psts.shouldExpand = true
        psts.tabPaddingLeftRight = 0
        psts.setViewPager(viewPager)
        psts.textSize = resources.getDimension(R.dimen.small_text).toInt()
        psts.setTypeface(null, Typeface.NORMAL)
        psts.setTextColorResource(R.drawable.filter_conditions_color)
        psts.setTextPadding(5, 15)

        psts.indicatorHeight = ViewUtils.dp2pxF(2f)
        psts.dividerColor = Color.WHITE//未选中的指示器
        psts.setOnPageChangeListener(this)
        psts.underlineColor = Color.WHITE
        psts.tabPaddingLeftRight = 80
        psts.indicatorColor = ContextCompat.getColor(context, R.color.white)

        //作用就是调整显示的界面状态和恢复界面
        viewPager.post {
            viewPager.currentItem = position
            onPageSelected(position)
        }
    }

    override fun initData() {
        getUserInfo()
    }

    private fun getUserInfo() {
        HttpHelper.getInstance(context).getUserInfo(UserManager.getInstance().userId.toString(), ProgressSubscriber(context, IOnNextListener<PersonInfo> { o ->
            name.text = o.username
            GlideImageLoadUtil.loadImage(context, o.headImg, headImg)
        }))
    }

    override fun onClicked(position: Int, bean: OneHeaderBean) {
        when (bean.getId()) {
            1 -> launchActivity(WeatherAct::class.java)
            2 -> launchActivity(LightShowAct::class.java)
        }
    }

    /**
     * 点击新闻的按钮
     */
    @OnClick(R.id.left_btn)
    internal fun onClick() {
        drawerLayout.openDrawer(GravityCompat.START) // 开启菜单
    }

    /**
     * inflateHeaderView 进来的布局要宽一些
     */
    private fun initDrawerLayout() {
        navView.inflateHeaderView(R.layout.nav_header_main)
        val headerView = navView.getHeaderView(0)
        headImg = headerView.findViewById<View>(R.id.img_head) as RoundedImageView
        name = headerView.findViewById<View>(R.id.tv_username) as TextView
        headerView.findViewById<View>(R.id.ll_nav_homepage).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_web).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_weather).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_light).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_nav_deedback).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_nav_about).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_nav_login).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_nav_collect).setOnClickListener(this)
        headerView.findViewById<View>(R.id.ll_nav_exit).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
        // 项目首页
            R.id.ll_nav_homepage -> launchActivity(ProjectPackAct::class.java)

        // 可康安网页项目测试
            R.id.ll_web -> launchActivity(WebTestActivity::class.java)
        // 我的天气
            R.id.ll_weather -> launchActivity(WeatherAct::class.java)
        // 我的灯光
            R.id.ll_light -> launchActivity(LightShowAct::class.java)
        //  问题反馈
            R.id.ll_nav_deedback -> showAlert("提示", "我们有什么地方做的不好的，请及时联系我们，谢谢！", "QQ联系", "留言联系", { _, _ ->
                val url = "mqqwpa://im/chat?chat_type=wpa&uin=601211498"
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    showToast("未安装手Q或安装的版本不支持")
                }
            }) { _, _ -> launchActivity(FeedBookAct::class.java) }.show()
        //  关于我们
            R.id.ll_nav_about -> launchActivity(AboutUsAct::class.java)
        //  登录
            R.id.ll_nav_login -> launchActivity(LoginAct::class.java)
        //  我的新闻
            R.id.ll_nav_collect -> launchActivity(MineNewsAct::class.java)
        //  退出登录
            R.id.ll_nav_exit -> context.finish()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(positions: Int) {
        position = positions
        val count = psts.tabCount
        for (i in 0 until count) {
            val tab = psts.getTab(i) as TextView
            tab.setTextColor(ContextCompat.getColor(context, R.color.auxiliary_text_bg))
        }

        val tab = psts.getTab(position) as TextView
        tab.setTextColor(ContextCompat.getColor(context, R.color.wangyi_red))
    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}


