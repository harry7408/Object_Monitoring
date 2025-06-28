package com.harry.presentation.main

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harry.domain.usecase.ObjectDetectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val objectDetectUseCase: ObjectDetectUseCase
) : ViewModel() {

    fun onImageCaptured(image: ImageProxy) {
        viewModelScope.launch {


        }
    }

}