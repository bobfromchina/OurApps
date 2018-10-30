package com.example.wangbo.ourapp.activity

import android.os.Bundle
import android.widget.TextView
import butterknife.BindView
import com.example.wangbo.ourapp.R
import com.jackmar.jframelibray.base.JBaseAct

/**
 * Created by wangbo on 2018/8/27.
 *
 *   一个Kotlin使用的测试类
 */
class KotlinTestAct : JBaseAct() {

//    @BindView(R.id.tv_showMessage)
//    lateinit var tvMessage: TextView

    // 定义变量
    var name: String = "吃饭啦"

    var age = 10

    var isVip = true

    var list: List<String> = ArrayList()

    // 定义常量
    object Config {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContent(R.layout.activity_kotlin, true, "Kotlin使用的测试类")
    }

    override fun initView() {

        //  for 循环使用
        initFor()

        // 不固定的参数的使用
        initParams(1, 2, 3)
    }

    private fun initParams(vararg i: Int) {
        for (value in i) {

        }
    }

    private fun initFor() {
        for (i in 0..10) {

        }

        val list: ArrayList<String> = ArrayList()
        for (item in list) {

        }
    }

    override fun initData() {

    }
}