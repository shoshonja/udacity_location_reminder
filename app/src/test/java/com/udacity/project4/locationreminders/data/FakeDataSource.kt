package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDataItem>? = mutableListOf()) :
    ReminderDataSource {

    var remindersAsDTO = mutableListOf<ReminderDTO>()

    init {
        for (reminder in reminders!!) {
            remindersAsDTO.add(
                ReminderDTO(
                    title = reminder.title,
                    description = reminder.description,
                    location = reminder.location,
                    latitude = reminder.latitude,
                    longitude = reminder.longitude,
                    id = reminder.id
                )
            )
        }
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        remindersAsDTO?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(
            "No Reminders!"
        )
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remindersAsDTO.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        remindersAsDTO.let {
            for (reminder in it) {
                if (reminder.id == id)
                    return Result.Success(reminder)
            }
        }
        return Result.Error("Reminder not found!")
    }

    override suspend fun deleteAllReminders() {
        remindersAsDTO.clear()
    }
}