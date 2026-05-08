package com.jfb.orderops.product.data.dto

import com.jfb.orderops.product.domain.model.Product

fun ProductResponse.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        categoryId = categoryId,
        categoryName = categoryName,
        description = description,
        price = price,
        active = active
    )
}