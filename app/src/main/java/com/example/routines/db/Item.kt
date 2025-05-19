package com.example.routines.db


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Item (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "tagRem")
    var tagRem: String,
    @ColumnInfo(name = "descriptionRem")
    var descriptionRem: String,
    @ColumnInfo(name = "dateRem")
    var  dateRem: String,
    @ColumnInfo(name = "timeRem")
    var timeRem: String,
    @ColumnInfo(name = "activRem")
    var activRem: Int,


    )