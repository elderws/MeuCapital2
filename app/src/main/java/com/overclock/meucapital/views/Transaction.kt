package com.overclock.meucapital.views
data class Transaction(
    val id: Int,
    val descricao: String,
    val tipo: String,
    val valor: Double,
    val data: String
)