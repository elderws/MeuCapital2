package com.overclock.meucapital

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.overclock.meucapital.database.DataBaseHandler
import com.overclock.meucapital.views.Transaction
import java.util.*

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var dbHandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        dbHandler = DataBaseHandler(this)

        val etDescricao: EditText = findViewById(R.id.etDescricao)
        val etValor: EditText = findViewById(R.id.etValor)
        val tvData: TextView = findViewById(R.id.tvData)
        val spinnerTipo: Spinner = findViewById(R.id.spinnerTipo)
        val btnSave: Button = findViewById(R.id.btnSaveTransaction)

        // Configurar o adaptador do Spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.tipo_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTipo.adapter = adapter
        }

        // Configurar o DatePickerDialog
        tvData.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d/%02d/%02d", selectedDay, selectedMonth + 1, selectedYear % 100)
                tvData.text = selectedDate
            }, year, month, day)

            datePickerDialog.show()
        }

        btnSave.setOnClickListener {
            val newTransaction = Transaction(
                descricao = etDescricao.text.toString(),
                valor = etValor.text.toString().toDouble(),
                data = tvData.text.toString(),
                tipo = spinnerTipo.selectedItem.toString()
            )
            dbHandler.addTransaction(newTransaction)
            setResult(android.app.Activity.RESULT_OK)
            finish()
        }
    }
}