package com.example.porject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.airbnb.lottie.LottieAnimationView

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val loadingImage = findViewById(R.id.loading_Image) as LottieAnimationView
        loadingImage.playAnimation()
        startLoading()
    }
    private fun startLoading(){
        val handler = Handler()
        handler.postDelayed({finish()}, 2000)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}