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

    // Специальные запросы для работы с уведомлениями
    @Query("SELECT * FROM reminders WHERE activRem == 1 AND dateRem = :date AND timeRem = :time")
    suspend fun getRemindersForExactTime(date: String, time: String): List<Item>

    @Query("UPDATE reminders SET activRem = :isActive WHERE id = :id")
    suspend fun updateReminderStatus(id: Int, isActive: Int)

    @Query("SELECT * FROM reminders WHERE dateRem = :todayDate AND activRem = 1 ORDER BY timeRem ASC")
    fun getTodayReminders(todayDate: String): Flow<List<Item>>

    // Операции для отладки и тестирования
    @Query("DELETE FROM reminders")
    suspend fun clearAllReminders()

    @Query("SELECT COUNT(*) FROM reminders WHERE activRem = 1")
    suspend fun getActiveRemindersCount(): Int
}
