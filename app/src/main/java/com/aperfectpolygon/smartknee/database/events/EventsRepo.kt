package com.aperfectpolygon.smartknee.database.events

import com.aperfectpolygon.smartknee.helper.ObjectBox
import com.aperfectpolygon.smartknee.model.event.Event
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import io.objectbox.Box

object EventsRepo : OnEventsRepo {
	private val mbox: Box<Event> by lazy { ObjectBox.boxStore.boxFor(Event::class.java) }

	override val isEventsEmpty: Boolean
		get() = mbox.isEmpty

	override var events: Event?
		get() = mbox.query().build().findFirst()?.also { event ->
			GsonBuilder().apply {
				serializeNulls()
				setLenient()
				setPrettyPrinting()
				serializeSpecialFloatingPointValues()
				enableComplexMapKeySerialization()
				registerTypeAdapter(
					Double::class.java,
					JsonSerializer<Double?> { src, _, _ -> JsonPrimitive(src.toBigDecimal()) }
				)
			}.create().fromJson<ArrayList<List<Long>>>(
				event._data?.trim(),
				object : TypeToken<ArrayList<List<Long>>>() {}.type
			).also { event.data = it }
		}
		set(value) = with(mbox) {
			value?.let { event ->
				event.data?.also { events?.data?.addAll(it) }
				event.data?.distinct()
				mbox.put(event.also { it._data = it.data.toString() })
			}
		}

	override fun removeEvent(id: Long): Boolean = mbox.remove(id)
	override fun removeEvents() = mbox.removeAll()
}

