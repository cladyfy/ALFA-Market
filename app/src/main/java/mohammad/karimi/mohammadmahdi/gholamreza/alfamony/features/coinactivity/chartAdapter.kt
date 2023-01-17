package mohammad.karimi.mohammadmahdi.gholamreza.alfamony.features.coinactivity

import com.robinhood.spark.SparkAdapter
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.chartdata

class chartAdapter(private val historycal : List<chartdata.Data> , private val baseline : String?) : SparkAdapter() {
    override fun getCount(): Int {
      return historycal.size
    }

    override fun getItem(index: Int): chartdata.Data{
        return historycal[index]
    }

    override fun getY(index: Int): Float {

        return historycal[index].close.toFloat()
    }
    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return  baseline?.toFloat() ?: super.getBaseLine()


    }


}