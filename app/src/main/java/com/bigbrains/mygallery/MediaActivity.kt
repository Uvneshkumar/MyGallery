package com.bigbrains.mygallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class MediaActivity : Activity() {

    private lateinit var mediaIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        saveIntent()
    }

    override fun onResume() {
        super.onResume()
        if (mediaIntent.action.equals(Intent.ACTION_SEND) && (mediaIntent.type?.startsWith("image/") == true || mediaIntent.type?.startsWith(
                "video/"
            ) == true)
        ) {
            if (Helper.hasPermissions(this)) {
                copyFileFromIntent()
                finish()
            } else {
                startActivity(Intent(this, PermissionActivity::class.java))
            }
        } else {
            setContentView(R.layout.activity_main)
        }
    }

    private fun saveIntent() {
        mediaIntent = intent
    }

    private fun copyFileFromIntent() {
        val receivedUri = mediaIntent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        val receivedInputStream = contentResolver.openInputStream(receivedUri)
        val fileName = Helper.queryName(contentResolver, receivedUri)

        val myDir = File(Environment.getExternalStorageDirectory(), "MyGallery")
        if (!myDir.exists()) {
            if (!myDir.mkdirs()) {
                showToast("Error creating dir")
            }
        }
        val myFile = File(myDir, fileName)
        val outputStream = FileOutputStream(myFile)

        Helper.copyFile(this, receivedInputStream, outputStream)
    }

}

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}