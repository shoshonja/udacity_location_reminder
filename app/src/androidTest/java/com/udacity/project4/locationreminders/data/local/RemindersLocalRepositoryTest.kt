package com.udacity.project4.locationreminders.data.local

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.LOG_TAG
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private val reminder1 = ReminderDTO(
        title = "title1",
        description = "description1",
        location = "location1",
        latitude = 0.0,
        longitude = 0.0,
        id = "id1"
    )
    private val reminder2 = ReminderDTO(
        title = "title2",
        description = "description2",
        location = "location2",
        latitude = 2.2,
        longitude = 2.2,
        id = "id2"
    )
    private val reminder3 = ReminderDTO(
        title = "title3",
        description = "description3",
        location = "location3",
        latitude = 3.3,
        longitude = 3.3,
        id = "id3"
    )

    private lateinit var subjectUnderTest: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        subjectUnderTest = RemindersLocalRepository(database.reminderDao(), Dispatchers.Main)
    }

    @Test
    fun saveAndGetReminder_completes_reminderInDatabase() = runTest {
        //Given
        subjectUnderTest.saveReminder(reminder1)

        //When
        val savedReminder = (subjectUnderTest.getReminder(reminder1.id) as Result.Success).data

        //Then
        assertThat(savedReminder as ReminderDTO, CoreMatchers.notNullValue())
        assertThat(savedReminder.id, `is`(reminder1.id))
        assertThat(savedReminder.description, `is`(reminder1.description))
        assertThat(savedReminder.location, `is`(reminder1.location))
        assertThat(savedReminder.latitude, `is`(reminder1.latitude))
        assertThat(savedReminder.longitude, `is`(reminder1.longitude))
        assertThat(savedReminder.title, `is`(reminder1.title))

    }

    @Test
    fun getReminders_completes_remindersReturned() = runTest {
        //Given
        subjectUnderTest.saveReminder(reminder1)
        subjectUnderTest.saveReminder(reminder2)
        subjectUnderTest.saveReminder(reminder3)

        //When
        val savedReminders = (subjectUnderTest.getReminders() as Result.Success).data
        Log.d(LOG_TAG, savedReminders[0].id)
        Log.d(LOG_TAG, savedReminders[1].id)
        Log.d(LOG_TAG, savedReminders[2].id)

        //Then
        Assert.assertTrue(savedReminders.isNotEmpty())
        Assert.assertTrue(savedReminders.containsAll(listOf(reminder1, reminder2, reminder3)))

    }

    @Test
    fun deleteReminders_completes_emptyDatabase() = runTest {
        //Given
        subjectUnderTest.saveReminder(reminder1)
        subjectUnderTest.saveReminder(reminder2)
        subjectUnderTest.saveReminder(reminder3)

        subjectUnderTest.deleteAllReminders()

        //When
        val savedReminders = (subjectUnderTest.getReminders() as Result.Success).data

        //Then
        Assert.assertTrue(savedReminders.isEmpty())
    }

    @After
    fun cleanUp() {
        database.close()
    }


}