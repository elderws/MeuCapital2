package com.overclock.meucapital.views

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.overclock.meucapital.EditTransactionActivity
import com.overclock.meucapital.MainActivity
import com.overclock.meucapital.R
import com.overclock.meucapital.database.DataBaseHandler

class TransactionAdapter(private val transactions: List<Transaction>, private val dbHandler: DataBaseHandler) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDescricao: TextView = itemView.findViewById(R.id.tvDescricao)
        private val tvValor: TextView = itemView.findViewById(R.id.tvValor)
        private val tvData: TextView = itemView.findViewById(R.id.tvData)
        private val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        private val btnOptions: ImageButton = itemView.findViewById(R.id.btnOptions)

        fun bind(transaction: Transaction) {
            tvDescricao.text = transaction.descricao
            tvValor.text = transaction.valor.toString()
            tvData.text = transaction.data
            tvTipo.text = transaction.tipo

            btnOptions.setOnClickListener {
                val popup = PopupMenu(itemView.context, it)
                popup.inflate(R.menu.transaction_options_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            val intent = Intent(itemView.context, EditTransactionActivity::class.java)
                            intent.putExtra("TRANSACTION_ID", transaction.id)
                            (itemView.context as MainActivity).startActivityForResult(intent, 2)
                            true
                        }
                        R.id.action_delete -> {
                            transaction.id?.let { id ->
                                dbHandler.deleteTransaction(id)
                                (itemView.context as MainActivity).updateRecyclerView()
                            }
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        }
    }
}