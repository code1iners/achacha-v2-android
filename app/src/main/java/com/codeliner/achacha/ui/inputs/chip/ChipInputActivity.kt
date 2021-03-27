package com.codeliner.achacha.ui.inputs.chip

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityChipInputBinding
import com.codeliner.achacha.utils.Const.INPUT
import com.codeliner.achacha.utils.Const.TAGS
import com.codeliner.achacha.utils.Const.TITLE
import com.example.helpers.MeasureManager.toDp
import com.example.helpers.WidgetManager.LayoutParamsManager.Companion.setMarginHorizontal
import com.example.helpers.WidgetManager.LayoutParamsManager.Companion.setMarginVertical
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ChipInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChipInputBinding
    private val viewModel: ChipInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        observers()
    }

    private fun initialize() {
        initializeBinding()
        initializePassedData()
    }

    private fun initializeBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chip_input)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initializePassedData() {
        intent.extras?.let {
            // note. Set passed title.
            it.getString(TITLE)?.let { title ->
                viewModel.setTitle(title)
            }

            // note. Set passed tags.
            it.getStringArrayList(TAGS)?.let { tags ->
                viewModel.setTags(tags)
            }
        }
    }

    private fun observers() {
        observeTitle()
        observeTags()
        observeSave()
    }

    private fun observeTitle() {
        viewModel.onTitle.observe(this) {
            it?.let { title ->
                binding.headerTitle.text = title
            }
        }
    }

    private fun observeTags() {
        // note. Observe tags object 1
        whenPassedTagsDataExist()
        // note. Observe tags object 2
        whenUserSelectedTagItem()
    }

    private fun whenPassedTagsDataExist() {
        viewModel.tags.observe(this) {
            it?.let { tags ->
                for (item in tags) {
                    // note. Add tag items with attributes..
                    val itemView = TextView(this)
                    itemView.text = item
                    itemView.setMarginVertical(2.toDp(this))
                    itemView.setPadding(8.toDp(this), 2.toDp(this), 8.toDp(this), 2.toDp(this))
                    itemView.isClickable = true
                    itemView.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))
                    itemView.setBackgroundResource(R.drawable.item_touch_effect_rounded_m)
                    itemView.setOnClickListener {
                        // note. Add or Remove in list When clicked chip item.
                        viewModel.tagAddOrRemove(it as TextView)
                    }
                    // note. Added child chip item in flex box.
                    binding.bodyFlexBox.addView(itemView)

                    // note. Add spacing between tag items.
                    val space = TextView(this)
                    space.setMarginHorizontal(1.toDp(this))
                    binding.bodyFlexBox.addView(space)
                }
            }
        }
    }

    private fun whenUserSelectedTagItem() {
        viewModel.onSelectedTags.observe(this) {
            it?.let { tags ->
                retrieveSelectedItems(tags)
                updateSelectedItemUI()
            }
        }
    }

    private fun retrieveSelectedItems(tags: ArrayList<TextView>) {
        binding.bodyFlexBox.forEachIndexed { index, flexView ->
            // note. Convert view type.
            val flexItem = flexView as TextView

            // note. Check item is tag or spacer.
            if (!flexItem.text.isNullOrEmpty()) {
                // note. Clear all tags text style by default.
                flexItem.setTypeface(null, Typeface.NORMAL)
                flexItem.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor))

                // note. Add selected item position.
                if (tags.contains(flexItem)) viewModel.selectItem(index)
            }
        }
    }

    private fun updateSelectedItemUI() {
        for (selectedItem in viewModel.selectedItems) {
            val flexItem = binding.bodyFlexBox[selectedItem] as TextView
            flexItem.setTypeface(null, Typeface.BOLD)
            flexItem.setTextColor(ContextCompat.getColor(this, R.color.sexyBlue2))
        }
    }

    // note. In progress.
    private fun observeSave() {
        viewModel.save.observe(this) {
            it?.let { started ->
                Timber.w("started: $started")
                if (started) {
                    // note. In progress.
                    viewModel.resultsTags.value?.let { results ->
                        for (result in results) {
                            Timber.d("result: $result")
                        }

//                    setResult(RESULT_OK, Intent().apply {
//                        putExtra(TAGS, viewModel.sel)
//                    })
                    }

                    viewModel.saveComplete()
                }
            }
        }
    }
}