package com.digitalme.digitalme

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.digitalme.digitalme.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.InputStream

class MainActivity : AppCompatActivity(), FunctionsImpl {
    private lateinit var binding: ActivityMainBinding
    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBinding()

        val view = binding.root

        onChooseImageClickListener()

        setContentView(view)
    }

    override fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    private val chooseImageActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            it.data?.data?.let {
                processChoosenImage(it)

                binding.itemBtnProcessImage.visibility = View.VISIBLE
                mBitmap = loadBitmapFromView(binding.root)
            }
        }
    }
    override fun onChooseImageClickListener() {
        binding.itemBtnChoose.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"

            chooseImageActivityResult.launch(photoPickerIntent)
        }
    }

    override fun processChoosenImage(uriData: Uri) {
        try {
            val imageUri: Uri = uriData
            val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            binding.itemImage.setImageBitmap(selectedImage)

            onProcessImageClickListener(binding.itemImage.drawable.toBitmap(binding.itemImage.width, binding.itemImage.height))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    override fun onProcessImageClickListener(imageView: Bitmap) {
        binding.itemBtnProcessImage.setOnClickListener {
            getImagePixels(imageView)
        }
    }

    override fun getImagePixels(imageView: Bitmap) {
        println("Image to ASCII: ${ImageToASCII().GrayscaleImageToASCII(imageView)}")
        binding.itemResultText.text = ImageToASCII().GrayscaleImageToASCII(imageView)
    }

    override fun getScreenSize(): ScreenModel {
        val display: Display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y

        return ScreenModel(
            width = width,
            height = height,
        )
    }

    private fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(
            v.width,
            v.height,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        v.layout(0, 0, v.width, v.height)
        v.draw(c)
        return b
    }
}

interface FunctionsImpl {
    fun initBinding()
    fun onChooseImageClickListener()
    fun processChoosenImage(uriData: Uri)
    fun onProcessImageClickListener(imageView: Bitmap)
    fun getImagePixels(imageView: Bitmap)
    fun getScreenSize(): ScreenModel
}

data class ScreenModel(
    val width: Int,
    val height: Int,
)
