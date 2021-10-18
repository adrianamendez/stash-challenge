package viewmodel

import android.view.View
import android.view.View.GONE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.api.StashImageService
import com.stashinvest.stashchallenge.common.DialogInfoUiModel
import com.stashinvest.stashchallenge.ui.fragment.uimodel.ImageUi
import com.stashinvest.stashchallenge.ui.viewmodel.MainViewModel
import com.stashinvest.stashchallenge.util.ILogger
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import utils.TestScheduler
import utils.callPrivateFun
import utils.getSampleImagesResponse
import utils.setField

private const val FIELD_NAME_LOADER = "_loaderVisibility"
private const val FIELD_NAME_IMAGES_LIST = "_imagesList"
private const val FIELD_NAME_ERROR = "_imagesList"
private const val FIELD_NAME_COMPOSITE_DISPOSABLE = "compositeDisposable"
private const val FUNCTION_NAME_SET_LOADER_VISIBILITY = "setLoaderVisibility"
private const val FUNCTION_NAME_ON_CLEARED = "onCleared"

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private lateinit var sut: MainViewModel
    private val testScheduler = TestScheduler()

    @Mock
    private lateinit var stashImageServiceMock: StashImageService

    @Mock
    private lateinit var loggerMock: ILogger

    @Mock
    private lateinit var compositeDisposableMock: CompositeDisposable

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        sut = MainViewModel(stashImageServiceMock, testScheduler, loggerMock)
    }

    @Test
    fun getLoader_loaderSet_returnVisibility() {
        // given
        val expectedVisibility = View.GONE
        setField(FIELD_NAME_LOADER, MutableLiveData(expectedVisibility), sut)
        // when
        val result = sut.loaderVisibility
        // then
        assertEquals(expectedVisibility, result.value)
    }

    @Test
    fun getImagesList_listSet_returnList() {
        // given
        val expectedList = listOf<ImageUi>()
        setField(FIELD_NAME_IMAGES_LIST, MutableLiveData(expectedList), sut)
        // when
        val result = sut.imagesList
        // then
        assertEquals(expectedList, result.value)
    }

    @Test
    fun getError_errorSet_returnError() {
        // given
        val expectedError = null
        setField(FIELD_NAME_ERROR, MutableLiveData(expectedError), sut)
        // when
        val result = sut.errorEvent
        // then
        assertEquals(expectedError, result.value)
    }

    @Test
    fun setLoaderVisibility_invoked_visibilityChanged() {
        // given
        val expectedVisibility = View.GONE
        setField(FIELD_NAME_LOADER, MutableLiveData(expectedVisibility), sut)
        // when
        sut.callPrivateFun(FUNCTION_NAME_SET_LOADER_VISIBILITY, expectedVisibility)
        // then
        assertEquals(expectedVisibility, sut.loaderVisibility.value)
    }

    @Test
    fun onCleared_invoked_disposableCleared() {
        // given
        setField(FIELD_NAME_COMPOSITE_DISPOSABLE, compositeDisposableMock, sut)
        // when
        sut.callPrivateFun(FUNCTION_NAME_ON_CLEARED)
        // then
        verify(compositeDisposableMock).clear()
    }

    @Test
    fun searchImages_invoked_success() {
        // given
        val imagesResponse = getSampleImagesResponse()
        val expectedList = getSampleImagesResponse().images.map { ImageUi.mapFromDomain(it) }

        whenever(
            stashImageServiceMock.searchImages(
                ArgumentMatchers.anyString()
            )
        ).thenReturn(Single.just(imagesResponse))
        setField(FIELD_NAME_IMAGES_LIST, MutableLiveData(expectedList), sut)
        // when
        sut.searchImages(ArgumentMatchers.anyString())
        // then
        assertEquals(GONE, sut.loaderVisibility.value)
        assertEquals(expectedList.size, sut.imagesList.value?.size)
        assertEquals(expectedList.first().id, sut.imagesList.value?.first()?.id)
        assertEquals(expectedList.first().title, sut.imagesList.value?.first()?.title)
    }

    @Test
    fun searchImages_invoked_failure() {
        // given
        val error = Throwable("error fetching images")
        val errorInfo = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_images_search
        )
        whenever(
            stashImageServiceMock.searchImages(
                ArgumentMatchers.anyString()
            )
        ).thenReturn(Single.error(error))
        setField(FIELD_NAME_ERROR, MutableLiveData(error), sut)
        // when
        sut.searchImages(ArgumentMatchers.anyString())
        // then
        assertEquals(GONE, sut.loaderVisibility.value)
        assertEquals(errorInfo, sut.errorEvent.value)
    }
}