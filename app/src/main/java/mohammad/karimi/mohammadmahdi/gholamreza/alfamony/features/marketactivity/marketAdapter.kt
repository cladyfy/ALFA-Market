package mohammad.karimi.mohammadmahdi.gholamreza.alfamony.features.marketactivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.BASE_URL_IMAGE
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.ApiManager.model.coinsdata
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.R
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.databinding.ActivityCoinBinding
import mohammad.karimi.mohammadmahdi.gholamreza.alfamony.databinding.ItemRecycelerMarketBinding

class marketAdapter(
    private val data: ArrayList<coinsdata.Data>,
    private val recyclerCallback: recyclercallback,
) :
    RecyclerView.Adapter<marketAdapter.marketviewholder>() {

    lateinit var binding: ItemRecycelerMarketBinding

    inner class marketviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        @SuppressLint("SetTextI18n")
        fun bindViews(datacoin: coinsdata.Data) {
            binding.txtCoinName.text = datacoin.coinInfo.fullName
            binding.txtPrice.text = datacoin.dISPLAY.uSD.pRICE
            val marketkapt = datacoin.rAW.uSD.mKTCAP / 1000000000
            val indexDot = marketkapt.toString().indexOf(".")
            binding.txtMarketcap.text = "$ " + marketkapt.toString().substring(0 , indexDot + 3) + "B"
            val taghir = datacoin.rAW.uSD.cHANGE24HOUR
            if (taghir > 0) {

                binding.txtTaghir.setTextColor(ContextCompat.getColor(itemView.context,
                    R.color.colorGain))

                binding.txtTaghir.text =   datacoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 4) + "%"


            } else if (taghir < 0) {
                binding.txtTaghir.setTextColor(ContextCompat.getColor(itemView.context,
                    R.color.colorLoss))


                binding.txtTaghir.text = datacoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"

            } else {
                binding.txtTaghir.text = "0%"

            }


            Glide
                .with(itemView.context)
                .load(BASE_URL_IMAGE + datacoin.coinInfo.imageUrl)
                .into(binding.imgItem)
            itemView.setOnClickListener {
                recyclerCallback.oncoinitemcliced(datacoin)

            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): marketviewholder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRecycelerMarketBinding.inflate(inflater, parent, false)

        return marketviewholder(binding.root)
    }

    override fun onBindViewHolder(holder: marketviewholder, position: Int) {

        holder.bindViews(data[position])
    }

    override fun getItemCount(): Int {

        return data.size


    }

    interface recyclercallback {

        fun oncoinitemcliced(data: coinsdata.Data)


    }


}