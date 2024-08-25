package com.overclock.meucapital.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.overclock.meucapital.views.Transaction

class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "DBMeuCapital"
        private const val TABLE_NAME = "lancamentos"
        private const val KEY_ID = "id"
        private const val KEY_DESCRICAO = "descricao"
        private const val KEY_TIPO = "tipo"
        private const val KEY_VALOR = "valor"
        private const val KEY_DATA = "data"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DESCRICAO + " TEXT,"
                + KEY_TIPO + " TEXT," + KEY_VALOR + " DOUBLE," + KEY_DATA + " TEXT" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addTransaction(descricao: String, valor: Double, data: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_DESCRICAO, descricao)
        contentValues.put(KEY_VALOR, valor)
        contentValues.put(KEY_DATA, data)
        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    fun getAllTransactions(): List<Transaction> {
        val transactionList: ArrayList<Transaction> = ArrayList()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                val descricao = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRICAO)) ?: ""
                val tipo = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIPO)) ?: ""
                val valor = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_VALOR))
                val data = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA)) ?: ""

                val transaction = Transaction(
                    id = id,
                    descricao = descricao,
                    tipo = tipo,
                    valor = valor,
                    data = data
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
        val success = db.delete(TABLE_NAME, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }

    fun clearDatabase(): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, null, null)
        db.close()
        return success
    }
}