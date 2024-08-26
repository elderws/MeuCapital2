package com.overclock.meucapital

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.overclock.meucapital.database.DataBaseHandler
import com.overclock.meucapital.views.Transaction

class EditTransactionActivity : AppCompatActivity() {
    private lateinit var dbHandler: DataBaseHandler
    private var transactionId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

        dbHandler = DataBaseHandler(this)
        transactionId = intent.getIntExtra("TRANSACTION_ID", -1)

        val etDescricao: EditText = findViewById(R.id.etDescricao)
        val etValor: EditText = findViewById(R.id.etValor)
        val etData: EditText = findViewById(R.id.etData)
        val etTipo: EditText = findViewById(R.id.etTipo)
        val btnSave: Button = findViewById(R.id.btnSave)

        val transaction = dbHandler.getTransaction(transactionId)
        if (transaction != null) {
            etDescricao.setText(transaction.descricao)
            etValor.setText(transaction.valor.toString())
            etData.setText(transaction.data)
            etTipo.setText(transaction.tipo)
        }

        btnSave.setOnClickListener {
            val updatedTransaction = Transaction(
                id = transactionId,
                descricao = etDescricao.text.toString(),
                valor = etValor.text.toString().toDouble(),
                data = etData.text.toString(),
                tipo = etTipo.text.toString()
            )
            dbHandler.updateTransaction(updatedTransaction)
            finish()
        }
    }
}