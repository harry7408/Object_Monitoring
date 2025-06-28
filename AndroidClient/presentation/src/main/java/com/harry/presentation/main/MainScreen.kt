package com.harry.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val value = viewModel.container.stateFlow.value

    Column(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        CameraScreen(
            modifier = Modifier.weight(0.7f),
            viewModel = viewModel,
        )

        // 하단에 인식된 물체 나타내는 부분
        if (value.apiResult?.data?.isEmpty() == true) {
            Text(
                text = "인식된 물체가 없습니다",
                color = Color.Yellow
            )
        } else {
            LazyColumn {
                items(value.apiResult?.data?.size ?: 0, key = null) { idx ->
                    value.apiResult?.data[idx].run {
                        Text(text = "${this?.item} : ${this?.confidence}")
                    }
                }
            }
        }
    }
}