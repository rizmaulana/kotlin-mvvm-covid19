package id.rizmaulana.covid19.util

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.NumberFormat
import java.util.*

/**
 * rizmaulana 04/03/20.
 */
object NumberUtils {

    fun numberFormat(number: Int?) = number?.let {
        NumberFormat.getNumberInstance(Locale.getDefault()).format(number)
    } ?: "-"

    fun extractDigit(number: String) = Regex("[^0-9]").replace(number, "").toInt()

    //TODO: Move to another class for date formatter
    fun formatTime(time: Long): String {
        val instant = Instant.ofEpochMilli(time)
        val local = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        return local.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    }


    fun formatShortDate(time: Long): String {
        val instant = Instant.ofEpochMilli(time)
        val local = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        return local.format(DateTimeFormatter.ofPattern("dd MMM"))
    }

    fun formatStringDate(time: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val local = LocalDate.parse(time, formatter)
        return local.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
    }


}