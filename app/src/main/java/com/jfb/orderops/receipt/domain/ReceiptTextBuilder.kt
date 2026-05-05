package com.jfb.orderops.receipt.domain

import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.payment.domain.model.PaymentMethod
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ReceiptTextBuilder {

    private const val WIDTH = 42

    fun build(
        order: Order,
        paymentMethod: PaymentMethod,
        companyName: String = "OrderOps",
        document: String = "00.000.000/0000-00",
        addressLine1: String = "Endereço não informado",
        addressLine2: String = "",
        openedAt: LocalDateTime = LocalDateTime.now(),
        printedAt: LocalDateTime = LocalDateTime.now()
    ): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")

        return buildString {
            appendLine(center(companyName))
            appendLine(center(document))
            appendLine(center(addressLine1))
            if (addressLine2.isNotBlank()) {
                appendLine(center(addressLine2))
            }

            separator()

            appendLine(center("Pedido: ${order.id}"))
            appendLine(center("Mesa ${order.serviceTableId.toString().padStart(2, '0')}"))

            separator()

            appendLine(center("Tipo: Consumo no local"))

            separator()

            order.items.forEach { item ->
                val name = item.productName
                val price = "${item.quantity}x ${money(item.totalPrice)}"

                appendLine(row(name, price))
            }

            separator()

            appendLine(row("Subtotal (R$)", money(order.totalAmount)))
            appendLine(row("Total (R$)", money(order.totalAmount)))

            separator()

            appendLine("Pagamentos")
            appendLine(row(paymentMethod.toReceiptText(), money(order.totalAmount)))

            separator()

            appendLine("Data abertura: ${openedAt.format(dateFormatter)}")
            appendLine("Data impressão: ${printedAt.format(dateFormatter)}")

            appendLine()
            appendLine(center("Obrigado pela compra"))
        }
    }

    private fun StringBuilder.separator() {
        appendLine("-".repeat(WIDTH))
    }

    private fun center(text: String): String {
        if (text.length >= WIDTH) return text

        val totalPadding = WIDTH - text.length
        val leftPadding = totalPadding / 2
        val rightPadding = totalPadding - leftPadding

        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding)
    }

    private fun row(left: String, right: String): String {
        val maxLeft = WIDTH - right.length - 1
        val trimmedLeft = if (left.length > maxLeft) {
            left.substring(0, maxLeft)
        } else {
            left
        }

        val spaces = WIDTH - trimmedLeft.length - right.length
        return trimmedLeft + " ".repeat(spaces.coerceAtLeast(1)) + right
    }

    private fun money(value: Double): String {
        return "%.2f".format(value).replace(".", ",")
    }

    private fun PaymentMethod.toReceiptText(): String {
        return when (this) {
            PaymentMethod.CASH -> "Dinheiro"
            PaymentMethod.CREDIT_CARD -> "Crédito"
            PaymentMethod.DEBIT_CARD -> "Débito"
            PaymentMethod.PIX -> "PIX"
        }
    }
}