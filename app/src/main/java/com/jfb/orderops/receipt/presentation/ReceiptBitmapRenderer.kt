package com.jfb.orderops.receipt.presentation

import android.graphics.*
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.payment.domain.model.PaymentMethod
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ReceiptBitmapRenderer {

    private const val WIDTH = 576
    private const val PADDING = 32
    private const val LINE_HEIGHT = 34

    fun render(
        order: Order,
        paymentMethod: PaymentMethod
    ): Bitmap {
        val lines = buildLines(order, paymentMethod)

        val height = PADDING * 2 + lines.size * LINE_HEIGHT + 200

        val bitmap = Bitmap.createBitmap(WIDTH, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.WHITE)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 26f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }

        val boldPaint = Paint(paint).apply {
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        }

        var y = PADDING + 30f

        lines.forEach { line ->
            val currentPaint = if (line.bold) boldPaint else paint

            when (line.align) {
                Align.CENTER -> {
                    val textWidth = currentPaint.measureText(line.text)
                    canvas.drawText(
                        line.text,
                        (WIDTH - textWidth) / 2f,
                        y,
                        currentPaint
                    )
                }

                Align.LEFT -> {
                    canvas.drawText(
                        line.text,
                        PADDING.toFloat(),
                        y,
                        currentPaint
                    )
                }

                Align.RIGHT -> {
                    val textWidth = currentPaint.measureText(line.text)
                    canvas.drawText(
                        line.text,
                        WIDTH - PADDING - textWidth,
                        y,
                        currentPaint
                    )
                }
            }

            y += LINE_HEIGHT
        }

        return bitmap
    }

    fun renderLoading(): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, 300, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 28f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }

        canvas.drawText("Carregando comprovante...", PADDING.toFloat(), 120f, paint)

        return bitmap
    }

    private fun buildLines(
        order: Order,
        paymentMethod: PaymentMethod
    ): List<ReceiptLine> {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")

        return buildList {
            addCenter("OrderOps", bold = true)
            addCenter("00.000.000/0000-00")
            addCenter("Endereço não informado")
            separator()

            addCenter("Pedido: ${order.id}", bold = true)
            addCenter("Mesa ${order.serviceTableId.toString().padStart(2, '0')}")
            separator()

            addCenter("Tipo: Consumo no local")
            separator()

            order.items.forEach { item ->
                addLeftRight(
                    left = item.productName,
                    right = "${item.quantity}x ${money(item.totalPrice)}"
                )
            }

            separator()

            addLeftRight("Subtotal (R$)", money(order.totalAmount))
            addLeftRight("Total (R$)", money(order.totalAmount), bold = true)

            separator()

            addLeft("Pagamentos")
            addLeftRight(paymentMethod.toReceiptText(), money(order.totalAmount))

            separator()

            addLeft("Data impressão: ${now.format(formatter)}")

            addBlank()
            addCenter("Obrigado pela compra")
        }
    }

    private fun MutableList<ReceiptLine>.separator() {
        add(ReceiptLine("-".repeat(36), Align.CENTER))
    }

    private fun MutableList<ReceiptLine>.addBlank() {
        add(ReceiptLine("", Align.LEFT))
    }

    private fun MutableList<ReceiptLine>.addCenter(
        text: String,
        bold: Boolean = false
    ) {
        add(ReceiptLine(text, Align.CENTER, bold))
    }

    private fun MutableList<ReceiptLine>.addLeft(
        text: String,
        bold: Boolean = false
    ) {
        add(ReceiptLine(text, Align.LEFT, bold))
    }

    private fun MutableList<ReceiptLine>.addLeftRight(
        left: String,
        right: String,
        bold: Boolean = false
    ) {
        val maxLeft = 24
        val cleanLeft = if (left.length > maxLeft) left.take(maxLeft) else left

        val spaces = " ".repeat(36 - cleanLeft.length - right.length)
        add(ReceiptLine(cleanLeft + spaces + right, Align.LEFT, bold))
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

    private data class ReceiptLine(
        val text: String,
        val align: Align,
        val bold: Boolean = false
    )

    private enum class Align {
        LEFT,
        CENTER,
        RIGHT
    }
}