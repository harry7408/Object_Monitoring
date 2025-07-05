package com.harry.presentation.main

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.harry.presentation.ui.theme.Background300

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val value = viewModel.container.stateFlow.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        CameraScreen(
            modifier = Modifier.weight(0.75f),
            viewModel = viewModel,
        )

        Box(
            modifier = Modifier
                .weight(0.25f)
                .background(Background300)
                .fillMaxWidth()
        ) {
            // 하단에 인식된 물체 나타내는 부분
            if (value.apiResult?.data?.result?.isEmpty() == true) {
                Text(
                    text = "인식된 물체가 없습니다",
                    color = Color.Yellow
                )
            } else {
                LazyColumn {
                    items(value.apiResult?.data?.result?.size ?: 0, key = null) { idx ->
                        value.apiResult?.data?.result[idx].run {
                            Text(text = "${this?.item} : ${"%.2f".format(this?.confidence?.times(100))}%")
                        }
                    }
                }
            }
        }
    }
}