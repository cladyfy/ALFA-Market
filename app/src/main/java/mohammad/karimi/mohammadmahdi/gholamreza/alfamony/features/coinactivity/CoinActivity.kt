package mohammad.karimi.mohammadmahdi.gholamreza.alfamony.features.coinactivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import ir.dunijet.dunipool.apiManager.model.CoinAboutItem
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.*
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.chartdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.coinAboutData
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.coinsdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.R
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.databinding.ActivityCoinBinding
import java.time.Month

class coinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding
    lateinit var datacoin: coinsdata.Data
    lateinit var dataabout: CoinAboutItem
    val apimanager = Apimanager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent.getBundleExtra("bundle")!!
        datacoin = intent.getParcelable("bundle1")!!


        if( intent.getParcelable<CoinAboutItem>("bundle2") != null)
        {
            dataabout = intent.getParcelable("bundle2")!!

        }else{
            dataabout = CoinAboutItem()
        }



        binding.layoutToolbar.toolbar.title = "Loading ..."
        Handler(Looper.getMainLooper()).postDelayed(
            {
                initUi()


            }, 500

        )


    }

    private fun initUi() {
        initChartUi()
        initStatisticsUi()
        initAboutUi()

    }

    @SuppressLint("SetTextI18n")
    private fun initStatisticsUi() {


        binding.layoutToolbar.toolbar.title = datacoin.coinInfo.fullName
        binding.layoutStatistics.tvOpenAmount.text =
            datacoin.dISPLAY.uSD.oPEN24HOUR.toString()
        binding.layoutStatistics.tvTodaysHighAmount.text =
            datacoin.dISPLAY.uSD.hIGH24HOUR.toString()
        binding.layoutStatistics.tvTodayLowAmount.text =
            datacoin.dISPLAY.uSD.lOW24HOUR.toString()
        binding.layoutStatistics.tvChangeTodayAmount.text =
            datacoin.dISPLAY.uSD.cHANGE24HOUR.toString()
        binding.layoutStatistics.tvalgorithm.text = datacoin.coinInfo.algorithm
        binding.layoutStatistics.tvtotalvoloum.text = datacoin.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutStatistics.tvAvgMarketCapAmount.text = datacoin.dISPLAY.uSD.mKTCAP
        binding.layoutStatistics.tvSupplyNumber.text = datacoin.dISPLAY.uSD.sUPPLY


    }

    private fun initAboutUi() {

        binding.layoutAbout.txtWebsite.text = dataabout.coinWebsite
        binding.layoutAbout.txtGithub.text = dataabout.coinGithub
        binding.layoutAbout.txtTwitter.text = "@" + dataabout.coinTwitter
        binding.layoutAbout.txtReddit.text = dataabout.coinReddit
        binding.layoutAbout.txtAboutCoin.text = dataabout.coinDesc

        binding.layoutAbout.txtWebsite.setOnClickListener {

            openwebsite(dataabout.coinWebsite.toString())
        }
        binding.layoutAbout.txtGithub.setOnClickListener {

            openwebsite(dataabout.coinGithub.toString())
        }
        binding.layoutAbout.txtReddit.setOnClickListener {

            openwebsite(dataabout.coinReddit.toString())
        }

        binding.layoutAbout.txtTwitter.setOnClickListener {

            openwebsite("https://twitter.com/${dataabout.coinTwitter}")
        }

    }

    private fun openwebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)


    }


    private fun initChartUi() {
        var period = HOUR
        requestandshodata(period)
        binding.layoutChart.radiogroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.radio_12h -> period = HOUR
                R.id.radio_1d -> period = HOURS24
                R.id.radio_1w -> period = WEEK
                R.id.radio_1m -> period = MONTH
                R.id.radio_3m -> period = MONTH3
                R.id.radio_1y -> period = YEAR
                R.id.radio_all -> period = ALL


            }
            requestandshodata(period)

        }
        binding.layoutChart.txtChartPrice.text = datacoin.dISPLAY.uSD.pRICE
        binding.layoutChart.txtChartChange1.text = datacoin.dISPLAY.uSD.cHANGE24HOUR
          if (datacoin.coinInfo.fullName == "BUSD"){



          }

          val taghir =  datacoin.rAW.uSD.cHANGE24HOUR
        if (taghir > 0) {
            binding.layoutChart.txtChartChange2.text = datacoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 4) + "%"
            binding.layoutChart.txtChartChange2.setTextColor(ContextCompat.getColor(this, R.color.colorGain))
            binding.layoutChart.txtChartUpDown.setTextColor(ContextCompat.getColor(this, R.color.colorGain))
            binding.layoutChart.txtChartUpDown.text = "▲"
            binding.layoutChart.sparkView.lineColor = ContextCompat.getColor(this, R.color.colorGain)


        } else if (taghir < 0) {
            binding.layoutChart.txtChartChange2.text = datacoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
            binding.layoutChart.txtChartChange2.setTextColor(ContextCompat.getColor(this, R.color.colorLoss))
            binding.layoutChart.txtChartUpDown.setTextColor(ContextCompat.getColor(this, R.color.colorLoss))
            binding.layoutChart.txtChartUpDown.text = "▼"
            binding.layoutChart.sparkView.lineColor = ContextCompat.getColor(this, R.color.colorLoss)

 } else {

          binding.layoutChart.txtChartUpDown.setTextColor(ContextCompat.getColor(this, R.color.tertiaryTextColor))
            binding.layoutChart.txtChartChange2.setTextColor(ContextCompat.getColor(this, R.color.tertiaryTextColor))
             binding.layoutChart.txtChartChange2.text = "0%"

        }


        binding.layoutChart.sparkView.setScrubListener {
          if (it == null)
          {

         binding.layoutChart.txtChartPrice.text = datacoin.dISPLAY.uSD.pRICE


          }else {

              binding.layoutChart.txtChartPrice.text = "$" + (it as chartdata.Data ).close.toString()


          }


        }



    }

    private fun requestandshodata(period: String) {
        apimanager.getChartdata(datacoin.coinInfo.name,
            period,
            object : Apimanager.apicallback<Pair<List<chartdata.Data>, chartdata.Data?>> {
                override fun onSuccess(data: Pair<List<chartdata.Data>, chartdata.Data?>) {
                    val chartAdapter = chartAdapter(data.first, data.second?.open.toString())
                    binding.layoutChart.sparkView.adapter = chartAdapter


                }

                override fun onEror(erorMassage: String) {
                    Toast.makeText(this@coinActivity, "Eror => {$erorMassage}", Toast.LENGTH_SHORT)
                        .show()


                }


            })

    }
}

