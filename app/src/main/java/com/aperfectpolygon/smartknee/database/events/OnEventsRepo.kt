package com.aperfectpolygon.smartknee.database.events

import com.aperfectpolygon.smartknee.model.event.Event

interface OnEventsRepo {
	val isEventsEmpty: Boolean
	var events: List<Event>
	fun removeEvent(id: Long): Boolean
	fun removeEvents()
}