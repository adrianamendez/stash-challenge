package com.stashinvest.stashchallenge.common

import com.stashinvest.stashchallenge.common.CustomDialogFragment.Companion.TAG
import dagger.android.support.DaggerFragment

abstract class BaseDaggerFragment : DaggerFragment() {

    protected fun showDialog(dialogModel: DialogInfoUiModel) {
        CustomDialogFragment.Builder()
            .setIcon(dialogModel.icon)
            .setTitle(dialogModel.title)
            .setMessage(dialogModel.message)
            .setPositiveButton(dialogModel.positiveButtonText, dialogModel.onPositiveClickListener)
            .setNegativeButton(dialogModel.negativeButtonText)
            .create()
            .show(childFragmentManager, TAG)
    }
}