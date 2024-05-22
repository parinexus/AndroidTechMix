package android.tech.mix.domain.extentions

import java.text.SimpleDateFormat
import java.util.*

const val DAILY_FORECAST_DATE_FORMAT_PATTERN = "E, MMM, d"
const val HOURLY_FORECAST_24_HOUR_TIME_FORMAT_PATTERN = "HH:mm"
const val HOURLY_FORECAST_12_HOUR_TIME_FORMAT_PATTERN = "hh:mm a"

/**
 * Converts a Unix timestamp to a 12-hour formatted time string.
 */
fun Long.to12HourTime(): String {
    return SimpleDateFormat(
        HOURLY_FORECAST_12_HOUR_TIME_FORMAT_PATTERN,
        Locale.getDefault()
    ).format(Date(this * 1000L))
}

/**
 * Converts a Unix timestamp to a 24-hour formatted time string.
 */
fun Long.to24HourTime(): String {
    return SimpleDateFormat(
        HOURLY_FORECAST_24_HOUR_TIME_FORMAT_PATTERN,
        Locale.getDefault()
    ).format(Date(this * 1000L))
}

/**
 * Converts a Unix timestamp to a formatted date string.
 */
fun Long.toFormattedDate(): String {
    return SimpleDateFormat(
        DAILY_FORECAST_DATE_FORMAT_PATTERN,
        Locale.getDefault()
    ).format(Date(this * 1000L))
}
