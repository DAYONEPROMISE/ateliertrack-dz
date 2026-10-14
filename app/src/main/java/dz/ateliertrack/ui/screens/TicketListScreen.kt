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
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dz.ateliertrack.R
import dz.ateliertrack.data.TicketStatus
import dz.ateliertrack.ui.AtelierCard
import dz.ateliertrack.ui.AtelierEmptyState
import dz.ateliertrack.ui.AtelierHeader
import dz.ateliertrack.ui.TicketViewModel
import dz.ateliertrack.util.PdfReceipt
import dz.ateliertrack.util.PriceFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    viewModel: TicketViewModel,
    onOpen: (Long) -> Unit,
    onAdd: () -> Unit,
    onSettings: () -> Unit
) {
    val tickets by viewModel.tickets.collectAsState()
    val query by viewModel.query.collectAsState()
    val filter by viewModel.statusFilter.collectAsState()
    val shown = if (filter == null) tickets else tickets.filter { it.ticketStatus == filter }

    Scaffold(
        topBar = {
            AtelierHeader(
                title = stringResource(R.string.app_name),
                trailing = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.action_settings))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.action_add_ticket))
            }
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(12.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::setQuery,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                label = { Text(stringResource(R.string.search_hint)) },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                ChoiceChip(
                    selected = filter == null,
                    onClick = { viewModel.setStatusFilter(null) },
                    label = stringResource(R.string.filter_all)
                )
                TicketStatus.entries.forEach { s ->
                    ChoiceChip(
                        selected = filter == s,
                        onClick = { viewModel.setStatusFilter(s) },
                        label = statusLabel(s)
                    )
                }
            }
            if (shown.isEmpty()) {
                AtelierEmptyState(
                    icon = Icons.Filled.ConfirmationNumber,
                    title = stringResource(R.string.empty_tickets_title),
                    hint = stringResource(R.string.empty_tickets_hint)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(shown, key = { it.id }) { t ->
                        AtelierCard(modifier = Modifier.clickable { onOpen(t.id) }) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(R.string.ticket_title, t.id.toString()),
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.weight(1f)
                                )
                                StatusPill(statusLabel(t.ticketStatus))
                            }
                            Text(
                                "${t.clientName} • ${t.deviceBrand} ${t.deviceModel}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "${t.faultDescription.take(60)} • ${PriceFormatter.formatDzd(t.priceEstimateDzd)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Small amber-outlined status badge on ticket cards. */
@Composable
private fun StatusPill(label: String) {
    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

/** Selection chip (replaces radio buttons). */
@Composable
private fun ChoiceChip(selected: Boolean, onClick: () -> Unit, label: String) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelLarge) },
        shape = RoundedCornerShape(50),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

/** Status labels come from string resources. */
@Composable
private fun statusLabel(status: TicketStatus): String = when (status) {
    TicketStatus.RECU -> stringResource(R.string.status_received)
    TicketStatus.EN_COURS -> stringResource(R.string.status_in_progress)
    TicketStatus.PRET -> stringResource(R.string.status_ready)
    TicketStatus.LIVRE -> stringResource(R.string.status_delivered)
}
