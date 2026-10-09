package dz.ateliertrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Repair-ticket workflow status. Forward-only in normal shop practice:recu -> en cours -> pret -> livre. */
enum class TicketStatus(val key: String) {
    RECU("recu"),
    EN_COURS("en_cours"),
    PRET("pret"),
    LIVRE("livre");

    companion object {
        fun fromKey(key: String): TicketStatus =
            entries.firstOrNull { it.key == key } ?: RECU
    }
}

/**
 * One repair ticket: client + device + fault + status + spare-parts notes.
 * Strictly repair-ticket workflow (not a generic CRM).
 */
@Entity(tableName = "tickets")
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String,
    val clientPhone: String,
    val deviceBrand: String,
    val deviceModel: String,
    val deviceType: String = "", // e.g. "Telephone", "Televiseur", "Machine a laver"
    val faultDescription: String,
    val sparePartsNotes: String = "",
    val priceEstimateDzd: Long = 0, // Algerian dinars, integer minor unit
    val depositDzd: Long = 0,
    val status: String = TicketStatus.RECU.key,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val ticketStatus: TicketStatus get() = TicketStatus.fromKey(status)
    val balanceDzd: Long get() = (priceEstimateDzd - depositDzd).coerceAtLeast(0)
}
