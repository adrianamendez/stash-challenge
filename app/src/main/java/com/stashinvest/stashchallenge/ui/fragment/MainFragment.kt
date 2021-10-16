package com.stashinvest.stashchallenge.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.recyclerview.widget.GridLayoutManager
import com.stashinvest.stashchallenge.App
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.api.StashImageService
import com.stashinvest.stashchallenge.api.model.ImageResponse
import com.stashinvest.stashchallenge.api.model.ImageResult
import com.stashinvest.stashchallenge.databinding.FragmentMainBinding
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter
import com.stashinvest.stashchallenge.ui.factory.ImageFactory
import com.stashinvest.stashchallenge.ui.viewmodel.MainViewModel
import com.stashinvest.stashchallenge.util.SpaceItemDecoration
import com.stashinvest.stashchallenge.util.fragmentBinding
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainFragment : DaggerFragment(), Callback<ImageResponse> {
    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    @Inject
    lateinit var adapter: ViewModelAdapter

    @Inject
    lateinit var stashImageService: StashImageService

    @Inject
    lateinit var imageFactory: ImageFactory

    @Inject
    lateinit var viewModelFactory: Factory

    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentMainBinding

    private val space: Int by lazy { requireContext().resources.getDimensionPixelSize(R.dimen.image_space) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchPhrase.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search()
                return@setOnEditorActionListener true
            }
            false
        }

        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(SpaceItemDecoration(space, space, space, space))
    }

    override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
        progressBar.visibility = GONE

        if (response.isSuccessful) {
            val images = response.body()?.images ?: listOf()
            updateImages(images)
        } else {
            //todo - show error
        }
    }

    override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
        progressBar.visibility = GONE
        //todo - show error
    }

    private fun search() {
        progressBar.visibility = View.VISIBLE
        val call = stashImageService.searchImages(searchPhrase.text.toString())
        call.enqueue(this)
    }

    private fun updateImages(images: List<ImageResult>) {
        val viewModels = images.map { imageFactory.createImageViewModel(it, ::onImageLongPress) }
        adapter.setViewModels(viewModels)
    }

    fun onImageLongPress(id: String, uri: String?) {
        //todo - implement new feature
    }
}
