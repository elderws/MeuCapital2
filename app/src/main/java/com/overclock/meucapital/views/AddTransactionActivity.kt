package com.overclock.meucapital

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.overclock.meucapital.database.DataBaseHandler

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val dbHandler = DataBaseHandler(this)

        val etDescricao: EditText = findViewById(R.id.etDescricao)
        val etValor: EditText = findViewById(R.id.etValor)
        val etData: EditText = findViewById(R.id.etData)
        val btnSaveTransaction: Button = findViewById(R.id.btnSaveTransaction)

        btnSaveTransaction.setOnClickListener {
            val descricao = etDescricao.text.toString()
            val valor = etValor.text.toString().toDoubleOrNull() ?: 0.0
            val data = etData.text.toString()
            dbHandler.addTransaction(descricao, valor, data)
            finish() // Fecha a atividade após salvar a transação
        }
    }
}