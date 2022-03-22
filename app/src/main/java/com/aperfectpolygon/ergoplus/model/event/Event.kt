package com.aperfectpolygon.ergoplus.model.event

import com.google.gson.annotations.Expose

data class Event(
	@Expose val type: String = "",
	@Expose var data: ArrayList<List<Long>>? = null,
)