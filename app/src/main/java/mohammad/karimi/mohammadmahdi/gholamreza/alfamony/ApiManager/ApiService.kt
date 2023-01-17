package mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager



import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.chartdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.coinsdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.newsdata
import retrofit2.Call

import retrofit2.http.GET

import retrofit2.http.Headers
import retrofit2.http.Path

import retrofit2.http.Query

interface ApiService {


    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(

      @Query("sortOrder") sortOrder : String = "popular"
    ) : Call<newsdata>


  @Headers(API_KEY)
  @GET("top/totalvolfull")
    fun getTopCoins(

    @Query("tsym" ) tosimbol : String = "USD",
    @Query("limit" ) limitdata : Int = 10
    ) : Call<coinsdata>

    @Headers(API_KEY)
    @GET("{period}")
    fun getChartData(

        @Path("period") period : String,
        @Query("fsym") fromSymbol : String ,
        @Query("limit" ) limit : Int,
       @Query("aggregate") aggregate  : Int ,
        @Query("tsym")  toSymbol : String = "USD"

    ) :  Call<chartdata>


}
