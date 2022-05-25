package com.example.aston_view_hw2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import java.lang.ref.WeakReference
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editSearch: EditText = findViewById(R.id.editTextForURL)


        editSearch.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val url = editSearch.text.toString()
                DownloadImageFromInternet(this).execute(url)

                val progressBarLoading = findViewById<ProgressBar>(R.id.progressBarLoadingImage)
                progressBarLoading.visibility = View.VISIBLE
                hideKeyboard()
                return@OnKeyListener true
            }
            false
        })
    }

    private class DownloadImageFromInternet(context: MainActivity) : AsyncTask<String, Void, Bitmap?>() {

        private val activityRef: WeakReference<MainActivity> = WeakReference(context)

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            val image: Bitmap?
            try {
                image = BitmapFactory.decodeStream(URL(imageURL).openStream())
            }
            catch (e: Exception) {
                Log.e("MainActivity", e.message ?: "")
                return null
            }
            return image
        }
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: Bitmap?) {
            val activity = activityRef.get()
            if (activity == null || activity.isFinishing) return
            val imageViewFromURL = activity.findViewById<ImageView>(R.id.imageViewFromURL)
            val progressBarLoading = activity.findViewById<ProgressBar>(R.id.progressBarLoadingImage)
            progressBarLoading.visibility = View.INVISIBLE
            if (result != null){
                imageViewFromURL.setImageBitmap(result)
            } else {
                val errorMsg = activity.resources.getString(R.string.error_msg)
                Toast.makeText(activity.applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                imageViewFromURL.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }

    private fun hideKeyboard() {
        val focus = this.currentFocus
        if (focus != null) {
            val hideKB = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideKB.hideSoftInputFromWindow(focus.windowToken, 0)
        }
        //else
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}