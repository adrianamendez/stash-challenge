package com.stashinvest.stashchallenge.ui.fragment.uimodel

import android.os.Parcelable
import com.stashinvest.stashchallenge.api.model.DetailImageResponse
import com.stashinvest.stashchallenge.util.EMPTY_STRING
import kotlinx.android.parcel.Parcelize

@Parcelize
class DetailImageUi(
    var id: String = EMPTY_STRING,
    var title: String = EMPTY_STRING,
    var artist: String = EMPTY_STRING,
    var caption: String = EMPTY_STRING
) : Parcelable {

    companion object {
        fun mapFromDomain(detailImageResponse: DetailImageResponse) =
            detailImageResponse.metadataResponse.metadata.first().run {
                DetailImageUi(
                    this.id,
                    this.title,
                    this.artist,
                    this.caption
                )
            }
    }
}