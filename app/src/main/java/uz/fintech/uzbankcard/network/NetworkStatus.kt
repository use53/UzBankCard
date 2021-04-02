package uz.fintech.uzbankcard.network

import androidx.annotation.StringRes
import uz.fintech.uzbankcard.R

sealed class NetworkStatus{
    object Loading : NetworkStatus()
    object Success : NetworkStatus()
    object NoCardError : NetworkStatus()
    class Error(@StringRes val errorMsg: Int) : NetworkStatus()
    class Offline(@StringRes val errorMsg: Int = R.string.error_offline) : NetworkStatus()

}