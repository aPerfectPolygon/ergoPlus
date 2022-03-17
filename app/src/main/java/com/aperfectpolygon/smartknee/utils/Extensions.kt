package com.aperfectpolygon.smartknee.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.aperfectpolygon.smartknee.R
import com.google.android.material.R.id.snackbar_action
import com.google.android.material.R.id.snackbar_text
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder

fun Context.snackBar(
	rootLayout: View,
	text: String,
	isError: Boolean = true,
	@ColorInt textColor: Int = getColor(android.R.color.white),
	fontSTYLE: FontSTYLE = FontSTYLE.BOLD,
	anchorView: View? = null,
	textGravity: Int = Gravity.CENTER,
	duration: Int = Snackbar.LENGTH_SHORT,
) = Snackbar.make(rootLayout, text, duration).apply {
	animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
	when {
		isError -> R.color.red
		else -> R.color.green
	}.also { setBackgroundTint(getColor(it)) }
	if (fontSTYLE == FontSTYLE.BOLD) boldFont else normalFont
	if (anchorView != null) setAnchorView(anchorView)
	setActionTextColor(getColor(R.color.disableTextColor))
	actionTypeface
	textSize(12f)
	setTextColor(textColor)
	gravity(textGravity)
	show()
}

fun Context.snackBar(
	rootLayout: View,
	@StringRes text: Int,
	isError: Boolean = true,
	@ColorInt textColor: Int = getColor(android.R.color.white),
	fontSTYLE: FontSTYLE = FontSTYLE.BOLD,
	anchorView: View? = null,
	textGravity: Int = Gravity.CENTER,
	duration: Int = Snackbar.LENGTH_SHORT,
) = Snackbar.make(rootLayout, getString(text), duration).apply {
	animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
	when {
		isError -> R.color.red
		else -> R.color.green
	}.also { setBackgroundTint(getColor(it)) }
	if (fontSTYLE == FontSTYLE.BOLD) boldFont else normalFont
	if (anchorView != null) setAnchorView(anchorView)
	setActionTextColor(getColor(R.color.disableTextColor))
	actionTypeface
	textSize(12f)
	setTextColor(textColor)
	gravity(textGravity)
	show()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) =
	addTextChangedListener(
		object : TextWatcher {
			override fun afterTextChanged(editable: Editable?) =
				afterTextChanged.invoke(editable.toString())

			override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
			override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
		}
	)

val Snackbar.normalFont
	get() = (view.findViewById(snackbar_text) as TextView)
		.apply { typeface = TypeFace.createNormal(context) }

/** Extension function to creating boldFont in snackbar*/
val Snackbar.boldFont
	get() = (view.findViewById(snackbar_text) as TextView)
		.apply { typeface = TypeFace.createBold(context) }

val Snackbar.actionTypeface
	get() = (view.findViewById(snackbar_action) as TextView).apply {
		textSize = 12f
		typeface = TypeFace.createNormal(context)
	}

/** Extension function to changing textSize in snackbar*/
fun Snackbar.textSize(txtSize: Float) {
	(view.findViewById<TextView>(snackbar_text)).apply { textSize = txtSize }
}

/** Extension function to changing gravity in snackbar*/
fun Snackbar.gravity(gravity: Int) {
	(view.findViewById<TextView>(snackbar_text)).apply { this.gravity = gravity }
}

val View.textString: String
	get() = when (this) {
		is TextView -> if (text == null) "" else text.toString()
		else -> throw Throwable("view does not contain text")
	}

//region regex
val ZERO_WIDTH_SPACE: Regex = Regex("[\\p{javaWhitespace}\u00A0\u2007\u202F\u200c]+")
val MATCH_USERNAME_SIGNUP: Regex =
	Regex("^\$|^(?=.{5,20}\$)(?![_.])(?!Candle_)(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
val MATCH_USERNAME_PROFILE: Regex =
	Regex("^(?=.{5,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")
val MATCH_MOBILE_INTERNATIONAL: Regex = Regex("^\\d{3,15}\$")
val MATCH_MOBILE: Regex = Regex("^9([012349])[0-9]{8}$")
val MATCH_MAIL: Regex = Regex("^$|^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$")
val PASSWORD: Regex = Regex("^(?=.*[A-Za-z])(?=.*[0-9]).{8,}\$")
val MATCH_NAME_LAST_NAME: Regex =
	Regex("^$|^[\\u0621-\\u0628\\u062A-\\u063A\\u0641-\\u0642\\u0644-\\u0648\\u064E-\\u0651\\u0655\\u067E\\u0686\\u0698\\u0020\\u2000-\\u200F\\u2028-\\u202F\\u06A9\\u06AF\\u06BE\\u06CC\\u0629\\u0643\\u0649-\\u064B\\u064D\\u06D5A-Za-z]{3,30}\$")
//endregion

val String.isUserNameValid: Boolean
	get() {
		var value = this
		if (value.startsWith("+")) value = "00" + value.substring(1)
		return isNotEmpty() and (MATCH_USERNAME_SIGNUP.matches(value)
				or (MATCH_MOBILE.matches(value) or MATCH_MOBILE_INTERNATIONAL.matches(value)))
	}

val String.isPasswordValid: Boolean
	get() = PASSWORD.matches(this)

val String.isMobileValid: Boolean
	get() = MATCH_MOBILE_INTERNATIONAL.matches(this)

val View.isPasswordValid: Boolean
	get() = textString.isPasswordValid
val View.isMobileValid: Boolean
	get() = textString.isMobileValid
val View.isEmpty: Boolean
	get() = textString.isEmpty()
val View.isNotEmpty: Boolean
	get() = textString.isNotEmpty()
val View.makeEmpty: String
	get() = when (this) {
		is TextView -> "".also { text = it }
		else -> throw Throwable("view does not contain text")
	}

private val persianNumbers = arrayOf("۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹")
private val englishNumbers = arrayOf("0".."9")

val String.persianToEnglishNumber: String
	get() = if (isEmpty()) "" else buildString {
		this@persianToEnglishNumber.forEach { element ->
			element.apply {
				when {
					toString() == "،" -> append("٫")
					toString() in persianNumbers -> persianNumbers.forEachIndexed { index, item ->
						if (item == toString()) append(englishNumbers[index])
					}
					else -> append(this)
				}
			}
		}
	}.trim()

val Context.isWifiConnected: Boolean
	get() = with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
		when {
			(getNetworkCapabilities(activeNetwork ?: return false) ?: return false)
				.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
			else -> {
				startActivity(Intent(Settings.ACTION_WIFI_SETTINGS));
				false
			}
		}
	}

val Context.localWifiIpAddress: String?
	get() {
		val wifiManager = getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
		var ipAddress = wifiManager.connectionInfo.ipAddress
		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
			ipAddress = Integer.reverseBytes(ipAddress)
		}
		val ipByteArray: ByteArray = ipAddress.toBigInteger().toByteArray()
		val ipAddressString: String? = try {
			InetAddress.getByAddress(ipByteArray).hostAddress
		} catch (ex: UnknownHostException) {
			null
		}
		return ipAddressString?.trim()
	}
