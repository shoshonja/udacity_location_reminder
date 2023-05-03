package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(
            "No Reminders!"
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders?.let {
            for (reminder in it) {
                if (reminder.id == id)
                    return Result.Success(reminder)
            }
        }
        return Result.Error("Reminder not found!")
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }
}