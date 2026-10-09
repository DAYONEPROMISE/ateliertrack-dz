package dz.ateliertrack.util

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import dz.ateliertrack.data.Ticket
import dz.ateliertrack.data.TicketStatus
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Builds a printable receipt PDF with PdfDocument (no dependency, works offline). */
object PdfReceipt {
    fun statusLabel(status: TicketStatus, fr: Boolean = true): String = when (status) {
        TicketStatus.RECU -> if (fr) "Recu" else "مستلم"
        TicketStatus.EN_COURS -> if (fr) "En cours" else "قيد الإصلاح"
        TicketStatus.PRET -> if (fr) "Pret" else "جاهز"
        TicketStatus.LIVRE -> if (fr) "Livre" else "تم التسليم"
    }

    fun buildLines(ticket: Ticket, fr: Boolean = true): List<String> {
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH)
            .format(Date(ticket.createdAt))
        return if (fr) {
            listOf(
                "ATELIER TRACK - Recu de reparation",
                "Ticket #${ticket.id} - $date",
                "Client: ${ticket.clientName} (${ticket.clientPhone})",
                "Appareil: ${ticket.deviceBrand} ${ticket.deviceModel} ${ticket.deviceType}".trim(),
                "Panne: ${ticket.faultDescription}",
                "Pieces: ${ticket.sparePartsNotes.ifBlank { "-" }}",
                "Prix estime: ${PriceFormatter.formatDzd(ticket.priceEstimateDzd)}",
                "Acompte: ${PriceFormatter.formatDzd(ticket.depositDzd)}",
                "Reste: ${PriceFormatter.formatDzd(ticket.balanceDzd)}",
                "Statut: ${statusLabel(ticket.ticketStatus, true)}"
            )
        } else {
            listOf(
                "AtelierTrack - وصل إصلاح",
                "Ticket #${ticket.id} - $date",
                "الزبون: ${ticket.clientName} (${ticket.clientPhone})",
                "الجهاز: ${ticket.deviceBrand} ${ticket.deviceModel}",
                "العطل: ${ticket.faultDescription}",
                "قطع الغيار: ${ticket.sparePartsNotes.ifBlank { "-" }}",
                "السعر: ${PriceFormatter.formatDzd(ticket.priceEstimateDzd)}",
                "العربون: ${PriceFormatter.formatDzd(ticket.depositDzd)}",
                "الباقي: ${PriceFormatter.formatDzd(ticket.balanceDzd)}",
                "الحالة: ${statusLabel(ticket.ticketStatus, false)}"
            )
        }
    }

    fun writePdf(context: Context, ticket: Ticket, fr: Boolean = true): File {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 points
        val page = doc.startPage(pageInfo)
        val paint = Paint().apply { textSize = 14f }
        val title = Paint().apply { textSize = 20f; isFakeBoldText = true }
        var y = 60f
        page.canvas.drawText(
            if (fr) "ATELIER TRACK - Recu" else "AtelierTrack - وصل",
            40f, y, title
        )
        y += 30f
        for (line in buildLines(ticket, fr)) {
            y += 26f
            page.canvas.drawText(line.take(90), 40f, y, paint)
        }
        doc.finishPage(page)
        val file = File(context.cacheDir, "recu_ticket_${ticket.id}.pdf")
        file.outputStream().use { doc.writeTo(it) }
        doc.close()
        return file
    }

    fun shareIntent(context: Context, ticket: Ticket, fr: Boolean = true): Intent {
        val file = writePdf(context, ticket, fr)
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
