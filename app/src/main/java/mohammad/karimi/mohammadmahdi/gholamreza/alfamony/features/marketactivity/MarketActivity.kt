package mohammad.karimi.mohammadmahdi.gholamreza.alfamony.features.marketactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import ir.dunijet.dunipool.apiManager.model.CoinAboutItem
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.Apimanager
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.coinAboutData
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.coinsdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.databinding.ActivityMarketBinding
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.features.coinactivity.coinActivity


class marketActivity : AppCompatActivity()  , marketAdapter.recyclercallback{
    lateinit var binding: ActivityMarketBinding
    lateinit var datanews: ArrayList<Pair<String, String>>
    lateinit var aboutmap : MutableMap<String ,  CoinAboutItem>
    val apimanager = Apimanager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAboutDataFromassets()
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.layoutToolbar.toolbar.title = "Market"

        binding.siperefreshmain.setOnRefreshListener {
            initUi()

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding.siperefreshmain.isRefreshing = false

                } , 1000

            )



        }
        binding.layoutWatchlist.btnShowmore.setOnClickListener {
         val intent = Intent(Intent.ACTION_VIEW , Uri.parse("https://www.coingecko.com/en/watchlists"))
         startActivity(intent)

        }

    }




    override fun onResume() {
        super.onResume()
        initUi()
    }




    private fun initUi() {

        getTopCoinsFromApi()
        getNewsfromApi()
    }


    private fun getAboutDataFromassets() {
        val fileinString = applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use {
                it.readText()

            }
        aboutmap = mutableMapOf<String ,CoinAboutItem>()
        val gson = Gson()
        val dataaboutall = gson.fromJson(fileinString , coinAboutData :: class.java)
        dataaboutall.forEach{
            aboutmap[it.currencyName] = CoinAboutItem(it.info.web , it.info.github , it.info.twt , it.info.desc , it.info.reddit)

        }



    }




    private fun getTopCoinsFromApi() {


        apimanager.getCoinsList(object : Apimanager.apicallback<List<coinsdata.Data>>{
            override fun onSuccess(data: List<coinsdata.Data>) {
            showDataInRecycler(data)

            }

            override fun onEror(erorMassage: String) {
               Log.v("textapi" , erorMassage)
                Toast.makeText(this@marketActivity, "Eror => ($erorMassage)", Toast.LENGTH_SHORT).show()
            }

        })

    }



    private fun getNewsfromApi() {
        apimanager.getNews(object : Apimanager.apicallback<ArrayList<Pair<String, String>>> {
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {

           datanews = data
           refreshnews()


            }

            override fun onEror(erorMassage: String) {
                Log.v("testapi" , erorMassage)
                Toast.makeText(this@marketActivity, "eror => " + erorMassage, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }



    private fun refreshnews() {
        val random = (1..49).random()
       binding.layoutNews.txtNews.text = datanews[random].first
        binding.layoutNews.imgNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW , Uri.parse(datanews[random].second))
            startActivity(intent)
        }

        binding.layoutNews.txtNews.setOnClickListener {
           refreshnews()

        }


    }



    private fun showDataInRecycler(data: List<coinsdata.Data>){
         val marketAdapter = marketAdapter(ArrayList(data) , this)
         binding.layoutWatchlist.recyclerView.adapter = marketAdapter
         binding.layoutWatchlist.recyclerView.layoutManager = LinearLayoutManager(this )



    }



    override fun oncoinitemcliced(data: coinsdata.Data) {
        val Intent = Intent(this , coinActivity :: class.java)

        val bundle = Bundle()
        bundle.putParcelable("bundle1" , data)
        bundle.putParcelable("bundle2" , aboutmap[data.coinInfo.name])


        Intent.putExtra("bundle" , bundle)
        startActivity(Intent)


    }


}
