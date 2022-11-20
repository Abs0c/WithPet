package com.example.porject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView

class ViewActivity : AppCompatActivity() {
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    lateinit var img : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        img = findViewById<ImageView>(R.id.image_full)
        img.setImageBitmap(Picture)
        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
    }
    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {

        // 제스처 이벤트를 처리하는 메소드를 호출
        mScaleGestureDetector!!.onTouchEvent(motionEvent)
        return true
    }

    // 제스처 이벤트를 처리하는 클래스
    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {

            scaleFactor *= scaleGestureDetector.scaleFactor

            // 최소 0.5, 최대 2배
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 5.0f))

            // 이미지에 적용
            img.scaleX = scaleFactor
            img.scaleY = scaleFactor
            return true
        }
    }

}