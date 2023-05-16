package com.example.porject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.airbnb.lottie.LottieAnimationView

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val loadingImage = findViewById(R.id.loading_Image) as LottieAnimationView
        loadingImage.playAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
        //startLoading()
    }
    private fun startLoading(){
        val handler = Handler()

        handler.postDelayed({finish()}, 2000)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}