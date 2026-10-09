package dz.ateliertrack

import dz.ateliertrack.data.Ticket
import dz.ateliertrack.util.TicketValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TicketValidatorTest {
    private fun ticket(
        name: String = "Karim",
        brand: String = "Samsung",
        model: String = "A12",
        fault: String = "Ecran casse",
        price: Long = 5000,
        deposit: Long = 1000
    ) = Ticket(
        clientName = name, clientPhone = "0550123456",
        deviceBrand = brand, deviceModel = model, faultDescription = fault,
        priceEstimateDzd = price, depositDzd = deposit
    )

    @Test
    fun `complete ticket is valid`() {
        assertTrue(TicketValidator.isValid(ticket()))
    }

    @Test
    fun `blank client name is invalid`() {
        assertFalse(TicketValidator.isValid(ticket(name = "  ")))
    }

    @Test
    fun `ticket without device and without fault is invalid`() {
        assertFalse(TicketValidator.isValid(ticket(brand = "", model = "", fault = "")))
    }

    @Test
    fun `deposit above price is invalid`() {
        assertFalse(TicketValidator.isValid(ticket(price = 1000, deposit = 2000)))
    }

    @Test
    fun `negative price is invalid`() {
        assertFalse(TicketValidator.isValid(ticket(price = -1)))
    }

    @Test
    fun `balance never negative`() {
        assertTrue(ticket(price = 1000, deposit = 9999).balanceDzd >= 0)
    }

    @Test
    fun `search matches client device fault case-insensitively`() {
        val t = ticket(name = "Amine Benali", brand = "Xiaomi", fault = "Batterie gonflee")
        assertTrue(TicketValidator.matches(t, "amine"))
        assertTrue(TicketValidator.matches(t, "XIAOMI"))
        assertTrue(TicketValidator.matches(t, "batterie"))
        assertFalse(TicketValidator.matches(t, "iphone"))
        assertTrue(TicketValidator.matches(t, ""))
    }
}
