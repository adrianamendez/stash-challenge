package utils

import com.stashinvest.stashchallenge.api.model.*
import com.stashinvest.stashchallenge.ui.fragment.uimodel.DetailImageUi

fun getSampleImagesResponse() =
    ImageResponse(
        1,
        getSampleImagesResult()
    )

fun getSampleImagesResult() = listOf(
    ImageResult(
        "id",
        "title",
        getDisplaySizes()
    )
)

fun getDisplaySizes() = listOf(
    DisplaySize(
        false,
        "name",
        "uri"
    )
)

fun getDetailImages() = DetailImageUi()

fun getDetailImageResponse() = DetailImageResponse(getMetadataResponse(), getSampleImagesResponse())

fun getMetadataResponse() = MetadataResponse(listOf(getMetadata()))

fun getMetadata() = Metadata(
    "id",
    "title",
    "artist",
    "caption"
)