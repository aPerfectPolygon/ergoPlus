package com.aperfectpolygon.smartknee.database.events

import com.aperfectpolygon.smartknee.helper.ObjectBox
import com.aperfectpolygon.smartknee.model.event.Event
import io.objectbox.Box

object EventsRepo : OnEventsRepo {
	private val mbox: Box<Event> by lazy { ObjectBox.boxStore.boxFor(Event::class.java) }

	override val isEventsEmpty: Boolean
		get() = mbox.isEmpty
	override var events: List<Event>
		get() = mbox.query().build().findLazyCached() ?: listOf()
		set(value) = with(mbox) {
			removeEvents()
			put(value)
		}

	override fun removeEvent(id: Long): Boolean = mbox.remove(id)
	override fun removeEvents() = mbox.removeAll()
}

