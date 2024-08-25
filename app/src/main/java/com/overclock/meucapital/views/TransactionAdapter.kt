package com.overclock.meucapital.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(transaction: Transaction) {
            tvDescricao.text = transaction.descricao
            tvValor.text = transaction.valor.toString()
            tvData.text = transaction.data

            btnDelete.setOnClickListener {
                dbHandler.deleteTransaction(transaction.id)
                (itemView.context as MainActivity).updateRecyclerView()
            }
        }
    }
}