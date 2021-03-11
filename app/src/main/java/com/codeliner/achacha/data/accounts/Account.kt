package com.codeliner.achacha.data.accounts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codeliner.achacha.utils.Const.HINT
import com.codeliner.achacha.utils.Const.PASSWORD
import com.codeliner.achacha.utils.Const.SUBTITLE
import com.codeliner.achacha.utils.Const.TITLE
import com.codeliner.achacha.utils.Const.USERNAME

@Entity(tableName = "accounts_table")
data class Account (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "created")
    var created: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "subtitle")
    var subtitle: String? = null,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "password")
    var password: String? = null,

    @ColumnInfo(name = "hint")
    var hint: String? = null,

    @ColumnInfo(name = "thumbnail")
    var thumbnail: String? = null
) {
    fun isValid(): Pair<Boolean, String?> {
        var values = Pair<Boolean, String?>(true, null)

        when {
            title.isNullOrEmpty() -> {
                values = values.copy(first = false, second = TITLE)
            }

            subtitle.isNullOrEmpty() -> {
                values = values.copy(first = false, second = SUBTITLE)
            }

            username.isNullOrEmpty() -> {
                values = values.copy(first = false, second = USERNAME)
            }

            password.isNullOrEmpty() -> {
                values = values.copy(first = false, second = PASSWORD)
            }

            hint.isNullOrEmpty() -> {
                values = values.copy(first = false, second = HINT)
            }
        }
        return values
    }
}