package com.harry.presentation.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.domain.model.ApiResult
import com.harry.domain.model.ImageFrame
import com.harry.domain.usecase.ObjectDetectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val objectDetectUseCase: ObjectDetectUseCase
) : ViewModel(), ContainerHost<MyScreenState, MyScreenSideEffect> {

    // private val _frameFlow = MutableSharedFlow<ImageFrame>(replay = 0, extraBufferCapacity = 1)
    private var reusableBitmap: Bitmap? = null

    override val container: Container<MyScreenState, MyScreenSideEffect> = container(
        initialState = MyScreenState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(
                        MyScreenSideEffect.Toast(throwable.message.orEmpty())
                    )
                }
            }
        }
    )

    fun onImageCaptured(imageFrame: ImageFrame) = intent {
        Timber.tag("MainViewModel").v("onImageCaptured Called")
        // I.O Scope에서 동작하도록 CoroutineBuilder 생성
        viewModelScope.launch(Dispatchers.IO) {

            // Bounding Box를 그리는 ResultOverlay에 필요한 정보를 위해 State에 값 저장
            reduce {
                state.copy(
                    frameWidth = imageFrame.width,
                    frameHeight = imageFrame.height,
                )
            }

            val imageFrame = ImageFrame(
                imageData = imageFrame.imageData,
                width = if (imageFrame.rotationDegree == 90 || imageFrame.rotationDegree == 270)
                    imageFrame.height else imageFrame.width,
                height = if (imageFrame.rotationDegree == 90 || imageFrame.rotationDegree == 270)
                    imageFrame.width else imageFrame.height,
                rotationDegree = 0 // 이미 회전된 상태
            )

            objectDetectUseCase(imageFrame)
                // Flow 예외 처리
                .catch { exception ->
                    Timber.tag("MainViewModel").e(exception.message.toString())

                    reduce {
                        state.copy(
                            errorMessage = exception.message.toString()
                        )
                    }

                    postSideEffect(
                        MyScreenSideEffect.Toast(message = exception.message.orEmpty())
                    )
                }
                .collect {
                    reduce {
                        // 결과 받아오는 부분
                        state.copy(
                            apiResult = it,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}

data class MyScreenState(
    val apiResult: ApiResult? = null,
    val frameWidth: Int = 0,
    val frameHeight: Int = 0,
    val errorMessage: String? = null,
)

sealed interface MyScreenSideEffect {
    class Toast(val message: String) : MyScreenSideEffect
}