package uz.fintech.uzbankcard.network

import androidx.annotation.StringRes
import uz.fintech.uzbankcard.R

sealed class StatusLoading {
    object DeleteCard:StatusLoading()
    object Loading : StatusLoading()
    object LoadingBigin : StatusLoading()
    object Success : StatusLoading()
    object SuccessUpdate : StatusLoading()
    class Error(@StringRes val errorMsg: Int) : StatusLoading()
    class Offline(@StringRes val errorMsg: Int = R.string.error_offline) : StatusLoading()

}