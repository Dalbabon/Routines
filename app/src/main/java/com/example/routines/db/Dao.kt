package com.example.routines.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Query("SELECT * FROM reminders")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM reminders WHERE activRem == 1")
    fun getFavoriteItems(): Flow<List<Item>>


}