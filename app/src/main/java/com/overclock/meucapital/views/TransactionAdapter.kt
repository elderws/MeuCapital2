package com.overclock.meucapital.views

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.overclock.meucapital.EditTransactionActivity
import com.overclock.meucapital.MainActivity
import com.overclock.meucapital.R
import com.overclock.meucapital.database.DataBaseHandler

class TransactionAdapter(private val transactions: List<Transaction>, private val dbHandler: DataBaseHandler) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDescricao: TextView = itemView.findViewById(R.id.tvDescricao)
        private val tvValor: TextView = itemView.findViewById(R.id.tvValor)
        private val tvData: TextView = itemView.findViewById(R.id.tvData)
        private val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        private val btnDetails: ImageButton = itemView.findViewById(R.id.btnDetails)

        fun bind(transaction: Transaction) {
            tvDescricao.text = transaction.descricao
            tvValor.text = transaction.valor.toString()
            tvData.text = transaction.data
            tvTipo.text = transaction.tipo

            btnDetails.setOnClickListener {
                val popupMenu = PopupMenu(itemView.context, btnDetails)
                popupMenu.menuInflater.inflate(R.layout.transaction_options_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_delete -> {
                            dbHandler.deleteTransaction(transaction.id)
                            (itemView.context as MainActivity).updateRecyclerView()
                            true
                        }
                        R.id.action_edit -> {
                            val intent = Intent(itemView.context, EditTransactionActivity::class.java)
                            intent.putExtra("TRANSACTION_ID", transaction.id)
                            itemView.context.startActivity(intent)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }
    }
}