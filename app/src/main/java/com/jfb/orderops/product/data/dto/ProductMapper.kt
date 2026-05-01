package com.jfb.orderops.product.data.dto

import com.jfb.orderops.product.domain.model.Product

fun ProductResponse.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description,
        price = price,
        active = active
    )
}