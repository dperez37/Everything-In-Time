package com.everything.time.data.manager

import com.everything.time.ui.util.KotlinClock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class KotlinTimeManagerImpl(
    private val clock: KotlinClock,
) : KotlinTimeManager {
    override val systemTimeZone: TimeZone get() = TimeZone.currentSystemDefault()
    override val nowInstant: Instant get() = clock.now()
    override val nowDate: LocalDate get() = nowDateTime.date
    override val nowTime: LocalTime get() = nowDateTime.time
    override val nowDateTime: LocalDateTime get() = nowInstant.toLocalDateTime(systemTimeZone)
}
