package com.stashinvest.stashchallenge.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.recyclerview.widget.GridLayoutManager
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.api.StashImageService
import com.stashinvest.stashchallenge.common.BaseDaggerFragment
import com.stashinvest.stashchallenge.common.DialogInfoUiModel
import com.stashinvest.stashchallenge.databinding.FragmentMainBinding
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter
import com.stashinvest.stashchallenge.ui.factory.ImageFactory
import com.stashinvest.stashchallenge.ui.viewmodel.MainViewModel
import com.stashinvest.stashchallenge.util.SpaceItemDecoration
import com.stashinvest.stashchallenge.util.hideKeyboard
import javax.inject.Inject

private const val TAG = "MainFragment"

class MainFragment : BaseDaggerFragment() {
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
        addListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeSearch()
        initializeRecyclerView()
    }

    private fun updateImages() {
        val viewModels = mainViewModel.imagesList.value?.map {
            imageFactory.createImageViewModel(
                it,
                ::onImageLongPress
            )
        }
        viewModels?.let {
            adapter.setViewModels(it)
        }
    }

    private fun onImageLongPress(id: String, uri: String?) {
        val dialog = PopUpDialogFragment.newInstance(
            id,
            uri
        )
        activity?.let { dialog.show(it.supportFragmentManager, TAG) }
    }

    private fun addListeners() {
        mainViewModel.imagesList.observe(viewLifecycleOwner, Observer { updateImages() })
        mainViewModel.errorEvent.observe(viewLifecycleOwner, Observer { dialogInfo ->
            dialogInfo?.let { showInfoDialog(it) }
        })
        mainViewModel.hideKeyboardEvent.observe(viewLifecycleOwner, Observer {
            hideAndClearKeyboard()
        })
    }

    private fun showInfoDialog(dialogInfoUiModel: DialogInfoUiModel) {
        showDialog(dialogInfoUiModel)
    }

    private fun hideAndClearKeyboard() {
        binding.searchPhrase.hideKeyboard()
        binding.searchPhrase.clearFocus()
    }

    private fun initializeSearch() {

        binding.searchPhrase.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mainViewModel.searchImages(binding.searchPhrase.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun initializeRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(SpaceItemDecoration(space, space, space, space))
    }
}
