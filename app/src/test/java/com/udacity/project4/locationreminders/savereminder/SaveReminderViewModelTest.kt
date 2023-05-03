package com.udacity.project4.locationreminders.savereminder

import androidx.test.ext.junit.runners.AndroidJUnit4

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @Test
    fun onClear_completes_dataIsNull() {

    }

    @Test
    fun propagatePoiData_emptyPoi_emptyLiveData() {

    }

    @Test
    fun propagatePoiData_existingPoi_populatedLiveData() {

    }

    @Test
    fun validateEnteredData_emptyTitle_returnsFalse() {

    }

    @Test
    fun validateEnteredData_emptyLocation_returnsFalse() {

    }

    @Test
    fun validateEnteredData_validData_returnsTrue() {

    }

    //This is tough one - fake base and fakes data source
    @Test
    fun saveReminder_completes_dataSaved(){

    }

    @Test
    fun validateAndSave_validData_validDataIsSaved(){

    }

    @Test
    fun validateAndSave_invalidData_saveNotCalled(){

    }

    //TODO: provide testing to the SaveReminderView and its live data objects


}