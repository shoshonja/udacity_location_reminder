package com.udacity.project4.locationreminders.savereminder

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private val reminder1 = ReminderDataItem(
        title = "title1",
        description = "description1",
        location = "location1",
        latitude = 0.0,
        longitude = 0.0,
        id = "id1"
    )
    private val reminder2 = ReminderDataItem(
        title = "title2",
        description = "description2",
        location = "location2",
        latitude = 2.2,
        longitude = 2.2,
        id = "id2"
    )
    private val reminder3 = ReminderDataItem(
        title = "title3",
        description = "description3",
        location = "location3",
        latitude = 3.3,
        longitude = 3.3,
        id = "id1"
    )
    private val reminder4 = ReminderDataItem(
        title = "title4",
        description = "description4",
        location = "location4",
        latitude = 4.0,
        longitude = 4.0,
        id = "id4"
    )

    private val reminder5 = ReminderDataItem(
        title = "title5",
        description = "description5",
        location = "location5",
        latitude = 5.0,
        longitude = 5.0,
        id = "id5"
    )

    lateinit var fakeLocalRepository: ReminderDataSource
    lateinit var subjectUnderTest: SaveReminderViewModel

    @Before
    fun createRepository() {
        fakeLocalRepository =
            FakeDataSource(reminders = mutableListOf(reminder1, reminder2, reminder3))
        subjectUnderTest =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeLocalRepository)
    }

    @Test
    fun onClear_completes_dataIsNull() {
        //Given
        subjectUnderTest.reminderTitle.value = "title"
        subjectUnderTest.reminderDescription.value = "description"
        subjectUnderTest.reminderSelectedLocationStr.value = "location"
        subjectUnderTest.selectedPOI.value = PointOfInterest(LatLng(0.0, 0.0), "Param1", "Param2")
        subjectUnderTest.latitude.value = 0.0
        subjectUnderTest.longitude.value = 0.0

        //When
        subjectUnderTest.onClear()

        //Then
        assertTrue(subjectUnderTest.selectedPOI.value == null)
    }

    @Test
    fun propagatePoiData_emptyPoi_emptyLiveData() {
        //Given
        subjectUnderTest.reminderTitle.value = "title"
        subjectUnderTest.reminderDescription.value = "description"
        subjectUnderTest.reminderSelectedLocationStr.value = "location"
        subjectUnderTest.selectedPOI.value = null
        subjectUnderTest.latitude.value = 0.0
        subjectUnderTest.longitude.value = 0.0

        //When
        subjectUnderTest.propagatePoiData()

        //Then
        assertTrue(subjectUnderTest.reminderTitle.value == "title")

    }

    @Test
    fun propagatePoiData_existingPoi_populatedLiveData() {
        //Given
        subjectUnderTest.reminderTitle.value = "title"
        subjectUnderTest.reminderDescription.value = "description"
        subjectUnderTest.reminderSelectedLocationStr.value = "location"
        subjectUnderTest.selectedPOI.value = PointOfInterest(LatLng(10.1, 10.1), "placeID", "name")
        subjectUnderTest.latitude.value = 0.0
        subjectUnderTest.longitude.value = 0.0

        //When
        subjectUnderTest.propagatePoiData()

        //Then
        assertTrue(
            (subjectUnderTest.reminderSelectedLocationStr.value == "name") &&
                    (subjectUnderTest.latitude.value == 10.1) &&
                    (subjectUnderTest.longitude.value == 10.1)
        )
    }

    @Test
    fun validateEnteredData_emptyTitle_returnsFalse() {
        //Given
        val reminderNoTitle = ReminderDataItem(
            title = null,
            description = "description5",
            location = "location5",
            latitude = 5.0,
            longitude = 5.0,
            id = "id5"
        )
        //When
        //Then
        assertTrue(!subjectUnderTest.validateEnteredData(reminderNoTitle))
    }

    @Test
    fun validateEnteredData_emptyLocation_returnsFalse() {
        //Given
        val reminderNoTitle = ReminderDataItem(
            title = "title5",
            description = "description5",
            location = null,
            latitude = 5.0,
            longitude = 5.0,
            id = "id5"
        )
        //When
        //Then
        assertTrue(!subjectUnderTest.validateEnteredData(reminderNoTitle))
    }

    @Test
    fun validateEnteredData_validData_returnsTrue() {
        //Given
        val validReminder = reminder4
        //When
        //Then
        assertTrue(subjectUnderTest.validateEnteredData(validReminder))
    }

    //This is tough one - fake base and fakes data source
    @Test
    fun saveReminder_completes_dataSaved() = runBlocking {
        //Given
        //When
        subjectUnderTest.saveReminder(reminder4)

        //Then
        val savedReminder = (fakeLocalRepository.getReminder(reminder4.id) as Result.Success).data
        assertTrue(savedReminder.id == reminder4.id)
    }

    @Test
    fun validateAndSave_validData_validDataIsSaved() = runBlocking {
        //Given
        //When
        subjectUnderTest.validateAndSaveReminder(reminder5)

        //Then
        val savedReminder = (fakeLocalRepository.getReminder(reminder5.id) as Result.Success).data
        assertTrue(savedReminder.id == reminder5.id)
    }

    @Test
    fun validateAndSave_invalidData_saveNotCalled() = runBlocking {
        //Given
        val reminderNoTitle = ReminderDataItem(
            title = "title6",
            description = "description6",
            location = null,
            latitude = 5.0,
            longitude = 5.0,
            id = "id6"
        )
        //When
        subjectUnderTest.validateAndSaveReminder(reminderNoTitle)
        //Then
        val savedReminder = fakeLocalRepository.getReminder(reminder5.id)
        assertTrue(savedReminder is Result.Error)
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}