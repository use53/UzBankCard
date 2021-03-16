package uz.fintech.uzbankcard.common

import android.text.TextWatcher
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun <T> lazyFast(mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE, initializer: () -> T): Lazy<T> = lazy(mode, initializer)


fun Double.formatDecimals(): String {
    val decimalFormat = DecimalFormat()
    decimalFormat.groupingSize = 3
    decimalFormat.maximumFractionDigits = 2
    val symbol = DecimalFormatSymbols()
    symbol.groupingSeparator = ' '
    decimalFormat.decimalFormatSymbols = symbol
    return decimalFormat.format(this)
}
fun cardNumber(text:String):String{
    val textParsed = when {
        text.length >= 12 -> {
            text.substring(0, 4) + "  " + text.substring(4, 8) +
                    "  " + text.subSequence(8, 12) + "  " + text.substring(12)
        }
        text.length >= 8 -> {
            text.substring(0, 4) + "  " + text.substring(4, 8) + "  " + text.substring(8)
        }
        text.length >= 4 -> {
            text.substring(0, 4) + "  " + text.substring(4)
        }
        else -> text

    }
    return textParsed
}

fun cardDateUtils(text: String):String{
    val textParsed = when {
        text.length >= 3 -> {
            text.substring(0, 2) + " / " + text.substring(2)
        }
        else -> text
    }
    return textParsed
}