package com.jfb.orderops.company.data.dto

import com.jfb.orderops.company.domain.model.Company
import com.jfb.orderops.company.domain.model.CompanyAddress

fun CompanyResponse.toDomain(): Company {
    return Company(
        id = id,
        name = name,
        document = document,
        phone = phone,
        address = address?.toDomain()
    )
}

private fun CompanyAddressResponse.toDomain(): CompanyAddress {
    return CompanyAddress(
        street = street,
        number = number,
        complement = complement,
        city = city,
        state = state,
        zipCode = zipCode
    )
}