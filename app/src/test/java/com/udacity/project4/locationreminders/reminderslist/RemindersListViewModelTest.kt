package com.udacity.project4.locationreminders.reminderslist

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

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

    lateinit var fakeLocalRepository: ReminderDataSource
    lateinit var subjectUnderTest: RemindersListViewModel

    @Test
    fun loadReminders_completes_reminderListUpdated() {
        //Given
        fakeLocalRepository =
            FakeDataSource(reminders = mutableListOf(reminder1, reminder2, reminder3))
        subjectUnderTest =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeLocalRepository)
        //When
        subjectUnderTest.loadReminders()
        //Then
        val reminders = subjectUnderTest.remindersList.getOrAwaitValue()
        assertTrue(reminders != null)
    }

    @Test
    fun loadReminders_fails_snackBarUpdated() {
        //Given
        fakeLocalRepository = FakeDataSource()
        subjectUnderTest =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeLocalRepository)
        //When
        subjectUnderTest.loadReminders()
        //Then
        val reminders = subjectUnderTest.remindersList.getOrAwaitValue()
        assertTrue(reminders.isEmpty())
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}