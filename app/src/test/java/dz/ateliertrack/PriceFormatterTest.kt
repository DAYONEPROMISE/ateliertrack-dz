package dz.ateliertrack

import dz.ateliertrack.util.PriceFormatter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class PriceFormatterTest {
    @Test
    fun `formats amount with DA suffix`() {
        val s = PriceFormatter.formatDzd(5000, Locale.FRENCH)
        assertTrueContains(s, "5")
        assertTrueContains(s, "DA")
    }

    @Test
    fun `zero formats without crash`() {
        assertTrueContains(PriceFormatter.formatDzd(0), "DA")
    }

    @Test
    fun `parse strips non-digits`() {
        assertEquals(5000L, PriceFormatter.parseDzd("5 000 DA"))
        assertEquals(0L, PriceFormatter.parseDzd(""))
        assertEquals(0L, PriceFormatter.parseDzd("abc"))
    }

    private fun assertTrueContains(s: String, part: String) {
        org.junit.Assert.assertTrue("'$s' should contain '$part'", s.contains(part))
    }
}
