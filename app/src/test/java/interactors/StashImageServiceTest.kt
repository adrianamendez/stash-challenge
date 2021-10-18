package interactors

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.stashinvest.stashchallenge.api.StashImageService
import com.stashinvest.stashchallenge.api.StashImagesApi
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import utils.getMetadataResponse
import utils.getSampleImagesResponse

@RunWith(MockitoJUnitRunner::class)
class StashImageServiceTest {

    companion object {
        const val PHRASE = "phrase"
        const val ID = "id"
    }

    private lateinit var sut: StashImageService

    @Mock
    lateinit var apiMock: StashImagesApi

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        sut = StashImageService(apiMock)
    }

    @Test
    fun searchImages_onSuccess_returnsImagesSearch() {
        // given
        val imagesResponse = getSampleImagesResponse()
        whenever(
            apiMock.searchImages(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
            )
        )
            .thenReturn(Single.just(imagesResponse))
        //when
        val response = sut.searchImages(PHRASE)
        //then
        assertEquals(imagesResponse.images, response.blockingGet().images)
    }

    @Test
    fun searchImages_failure_returnsError() {
        // given
        val error = Throwable("error searching products")
        whenever(
            apiMock.searchImages(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
            )
        )
            .thenReturn(Single.error(error))
        //when
        val response = sut.searchImages(PHRASE)
        //then
        response.test().assertError(error)
    }

    @Test
    fun getComposeDetailImages_onSuccess_returnsImagesSearch() {
        // given
        val imagesResponse = getSampleImagesResponse()
        val metadataResponse = getMetadataResponse()
        whenever(apiMock.getImageMetadata(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(metadataResponse))
        whenever(apiMock.getSimilarImages(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(imagesResponse))
        //when
        val response = sut.getComposeDetailImages(ID)
        //then
        assertEquals(imagesResponse, response.blockingGet().imageResponse)
        assertEquals(metadataResponse, response.blockingGet().metadataResponse)
    }

    @Test
    fun getComposeDetailImages_failureSimilarImages_returnsError() {
        // given
        val error = Throwable("error searching products")
        val metadataResponse = getMetadataResponse()
        whenever(apiMock.getImageMetadata(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(metadataResponse))
        whenever(apiMock.getSimilarImages(ArgumentMatchers.anyString()))
            .thenReturn(Single.error(error))
        //when
        val response = sut.getComposeDetailImages(ID)
        //then
        response.test().assertError(error)
    }

    @Test
    fun getComposeDetailImages_failureMetadataImages_returnsError() {
        // given
        val imagesResponse = getSampleImagesResponse()
        val error = Throwable("error searching products")
        whenever(apiMock.getImageMetadata(ArgumentMatchers.anyString()))
            .thenReturn(Single.error(error))
        whenever(apiMock.getSimilarImages(ArgumentMatchers.anyString()))
            .thenReturn(Single.just(imagesResponse))
        //when
        val response = sut.getComposeDetailImages(ID)
        //then
        response.test().assertError(error)
    }
}