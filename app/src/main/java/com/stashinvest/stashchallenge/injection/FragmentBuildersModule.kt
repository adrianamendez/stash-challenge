package com.stashinvest.stashchallenge.injection

import com.stashinvest.stashchallenge.ui.fragment.MainFragment
import com.stashinvest.stashchallenge.ui.fragment.PopUpDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
internal abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainFragment(): MainFragment

    @ContributesAndroidInjector
    internal abstract fun bindPopUpDialogFragment(): PopUpDialogFragment
}