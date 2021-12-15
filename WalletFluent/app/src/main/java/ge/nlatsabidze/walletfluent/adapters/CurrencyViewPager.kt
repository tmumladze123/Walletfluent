package ge.nlatsabidze.walletfluent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nlatsabidze.walletfluent.databinding.CurrencyPageBinding


class CurrencyViewPager (): RecyclerView.Adapter<CurrencyViewPager.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewPager.ViewHolder =
        ViewHolder(CurrencyPageBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: CurrencyViewPager.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int =3

    inner class ViewHolder(val binding: CurrencyPageBinding): RecyclerView.ViewHolder(binding.root){

    }
}