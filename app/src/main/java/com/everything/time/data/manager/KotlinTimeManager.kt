package com.everything.time.data.manager

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.time.Instant

interface KotlinTimeManager {
    val systemTimeZone: TimeZone
    val nowInstant: Instant
    val nowDate: LocalDate
    val nowTime: LocalTime
    val nowDateTime: LocalDateTime
}
