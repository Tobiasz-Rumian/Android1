package com.example.project1.models

import java.math.BigDecimal

data class Product(
    val title: String? = "Untitled",
    val price: BigDecimal? = BigDecimal.ZERO,
    val amount: Int = 0,
    val purchased: Boolean = false
)

