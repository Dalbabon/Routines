package com.example.routines.adapter

import java.io.Serializable

data class ItemScenario(val id: Int? = null, val tagScen: String, val ifScen: String, val  elseScen: String, val activScen: Int) :
    Serializable
