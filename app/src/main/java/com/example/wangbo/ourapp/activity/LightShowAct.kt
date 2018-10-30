package com.example.wangbo.ourapp.activity

import android.annotation.SuppressLint
import android.app.Service
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

import com.example.wangbo.ourapp.R
import com.jackmar.jframelibray.base.JBaseAct

import java.util.Arrays

import butterknife.BindView
import butterknife.OnClick
import android.os.Vibrator
import android.widget.TextView

/**
 * Created by wangbo on 2018/7/27.
 *
 * 灯光实验室
 */
class LightShowAct : JBaseAct() {

    private lateinit var fadeAlpha: Animation

    private lateinit var showAlpha: Animation

    private val BREATH_TIME = 3000

    @BindView(R.id.image_bg)
    lateinit var imgBg: ImageView

    @BindView(R.id.open_light)
    lateinit var showLight: TextView

    internal var list = Arrays.asList("#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#00FFFF", "#0000FF", "#8B00FF")

    private var isOpen: Boolean = false

    lateinit var camera: Camera

    private var position: Int = 0

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val color = Color.parseColor(list[position])
            val colorDrawable = ColorDrawable(color)
            imgBg.setBackgroundDrawable(colorDrawable)
            imgBg.clearAnimation()
            imgBg.animation = show
            imgBg.clearAnimation()
            imgBg.animation = fade
            val vv = application.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vv.vibrate(2000)//震半秒钟
        }
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            if (isStar) {
                handler.sendEmptyMessage(position)
                position = (position + 1) % list.size
            }

            handler.postDelayed(this, 3000)
        }
    }

    private val show: Animation
        get() {
            showAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha_show)
            showAlpha.duration = BREATH_TIME.toLong()
            showAlpha.startOffset = 100
            return showAlpha
        }

    private val fade: Animation
        get() {
            fadeAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha_fade)
            fadeAlpha.duration = BREATH_TIME.toLong()
            fadeAlpha.startOffset = 100
            return fadeAlpha
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLyContentNoTitile(R.layout.act_light_show, true)
    }

    override fun initView() {
        camera = Camera.open()
        camera.startPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.stopPreview()
        camera.release()
    }

    override fun initData() {

    }

    /**
     * 灯光控制
     */
    @OnClick(R.id.open_light, R.id.star_light, R.id.mid_light, R.id.hight_light)
    internal fun onClicked(view: View) {
        when (view.id) {
            R.id.open_light -> {
                val parameter = camera.parameters
                if (!isOpen) {
                    openLight(parameter)
                    showLight.text = "关灯"
                } else {
                    closeLight(parameter)
                    showLight.text = "开灯"
                }
            }

            R.id.star_light -> if (isStar) {
                closeStar()
            } else {
                openStar()
            }

            R.id.mid_light -> showLightValue(0.5.toFloat())

            R.id.hight_light -> showLightValue(1.toFloat())
        }
    }

    private fun showLightValue(v: Float) {
        val window = window
        val lp = window.attributes
        lp.screenBrightness = v
        window.attributes = lp
    }

    private fun closeLight(parameter: Camera.Parameters) {
        isOpen = false
        parameter.flashMode = Camera.Parameters.FLASH_MODE_OFF
        camera.parameters = parameter
    }

    private fun openLight(parameter: Camera.Parameters) {
        isOpen = true
        parameter.flashMode = Camera.Parameters.FLASH_MODE_TORCH
        camera.parameters = parameter
    }

    private fun openStar() {
        position = 0
        isStar = true
        handler.postDelayed(runnable, 3000)
    }

    private fun closeStar() {
        position = 0
        isStar = false
        imgBg.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    companion object {

        var isStar = false
    }
}
