package com.stashinvest.stashchallenge.common

import dagger.android.support.DaggerDialogFragment

abstract class BaseDaggerDialogFragment : DaggerDialogFragment() {

    protected fun showDialog(dialogModel: DialogInfoUiModel) {
        CustomDialogFragment.Builder()
            .setIcon(dialogModel.icon)
            .setTitle(dialogModel.title)
            .setMessage(dialogModel.message)
            .setPositiveButton(dialogModel.positiveButtonText, dialogModel.onPositiveClickListener)
            .setNegativeButton(dialogModel.negativeButtonText)
            .create()
            .show(childFragmentManager, CustomDialogFragment.TAG)
    }
}