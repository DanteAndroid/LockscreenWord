package com.danteandroi.lockscreenword.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * @author Du Wenyu
 * 2022/2/13
 */
@Entity(
    tableName = "46ji",
    primaryKeys = ["word"]
//    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["user_id"], childColumns = ["userId"])],
)
@Parcelize
data class Word(
    val word: String,
    val meaning: String
) : Parcelable {

    @IgnoredOnParcel
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false
    @IgnoredOnParcel
    @ColumnInfo(name = "deleted")
    var deleted: Boolean = false

}