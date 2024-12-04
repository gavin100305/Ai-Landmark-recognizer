package com.example.landmarkrecognition

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.landmarkrecognition.Presentation.CameraPreview
import com.example.landmarkrecognition.Presentation.LandmarkImageAnalyzer
import com.example.landmarkrecognition.data.TfLiteClassifier
import com.example.landmarkrecognition.domain.Classification
import com.example.landmarkrecognition.ui.theme.LandMarkRecognitionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if(!hasCameraPermission()){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),0
            )
        }
        setContent {
            LandMarkRecognitionTheme {

                var classifications = remember {
                    mutableStateOf(emptyList<Classification>())
                }

                val analyzer = remember {
                    LandmarkImageAnalyzer(
                        classifier = TfLiteClassifier(
                            context = applicationContext
                        ),
                        onResults = { results ->
                            classifications.value = results // Properly update the state value
                        }
                    )
                }

                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                        setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(applicationContext),
                            analyzer
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    CameraPreview(controller, modifier = Modifier.fillMaxSize())

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)) {

                        classifications.value.forEach {
                            Text(
                                text = it.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                textAlign = TextAlign.Center,
                                fontSize = 32.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    )== PackageManager.PERMISSION_GRANTED
}

