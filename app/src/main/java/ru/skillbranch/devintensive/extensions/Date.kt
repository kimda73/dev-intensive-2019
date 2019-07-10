package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(
    pattern:String="HH:mm:ss dd.MM.yy"
) : String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(
    value:Int, units: TimeUnits = TimeUnits.SECOND
) : Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR   -> value * HOUR
        TimeUnits.DAY    -> value * DAY
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(
    date: Date = Date()
) : String {
    var newDate = Calendar.getInstance()
    newDate.time = date

    var oldDate = Calendar.getInstance()
    oldDate.time = this

    if (oldDate.before(newDate)) {
        if (oldDate.before(newDate) && oldDate.addField(Calendar.YEAR, 1).addField(Calendar.MILLISECOND, -1).before(newDate)) {
            return "больше года назад"
        }

        var days = ((newDate.timeInMillis - oldDate.timeInMillis) / DAY).toInt()
        if (days > 0) {
            return "${TimeUnits.DAY.plural(days)} назад"
        }

        var hours = ((newDate.timeInMillis - oldDate.timeInMillis) / HOUR).toInt()
        if (hours > 0) {
            return "${TimeUnits.HOUR.plural(hours)} назад"
        }

        var minutes = (((newDate.timeInMillis - oldDate.timeInMillis) / MINUTE)).toInt()
        if (minutes > 0) {
            return "${TimeUnits.MINUTE.plural(minutes)} назад"
        }

        var seconds = (((newDate.timeInMillis - oldDate.timeInMillis) / MINUTE)).toInt()
        if (seconds > 0) {
            return "несколько секунд назад"
        }

        if (seconds == 0) {
            return "только что"
        }
    }

    if (newDate.addField(Calendar.YEAR, 1).addField(Calendar.MILLISECOND, -1).before(oldDate)) {
        return "больше, чем через год"
    }

    var days = ((oldDate.timeInMillis - newDate.timeInMillis) / DAY).toInt()
    if (days > 0) {
        return "через ${TimeUnits.DAY.plural(days)}"
    }

    var hours = ((oldDate.timeInMillis - newDate.timeInMillis) / HOUR).toInt()
    if (hours > 0) {
        return "через ${TimeUnits.HOUR.plural(hours)}"
    }

    var minutes = (((oldDate.timeInMillis - newDate.timeInMillis) / MINUTE)).toInt()
    if (minutes > 0) {
        return "через ${TimeUnits.MINUTE.plural(minutes)}"
    }

    var seconds = (((oldDate.timeInMillis - newDate.timeInMillis) / SECOND)).toInt()
    if (seconds > 0) {
        return "через несколько секунд"
    }

    return "только что"
}

private fun Calendar.addField(
    field: Int, value: Int
) : Calendar {
    val newCalendar = Calendar.getInstance()
    newCalendar.time = this.time
    newCalendar.add(field, value)
    return newCalendar
}

enum class TimeUnits {

    SECOND {
        override fun plural(value : Int) : String {
            return plural(value, arrayOf("секунду", "секунды", "секунд"))
        }
    },

    MINUTE {
        override fun plural(value : Int) : String {
            return plural(value, arrayOf("минуту", "минуты", "минут"))
        }
    },

    HOUR {
        override fun plural(value : Int) : String {
            return plural(value, arrayOf("час", "часа", "часов"))
        }
    },

    DAY {
        override fun plural(value : Int) : String {
            return plural(value, arrayOf("день", "дня", "дней"))
        }
    };

    abstract fun plural(value : Int) : String

}

private fun plural(
    value : Int, variants : Array<String>
) : String {
    return when {
        value % 10 == 1 && (value % 100 < 10 || value % 100 > 20) -> "$value ${variants[0]}"
        value % 10 in 2..4 -> "$value ${variants[1]}"
        else -> "$value ${variants[2]}"
    }
}
