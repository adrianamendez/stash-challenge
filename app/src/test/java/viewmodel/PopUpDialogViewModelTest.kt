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
import com.stashinvest.stashchallenge.ui.viewmodel.PopUpDialogViewModel
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
import utils.*

private const val FIELD_NAME_LOADER = "_loaderVisibility"
private const val FIELD_NAME_IMAGES_LIST = "_imagesList"
private const val FIELD_NAME_IMAGES_DETAIL = "_imageDetail"
private const val FIELD_NAME_RESULT_COUNT = "_resultCount"
private const val FIELD_NAME_ERROR = "_imagesList"
private const val FIELD_NAME_IMAGE_URI = "_imageUri"
private const val FIELD_NAME_COMPOSITE_DISPOSABLE = "compositeDisposable"
private const val FUNCTION_NAME_SET_LOADER_VISIBILITY = "setLoaderVisibility"
private const val FUNCTION_NAME_ON_CLEARED = "onCleared"

@RunWith(MockitoJUnitRunner::class)
class PopUpDialogViewModelTest {

    companion object {
        const val ID = "123312"
        const val URI = "https://youtu.be/KVgkE7M6QYA"
        const val EMPTY_URI = ""
    }

    private lateinit var sut: PopUpDialogViewModel
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
        sut = PopUpDialogViewModel(stashImageServiceMock, testScheduler, loggerMock)
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
    fun getImagesDetail_imagesDetailSet_returnDetail() {
        // given
        val expectedDetail = getDetailImages()
        setField(FIELD_NAME_IMAGES_DETAIL, MutableLiveData(expectedDetail), sut)
        // when
        val result = sut.imageDetail
        // then
        assertEquals(expectedDetail, result.value)
    }

    @Test
    fun getResultCount_resultCountSet_returnCount() {
        // given
        val expectedCount = 1
        setField(FIELD_NAME_RESULT_COUNT, MutableLiveData(expectedCount), sut)
        // when
        val result = sut.resultCount
        // then
        assertEquals(expectedCount, result.value)
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
    fun setMainImageUri_invoked_uriSet() {
        // given
        val imagesResponse = getDetailImageResponse()
        val expectedDetail = getDetailImages()
        val expectedList = getSampleImagesResponse().images.map { ImageUi.mapFromDomain(it) }
        val expectedCount = 1
        whenever(
            stashImageServiceMock.getComposeDetailImages(
                ArgumentMatchers.anyString()
            )
        ).thenReturn(Single.just(imagesResponse))
        setField(FIELD_NAME_IMAGES_LIST, MutableLiveData(expectedList), sut)
        setField(FIELD_NAME_RESULT_COUNT, MutableLiveData(expectedCount), sut)
        setField(FIELD_NAME_IMAGES_DETAIL, MutableLiveData(expectedDetail), sut)
        setField(FIELD_NAME_IMAGE_URI, MutableLiveData(URI), sut)
        // when
        sut.initView(ID, URI)

        // then
        assertEquals(URI, sut.imageUri.value)
    }

    @Test
    fun initView_setMainImageUriInvoked_uriEmpty() {
        // given
        val imagesResponse = getDetailImageResponse()
        val expectedDetail = getDetailImages()
        val expectedList = getSampleImagesResponse().images.map { ImageUi.mapFromDomain(it) }
        val expectedCount = 1
        whenever(
            stashImageServiceMock.getComposeDetailImages(
                ArgumentMatchers.anyString()
            )
        ).thenReturn(Single.just(imagesResponse))
        setField(FIELD_NAME_IMAGES_LIST, MutableLiveData(expectedList), sut)
        setField(FIELD_NAME_RESULT_COUNT, MutableLiveData(expectedCount), sut)
        setField(FIELD_NAME_IMAGES_DETAIL, MutableLiveData(expectedDetail), sut)
        setField(FIELD_NAME_IMAGE_URI, MutableLiveData(EMPTY_URI), sut)
        // when
        sut.initView(ID, EMPTY_URI)

        // then
        assertEquals(EMPTY_URI, sut.imageUri.value)
    }

    @Test
    fun initView_loadInfoAndRelatedImagesInvoked_successResponse() {
        // given
        val imagesResponse = getDetailImageResponse()
        val expectedDetail = getDetailImages()
        val expectedList = getSampleImagesResponse().images.map { ImageUi.mapFromDomain(it) }
        val expectedCount = 1
        whenever(
            stashImageServiceMock.getComposeDetailImages(
                ArgumentMatchers.anyString()
            )
        ).thenReturn(Single.just(imagesResponse))
        setField(FIELD_NAME_IMAGES_LIST, MutableLiveData(expectedList), sut)
        setField(FIELD_NAME_RESULT_COUNT, MutableLiveData(expectedCount), sut)
        setField(FIELD_NAME_IMAGES_DETAIL, MutableLiveData(expectedDetail), sut)
        setField(FIELD_NAME_IMAGE_URI, MutableLiveData(EMPTY_URI), sut)
        // when
        sut.initView(ID, EMPTY_URI)

        // then
        assertEquals(GONE, sut.loaderVisibility.value)
        assertEquals(1, sut.resultCount.value)
        assertEquals(expectedList.first().id, sut.imagesList.value?.first()?.id)
        assertEquals(expectedList.size, sut.imagesList.value?.size)
    }

    @Test
    fun initView_loadInfoAndRelatedImagesInvoked_failure() {
        // given
        val error = Throwable("error fetching images")
        val errorInfo = DialogInfoUiModel(
            R.drawable.ic_error,
            R.string.error_title_generic,
            R.string.error_images_detail_search
        )
        whenever(
            stashImageServiceMock.getComposeDetailImages(
                ArgumentMatchers.anyString()
            )
        ).thenReturn(Single.error(error))
        setField(FIELD_NAME_IMAGE_URI, MutableLiveData(EMPTY_URI), sut)
        setField(FIELD_NAME_ERROR, MutableLiveData(error), sut)
        // when
        sut.initView(ID, EMPTY_URI)

        // then
        assertEquals(View.GONE, sut.loaderVisibility.value)
        assertEquals(errorInfo, sut.errorEvent.value)
    }
}