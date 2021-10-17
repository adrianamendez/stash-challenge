package com.stashinvest.stashchallenge.api.domain.model

import com.stashinvest.stashchallenge.api.model.ImageResult

data class ImageGeneralEntity(
    val resultCount: Int,
    val images: List<ImageEntity>
)