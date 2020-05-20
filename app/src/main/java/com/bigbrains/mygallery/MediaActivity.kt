package com.bigbrains.mygallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import java.io.FileOutputStream

class MediaActivity : Activity() {

    private fun checkIntent(): Boolean {
        if (intent.action.equals(Intent.ACTION_SEND)) {
            if (intent.type?.startsWith("image/") == true || intent.type?.startsWith("video/") == true) {
                val receivedUri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                val receivedInputStream = contentResolver.openInputStream(receivedUri)
                val fileName = Helper.queryName(contentResolver, receivedUri)
                val outputStream =
                    FileOutputStream(getExternalFilesDir("MyGallery")?.path + "/" + fileName)
                Helper.copyFile(this, receivedInputStream, outputStream)
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkIntent()) {
            setContentView(R.layout.activity_main)
        } else {
            finish()
        }
    }

}

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}