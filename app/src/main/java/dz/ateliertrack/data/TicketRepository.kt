package dz.ateliertrack.data

import kotlinx.coroutines.flow.Flow

/** Single source of truth over the local Room database. No network: 100% offline. */
class TicketRepository(private val dao: TicketDao) {
    fun observeAll(): Flow<List<Ticket>> = dao.observeAll()
    fun observeById(id: Long): Flow<Ticket?> = dao.observeById(id)
    fun search(query: String): Flow<List<Ticket>> =
        if (query.isBlank()) dao.observeAll() else dao.search(query.trim())

    fun observeByStatus(status: TicketStatus): Flow<List<Ticket>> =
        dao.observeByStatus(status.key)

    suspend fun countByStatus(status: TicketStatus): Int = dao.countByStatus(status.key)

    suspend fun save(ticket: Ticket): Long =
        if (ticket.id == 0L) dao.insert(ticket)
        else {
            dao.update(ticket.copy(updatedAt = System.currentTimeMillis()))
            ticket.id
        }

    suspend fun advanceStatus(ticket: Ticket): Ticket {
        val next = when (ticket.ticketStatus) {
            TicketStatus.RECU -> TicketStatus.EN_COURS
            TicketStatus.EN_COURS -> TicketStatus.PRET
            TicketStatus.PRET -> TicketStatus.LIVRE
            TicketStatus.LIVRE -> TicketStatus.LIVRE
        }
        val updated = ticket.copy(status = next.key, updatedAt = System.currentTimeMillis())
        dao.update(updated)
        return updated
    }

    suspend fun delete(ticket: Ticket) = dao.delete(ticket)
    suspend fun getById(id: Long): Ticket? = dao.getById(id)
}
