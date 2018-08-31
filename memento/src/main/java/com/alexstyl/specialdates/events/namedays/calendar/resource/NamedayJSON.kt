package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.events.namedays.NamedayLocale
import org.json.JSONArray

data class NamedayJSON(val locale: NamedayLocale, val namedaysJSON: JSONArray, val special: JSONArray)
