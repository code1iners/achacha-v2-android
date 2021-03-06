package com.codeliner.achacha.utils

import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.codeliner.achacha.R
import com.codeliner.achacha.domains.todos.Todo
import com.example.helpers.CreatedParser
import timber.log.Timber

@BindingAdapter("todoWorkString")
fun TextView.setTodoWork(item: Todo?) {
    item?.let {
        text = item.work
    }
}

@BindingAdapter("todoHelpString")
fun TextView.setTodoHelp(item: Todo?) {
    item?.let {
        text = item.help
    }
}

@BindingAdapter("todoIsFinished")
fun ImageButton.setIsFinished(item: Todo?) {
    item?.let {
        when (it.isFinished) {
            true -> setImageResource(R.drawable.icon_radio_button_checked)
            false -> setImageResource(R.drawable.icon_radio_button_unchecked)
        }
    }
}

@BindingAdapter("todoCreatedFormatted")
fun TextView.setTodoCreated(item: Todo?) {
    item?.let {
        val date = CustomDate().apply {
            currentTimeAsMilli = item.created
        }
        val newFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val formattedDate = date.getYourType(newFormat)

        // note. normal format
//        text = "${date.getYearWith(DateType.YearFourDigit)}-${date.getMonthWith(DateType.MonthNumTwoDigit)}-${date.getDayWith(DateType.DayTwoDigit)}"
        
        // note. new format
        text = CreatedParser().parse(formattedDate)
    }
}

@BindingAdapter("todoIsFinishedString")
fun TextView.setTodoIsFinishedAsString(item: Todo?) {
    item?.let {
        text = it.isFinished.toString()
    }
}