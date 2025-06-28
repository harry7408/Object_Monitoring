package com.harry.presentation.util

import android.Manifest
import android.content.Context
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

fun requestCameraPermission(context: Context, onGranted: ()-> Unit) {
    TedPermission.create()
        .setPermissionListener(object: PermissionListener {
            override fun onPermissionGranted() {
                onGranted()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(context, "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        })
        .setPermissions(Manifest.permission.CAMERA)
        .check()
}