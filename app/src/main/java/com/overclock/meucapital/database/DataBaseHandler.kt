package com.overclock.meucapital.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.overclock.meucapital.views.Transaction

class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "transactions.db"
        private const val TABLE_TRANSACTIONS = "transactions"

        private const val KEY_ID = "id"
        private const val KEY_DESCRICAO = "descricao"
        private const val KEY_VALOR = "valor"
        private const val KEY_DATA = "data"
        private const val KEY_TIPO = "tipo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_TRANSACTIONS ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_DESCRICAO TEXT,"
                + "$KEY_VALOR REAL,"
                + "$KEY_DATA TEXT,"
                + "$KEY_TIPO TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    fun addTransaction(transaction: Transaction) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_DESCRICAO, transaction.descricao)
        values.put(KEY_VALOR, transaction.valor)
        values.put(KEY_DATA, transaction.data)
        values.put(KEY_TIPO, transaction.tipo)

        db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
    }

    fun getTransaction(id: Int): Transaction? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TRANSACTIONS, arrayOf(KEY_ID, KEY_DESCRICAO, KEY_VALOR, KEY_DATA, KEY_TIPO),
            "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null
        )
        cursor?.moveToFirst()
        val transaction = if (cursor != null && cursor.count > 0) {
            Transaction(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                descricao = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRICAO)),
                valor = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_VALOR)),
                data = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA)),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIPO))
            )
        } else {
            null
        }
        cursor?.close()
        db.close()
        return transaction
    }

    fun updateTransaction(transaction: Transaction): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_DESCRICAO, transaction.descricao)
        values.put(KEY_VALOR, transaction.valor)
        values.put(KEY_DATA, transaction.data)
        values.put(KEY_TIPO, transaction.tipo)

        return db.update(TABLE_TRANSACTIONS, values, "$KEY_ID=?", arrayOf(transaction.id.toString()))
    }

    fun getAllTransactions(): List<Transaction> {
        val transactionList: MutableList<Transaction> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val transaction = Transaction(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    descricao = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRICAO)),
                    valor = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_VALOR)),
                    data = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA)),
                    tipo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIPO))
                )
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return transactionList
    }

    fun deleteTransaction(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_TRANSACTIONS, "$KEY_ID=?", arrayOf(id.toString()))
    }
    fun clearAllTransactions() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_TRANSACTIONS")
        db.close()
    }
}