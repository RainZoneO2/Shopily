package com.rain.shopily

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var animLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, ScrollingActivity::class.java)

        animLogo = findViewById(R.id.ivCart)
        animLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_splash))

        startAnim()
    }

    fun startAnim() {
        var runnable = Runnable {
            startActivity(Intent(this, ScrollingActivity::class.java))
            finish()
        }
        var handler = Handler()
        handler.postDelayed(runnable, 3000)

    }
}