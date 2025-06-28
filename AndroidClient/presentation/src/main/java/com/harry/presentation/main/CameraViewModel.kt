package com.harry.presentation.main

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import com.harry.domain.model.ApiResult
import com.harry.domain.usecase.ObjectDetectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val objectDetectUseCase: ObjectDetectUseCase
) : ViewModel(), ContainerHost<MyScreenState, MyScreenSideEffect> {

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



    fun onImageCaptured(imageProxy: ImageProxy) {

    }
}

data class MyScreenState(
    val apiResult: ApiResult? = null,
    val errorMessage: String? = null,
)

sealed interface MyScreenSideEffect {
    class Toast(val message: String) : MyScreenSideEffect
}