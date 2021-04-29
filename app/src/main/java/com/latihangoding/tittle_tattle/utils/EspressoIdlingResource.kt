package com.latihangoding.tittle_tattle.utils

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource

//Objek Espresso Idling Resource
object EspressoIdlingResource {
    private val RESOURCE = "GLOBAL"
    private val countingIdlingResource = CountingIdlingResource(RESOURCE)

//    mendapatkan total objek / view yang memiliki idling resource
    val idlingresource: IdlingResource
        get() = countingIdlingResource

//    melakukan increment pada perhitungan idling resouce
    fun increment() {
        countingIdlingResource.increment()
    }

//    melakukan decrement pada perhitungan idling resource
    fun decrement() {
        countingIdlingResource.decrement()
    }
}