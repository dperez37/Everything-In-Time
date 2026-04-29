package com.everything.time.ui.screen.viewmodel

import com.everything.time.data.manager.KotlinTimeManager
import com.everything.time.data.model.DateCategory
import com.everything.time.data.util.groupDates
import com.everything.time.data.util.toStandardFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

@HiltViewModel
class KotlinViewModel @Inject constructor(
    private val kotlinTimeManager: KotlinTimeManager,
) : BaseViewModel<KotlinState, Unit, Unit>(
    state = run {
        val timeZone = kotlinTimeManager.systemTimeZone
        KotlinState(
            times = (0L..50L)
                .map { kotlinTimeManager.nowInstant.minus(it.days) }
                .groupDates(
                    timeZone = timeZone,
                    today = kotlinTimeManager.nowDate,
                )
                .mapValues { (_, instants) ->
                    instants.map { it.toStandardFormat(timeZone = timeZone) }
                }
        )
    },
) {
    override fun handleAction(action: Unit) = Unit
}

data class KotlinState(
    val times: Map<DateCategory, List<String>>,
)
