package com.harry.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.harry.presentation.component.CameraPreview
import com.harry.presentation.component.ResultOverlay
import com.harry.presentation.util.requestCameraPermission
import timber.log.Timber

@Composable
fun CameraScreen(
    modifier: Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val value = viewModel.container.stateFlow.collectAsState().value
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
        if (hasPermission) {
            CameraPreview(
                modifier = Modifier.fillMaxSize()
            ) { imageFrame ->
                // 수정 필요
                Timber.tag("CameraScreen").v("onImageCaptured")
                viewModel.onImageCaptured(imageFrame)
            }
        } else {
            Text(
                text = "카메라 권한이 필요합니다",
                color = Color.Yellow
            )
        }

        value.apiResult?.let { apiResult ->
            ResultOverlay(
                apiResult = apiResult,
                frameWidth = value.frameWidth,
                frameHeight = value.frameHeight
            )
        }
    }
}





