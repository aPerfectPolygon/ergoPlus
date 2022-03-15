package com.aperfectpolygon.smartknee.model.event

import com.google.gson.annotations.Expose
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Event(
	@Id val id: Long = 0,
	@Expose val type: String = "",
	@Expose val data: List<List<Long>> = emptyList()
)