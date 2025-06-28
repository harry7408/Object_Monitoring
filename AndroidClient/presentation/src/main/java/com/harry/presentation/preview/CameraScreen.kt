package com.harry.presentation.preview

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.harry.presentation.util.requestCameraPermission

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        requestCameraPermission(context) {
            hasPermission = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        CameraPreview(
            modifier = Modifier.fillMaxSize()
        ) { image ->
            viewModel.onImageCaptured(image)
        }

        Text(
            text = "카메라 Preview",
            color = Color.Yellow
        )
    }

}

// 카메라 화면 불러오기 (Android View 활용)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onImageCaptured: (ImageProxy) -> Unit
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val previewView = remember {
        PreviewView(context)
    }


    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder().build().apply {
            surfaceProvider = previewView.surfaceProvider
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    FrameAnalyzer(onImageCaptured)
                )
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifeCycleOwner, cameraSelector, preview, imageAnalysis)
    }

    AndroidView(
        factory = {
            previewView
        },
        modifier = modifier
    )
}



