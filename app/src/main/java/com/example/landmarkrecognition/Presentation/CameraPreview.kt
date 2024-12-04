package com.example.landmarkrecognition.Presentation

import android.service.controls.Control
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraPreview(
    controller : LifecycleCameraController,
    modifier : Modifier = Modifier
){
    val LifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {Context->
            PreviewView(Context).apply {
                this.controller = controller
                controller.bindToLifecycle(LifecycleOwner)
            }
        },
        modifier = modifier

    )
}
