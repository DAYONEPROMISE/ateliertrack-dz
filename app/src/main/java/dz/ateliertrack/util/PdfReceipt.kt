package dz.ateliertrack.util

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import dz.ateliertrack.R
import dz.ateliertrack.data.Ticket
import dz.ateliertrack.data.TicketStatus
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Builds a printable receipt PDF with PdfDocument (no dependency, works offline). */
object PdfReceipt {
    fun statusLabel(context: Context, status: TicketStatus): String = when (status) {
        TicketStatus.RECU -> context.getString(R.string.status_received)
        TicketStatus.EN_COURS -> context.getString(R.string.status_in_progress)
        TicketStatus.PRET -> context.getString(R.string.status_ready)
        TicketStatus.LIVRE -> context.getString(R.string.status_delivered)
    }

    fun buildLines(context: Context, ticket: Ticket): List<String> {
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH).format(Date(ticket.createdAt))
        return listOf(
            "${context.getString(R.string.app_name)} - ${context.getString(R.string.status_received)}",
            "${context.getString(R.string.ticket_title, ticket.id.toString())} - $date",
            "${context.getString(R.string.field_client)}: ${ticket.clientName} (${ticket.clientPhone})",
            "${context.getString(R.string.field_device)}: ${ticket.deviceBrand} ${ticket.deviceModel} ${ticket.deviceType}".trim(),
            "${context.getString(R.string.field_fault)}: ${ticket.faultDescription}",
            "${context.getString(R.string.field_parts)}: ${ticket.sparePartsNotes.ifBlank { "-" }}",
            "${context.getString(R.string.field_price)}: ${PriceFormatter.formatDzd(ticket.priceEstimateDzd)}",
            "${context.getString(R.string.field_deposit)}: ${PriceFormatter.formatDzd(ticket.depositDzd)}",
            "${context.getString(R.string.field_balance)}: ${PriceFormatter.formatDzd(ticket.balanceDzd)}",
            "${context.getString(R.string.field_status)}: ${statusLabel(context, ticket.ticketStatus)}"
        )
    }

    fun writePdf(context: Context, ticket: Ticket): File {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 points
        val page = doc.startPage(pageInfo)
        val paint = Paint().apply { textSize = 14f }
        val title = Paint().apply { textSize = 20f; isFakeBoldText = true }
        var y = 60f
        page.canvas.drawText(
            "${context.getString(R.string.app_name)} - ${context.getString(R.string.field_status)}",
            40f, y, title
        )
        y += 30f
        for (line in buildLines(context, ticket)) {
            y += 26f
            page.canvas.drawText(line.take(90), 40f, y, paint)
        }
        doc.finishPage(page)
        val file = File(context.cacheDir, "recu_ticket_${ticket.id}.pdf")
        file.outputStream().use { doc.writeTo(it) }
        doc.close()
        return file
    }

    fun shareIntent(context: Context, ticket: Ticket): Intent {
        val file = writePdf(context, ticket)
        val uri = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
        return Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
}
