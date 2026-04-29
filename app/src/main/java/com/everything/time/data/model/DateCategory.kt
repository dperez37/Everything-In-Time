package com.everything.time.data.model

enum class DateCategory(val priority: Int) {
    FUTURE(priority = -2),
    NEXT_WEEK(priority = -1),
    TODAY(priority = 0),
    THIS_WEEK(priority = 1),
    LAST_WEEK(priority = 2),
    THIS_MONTH(priority = 3),
    OLD(priority = 4),
}
