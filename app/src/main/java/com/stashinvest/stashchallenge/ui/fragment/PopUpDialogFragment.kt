package com.stashinvest.stashchallenge.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.stashinvest.stashchallenge.R
import com.stashinvest.stashchallenge.common.BaseDaggerDialogFragment
import com.stashinvest.stashchallenge.databinding.FragmentDialogPopupBinding
import com.stashinvest.stashchallenge.ui.adapter.ViewModelAdapter
import com.stashinvest.stashchallenge.ui.factory.ImageFactory
import com.stashinvest.stashchallenge.ui.viewmodel.PopUpDialogViewModel
import com.stashinvest.stashchallenge.util.SpaceItemDecoration
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class PopUpDialogFragment : BaseDaggerDialogFragment() {

    companion object {
        private const val ID_ARGUMENT = "ID_ARGUMENT"
        private const val URI_ARGUMENT = "URI_ARGUMENT"

        fun newInstance(id: String, uri: String?): PopUpDialogFragment {
            val fragment = PopUpDialogFragment()
            val arguments = Bundle().apply {
                putString(ID_ARGUMENT, id)
                putString(URI_ARGUMENT, uri)
            }
            fragment.arguments = arguments
            return fragment
        }
    }

    @Inject
    lateinit var adapter: ViewModelAdapter

    @Inject
    lateinit var imageFactory: ImageFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: PopUpDialogViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentDialogPopupBinding
    private val space: Int by lazy { requireContext().resources.getDimensionPixelSize(R.dimen.image_space) }
    private var viewInitialization = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogPopupBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        loadArguments()
        addListeners()
        return binding.root
    }

    private fun loadArguments() {
        val id = arguments?.getString(ID_ARGUMENT).orEmpty()
        val uri = arguments?.getString(URI_ARGUMENT).orEmpty()
        viewModel.initView(id, uri)
    }

    private fun addListeners() {
        viewModel.resultCount.observe(viewLifecycleOwner, Observer {
            if (!viewInitialization) initializeRecyclerView() else viewInitialization = false
        })
        viewModel.errorEvent.observe(viewLifecycleOwner, Observer { dialogInfo ->
            dialogInfo?.let {
                showDialog(it)
            }
        })
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewSimilarImages.visibility = VISIBLE
        binding.recyclerViewSimilarImages.layoutManager =
            GridLayoutManager(context, viewModel.resultCount.value ?: 1)
        binding.recyclerViewSimilarImages.adapter = adapter
        binding.recyclerViewSimilarImages.addItemDecoration(
            SpaceItemDecoration(
                space,
                space,
                space,
                space
            )
        )
        val viewModels = viewModel.imagesList.value?.map {
            imageFactory.createImageViewModel(
                it,
                null
            )
        }
        viewModels?.let {
            adapter.setViewModels(it)
        }
    }
}
