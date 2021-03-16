package com.codeliner.achacha.ui.todos.list

import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.codeliner.achacha.R
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.utils.CustomDate
import com.codeliner.achacha.utils.DateType
import com.example.helpers.CreatedParser


@BindingAdapter("todoWorkString")
fun TextView.setTodoWork(item: Todo?) {
    item?.let {
        text = item.work
    }
}

@BindingAdapter("todoHelpString")
fun TextView.setTodoHelp(item: Todo?) {
    item?.let {
        text = item.tags
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

@BindingAdapter("todoMemo")
fun TextView.setTodoMemo(item: Todo?) {
    item?.let {
        text = it.memo
    }
}

@BindingAdapter("todoCreatedNormally")
fun TextView.setTodoCreatedNormally(item: Todo?) {
    item?.let {
        val date = CustomDate().apply {
            currentTimeAsMilli = item.created
        }
        text = "${date.getYearWith(DateType.YearFourDigit)}-${date.getMonthWith(DateType.MonthNumTwoDigit)}-${date.getDayWith(DateType.DayTwoDigit)}"
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

        text = when (it.isFinished) {
            true -> this.context.getString(R.string.completed)
            false -> this.context.getString(R.string.working)
        }
    }
}

@BindingAdapter("todoSetTextDarkWhenFinished")
fun TextView.setTextDarkWhenFinished(item: Todo?) {
    item?.let {
        when (it.isFinished) {
            true -> this.alpha = 0.4.toFloat()
            false -> this.alpha = 1.0.toFloat()
        }
    }
}