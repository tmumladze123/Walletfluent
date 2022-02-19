package ge.nlatsabidze.walletfluent.ui.crypto.cryptoAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nlatsabidze.walletfluent.databinding.CryptoItemBinding
import ge.nlatsabidze.walletfluent.extensions.setImage
import ge.nlatsabidze.walletfluent.model.cryptoModel.MarketsItem
import java.util.*

class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CryptoItemViewHolder>() {

    var cryptoExchanges: List<MarketsItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClick: ((MarketsItem) -> Unit)? = null

    inner class CryptoItemViewHolder(private val binding: CryptoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentItem: MarketsItem

        fun onBind() {
            currentItem = cryptoExchanges[bindingAdapterPosition]
            with(binding) {
                with(currentItem) {
                    imCryptoWrapper.setImage(image)
                    tvName.text = currentItem.name.toString()
                    tvSymbol.text = currentItem.symbol.toString().uppercase(Locale.getDefault())
                    tvCurrentPrice.text = "$" + currentItem.current_price.toString()

                    var currentPrice = currentItem.price_change_24h.toString()
                    var indexOfDot = findDotOccurence(currentPrice)

                    if (currentItem.price_change_24h.toString()[0] == '-') {
                        tvChange.setTextColor(Color.RED)
                        currentPrice = currentPrice.drop(1)
                        currentPrice = currentPrice.substring(0, indexOfDot + 2)
                        tvChange.text = "▼$currentPrice"
                    } else {
                        tvChange.setTextColor(Color.GREEN)
                        currentPrice = currentPrice.substring(0, indexOfDot + 2)
                        tvChange.text = "▲$currentPrice"
                    }
                    root.setOnClickListener {
                        onItemClick?.invoke(currentItem)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoItemViewHolder =
        CryptoItemViewHolder(CryptoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CryptoItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = cryptoExchanges.size

    fun findDotOccurence(number: String): Int {
        var index = 0
        for (i in number.indices) {
            if (number[i] == '.') {
                index = i
            }
        }
        return index
    }


}