package com.stashinvest.stashchallenge.ui.viewmodel

import android.view.View
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.api.IScheduler
import com.stashinvest.stashchallenge.api.StashImageService
import com.stashinvest.stashchallenge.api.model.DetailImageResponse
import com.stashinvest.stashchallenge.common.DialogInfoUiModel
import com.stashinvest.stashchallenge.ui.fragment.uimodel.DetailImageUi
import com.stashinvest.stashchallenge.ui.fragment.uimodel.ImageUi
import com.stashinvest.stashchallenge.util.EMPTY_STRING
import com.stashinvest.stashchallenge.util.ILogger
import com.stashinvest.stashchallenge.util.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

private const val TAG = "PopUpDialogViewModel"

class PopUpDialogViewModel @Inject constructor(
    private val stashImageService: StashImageService,
    private val scheduler: IScheduler,
    private val logger: ILogger
) : ViewModel() {

    private lateinit var id: String
    private lateinit var uri: String
    private val compositeDisposable = CompositeDisposable()

    private var _errorEvent = SingleLiveEvent<DialogInfoUiModel>()
    val errorEvent: LiveData<DialogInfoUiModel> get() = _errorEvent

    private var _imageDetail = MutableLiveData(DetailImageUi())
    val imageDetail: LiveData<DetailImageUi> get() = _imageDetail

    private var _imagesList = MutableLiveData(listOf<ImageUi>())
    val imagesList: LiveData<List<ImageUi>> get() = _imagesList

    val resultCount: LiveData<Int> get() = _resultCount
    private var _resultCount = MutableLiveData(1)

    val imageUri: LiveData<String> get() = _imageUri
    private var _imageUri = MutableLiveData(EMPTY_STRING)

    private var _loaderVisibility = MutableLiveData(View.GONE)
    val loaderVisibility: LiveData<Int> get() = _loaderVisibility

    fun initView(id: String, uri: String?) {
        this.id = id
        this.uri = uri ?: EMPTY_STRING
        setMainImageUri()
        loadInfoAndRelatedImages()
    }

    private fun setMainImageUri() {
        if (this.uri.isNotEmpty()) {
            _imageUri.value = this.uri
        }
    }

    private fun loadInfoAndRelatedImages() {
        setLoaderVisibility(VISIBLE)
        compositeDisposable.add(stashImageService.getComposeDetailImages(id)
            .compose(scheduler.applySingleDefaultSchedulers()).subscribe(
                { handleInfoAndRelatedImageSuccess(it) },
                { handleInfoAndRelatedImageError(it) }
            )
        )
    }

    /**
     * Process the successful description and related images scenario. Hides the loader
     * @param response The results of the image information.
     */

    private fun handleInfoAndRelatedImageSuccess(response: DetailImageResponse) {
        setLoaderVisibility(View.GONE)
        if (!response.metadataResponse.metadata.isNullOrEmpty()) {
            _imageDetail.value = DetailImageUi.mapFromDomain(response)
        }
        _imagesList.value = response.imageResponse.images.map { ImageUi.mapFromDomain(it) }
        _resultCount.value = response.imageResponse.resultCount
    }

    /**
     * Handles the failure search scenario, for this it hides the loader, triggers the error event,
     * @param error The zip services error.
     */

    private fun handleInfoAndRelatedImageError(error: Throwable) {
        setLoaderVisibility(View.GONE)
        _errorEvent.value = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_images_detail_search
        )
        logger.log(TAG, error.toString(), error, ILogger.LogLevel.ERROR)
    }

    private fun setLoaderVisibility(visibility: Int) {
        _loaderVisibility.value = visibility
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}