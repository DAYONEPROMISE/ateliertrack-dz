package dz.ateliertrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dz.ateliertrack.data.Ticket
import dz.ateliertrack.data.TicketStatus
import dz.ateliertrack.util.PdfReceipt
import dz.ateliertrack.util.PriceFormatter
import dz.ateliertrack.util.TicketValidator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketEditScreen(
    existing: Ticket?,
    lang: String,
    onBack: () -> Unit,
    onSave: (Ticket) -> Unit
) {
    val fr = lang != "ar"
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

    fun t(f: String, a: String) = if (fr) f else a

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existing == null) t("Nouveau ticket", "تذكرة جديدة") else t("Modifier ticket", "تعديل التذكرة")) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) } }
            )
        }
    ) { pad ->
        Column(
            Modifier.fillMaxSize().padding(pad).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(clientName, { clientName = it }, label = { Text(t("Nom du client *", "اسم الزبون *")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(clientPhone, { clientPhone = it }, label = { Text(t("Telephone", "الهاتف")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(brand, { brand = it }, label = { Text(t("Marque *", "العلامة *")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(model, { model = it }, label = { Text(t("Modele *", "الموديل *")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(deviceType, { deviceType = it }, label = { Text(t("Type (telephone, TV, electromenager)", "النوع (هاتف، تلفاز...)")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(fault, { fault = it }, label = { Text(t("Panne *", "العطل *")) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            OutlinedTextField(parts, { parts = it }, label = { Text(t("Pieces detachees / notes", "قطع الغيار / ملاحظات")) }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            OutlinedTextField(price, { price = it.filter { c -> c.isDigit() } }, label = { Text(t("Prix estime (DA)", "السعر (دج)")) }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(deposit, { deposit = it.filter { c -> c.isDigit() } }, label = { Text(t("Acompte (DA)", "العربون (دج)")) }, modifier = Modifier.fillMaxWidth())
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = PdfReceipt.statusLabel(status, fr),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(t("Statut", "الحالة")) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    TicketStatus.entries.forEach { s ->
                        DropdownMenuItem(
                            text = { Text(PdfReceipt.statusLabel(s, fr)) },
                            onClick = { status = s; expanded = false }
                        )
                    }
                }
            }
            if (errors.isNotEmpty()) {
                Text(t("Champs requis manquants.", "حقول ناقصة."))
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
            ) { Text(t("Enregistrer", "حفظ")) }
        }
    }
}
