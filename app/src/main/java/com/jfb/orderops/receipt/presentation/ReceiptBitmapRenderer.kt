package com.jfb.orderops.receipt.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.jfb.orderops.order.domain.model.Order
import com.jfb.orderops.payment.domain.model.PaymentMethod
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ReceiptBitmapRenderer {

    private const val WIDTH = 576
    private const val PADDING = 28
    private const val LINE_HEIGHT = 30

    fun render(
        order: Order,
        paymentMethod: PaymentMethod,
        companyName: String = "OrderOps",
        document: String = "00.000.000/0000-00",
        address: String = "Endereço não informado"
    ): Bitmap {
        val lines = buildLines(
            order = order,
            paymentMethod = paymentMethod,
            companyName = companyName,
            document = document,
            address = address
        )

        val height = PADDING * 2 + lines.size * LINE_HEIGHT

        val bitmap = Bitmap.createBitmap(WIDTH, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.WHITE)

        val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 25f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }

        val boldPaint = Paint(normalPaint).apply {
            textSize = 27f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        }

        val titlePaint = Paint(normalPaint).apply {
            textSize = 34f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        }

        var y = PADDING + 28f

        lines.forEach { line ->
            val paint = when {
                line.title -> titlePaint
                line.bold -> boldPaint
                else -> normalPaint
            }

            val x = when (line.align) {
                Align.LEFT -> PADDING.toFloat()
                Align.CENTER -> (WIDTH - paint.measureText(line.text)) / 2f
                Align.RIGHT -> WIDTH - PADDING - paint.measureText(line.text)
            }

            canvas.drawText(line.text, x, y, paint)
            y += LINE_HEIGHT
        }

        return bitmap
    }

    fun renderLoading(): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, 220, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawColor(Color.WHITE)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 28f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }

        canvas.drawText("Carregando comprovante...", PADDING.toFloat(), 100f, paint)

        return bitmap
    }

    private fun buildLines(
        order: Order,
        paymentMethod: PaymentMethod,
        companyName: String,
        document: String,
        address: String
    ): List<ReceiptLine> {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm")

        return buildList {
            addCenter(companyName.uppercase(), title = true)
            addCenter(document)
            splitText(address, 34).forEach {
                addCenter(it)
            }
            separator()

            addCenter("PEDIDO #${order.id}", title = true)
            addCenter("MESA ${order.serviceTableId.toString().padStart(2, '0')}")
            separator()

            addCenter("CONSUMO NO LOCAL")
            separator()

            addLeft("ITENS", bold = true)
            order.items.forEach { item ->
                addLeftRight(
                    left = "${item.quantity}x ${item.productName}",
                    right = money(item.totalPrice)
                )
            }

            separator()

            addLeftRight("Subtotal", money(order.totalAmount))
            addLeftRight("TOTAL", money(order.totalAmount), bold = true)

            separator()

            addLeft("PAGAMENTO", bold = true)
            addLeftRight(paymentMethod.toReceiptText(), money(order.totalAmount))

            separator()

            addLeft("Impresso em: ${now.format(formatter)}")

            addBlank()
            addCenter("Obrigado pela preferência!")
        }
    }

    private fun MutableList<ReceiptLine>.separator() {
        add(ReceiptLine("-".repeat(34), Align.CENTER))
    }

    private fun MutableList<ReceiptLine>.addBlank() {
        add(ReceiptLine("", Align.LEFT))
    }

    private fun MutableList<ReceiptLine>.addCenter(
        text: String,
        bold: Boolean = false,
        title: Boolean = false
    ) {
        add(ReceiptLine(text, Align.CENTER, bold, title))
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
        val maxChars = 34
        val rightColumnWidth = 12
        val leftColumnWidth = maxChars - rightColumnWidth

        val cleanLeft = left
            .take(leftColumnWidth)
            .padEnd(leftColumnWidth)

        val cleanRight = right
            .take(rightColumnWidth)
            .padStart(rightColumnWidth)

        add(
            ReceiptLine(
                text = cleanLeft + cleanRight,
                align = Align.LEFT,
                bold = bold
            )
        )
    }

    private fun money(value: Double): String {
        return "R$ %.2f".format(value).replace(".", ",")
    }

    private fun PaymentMethod.toReceiptText(): String {
        return when (this) {
            PaymentMethod.CASH -> "Dinheiro"
            PaymentMethod.CREDIT_CARD -> "Crédito"
            PaymentMethod.DEBIT_CARD -> "Débito"
            PaymentMethod.PIX -> "PIX"
        }
    }

    private fun splitText(text: String, maxChars: Int): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()

        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"

            if (testLine.length <= maxChars) {
                currentLine = testLine
            } else {
                lines.add(currentLine)
                currentLine = word
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }

    private data class ReceiptLine(
        val text: String,
        val align: Align,
        val bold: Boolean = false,
        val title: Boolean = false
    )

    private enum class Align {
        LEFT,
        CENTER,
        RIGHT
    }
}