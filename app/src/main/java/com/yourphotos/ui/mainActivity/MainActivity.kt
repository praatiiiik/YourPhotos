package com.yourphotos.ui.mainActivity

import android.Manifest
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.yourphotos.R
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {


    private val permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
    }


    private fun checkPermission(){
        val permissionsToRequest = mutableListOf<String>()
        permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        if(permissionsToRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

}

