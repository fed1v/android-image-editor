package com.example.android_image_editor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var btnOpenCamera: Button
    lateinit var btnTextSettings: Button
    lateinit var image_text: TextView
    lateinit var enter_text: EditText
    lateinit var btn_ok: Button
    lateinit var textSettingsDialog: AlertDialog.Builder
    lateinit var root: LinearLayout
    lateinit var numberPicker: NumberPicker
    lateinit var textSizeView: View

    val textSettings = arrayOf("Text size", "Text color", "Text alignment", "Background")

    val textAlignmentOptions = arrayOf("Start", "Center", "End")
    var selectedTextAlignmentIndex = 0
    var selectedTextAlignmentOption = textAlignmentOptions[selectedTextAlignmentIndex]

    val textColorOptions = arrayOf("White", "Black", "Blue", "Red", "Green", "Yellow", "Cyan")
    var selectedTextColorIndex = 0
    var selectedTextColor = textColorOptions[selectedTextColorIndex]

    val backgroundOptions =
        arrayOf("No", "White", "Black", "Blue", "Red", "Green", "Yellow", "Cyan")
    var selectedBackgroundIndex = 0
    var selectedBackground = backgroundOptions[selectedBackgroundIndex]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        checkCameraPermissions()

        btnOpenCamera.setOnClickListener { openCamera() }

        btnTextSettings.setOnClickListener {
            textSettingsDialog
                .setTitle("Text settings")
                .setItems(textSettings) { dialogInterface, which ->
                    openSettingsDialog(which)
                }
                .show()
        }
        btn_ok.setOnClickListener {
            val text = enter_text.text.toString()
            image_text.text = text
        }

        image_text.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                view.x = (motionEvent.rawX - view.width / 2.0f)
                view.y = (motionEvent.rawY - 400.0f - view.height / 2.0f)
            }

            return@setOnTouchListener true
        }
    }

    private fun openSettingsDialog(which: Int) {
        textSizeView = layoutInflater.inflate(R.layout.picker_text_size, null)
        initNumberPicker()
        when (which) {
            0 -> openTextSizeSettings()
            1 -> openTextColorSettings()
            2 -> openTextAlignmentSettings()
            3 -> openBackgroundSettings()
        }
    }

    private fun openTextSizeSettings() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Text size")
            .setPositiveButton("Ok") { _, _ ->
                changeTextSize()
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .setView(textSizeView)
            .show()
    }

    private fun openTextAlignmentSettings() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Text alignment")
            .setSingleChoiceItems(
                textAlignmentOptions,
                selectedTextAlignmentIndex
            ) { dialog, which ->
                selectedTextAlignmentIndex = which
                selectedTextAlignmentOption = textAlignmentOptions[selectedTextAlignmentIndex]
            }
            .setPositiveButton("Ok") { _, _ ->
                changeTextAlignment(selectedTextAlignmentIndex)
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun openTextColorSettings() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Text color")
            .setSingleChoiceItems(textColorOptions, selectedTextColorIndex) { dialog, which ->
                selectedTextColorIndex = which
                selectedTextColor = textColorOptions[selectedTextColorIndex]
            }
            .setPositiveButton("Ok") { _, _ ->
                changeTextColor()
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun openBackgroundSettings() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Background")
            .setSingleChoiceItems(backgroundOptions, selectedBackgroundIndex) { dialog, which ->
                selectedBackgroundIndex = which
                selectedBackground = backgroundOptions[selectedBackgroundIndex]
            }
            .setPositiveButton("Ok") { _, _ ->
                changeBackgroundColor()
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun changeBackgroundColor() {
        when (selectedBackground) {
            "No" -> image_text.setBackgroundColor(Color.TRANSPARENT)
            "Black" -> image_text.setBackgroundColor(Color.BLACK)
            "White" -> image_text.setBackgroundColor(Color.WHITE)
            "Blue" -> image_text.setBackgroundColor(Color.BLUE)
            "Red" -> image_text.setBackgroundColor(Color.RED)
            "Green" -> image_text.setBackgroundColor(Color.GREEN)
            "Yellow" -> image_text.setBackgroundColor(Color.YELLOW)
            "Cyan" -> image_text.setBackgroundColor(Color.CYAN)
        }
    }

    private fun changeTextSize() {
        image_text.textSize = numberPicker.value.toFloat()
    }

    private fun changeTextAlignment(textAlignmentIndex: Int) {
        image_text.gravity = when (textAlignmentIndex) {
            0 -> Gravity.START
            2 -> Gravity.END
            else -> Gravity.CENTER
        }
    }

    private fun changeTextColor() {
        when (selectedTextColor) {
            "Black" -> image_text.setTextColor(Color.BLACK)
            "White" -> image_text.setTextColor(Color.WHITE)
            "Blue" -> image_text.setTextColor(Color.BLUE)
            "Red" -> image_text.setTextColor(Color.RED)
            "Green" -> image_text.setTextColor(Color.GREEN)
            "Yellow" -> image_text.setTextColor(Color.YELLOW)
            "Cyan" -> image_text.setTextColor(Color.CYAN)
        }
    }

    private fun initNumberPicker() {
        numberPicker = textSizeView.findViewById(R.id.number_picker)
        numberPicker.minValue = 0
        numberPicker.maxValue = 100
        numberPicker.value = 35
    }

    private fun initView() {
        root = findViewById(R.id.linear_layout)

        imageView = findViewById(R.id.image_view)
        image_text = findViewById(R.id.image_text)
        image_text.setTextColor(Color.BLACK)

        btnOpenCamera = findViewById(R.id.btn_open_camera)
        btnTextSettings = findViewById(R.id.btn_text_settings)
        btn_ok = findViewById(R.id.btn_ok)
        enter_text = findViewById(R.id.enter_text)

        textSettingsDialog = AlertDialog.Builder(this)

        textSizeView = layoutInflater.inflate(R.layout.picker_text_size, null)

        initNumberPicker()
    }

    private fun checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.extras != null) {
            val capturedImage = data.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(capturedImage)
        }
    }

}