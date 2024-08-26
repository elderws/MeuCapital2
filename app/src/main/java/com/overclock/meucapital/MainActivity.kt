package com.overclock.meucapital

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overclock.meucapital.database.DataBaseHandler
import com.overclock.meucapital.views.Transaction
import com.overclock.meucapital.views.TransactionAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var dbHandler: DataBaseHandler
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DataBaseHandler(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        updateRecyclerView()

        val btnAddTransaction: Button = findViewById(R.id.btnAddTransaction)
        btnAddTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivityForResult(intent, 1)
        }

        val btnClearDatabase: Button = findViewById(R.id.btnClearDatabase)
        btnClearDatabase.setOnClickListener {
            clearDatabase()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            updateRecyclerView()
        }
    }

    private fun clearDatabase() {
        dbHandler.clearAllTransactions()
        updateRecyclerView()
    }

    public fun updateRecyclerView() {
        val transactions: List<Transaction> = dbHandler.getAllTransactions()
        transactionAdapter = TransactionAdapter(transactions, dbHandler)
        recyclerView.adapter = transactionAdapter
    }
}