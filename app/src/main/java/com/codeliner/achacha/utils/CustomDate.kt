package com.codeliner.achacha.utils

import java.text.SimpleDateFormat

enum class DateType {
    YearTwoDigit, YearFourDigit,
    MonthNumOneDigit, MonthNumTwoDigit, MonthCharShort, MonthCharLong,
    DayOneDigit, DayTwoDigit
}

class CustomDate {
    
    private val form = SimpleDateFormat()
    var currentTimeAsMilli = System.currentTimeMillis()
    
    fun getYourType(type: String): String {
        try {
            form.applyPattern(type)
        } catch (e: Exception) {
            throw IllegalArgumentException("This type cannot be converted.")
        } finally {
            return form.format(currentTimeAsMilli)
        }
    }
    
    fun getYearWith(type: DateType): String {
        when (type) {
            DateType.YearTwoDigit -> {
                form.applyPattern("yy")
            }

            DateType.YearFourDigit -> {
                form.applyPattern("yyyy")
            }
            
            else -> throw IllegalArgumentException("This type cannot be converted.")
        }
        return form.format(currentTimeAsMilli)
    }

    fun getMonthWith(type: DateType): String {
        when (type) {
            DateType.MonthNumOneDigit -> {
                form.applyPattern("M")
            }

            DateType.MonthNumTwoDigit -> {
                form.applyPattern("MM")
            }

            DateType.MonthCharShort -> {
                form.applyPattern("MMM")
            }

            DateType.MonthCharLong -> {
                form.applyPattern("MMMM")
            }

            else -> throw IllegalArgumentException("This type cannot be converted.")
        }
        return form.format(currentTimeAsMilli)
    }

    fun getDayWith(type: DateType): String {
        when (type) {
            DateType.DayOneDigit -> {
                form.applyPattern("d")
            }

            DateType.DayTwoDigit -> {
                form.applyPattern("dd")
            }

            else -> throw IllegalArgumentException("This type cannot be converted.")
        }
        return form.format(currentTimeAsMilli)
    }
}