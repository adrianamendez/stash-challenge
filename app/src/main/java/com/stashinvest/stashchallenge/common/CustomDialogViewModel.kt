package com.stashinvest.stashchallenge.common

import androidx.lifecycle.ViewModel

class CustomDialogViewModel : ViewModel() {

    var onPositiveClickListener: (() -> Unit)? = null
    var onNegativeClickListener: (() -> Unit)? = null
}