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
                title = { Text(if (fr) "Ticket #${ticket?.id ?: ""}" else "\u062a\u0630\u0643\u0631\u0629 #${ticket?.id ?: ""}") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = if (fr) "Retour" else "\u0631\u062c\u0648\u0639") } },
                actions = {
                    ticket?.let { t ->
                        IconButton(onClick = {
                            context.startActivity(
                                android.content.Intent.createChooser(
                                    PdfReceipt.shareIntent(context, t, fr), "PDF"
                                )
                            )
                        }) { Icon(Icons.Filled.Share, contentDescription = if (fr) "Partager en PDF" else "\u0645\u0634\u0627\u0631\u0643\u0629 PDF") }
                        IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, contentDescription = if (fr) "Modifier" else "\u062a\u0639\u062f\u064a\u0644") }
                        IconButton(onClick = { confirmDelete = true }) { Icon(Icons.Filled.Delete, contentDescription = if (fr) "Supprimer" else "\u062d\u0630\u0641") }
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
            Text("${if (fr) "Client" else "\u0627\u0644\u0632\u0628\u0648\u0646"}: ${ticket.clientName} (${ticket.clientPhone})")
            Text("${if (fr) "Appareil" else "\u0627\u0644\u062c\u0647\u0627\u0632"}: ${ticket.deviceBrand} ${ticket.deviceModel} ${ticket.deviceType}")
            Text("${if (fr) "Panne" else "\u0627\u0644\u0639\u0637\u0644"}: ${ticket.faultDescription}")
            Text("${if (fr) "Pieces" else "\u0642\u0637\u0639 \u0627\u0644\u063a\u064a\u0627\u0631"}: ${ticket.sparePartsNotes.ifBlank { "-" }}")
            Text("${if (fr) "Prix" else "\u0627\u0644\u0633\u0639\u0631"}: ${PriceFormatter.formatDzd(ticket.priceEstimateDzd)}")
            Text("${if (fr) "Acompte" else "\u0627\u0644\u0639\u0631\u0628\u0648\u0646"}: ${PriceFormatter.formatDzd(ticket.depositDzd)}")
            Text("${if (fr) "Reste" else "\u0627\u0644\u0628\u0627\u0642\u064a"}: ${PriceFormatter.formatDzd(ticket.balanceDzd)}")
            Text("${if (fr) "Statut" else "\u0627\u0644\u062d\u0627\u0644\u0629"}: ${PdfReceipt.statusLabel(ticket.ticketStatus, fr)}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onAdvance(ticket) }) {
                    Text(if (fr) "Avancer le statut" else "\u062a\u0642\u062f\u064a\u0645 \u0627\u0644\u062d\u0627\u0644\u0629")
                }
                OutlinedButton(onClick = onEdit) { Text(if (fr) "Modifier" else "\u062a\u0639\u062f\u064a\u0644") }
            }
        }
        if (confirmDelete) {
            AlertDialog(
                onDismissRequest = { confirmDelete = false },
                confirmButton = {
                    TextButton(onClick = { confirmDelete = false; ticket?.let(onDelete) }) {
                        Text(if (fr) "Supprimer" else "\u062d\u0630\u0641")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { confirmDelete = false }) { Text(if (fr) "Annuler" else "\u0625\u0644\u063a\u0627\u0621") }
                },
                text = { Text(if (fr) "Supprimer ce ticket ?" else "\u062d\u0630\u0641 \u0647\u0630\u0647 \u0627\u0644\u062a\u0630\u0643\u0631\u0629\u061f") }
            )
        }
    }
}
