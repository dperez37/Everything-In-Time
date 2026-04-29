package com.everything.time.data.util

import com.everything.time.data.model.DateCategory
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Formats the [Instant] to a specific string pattern using the [timeZone] provided.
 */
fun Instant.toStandardFormat(
    timeZone: TimeZone,
): String = LocalDateTime
    .Format {
        monthName(names = MonthNames.ENGLISH_ABBREVIATED)
        char(value = ' ')
        day(padding = Padding.NONE)
        chars(value = ", ")
        year()
        chars(value = ", ")
        amPmHour(padding = Padding.NONE)
        char(value = ':')
        minute()
        char(value = ' ')
        amPmMarker(am = "AM", pm = "PM")
    }
    .format(value = this.toLocalDateTime(timeZone = timeZone))

/**
 * Filters out all [Instant]s that are not the [date] based on the provided [timeZone].
 */
fun List<Instant>.filterDate(
    timeZone: TimeZone,
    date: LocalDate,
): List<Instant> = this.filter { it.toLocalDateTime(timeZone = timeZone).date == date }

/**
 * Filters out all [Instant]s that are not in the [month] and [year] based on the provided
 * [timeZone].
 */
fun List<Instant>.filterMonth(
    timeZone: TimeZone,
    year: Int,
    month: Int,
): List<Instant> = this.filter {
    val localDate = it.toLocalDateTime(timeZone).date
    localDate.year == year && localDate.month.number == month
}

/**
 * Filters out all [Instant]s that are not in the [year] based on the provided [timeZone].
 */
fun List<Instant>.filterYear(
    timeZone: TimeZone,
    year: Int,
): List<Instant> = this.filter { it.toLocalDateTime(timeZone).year == year }

/**
 * Groups the [Instant]s by the [DateCategory] based on the provided [zone] and [today].
 */
fun List<Instant>.groupDates(
    timeZone: TimeZone,
    today: LocalDate,
): Map<DateCategory, List<Instant>> {
    val startOfWeek = today.minus(
        value = today.dayOfWeek.isoDayNumber - 1,
        unit = DateTimeUnit.DAY
    )
    val endOfWeek = startOfWeek.plus(value = 6, unit = DateTimeUnit.DAY)
    return this
        .groupBy {
            val date = it.toLocalDateTime(timeZone = timeZone).date
            when {
                date == today -> DateCategory.TODAY
                date in startOfWeek..endOfWeek -> DateCategory.THIS_WEEK
                date in endOfWeek..endOfWeek.plus(7, DateTimeUnit.DAY) -> {
                    DateCategory.NEXT_WEEK
                }

                date in startOfWeek.minus(7, DateTimeUnit.DAY)..startOfWeek -> {
                    DateCategory.LAST_WEEK
                }

                date > endOfWeek.plus(7, DateTimeUnit.DAY) -> DateCategory.FUTURE
                date.year == today.year && date.month == today.month -> DateCategory.THIS_MONTH
                else -> DateCategory.OLD
            }
        }
        .toSortedMap { dates, dates1 -> dates.priority.compareTo(dates1.priority) }
}
