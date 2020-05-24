package com.bigbrains.mygallery

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.bigbrains.mygallery.Helper.hasPermissions
import kotlinx.android.synthetic.main.activity_permissions.*

class PermissionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)
        grant_button.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                Helper.permissions,
                Helper.permissionRequestCode
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasPermissions(this)) {
            finish()
            return
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Helper.permissionRequestCode -> {
                if (grantResults.filter { it == PackageManager.PERMISSION_GRANTED }.size == grantResults.size) {
                    //Permission Granted
                    finish()
                } else {
                    showToast("Please allow all permissions from settings page")
                    Handler().postDelayed({
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                        })
                    }, 2000)
                }
            }
        }
    }

}