package com.aperfectpolygon.smartknee.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.Typeface.createFromAsset

object TypeFace {
	fun createNormal(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/comfortaa_regular.ttf")

	fun createBold(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/comfortaa_bold.ttf")

	fun createLight(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/comfortaa_light.ttf")

	fun createMedium(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/comfortaa_medium.ttf")
}

enum class FontSTYLE { NORMAL, BOLD, LIGHT, MEDIUM }