@file:Suppress("NOTHING_TO_INLINE")

package com.lukianbat.coreui.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

fun <T : View> RecyclerView.ViewHolder.bindView(@IdRes res: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return lazy(LazyThreadSafetyMode.NONE) { itemView.findViewById<T>(res) }
}

fun RecyclerView.ViewHolder.getString(@StringRes res: Int): String {
    return itemView.context.getString(res)
}

inline fun ViewGroup.inflate(@LayoutRes resId: Int): View =
    LayoutInflater.from(context).inflate(resId, this, false)

inline fun ViewGroup.inflate(
    @LayoutRes resId: Int,
    attach: Boolean = false,
    setup: View.() -> Unit
): View {
    val view = LayoutInflater.from(context).inflate(resId, this, false)
    view.apply(setup)
    if (attach) {
        addView(view)
    }
    return view
}

fun Boolean.toVisibility() = if (this) {
    View.VISIBLE
} else {
    View.INVISIBLE
}
