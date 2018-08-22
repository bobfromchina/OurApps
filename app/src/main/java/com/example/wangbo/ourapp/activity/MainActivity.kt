package com.example.wangbo.ourapp.activity

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.bean.TabEntity
import com.example.wangbo.ourapp.fragment.FourFragment
import com.example.wangbo.ourapp.fragment.OneFragment
import com.example.wangbo.ourapp.fragment.ThreeFragment
import com.example.wangbo.ourapp.fragment.TwoFragment
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.jackmar.jframelibray.base.JBaseAct

import java.util.ArrayList

import butterknife.BindView

/**
 * Created by wangbo on 2018/7/3.
 *
 *
 * Main
 */
class MainActivity : JBaseAct() {

    @BindView(R.id.cyl_tab)
    lateinit var mCylTab: CommonTabLayout

    private var homeFg: Fragment? = null

    private var newsFg: Fragment? = null

    private var transactionFg: Fragment? = null

    private var mineFg: Fragment? = null

    private val mTabs = arrayOf("新闻", "娱乐", "记事", "我们")

    private val mTabsIconSelected = intArrayOf(R.mipmap.zy2, R.mipmap.tp2, R.mipmap.zx2, R.mipmap.grzh2)

    private val mTabsIconUnSelected = intArrayOf(R.mipmap.zy, R.mipmap.tp, R.mipmap.zx, R.mipmap.grzx)

    private val mTabsentitys = ArrayList<CustomTabEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.activity_main, true)
    }

    override fun initView() {
        requestPermission(arrayOf(Manifest.permission.READ_PHONE_STATE), "我们需要您提供手机状态权限", 1001)
    }

    override fun initData() {
        initTab()
    }

    private fun initTab() {

        initFragment(0)
        for (i in mTabs.indices) {
            mTabsentitys.add(TabEntity(mTabs[i], mTabsIconSelected[i], mTabsIconUnSelected[i]))
        }
        mCylTab.setTabData(mTabsentitys)
        mCylTab.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                initFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    /**
     * 初始化fragment
     *
     * @param page index
     */
    private fun initFragment(page: Int) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        hideFragments(fragmentTransaction)
        when (page) {
            0 -> if (homeFg == null) {
                homeFg = OneFragment()
                fragmentTransaction.add(R.id.fl_content, homeFg)
            } else {
                fragmentTransaction.show(homeFg)
            }
            1 -> if (newsFg == null) {
                newsFg = TwoFragment()
                fragmentTransaction.add(R.id.fl_content, newsFg)
            } else {
                fragmentTransaction.show(newsFg)
            }
            2 -> if (transactionFg == null) {
                transactionFg = ThreeFragment()
                fragmentTransaction.add(R.id.fl_content, transactionFg)
            } else {
                fragmentTransaction.show(transactionFg)
            }

            3 -> if (mineFg == null) {
                mineFg = FourFragment()
                fragmentTransaction.add(R.id.fl_content, mineFg)
            } else {
                fragmentTransaction.show(mineFg)
            }
        }
        fragmentTransaction.commit()
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        if (homeFg != null) {
            transaction.hide(homeFg)
        }
        if (newsFg != null) {
            transaction.hide(newsFg)
        }
        if (transactionFg != null) {
            transaction.hide(transactionFg)
        }
        if (mineFg != null) {
            transaction.hide(mineFg)
        }
    }
}
