package dz.ateliertrack.util

import dz.ateliertrack.data.Ticket

/** Pure validation logic for repair tickets. JVM-testable, no Android dependency. */
object TicketValidator {
    fun validate(
        clientName: String,
        deviceBrand: String,
        deviceModel: String,
        faultDescription: String,
        priceEstimateDzd: Long,
        depositDzd: Long
    ): List<String> {
        val errors = mutableListOf<String>()
        if (clientName.isBlank()) errors += "client_name_required"
        if (deviceBrand.isBlank() && deviceModel.isBlank()) errors += "device_required"
        if (faultDescription.isBlank()) errors += "fault_required"
        if (priceEstimateDzd < 0) errors += "price_negative"
        if (depositDzd < 0) errors += "deposit_negative"
        if (depositDzd > priceEstimateDzd && priceEstimateDzd > 0) errors += "deposit_exceeds_price"
        return errors
    }

    fun isValid(ticket: Ticket): Boolean = validate(
        ticket.clientName,
        ticket.deviceBrand,
        ticket.deviceModel,
        ticket.faultDescription,
        ticket.priceEstimateDzd,
        ticket.depositDzd
    ).isEmpty()

    /** Case-insensitive multi-field match used by search (mirrors the DAO query). */
    fun matches(ticket: Ticket, query: String): Boolean {
        val q = query.trim().lowercase()
        if (q.isBlank()) return true
        return listOf(
            ticket.clientName, ticket.clientPhone, ticket.deviceBrand,
            ticket.deviceModel, ticket.faultDescription
        ).any { it.lowercase().contains(q) }
    }
}
