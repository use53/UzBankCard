package uz.fintech.uzbankcard.model.currencie


import com.google.gson.annotations.SerializedName

data class Quotes(
    @SerializedName("USDEUR")
    val uSDEUR: Double,
    @SerializedName("USDKZT")
    val uSDKZT: Double,
    @SerializedName("USDRUB")
    val uSDRUB: Double,
    @SerializedName("USDTJS")
    val uSDTJS: Double,
    @SerializedName("USDTMT")
    val uSDTMT: Double,
    @SerializedName("USDUZS")
    val uSDUZS: Double
)