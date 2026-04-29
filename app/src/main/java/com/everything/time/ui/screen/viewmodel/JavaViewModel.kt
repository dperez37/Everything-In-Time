package com.everything.time.ui.screen.viewmodel

import com.everything.time.data.model.DateCategory
import com.everything.time.data.util.groupDates
import com.everything.time.data.util.toDateTimeFormat
import com.everything.time.ui.util.JavaClock
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class JavaViewModel @Inject constructor(
    private val javaClock: JavaClock,
) : BaseViewModel<JavaState, Unit, Unit>(
    state = JavaState(
        times = (0L..50L)
            .map { javaClock.instant().minus(it, ChronoUnit.DAYS) }
            .groupDates(
                zone = javaClock.zone,
                today = javaClock.instant().atZone(javaClock.zone).toLocalDate(),
            )
            .mapValues { (_, instants) ->
                instants.map {
                    it.toDateTimeFormat(
                        dateFormatStyle = FormatStyle.MEDIUM,
                        timeFormatStyle = FormatStyle.SHORT,
                        zone = javaClock.zone,
                        locale = Locale.getDefault(),
                    )
                }
            }
    ),
) {
    override fun handleAction(action: Unit) = Unit
}

data class JavaState(
    val times: Map<DateCategory, List<String>>,
)
