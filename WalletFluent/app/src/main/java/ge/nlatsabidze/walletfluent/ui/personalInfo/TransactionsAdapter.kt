package ge.nlatsabidze.walletfluent.ui.personalInfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ge.nlatsabidze.walletfluent.databinding.TransactioItemBinding
import ge.nlatsabidze.walletfluent.ui.entry.userData.UserTransaction

class TransactionsAdapter : RecyclerView.Adapter<TransactionsAdapter.TransactionItemViewHolder>() {

    var userTransactions: MutableList<UserTransaction> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemSelected: ((UserTransaction) -> Unit)? = null

    inner class TransactionItemViewHolder(private val binding: TransactioItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var currentItem: UserTransaction

        fun onBind() {
            currentItem = userTransactions[bindingAdapterPosition]
            binding.apply {
                binding.tvTransactionPrice.text = currentItem.amount.toString()
                binding.tvTransactionPurpose.text = currentItem.purpose.toString()
                binding.tvCurrentTime.text = currentItem.currentTime.toString()
                root.setOnClickListener {
                    onItemSelected?.invoke(currentItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder =
        TransactionItemViewHolder(
            TransactioItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount(): Int = userTransactions.size


}