package dz.ateliertrack.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dz.ateliertrack.R
import dz.ateliertrack.data.Ticket
import dz.ateliertrack.ui.theme.AtelierCard
import dz.ateliertrack.ui.theme.AtelierHeader
import dz.ateliertrack.util.PdfReceipt
import dz.ateliertrack.util.PriceFormatter

@Composable
fun TicketDetailScreen(
    ticket: Ticket?,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAdvance: (Ticket) -> Unit,
    onDelete: (Ticket) -> Unit
) {
    val context = LocalContext.current
    var confirmDelete by remember { mutableStateOf(false) }

    AtelierHeader(
        title = ticket?.let { stringResource(R.string.ticket_title, it.id.toString()) }
            ?: stringResource(R.string.app_name),
        navigateUp = onBack,
        backLabel = stringResource(R.string.action_back),
        trailing = {
            ticket?.let { t ->
                IconButton(onClick = {
                    context.startActivity(
                        Intent.createChooser(PdfReceipt.shareIntent(context, t), "PDF")
                    )
                }) {
                    Icon(Icons.Filled.Share, contentDescription = stringResource(R.string.action_share_pdf))
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.action_edit))
                }
            }
        }
    )
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (ticket == null) {
            Text(stringResource(R.string.detail_loading))
            return@Column
        }
        DetailRow(stringResource(R.string.field_client), "${ticket.clientName} (${ticket.clientPhone})")
        DetailRow(
            stringResource(R.string.field_device),
            "${ticket.deviceBrand} ${ticket.deviceModel} ${ticket.deviceType}".trim()
        )
        DetailRow(stringResource(R.string.field_fault), ticket.faultDescription)
        DetailRow(stringResource(R.string.field_parts), ticket.sparePartsNotes.ifBlank { "—" })
        DetailRow(stringResource(R.string.field_price), PriceFormatter.formatDzd(ticket.priceEstimateDzd))
        DetailRow(stringResource(R.string.field_deposit), PriceFormatter.formatDzd(ticket.depositDzd))
        DetailRow(stringResource(R.string.field_balance), PriceFormatter.formatDzd(ticket.balanceDzd))
        DetailRow(stringResource(R.string.field_status), ticket.ticketStatus.label(context))

        AtelierCard {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onAdvance(ticket) }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.action_advance))
                }
                OutlinedButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.action_edit))
                }
            }
            TextButton(
                onClick = { confirmDelete = true },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    stringResource(R.string.action_delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            confirmButton = {
                TextButton(onClick = { confirmDelete = false; ticket?.let(onDelete) }) {
                    Text(stringResource(R.string.action_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text(stringResource(R.string.action_cancel)) }
            },
            text = { Text(stringResource(R.string.delete_confirm_title)) }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    AtelierCard {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

/** Status label resolved from resources for non-composable call sites. */
private fun dz.ateliertrack.data.TicketStatus.label(context: android.content.Context): String =
    PdfReceipt.statusLabel(context, this)
