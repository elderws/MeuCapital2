package com.overclock.meucapital.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.overclock.meucapital.views.Transaction

class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "transactionsDB"
        private const val TABLE_TRANSACTIONS = "transactions"
        private const val KEY_ID = "id"
        private const val KEY_DESCRICAO = "descricao"
        private const val KEY_VALOR = "valor"
        private const val KEY_DATA = "data"
        private const val KEY_TIPO = "tipo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTransactionsTable = ("CREATE TABLE $TABLE_TRANSACTIONS ("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_DESCRICAO TEXT,"
                + "$KEY_VALOR REAL,"
                + "$KEY_DATA TEXT,"
                + "$KEY_TIPO TEXT)")
        db?.execSQL(createTransactionsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    fun addTransaction(descricao: String, tipo: String, valor: Double, data: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_DESCRICAO, descricao)
            put(KEY_VALOR, valor)
            put(KEY_DATA, data)
            put(KEY_TIPO, tipo)
        }
        val success = db.insert(TABLE_TRANSACTIONS, null, contentValues)
        db.close()
        return success
    }

    fun clearDatabase() {
        val db = this.writableDatabase
        db.delete(TABLE_TRANSACTIONS, null, null)
        db.close()
    }

    fun getAllTransactions(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TRANSACTIONS", null)
        if (cursor.moveToFirst()) {
            do {
                val transaction = Transaction(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    descricao = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRICAO)),
                    valor = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_VALOR)),
                    data = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA)),
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIPO))
                )
                transactions.add(transaction)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return transactions
    }

    fun deleteTransaction(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_TRANSACTIONS, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun updateTransaction(transaction: Transaction): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_DESCRICAO, transaction.descricao)
            put(KEY_VALOR, transaction.valor)
            put(KEY_DATA, transaction.data)
            put(KEY_TIPO, transaction.tipo)
        }
        return db.update(TABLE_TRANSACTIONS, contentValues, "$KEY_ID = ?", arrayOf(transaction.id.toString()))
    }

    fun getTransaction(id: Int): Transaction? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TRANSACTIONS, arrayOf(KEY_ID, KEY_DESCRICAO, KEY_VALOR, KEY_DATA, KEY_TIPO),
            "$KEY_ID = ?", arrayOf(id.toString()), null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val transaction = Transaction(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                descricao = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRICAO)),
                valor = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_VALOR)),
                data = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA)),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIPO))
            )
            cursor.close()
            db.close()
            transaction
        } else {
            cursor?.close()
            db.close()
            null
        }
    }
}