package com.example.routines.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Query("SELECT * FROM reminders")
    fun getAllReminderItems(): Flow<List<Item>>

    @Query("SELECT * FROM reminders WHERE activRem == 1")
    fun getActivReminderItems(): Flow<List<Item>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    fun getReminderById(id: Int): Item?

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertItemScenario(item: ItemScenario)
//
//    @Update
//    fun updateItemScenario(item: ItemScenario)

//    @Query("SELECT * FROM scenario")
//    fun getAllScenarioItems(): Flow<List<ItemScenario>>
//
//    @Query("SELECT * FROM scenario WHERE activScen == 1")
//    fun getActivScenarioItems(): Flow<List<ItemScenario>>
//
//    @Query("SELECT * FROM scenario WHERE id = :id")
//    fun getScenarioById(id: Int): ItemScenario?

}
