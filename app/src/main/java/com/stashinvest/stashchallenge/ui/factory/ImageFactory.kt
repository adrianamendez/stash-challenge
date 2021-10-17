package com.stashinvest.stashchallenge.ui.factory

import com.stashinvest.stashchallenge.ui.fragment.uimodel.ImageUi
import com.stashinvest.stashchallenge.ui.viewmodel.ImageViewModel
import javax.inject.Inject

class ImageFactory @Inject constructor() {
    fun createImageViewModel(
        imageResult: ImageUi,
        listener: (id: String, uri: String?) -> Unit
    ): ImageViewModel {
        return ImageViewModel(imageResult, listener)
    }
}
