package com.stashinvest.stashchallenge.util

import androidx.databinding.ViewDataBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private abstract class BindingProperty<T, B : ViewDataBinding> : ReadOnlyProperty<T, B> {

    private var binding: B? = null

    override fun getValue(thisRef: T, property: KProperty<*>): B =
        binding ?: createBinding(thisRef).also { binding = it }

    abstract fun createBinding(thisRef: T): B
}