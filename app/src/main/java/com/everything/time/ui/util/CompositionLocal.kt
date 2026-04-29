package com.everything.time.ui.util

import androidx.compose.runtime.compositionLocalOf

typealias JavaClock = java.time.Clock

typealias KotlinClock = kotlin.time.Clock

val LocalJavaClock = compositionLocalOf { JavaClock.systemDefaultZone() }

val LocalKotlinClock = compositionLocalOf { kotlin.time.Clock.System }
