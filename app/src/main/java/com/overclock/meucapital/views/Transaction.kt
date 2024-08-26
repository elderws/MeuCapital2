// Verifique se a classe Transaction está definida corretamente
package com.overclock.meucapital.views

data class Transaction(
    val id: Int? = null,
    val descricao: String,
    val valor: Double,
    val data: String,
    val tipo: String
)