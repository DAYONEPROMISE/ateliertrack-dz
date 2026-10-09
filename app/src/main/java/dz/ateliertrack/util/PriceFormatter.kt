package dz.ateliertrack.util

import java.text.NumberFormat
import java.util.Locale

/** Formats Algerian-dinar amounts. JVM-testable. */
object PriceFormatter {
    fun formatDzd(amount: Long, locale: Locale = Locale.FRENCH): String {
        val nf = NumberFormat.getNumberInstance(locale)
        return "${nf.format(amount)} DA"
    }

    fun parseDzd(raw: String): Long =
        raw.filter { it.isDigit() }.toLongOrNull() ?: 0L
}
