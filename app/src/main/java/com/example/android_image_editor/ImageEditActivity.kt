/*
package com.example.android_image_editor

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import android.graphics.drawable.BitmapDrawable

class ImageEditActivity : AppCompatActivity() {
    lateinit var image_in_editor: ImageView
    lateinit var image_text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_edit)

        initView()



        image_text.setTextColor(Color.BLUE)
        image_text.setBackgroundColor(Color.RED)

        image_text.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                view.x = (motionEvent.rawX - view.width / 2.0f)
                view.y = (motionEvent.rawY - 200.0f - view.height / 2.0f)
            }

            return@setOnTouchListener true
        }

    }

    private fun setImageFromMainActivity() {
        val extras = intent.extras
        val byteArray = extras!!.getByteArray("image")
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
        image_in_editor.setImageBitmap(bmp)
    }

    private fun initView() {
        image_in_editor = findViewById(R.id.image_in_editor)
        image_text = findViewById(R.id.image_text)
    }
}*/
