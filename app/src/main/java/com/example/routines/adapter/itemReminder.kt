package com.example.routines.adapter

import java.io.Serializable

data class ItemReminder(val id: Int? = null, val tagRem: String, val descriptionRem: String, val  dateRem: String, val timeRem: String, val activRem: Int) :
    Serializable
