package com.stashinvest.stashchallenge.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stashinvest.stashchallenge.ui.viewmodel.MainViewModel
import com.stashinvest.stashchallenge.ui.viewmodel.PopUpDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PopUpDialogViewModel::class)
    internal abstract fun popUpDialogViewModel(viewModel: PopUpDialogViewModel): ViewModel
}