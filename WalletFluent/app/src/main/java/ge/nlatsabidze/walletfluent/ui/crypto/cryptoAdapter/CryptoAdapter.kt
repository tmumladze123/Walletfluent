package ge.nlatsabidze.walletfluent.ui.crypto.cryptoAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nlatsabidze.walletfluent.databinding.ExchangeItemBinding
import ge.nlatsabidze.walletfluent.extensions.setImage
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem

class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CryptoItemViewHolder>() {

    var cryptoExchanges: List<MarketsItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class CryptoItemViewHolder(private val binding: ExchangeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentItem: MarketsItem

        fun onBind() {
            currentItem = cryptoExchanges[bindingAdapterPosition]
            with(binding) {
                with(currentItem) {
                    imCrypto.setImage(image)
                    tvCountry.text = currentItem.name.toString()
                    tvDesc.text = currentItem.current_price.toString()
                    tvName.text = currentItem.atl_date.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoItemViewHolder =
        CryptoItemViewHolder(ExchangeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CryptoItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = cryptoExchanges.size


}