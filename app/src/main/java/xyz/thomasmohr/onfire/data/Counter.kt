package xyz.thomasmohr.onfire.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "counters")
data class Counter(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String = "",
    val position: Long = 0,
    val count: Long = 0
)