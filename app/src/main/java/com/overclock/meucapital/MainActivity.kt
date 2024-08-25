package com.overclock.meucapital

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.overclock.meucapital.database.DataBaseHandler
import com.overclock.meucapital.views.TransactionAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var dbHandler: DataBaseHandler
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DataBaseHandler(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnAddTransaction: Button = findViewById(R.id.btnAddTransaction)
        btnAddTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        val btnClearDatabase: Button = findViewById(R.id.btnClearDatabase)
        btnClearDatabase.setOnClickListener {
            dbHandler.clearDatabase()
            updateRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerView()
    }

    fun updateRecyclerView() {
        val transactions = dbHandler.getAllTransactions()
        recyclerView.adapter = TransactionAdapter(transactions, dbHandler)
    }

    fun deleteTransaction(id: Int) {
        dbHandler.deleteTransaction(id)
        updateRecyclerView()
    }
}