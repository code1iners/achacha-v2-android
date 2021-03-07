package com.codeliner.achacha.utils

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.codeliner.achacha.R
import com.codeliner.achacha.domains.todos.Todo
import com.example.helpers.CreatedParser
import com.example.helpers.ui.getMoveInRight
import com.example.helpers.ui.getMoveOutLeft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

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

@BindingAdapter("setVisible")
fun View.setVisible(status: Boolean) {
    when (status) {
        true -> this.visibility = View.VISIBLE
        false -> this.visibility = View.GONE
    }
}

@BindingAdapter("setAnimation")
fun View.setAnimation(status: Boolean) {
    val defaultDuration = this.resources.getInteger(R.integer.default_animation_duration)
    when (status) {
        true -> {
            this.startAnimation(this.context.getMoveInRight().apply {
                duration = defaultDuration.toLong()
            })
        }
        false -> {
            this.startAnimation(this.context.getMoveOutLeft().apply {
                duration = defaultDuration.toLong()
            })
        }
    }
    this.setVisible(status)
}
