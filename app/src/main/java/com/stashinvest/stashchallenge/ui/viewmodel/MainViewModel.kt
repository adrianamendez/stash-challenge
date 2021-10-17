package com.stashinvest.stashchallenge.ui.viewmodel

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.api.StashImageService
import com.stashinvest.stashchallenge.api.model.ImageResponse
import com.stashinvest.stashchallenge.api.model.ImageResult
import com.stashinvest.stashchallenge.api.domain.ILogger
import com.stashinvest.stashchallenge.api.domain.ILogger.LogLevel.ERROR
import com.stashinvest.stashchallenge.api.domain.IScheduler
import com.stashinvest.stashchallenge.common.DialogInfoUiModel
import com.stashinvest.stashchallenge.ui.fragment.uimodel.ImageUi
import com.stashinvest.stashchallenge.util.SingleLiveEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Response
import javax.inject.Inject

private const val TAG = "MainViewModel"

class MainViewModel @Inject constructor(
    private val stashImageService: StashImageService,
    private val scheduler: IScheduler,
    private val logger: ILogger
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var _loaderVisibility = MutableLiveData(GONE)
    val loaderVisibility: LiveData<Int> get() = _loaderVisibility
    private var _imagesList = MutableLiveData(listOf<ImageUi>())
    val imagesList: LiveData<List<ImageUi>> get() = _imagesList
    private var _errorEvent = SingleLiveEvent<DialogInfoUiModel>()
    val errorEvent: LiveData<DialogInfoUiModel> get() = _errorEvent
    private var _hideKeyboardEvent = SingleLiveEvent<Boolean>()
    val hideKeyboardEvent: LiveData<Boolean> get() = _hideKeyboardEvent

    private fun setLoaderVisibility(visibility: Int) {
        _loaderVisibility.value = visibility
    }

    /**
     * Searches the images. For this shows the loader and hides the keyboard. Then handles
     * possible errors or the search success.
     */

    fun searchImages(phrase: String) {
        setLoaderVisibility(VISIBLE)
        _hideKeyboardEvent.value = true
        compositeDisposable.add(stashImageService.searchImages(phrase)
            .compose(scheduler.applySingleDefaultSchedulers()).subscribe(
            { handleSearchSuccess(it) },
            { handleSearchError(it) }
        )
        )
    }

    /**
     * Process the successful search scenario. Hides the loader and
     * shows the empty state only if the result of the concatenation is empty.
     * @param response The results of the search.
     */

    private fun handleSearchSuccess(response: ImageResponse) {
        setLoaderVisibility(GONE)
        val images = response.images
        _imagesList.value = images.map { ImageUi.mapFromDomain(it) }
      //  updateImages(images)

    }

    /**
     * Handles the failure search scenario, for this it hides the loader, triggers the error event,
     * logs the error and show the empty state if there are no previously loaded products.
     * @param error The search error.
     */

    private fun handleSearchError(error: Throwable) {
        setLoaderVisibility(GONE)
        _errorEvent.value = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_images_search
        )
        logger.log(TAG, error.toString(), error, ERROR)
    }


}