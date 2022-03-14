package com.aperfectpolygon.smartknee.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.Typeface.createFromAsset

object TypeFace {
	fun createNormal(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/iran_sans_x_regular.ttf")

	fun createBold(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/iran_sans_x_bold.ttf")

	fun createLight(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/iran_sans_x_light.ttf")

	fun createMedium(context: Context): Typeface =
		createFromAsset(context.assets, "fonts/iran_sans_x_medium.ttf")
}

enum class FontSTYLE { NORMAL, BOLD, LIGHT, MEDIUM }