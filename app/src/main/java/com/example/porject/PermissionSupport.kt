package com.example.porject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.security.Permission
import java.security.Permissions

class PermissionSupport(var activity: Activity, var context: Context) {
    private val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //Manifest.permission.ACCESS_NETWORK_STATE,
        //Manifest.permission.ACCESS_FINE_LOCATION,
        //Manifest.permission.ACCESS_COARSE_LOCATION,
        //Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.CAMERA
    )
    var permissionList = ArrayList<String>()

    fun checkPermission(): Boolean{
        permissionList = ArrayList()
        for (tempper in permissions){
            val result = ContextCompat.checkSelfPermission(context, tempper)
            if (result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(tempper)
            }
        }
        if(!permissions.isEmpty()){
            return false
        }
        return true
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(activity, permissionList.toTypedArray(), PERMISSIONS_REQUEST_CODE)
    }
}