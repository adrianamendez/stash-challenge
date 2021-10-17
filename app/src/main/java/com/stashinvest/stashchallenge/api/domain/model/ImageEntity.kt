package com.stashinvest.stashchallenge.api.domain.model

import com.stashinvest.stashchallenge.api.model.DisplaySize

data class ImageEntity(
    val id: String,
    val title: String,
    val displaySizes: List<DisplaySize>
)
