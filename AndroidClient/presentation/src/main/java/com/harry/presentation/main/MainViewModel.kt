package com.harry.presentation.main

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.domain.model.ApiResult
import com.harry.domain.usecase.ObjectDetectUseCase
import com.harry.presentation.util.toDomain
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

    fun getReusableBitmap(width: Int, height: Int): Bitmap {
        if (reusableBitmap == null || reusableBitmap?.width != width || reusableBitmap?.height != height) {
            reusableBitmap?.recycle()
            reusableBitmap = createBitmap(width, height)
        }
        return reusableBitmap!!
    }

    fun onImageCaptured(imageProxy: ImageProxy, context: Context) = intent {
        // I.O Scope에서 동작하도록 CoroutineBuilder 생성
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = getReusableBitmap(imageProxy.width, imageProxy.height)
            val imageFrame = imageProxy.toDomain(context, bitmap)

            objectDetectUseCase(imageFrame!!)
                // Flow 예외 처리
                .catch { exception ->
                    Timber.tag("MainViewModel").v(exception.message.toString())

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
    val errorMessage: String? = null,
)

sealed interface MyScreenSideEffect {
    class Toast(val message: String) : MyScreenSideEffect
}