package ge.nlatsabidze.walletfluent.ui.currency.currencyPages.countryCurrencies.currencyAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nlatsabidze.walletfluent.databinding.CurrencyItemBinding
import ge.nlatsabidze.walletfluent.model.CommercialRates


class CurrencyAdapter : RecyclerView.Adapter<CurrencyAdapter.CurrencyItemViewHolder>() {
    var currencies : List<CommercialRates> = listOf()

    inner class CurrencyItemViewHolder(private val binding: CurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var item: CommercialRates

        fun onBind() {
            item = currencies[bindingAdapterPosition]
            binding.tvName.text = item.currency.toString()
            binding.tvBuyAmount.text = item.buy.toString()
            binding.tvSellAmount.text = item.sell.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemViewHolder =
        CurrencyItemViewHolder(
            CurrencyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CurrencyItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = currencies.size


}