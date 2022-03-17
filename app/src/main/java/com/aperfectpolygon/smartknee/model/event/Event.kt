package com.aperfectpolygon.smartknee.model.event

import com.google.gson.annotations.Expose
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient

@Entity
data class Event(
	@Id var id: Long = 0,
	@Expose val type: String = "",
	@Transient var data: ArrayList<List<Long>>? = null,
	@Expose var _data: String? = ""
)