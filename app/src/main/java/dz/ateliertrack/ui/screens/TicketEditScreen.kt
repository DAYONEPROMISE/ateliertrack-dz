package dz.ateliertrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dz.ateliertrack.R
import dz.ateliertrack.data.Ticket
import dz.ateliertrack.data.TicketStatus
import dz.ateliertrack.ui.theme.AtelierHeader
import dz.ateliertrack.util.PriceFormatter
import dz.ateliertrack.util.TicketValidator

/** Status label resolved from string resources. */
@Composable
private fun TicketStatus.ofLabel(): String = when (this) {
    TicketStatus.RECU -> stringResource(R.string.status_received)
    TicketStatus.EN_COURS -> stringResource(R.string.status_in_progress)
    TicketStatus.PRET -> stringResource(R.string.status_ready)
    TicketStatus.LIVRE -> stringResource(R.string.status_delivered)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketEditScreen(
    existing: Ticket?,
    onBack: () -> Unit,
    onSave: (Ticket) -> Unit
) {
    var clientName by remember { mutableStateOf(existing?.clientName ?: "") }
    var clientPhone by remember { mutableStateOf(existing?.clientPhone ?: "") }
    var brand by remember { mutableStateOf(existing?.deviceBrand ?: "") }
    var model by remember { mutableStateOf(existing?.deviceModel ?: "") }
    var deviceType by remember { mutableStateOf(existing?.deviceType ?: "") }
    var fault by remember { mutableStateOf(existing?.faultDescription ?: "") }
    var parts by remember { mutableStateOf(existing?.sparePartsNotes ?: "") }
    var price by remember { mutableStateOf(existing?.priceEstimateDzd?.toString() ?: "") }
    var deposit by remember { mutableStateOf(existing?.depositDzd?.toString() ?: "") }
    var status by remember { mutableStateOf(existing?.ticketStatus ?: TicketStatus.RECU) }
    var expanded by remember { mutableStateOf(false) }
    var errors by remember { mutableStateOf(emptyList<String>()) }

    AtelierHeader(
        title = stringResource(if (existing == null) R.string.edit_new else R.string.edit_modify),
        navigateUp = onBack,
        backLabel = stringResource(R.string.action_back)
    )
    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(clientName, { clientName = it }, label = { Text(stringResource(R.string.edit_client_name)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(clientPhone, { clientPhone = it }, label = { Text(stringResource(R.string.edit_phone)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(brand, { brand = it }, label = { Text(stringResource(R.string.edit_brand)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(model, { model = it }, label = { Text(stringResource(R.string.edit_model)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(deviceType, { deviceType = it }, label = { Text(stringResource(R.string.edit_type)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(fault, { fault = it }, label = { Text(stringResource(R.string.edit_fault)) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(parts, { parts = it }, label = { Text(stringResource(R.string.edit_parts)) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(price, { price = it.filter { c -> c.isDigit() } }, label = { Text(stringResource(R.string.edit_price)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(deposit, { deposit = it.filter { c -> c.isDigit() } }, label = { Text(stringResource(R.string.edit_deposit)) }, modifier = Modifier.fillMaxWidth())
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = status.ofLabel(),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.field_status)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                TicketStatus.entries.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.ofLabel()) },
                        onClick = { status = s; expanded = false }
                    )
                }
            }
        }
        if (errors.isNotEmpty()) {
            Text(
                stringResource(R.string.edit_missing),
                color = MaterialTheme.colorScheme.error
            )
        }
        Button(
            onClick = {
                val p = PriceFormatter.parseDzd(price)
                val d = PriceFormatter.parseDzd(deposit)
                errors = TicketValidator.validate(clientName, brand, model, fault, p, d)
                if (errors.isEmpty()) {
                    onSave(
                        (existing ?: Ticket(clientName = "", clientPhone = "", deviceBrand = "", deviceModel = "", faultDescription = "")).copy(
                            clientName = clientName.trim(),
                            clientPhone = clientPhone.trim(),
                            deviceBrand = brand.trim(),
                            deviceModel = model.trim(),
                            deviceType = deviceType.trim(),
                            faultDescription = fault.trim(),
                            sparePartsNotes = parts.trim(),
                            priceEstimateDzd = p,
                            depositDzd = d,
                            status = status.key
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(R.string.edit_save)) }
    }
}
