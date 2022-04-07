package com.aperfectpolygon.ergoplus.helper.sharedPrefrences

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.SharedPreferences
import com.aperfectpolygon.ergoplus.helper.BaseSocket
import com.aperfectpolygon.ergoplus.helper.constants.Constants.SHP_DARK_MOOD
import com.aperfectpolygon.ergoplus.helper.constants.Constants.SHP_INTRO_PLAYED
import com.aperfectpolygon.ergoplus.helper.constants.Constants.SHP_KEY
import com.aperfectpolygon.ergoplus.helper.constants.Constants.SHP_SERVER_ID
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken

object ShpHelper {

	val socket: BaseSocket by lazy { BaseSocket }

	private val Context.shp: SharedPreferences
		get() = getSharedPreferences(SHP_KEY, Context.MODE_PRIVATE)

	var Context.logs: String?
		get() = shp.getString(
			"SHP_LOGS", arrayListOf(
				listOf(1647626453, 1L),
				listOf(1647626453, 1L),
				listOf(1647526453, 0L),
				listOf(1647526453, 0L),
				listOf(1647526453, 0L),
				listOf(1647426453, 1L),
				listOf(1647326453, 0L),
				listOf(1647326453, 0L),
				listOf(1647226453, 1L),
				listOf(1647126453, 0L),
				listOf(1647126453, 0L),
				listOf(1647126453, 0L),
				listOf(1647026453, 1L)
			).toString()
		)
		set(value) = shp.edit().apply { putString("SHP_LOGS", value) }.apply()

	val Context.getLogs: MutableList<List<Long>>
		get() = GsonBuilder().apply {
			serializeNulls()
			setLenient()
			setPrettyPrinting()
			serializeSpecialFloatingPointValues()
			enableComplexMapKeySerialization()
			registerTypeAdapter(
				Double::class.java,
				JsonSerializer<Double?> { src, _, _ -> JsonPrimitive(src.toBigDecimal()) }
			)
		}.create().fromJson<ArrayList<List<Long>>?>(
			logs?.trim(),
			object : TypeToken<ArrayList<List<Long>>>() {}.type
		).sortedBy { it[0] }.toMutableList()

	var Context.darkMode: Boolean
		get() = shp.getBoolean(SHP_DARK_MOOD, false)
		set(value) = shp.edit().apply { putBoolean(SHP_DARK_MOOD, value) }.apply()

	var Context.firstGift: String?
		get() = shp.getString("SHP_FIRST_GIFT", null)
		set(value) = shp.edit().apply { putString("SHP_FIRST_GIFT", value) }.apply()

	var Context.secondGift: String?
		get() = shp.getString("SHP_SECOND_GIFT", null)
		set(value) = shp.edit().apply { putString("SHP_SECOND_GIFT", value) }.apply()

	var Context.canVibrate: Boolean
		get() = shp.getBoolean("SHP_CAN_VIBRATE", false)
		set(value) = shp.edit().apply { putBoolean("SHP_CAN_VIBRATE", value) }.apply()

	var Context.introPlayed: Boolean
		get() = shp.getBoolean(SHP_INTRO_PLAYED, false)
		set(value) = shp.edit().apply { putBoolean(SHP_INTRO_PLAYED, value) }.apply()

	var Context.serverId: Long?
		get() = shp.getLong(SHP_SERVER_ID, 0)
		set(value) = shp.edit().apply { value?.let { putLong(SHP_SERVER_ID, it) } }.apply()

	fun Context.clearCache() {
		kotlin.runCatching {
			(shp.edit().clear().apply())
			cacheDir.deleteRecursively()
			dataDir.deleteRecursively()
			codeCacheDir.deleteRecursively()
			externalCacheDir?.deleteRecursively()
			filesDir.deleteRecursively()
			noBackupFilesDir.deleteRecursively()
			obbDir.deleteRecursively()
			(getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
		}.onFailure { it.printStackTrace() }
	}

}