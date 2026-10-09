package dz.ateliertrack

import dz.ateliertrack.data.TicketStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class TicketStatusTest {
    @Test
    fun `fromKey round-trips all statuses`() {
        TicketStatus.entries.forEach { assertEquals(it, TicketStatus.fromKey(it.key)) }
    }

    @Test
    fun `unknown key falls back to RECU`() {
        assertEquals(TicketStatus.RECU, TicketStatus.fromKey("bogus"))
    }

    @Test
    fun `status keys are stable`() {
        assertEquals("recu", TicketStatus.RECU.key)
        assertEquals("en_cours", TicketStatus.EN_COURS.key)
        assertEquals("pret", TicketStatus.PRET.key)
        assertEquals("livre", TicketStatus.LIVRE.key)
    }
}
