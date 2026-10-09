package dz.ateliertrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dz.ateliertrack.data.Ticket
import dz.ateliertrack.util.PdfReceipt
import dz.ateliertrack.util.PriceFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    ticket: Ticket?,
    lang: String,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAdvance: (Ticket) -> Unit,
    onDelete: (Ticket) -> Unit
) {
    val fr = lang != "ar"
    val context = LocalContext.current
    var confirmDelete by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (fr) "Ticket #${ticket?.id ?: ""}" else "تذكرة #${ticket?.id ?: ""}") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) } },
                actions = {
                    ticket?.let { t ->
                        IconButton(onClick = {
                            context.startActivity(
                                android.content.Intent.createChooser(
                                    PdfReceipt.shareIntent(context, t, fr), "PDF"
                                )
                            )
                        }) { Icon(Icons.Filled.Share, null) }
                        IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, null) }
                        IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Filled.Delete, null) }
                    }
                }
            )
        }
    ) { pad ->
        if (ticket == null) {
            Text(if (fr) "Chargement..." else "...", Modifier.padding(pad).padding(16.dp))
            return@Scaffold
        }
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("${if (fr) "Client" else "الزبون"}: ${ticket.clientName} (${ticket.clientPhone})")
            Text("${if (fr) "Appareil" else "الجهاز"}: ${ticket.deviceBrand} ${ticket.deviceModel} ${ticket.deviceType}")
            Text("${if (fr) "Panne" else "العطل"}: ${ticket.faultDescription}")
            Text("${if (fr) "Pieces" else "قطع الغيار"}: ${ticket.sparePartsNotes.ifBlank { "-" }}")
            Text("${if (fr) "Prix" else "السعر"}: ${PriceFormatter.formatDzd(ticket.priceEstimateDzd)}")
            Text("${if (fr) "Acompte" else "العربون"}: ${PriceFormatter.formatDzd(ticket.depositDzd)}")
            Text("${if (fr) "Reste" else "الباقي"}: ${PriceFormatter.formatDzd(ticket.balanceDzd)}")
            Text("${if (fr) "Statut" else "الحالة"}: ${PdfReceipt.statusLabel(ticket.ticketStatus, fr)}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onAdvance(ticket) }) {
                    Text(if (fr) "Avancer le statut" else "تقديم الحالة")
                }
                OutlinedButton(onClick = onEdit) { Text(if (fr) "Modifier" else "تعديل") }
            }
        }
        if (confirmDelete) {
            AlertDialog(
                onDismissRequest = { confirmDelete = false },
                confirmButton = {
                    TextButton(onClick = { confirmDelete = false; ticket?.let(onDelete) }) {
                        Text(if (fr) "Supprimer" else "حذف")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { confirmDelete = false }) { Text(if (fr) "Annuler" else "إلغاء") }
                },
                text = { Text(if (fr) "Supprimer ce ticket ?" else "حذف هذه التذكرة؟") }
            )
        }
    }
}
