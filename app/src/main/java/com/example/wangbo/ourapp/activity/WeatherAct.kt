package com.example.wangbo.ourapp.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.example.wangbo.ourapp.R
import com.example.wangbo.ourapp.view.RecyclerViewHeaderAndFooter
import com.example.wangbo.ourapp.adapter.WeatherAdapter
import com.example.wangbo.ourapp.bean.WeatherBean
import com.example.wangbo.ourapp.bean.WeatherDetails
import com.example.wangbo.ourapp.http.HttpHelper
import com.example.wangbo.ourapp.utils.FontTextView
import com.jackmar.jframelibray.base.JBaseAct
import com.jackmar.jframelibray.http.subscriber.IOnNextListener
import com.jackmar.jframelibray.http.subscriber.ProgressSubscriber

import butterknife.BindView
import butterknife.OnClick
import com.example.wangbo.ourapp.utils.OurAnimation

/**
 * Created by wangbo on 2018/7/27.
 *
 * 天气预报
 */
class WeatherAct : JBaseAct() {

    private var weatherAdapter: WeatherAdapter? = null

    private var nowCity: TextView? = null

    private var nowDate: TextView? = null

    private var nowGanMao: TextView? = null

    private var nowWenDu: TextView? = null

    private var nowWind: TextView? = null

    private var headerContainer: LinearLayout? = null

    @BindView(R.id.common_list)
    lateinit var listView: RecyclerViewHeaderAndFooter

    @BindView(R.id.ll_container)
    lateinit var llContainer: LinearLayout

    @BindView(R.id.title)
    lateinit var title: FontTextView

    private var listData: ArrayList<WeatherBean> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.activity_wearth_list, true)
    }

    override fun initView() {
        weatherAdapter = WeatherAdapter(listData, context)
        listView.adapter = weatherAdapter
        val headView = layoutInflater.inflate(R.layout.activity_wearth, null, false)
        if (listView.headerCount < 1) {
            listView.addHeaderView(headView)
            nowCity = headView.findViewById(R.id.now_city)
            nowDate = headView.findViewById(R.id.now_date)
            nowGanMao = headView.findViewById(R.id.now_ganmao)
            nowWenDu = headView.findViewById(R.id.now_wendu)
            nowWind = headView.findViewById(R.id.now_wind)
            headerContainer = headView.findViewById(R.id.ll_container)
            headerContainer!!.visibility = View.GONE
        }
    }

    override fun initData() {
        getPageData("hangzhou")
        getWeatherDetails("杭州")
    }

    private fun getPageData(city: String) {
        HttpHelper.getInstance(context).getWeather(city, ProgressSubscriber(context, IOnNextListener<List<WeatherBean>>
        { o ->
            headerContainer!!.visibility = View.VISIBLE
            weatherAdapter!!.setData(o)
            OurAnimation.runLayoutAnimation(listView)
        }))
    }

    /**
     * 获取当前城市的天气详情
     *
     * @param city 城市name
     */
    @SuppressLint("SetTextI18n")
    private fun getWeatherDetails(city: String) {
        HttpHelper.getInstance(context).getWeatherDetails(city, ProgressSubscriber(context, IOnNextListener<WeatherDetails> { o ->

            val wBean: WeatherDetails = o

            title.text = wBean.cityno

            nowCity!!.text = "当前城市：" + wBean.citynm + "  " + wBean.cityno

            nowDate!!.text = "今日日期：" + wBean.days + "  " + wBean.week

            nowGanMao!!.text = "温度区间：" + wBean.weather + "  " + wBean.temperature

            nowWenDu!!.text = "当前温度：" + wBean.temperature_curr

            nowWind!!.text = "今日风向：" + wBean.wind + "  " + wBean.winp
        }))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000 && resultCode == Activity.RESULT_OK) {
            val cityName = data.getStringExtra("name")
            if (cityName == "重庆")
                llContainer.background = ContextCompat.getDrawable(context, R.drawable.chongqing_bg)

            if (cityName == "杭州")
                llContainer.background = ContextCompat.getDrawable(context, R.drawable.hangzhou_bg)

            if (cityName == "荣昌")
                llContainer.background = ContextCompat.getDrawable(context, R.drawable.rongchang_bg)

            if (cityName == "南川")
                llContainer.background = ContextCompat.getDrawable(context, R.drawable.nanchuan_bg)

            if (cityName == "镇江")
                llContainer.background = ContextCompat.getDrawable(context, R.drawable.zhenjiang_bg)

            if (cityName == "扬中")
                llContainer.background = ContextCompat.getDrawable(context, R.drawable.yangzhong_bg)

            getPageData(data.getStringExtra("desc"))
            getWeatherDetails(data.getStringExtra("desc"))
        }
    }

    @OnClick(R.id.left_btn, R.id.right_btn)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.left_btn -> finish()
            R.id.right_btn -> {
                val intent = Intent(context, SingleChooseAct::class.java)
                startActivityForResult(intent, 10000)
            }
        }
    }
}
