package com.udacity.project4.locationreminders.reminderslist

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeAndroidDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//UI Testing
@MediumTest
class ReminderListFragmentTest {

    private lateinit var repository: ReminderDataSource
    private lateinit var viewModel: RemindersListViewModel

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

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDb() {
        repository = FakeAndroidDataSource(mutableListOf(reminder1, reminder2, reminder3))
        viewModel = RemindersListViewModel(getApplicationContext(), repository)
    }

    @Test
    fun noActiveReminders_iconDisplayedInUi() {
        //Given
        launchFragmentInContainer<ReminderListFragment>(
            fragmentArgs = Bundle(),
            themeResId = R.style.AppTheme
        )
        runBlocking {
            //When
            repository.deleteAllReminders()
            viewModel.loadReminders()

            //Then
            onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun activeReminder_reminderDisplayedInUi() {
        //Given
        launchFragmentInContainer<ReminderListFragment>(
            fragmentArgs = Bundle(),
            themeResId = R.style.AppTheme
        )
        runBlocking {
            //When
            val reminderDto = ReminderDTO(
                title = "title4",
                description = "description4",
                location = "location4",
                latitude = 4.0,
                longitude = 4.0,
                id = "id4"
            )
            repository.saveReminder(reminderDto)
            viewModel.loadReminders()

            //Then
            onView(withId(R.id.addReminderFAB)).check(matches(isDisplayed()))
            onView(withId(R.id.reminderssRecyclerView)).check(matches(isDisplayed()))
            onView(withText("title1")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun fabClicked_addLocationFragmentLaunched() = runTest {
        //Given
        val scenario = launchFragmentInContainer<ReminderListFragment>(
            fragmentArgs = Bundle(),
            themeResId = R.style.AppTheme
        )

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        //When
        onView(withId(R.id.addReminderFAB)).perform(click())

        //Then
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }

}