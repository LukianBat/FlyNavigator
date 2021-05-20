@file:Suppress("NOTHING_TO_INLINE")

package com.lukianbat.coreui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

fun EditText.addAfterTextChangedListener(listener: (String) -> Unit) =
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) = listener(s.toString())
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })

inline val TextInputLayout.theEditText get() = editText!!
inline fun TextInputLayout.editText(withEditText: EditText.() -> Unit) =
    this.editText?.apply(withEditText)

fun Context.color(@ColorRes color: Int): Int = ContextCompat.getColor(this, color)
fun View.color(@ColorRes color: Int): Int = ContextCompat.getColor(this.context, color)
fun Fragment.color(@ColorRes color: Int): Int = ContextCompat.getColor(requireContext(), color)

fun Context.drawable(@DrawableRes drawable: Int): Drawable? =
    ContextCompat.getDrawable(this, drawable)

fun View.drawable(@DrawableRes drawable: Int): Drawable? =
    ContextCompat.getDrawable(this.context, drawable)

fun Fragment.drawable(@DrawableRes drawable: Int): Drawable? =
    ContextCompat.getDrawable(requireContext(), drawable)

inline fun <T> Fragment.argument(crossinline getArgument: Bundle.() -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        val arguments = arguments ?: throw IllegalArgumentException("Fragment has no arguments")
        arguments.getArgument()
    }
}

inline fun <T> Activity.argument(crossinline getArgument: Intent.() -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        intent.getArgument()
    }
}
