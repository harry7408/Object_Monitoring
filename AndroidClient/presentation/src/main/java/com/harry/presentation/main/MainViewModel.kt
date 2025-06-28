package com.harry.presentation.main

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.harry.domain.model.ApiResult
import com.harry.domain.model.ImageFrame
import com.harry.domain.usecase.ObjectDetectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import androidx.core.graphics.createBitmap
import androidx.lifecycle.viewModelScope
import com.harry.presentation.util.YuvToRgbConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val objectDetectUseCase: ObjectDetectUseCase
) : ViewModel(), ContainerHost<MyScreenState, MyScreenSideEffect> {

    private val _frameFlow = MutableSharedFlow<ImageFrame>(replay = 0, extraBufferCapacity = 1)
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

    fun onImageCaptured(imageProxy: ImageProxy,context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO : Domain Model로 전환 후 UseCase 호출
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