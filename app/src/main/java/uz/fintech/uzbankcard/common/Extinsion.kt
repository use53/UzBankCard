package uz.fintech.uzbankcard.common

import android.content.Context
import android.widget.EditText

fun <T> lazyFast(mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE, initializer: () -> T): Lazy<T> = lazy(mode, initializer)


