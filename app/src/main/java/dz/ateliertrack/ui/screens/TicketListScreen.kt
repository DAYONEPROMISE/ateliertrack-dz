package dz.ateliertrack.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dz.ateliertrack.data.TicketStatus
import dz.ateliertrack.ui.TicketViewModel
import dz.ateliertrack.util.PdfReceipt
import dz.ateliertrack.util.PriceFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    viewModel: TicketViewModel,
    lang: String,
    onOpen: (Long) -> Unit,
    onAdd: () -> Unit,
    onSettings: () -> Unit
) {
    val fr = lang != "ar"
    val tickets by viewModel.tickets.collectAsState()
    val query by viewModel.query.collectAsState()
    val filter by viewModel.statusFilter.collectAsState()
    val shown = if (filter == null) tickets else tickets.filter { it.ticketStatus == filter }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (fr) "AtelierTrack" else "أتيليي تراك") },
                actions = { IconButton(onClick = onSettings) { Icon(Icons.Filled.Settings, null) } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) { Icon(Icons.Filled.Add, null) }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(12.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::setQuery,
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                label = { Text(if (fr) "Rechercher (client, appareil, panne)" else "بحث (زبون، جهاز، عطل)") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                AssistChip(
                    onClick = { viewModel.setStatusFilter(null) },
                    label = { Text(if (fr) "Tous" else "الكل") }
                )
                TicketStatus.entries.forEach { s ->
                    AssistChip(
                        onClick = { viewModel.setStatusFilter(s) },
                        label = { Text(PdfReceipt.statusLabel(s, fr)) }
                    )
                }
            }
            if (shown.isEmpty()) {
                Text(if (fr) "Aucun ticket. Ajoutez le premier." else "لا توجد تذاكر بعد.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(shown, key = { it.id }) { t ->
                        Card(Modifier.fillMaxWidth().clickable { onOpen(t.id) }) {
                            Column(Modifier.padding(12.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("#${t.id} ${t.clientName}")
                                    Text(PdfReceipt.statusLabel(t.ticketStatus, fr))
                                }
                                Text("${t.deviceBrand} ${t.deviceModel} - ${t.faultDescription}".take(90))
                                Text(PriceFormatter.formatDzd(t.priceEstimateDzd))
                            }
                        }
                    }
                }
            }
        }
    }
}
