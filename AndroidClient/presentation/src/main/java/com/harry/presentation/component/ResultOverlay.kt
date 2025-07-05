package com.harry.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.harry.domain.model.ApiResult

@Composable
fun ResultOverlay(
    apiResult: ApiResult,
    frameWidth: Int,
    frameHeight: Int,
) {
    val detections = apiResult.data.result


    if (detections.isNotEmpty()) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val screenWidth = this.maxWidth.value
            val screenHeight = this.maxHeight.value

            detections.forEach { detection ->
                // Bounding Box 계산
                val boxWidth = (((detection.right - detection.left) / frameWidth) * screenWidth).toFloat()
                val boxHeight = (((detection.bottom - detection.top) / frameHeight) * screenHeight).toFloat()
                val boxLeftOffset = ((detection.left / frameWidth) * screenWidth).toFloat()
                val boxTopOffset = ((detection.top / frameHeight) * screenHeight).toFloat()


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(boxLeftOffset.dp, boxTopOffset.dp)
                ) {
                    // Bounding Box 그리기
                    Box(
                        modifier = Modifier
                            .border(3.dp, Color.Cyan)
                            .width(boxWidth.dp)
                            .height(boxHeight.dp)
                    )

                    // 결과 텍스트
                    Box(modifier = Modifier.padding(3.dp)) {
                        Text(
                            text = "${detection.item} ${"%.2f".format(detection.confidence * 100)}%",
                            modifier = Modifier
                                .background(Color.Black)
                                .padding(5.dp, 0.dp),
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}