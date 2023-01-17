package mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager

import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.chartdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.coinsdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.newsdata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Apimanager {
    private val apiService: ApiService

    init {


        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    fun getNews(apicalback: apicallback<ArrayList<Pair<String, String>>>) {


        apiService.getTopNews().enqueue(object : Callback<newsdata> {


            override fun onResponse(call: Call<newsdata>, response: Response<newsdata>) {
                val data = response.body()!!
                val datatosend: ArrayList<Pair<String, String>> = arrayListOf()
                data.data.forEach {
                    datatosend.add(Pair(it.title, it.url))
                }

                apicalback.onSuccess(datatosend)


            }

            override fun onFailure(call: Call<newsdata>, t: Throwable) {
                apicalback.onEror(t.message.toString())
            }
        })
    }

    fun getCoinsList(apicalback: apicallback<List<coinsdata.Data>>) {

        apiService.getTopCoins().enqueue(object : Callback<coinsdata> {
            override fun onResponse(call: Call<coinsdata>, response: Response<coinsdata>) {
                val data = response.body()

                apicalback.onSuccess(data!!.data)

            }

            override fun onFailure(call: Call<coinsdata>, t: Throwable) {

                apicalback.onEror(t.message.toString())


            }

        })


    }

    fun getChartdata(
        symbol: String,
        period: String,
        apicalback: apicallback<Pair<List<chartdata.Data>, chartdata.Data?>>,
    ) {

        var histoperiod = ""
        var limit: Int = 30
        var aggregate = 1
        when (period) {

            HOUR -> {
                histoperiod = HISTO_MINUTE
                limit = 60
                aggregate = 12

            }
            HOURS24 -> {

                histoperiod = HISTO_HOUR
                limit = 24


            }
            MONTH -> {

                histoperiod = HISTO_DAY
                limit = 30

            }
            MONTH3 -> {

                histoperiod = HISTO_DAY
                limit = 90


            }
            WEEK -> {
                histoperiod = HISTO_HOUR
                aggregate = 6

            }
            YEAR -> {

                histoperiod = HISTO_DAY
                aggregate = 13

            }
            ALL -> {
                histoperiod = HISTO_DAY
                aggregate = 30
                limit = 2000


            }


        }


        apiService.getChartData(histoperiod, symbol, limit, aggregate)
            .enqueue(object : Callback<chartdata> {


                override fun onResponse(call: Call<chartdata>, response: Response<chartdata>) {
                    val datafull = response.body()!!
                    val data1 = datafull.data
                    val data2 = datafull.data.maxByOrNull { it.close.toFloat() }
                    val returendata = Pair(data1, data2)


                    apicalback.onSuccess( returendata )
                }

                override fun onFailure(call: Call<chartdata>, t: Throwable) {
                    apicalback.onEror(t.message.toString())
                }


            })


    }


    interface apicallback<T> {

        fun onSuccess(data: T)


        fun onEror(erorMassage: String)


    }


}