package com.everything.time.data.util

import com.everything.time.data.model.DateCategory
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjusters
import java.util.Locale

/**
 * Creates a formatted string representing the [TemporalAccessor] based on the given
 * [patternFormat] and [zone].
 */
fun TemporalAccessor.toPatternFormat(
    patternFormat: String,
    zone: ZoneId,
): String = DateTimeFormatter
    .ofPattern(patternFormat)
    .withZone(zone)
    .format(this)

/**
 * Creates a formatted string representing the [TemporalAccessor] based on the given
 * [dateFormatStyle], [zone], and [locale].
 */
fun TemporalAccessor.toDateFormat(
    dateFormatStyle: FormatStyle,
    zone: ZoneId,
    locale: Locale = Locale.getDefault(),
): String = this.toPatternFormat(
    patternFormat = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        dateFormatStyle,
        null,
        IsoChronology.INSTANCE,
        locale,
    ),
    zone = zone,
)

/**
 * Creates a formatted string representing the [TemporalAccessor] based on the given
 * [timeFormatStyle], [zone], and [locale].
 */
fun TemporalAccessor.toTimeFormat(
    timeFormatStyle: FormatStyle,
    zone: ZoneId,
    locale: Locale = Locale.getDefault(),
): String = this.toPatternFormat(
    patternFormat = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        null,
        timeFormatStyle,
        IsoChronology.INSTANCE,
        locale,
    ),
    zone = zone,
)

/**
 * Creates a formatted string representing the [TemporalAccessor] based on the given
 * [dateFormatStyle], [timeFormatStyle], [zone], and [locale].
 */
fun TemporalAccessor.toDateTimeFormat(
    dateFormatStyle: FormatStyle,
    timeFormatStyle: FormatStyle,
    zone: ZoneId,
    locale: Locale = Locale.getDefault(),
): String = this.toPatternFormat(
    patternFormat = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        dateFormatStyle,
        timeFormatStyle,
        IsoChronology.INSTANCE,
        locale,
    ),
    zone = zone,
)

/**
 * Filters out all [Instant]s that are not the [date] based on the provided [zone].
 */
fun List<Instant>.filterDate(
    zone: ZoneId,
    date: LocalDate,
): List<Instant> = this.filter { it.atZone(zone).toLocalDate() == date }

/**
 * Filters out all [Instant]s that are not during the week associated with the [date] based on the
 * provided [zone].
 */
fun List<Instant>.filterWeek(
    zone: ZoneId,
    date: LocalDate,
): List<Instant> {
    val startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    return this.filter {
        val localDate = it.atZone(zone).toLocalDate()
        (localDate.isAfter(startOfWeek) || localDate.isEqual(startOfWeek)) &&
                (localDate.isBefore(endOfWeek) || localDate.isEqual(endOfWeek))
    }
}

/**
 * Filters out all [Instant]s that are not in the [month] and [year] based on the provided [zone].
 */
fun List<Instant>.filterMonth(
    zone: ZoneId,
    year: Int,
    month: Int,
): List<Instant> = this.filter {
    val localDate = it.atZone(zone).toLocalDate()
    localDate.year == year && localDate.monthValue == month
}

/**
 * Filters out all [Instant]s that are not in the [year] based on the provided [zone].
 */
fun List<Instant>.filterYear(
    zone: ZoneId,
    year: Int,
): List<Instant> = this.filter { it.atZone(zone).toLocalDate().year == year }

/**
 * Groups the [Instant]s by the [DateCategory] based on the provided [zone] and [today].
 */
fun List<Instant>.groupDates(
    zone: ZoneId,
    today: LocalDate,
): Map<DateCategory, List<Instant>> {
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    return this
        .groupBy {
            val date = it.atZone(zone).toLocalDate()
            when {
                date == today -> DateCategory.TODAY
                date in startOfWeek..endOfWeek -> DateCategory.THIS_WEEK
                date in endOfWeek..endOfWeek.plusDays(7) -> DateCategory.NEXT_WEEK
                date in startOfWeek.minusDays(7)..startOfWeek -> DateCategory.LAST_WEEK
                date > endOfWeek.plusDays(7) -> DateCategory.FUTURE
                date.year == today.year && date.month == today.month -> DateCategory.THIS_MONTH
                else -> DateCategory.OLD
            }
        }
        .toSortedMap { dates, dates1 -> dates.priority.compareTo(dates1.priority) }
}
