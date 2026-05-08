package com.jfb.orderops.category.data.dto

import com.jfb.orderops.category.domain.model.Category

fun CategoryResponse.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        active = active
    )
}