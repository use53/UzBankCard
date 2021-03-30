package uz.fintech.uzbankcard.api

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uz.fintech.uzbankcard.model.currencie.CurrencieModel
const val newapidb="512399061ce8ad130a01c5e18c495c22"
const val apidb="f719807af05383c0de5573a6de6ee4da"
 const val API_KEY="http://api.currencylayer.com/"

interface ICurrencieApi{


    @GET("live?")
    fun loadCours(
        @Query("access_key") key:String="512399061ce8ad130a01c5e18c495c22",
        @Query("currencies") currencies:String="EUR,RUB,UZS,TJS,TMT,KZT",
        @Query("format") format:String="1"
    ):Single<CurrencieModel>


    companion object{
        private var instanse:ICurrencieApi?=null
        fun instanse(): ICurrencieApi? {
            if (instanse==null){
                val retrofit=Retrofit.Builder()
                    .baseUrl(API_KEY)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                instanse=retrofit.create(ICurrencieApi::class.java)
            }else{
                instanse
            }
            return instanse
        }
    }
}