package com.harry.presentation.preview

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.domain.usecase.UploadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val uploadUseCase: UploadUseCase
) : ViewModel() {

    fun onImageCaptured(image: ImageProxy) {
        viewModelScope.launch {
            val byteArray = convertImageProxyToByteArray(image)
            uploadUseCase(byteArray)
        }
    }

}