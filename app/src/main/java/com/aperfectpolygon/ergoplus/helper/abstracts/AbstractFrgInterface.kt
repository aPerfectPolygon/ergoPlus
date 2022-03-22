package com.aperfectpolygon.ergoplus.helper.abstracts

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.orhanobut.logger.Logger

interface AbstractFrgInterface {
	val TAG: String
		get() = javaClass.simpleName + "_TAG"

	var circularProgressDrawable: CircularProgressDrawable

	fun Fragment.trackByFirebase(
		name: String = "",
		isAppOpen: Boolean = false,
		isSignUp: Boolean = false,
		isSignIn: Boolean = false,
	) {
		if (!isAdded or isDetached) return
		try {
			val bundle = Bundle().apply {
				when {
					isAppOpen -> putString(FirebaseAnalytics.Param.SCREEN_NAME, "App opened")
					isSignIn -> putString(FirebaseAnalytics.Param.SCREEN_NAME, "Signed In")
					isSignUp -> putString(FirebaseAnalytics.Param.SCREEN_NAME, "Signed Up")
				}
				if (name.isNotEmpty()) putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
			}
			FirebaseAnalytics.getInstance(requireContext()).apply {
				when {
					isAppOpen -> logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
					isSignIn -> logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
					isSignUp -> logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
				}
				logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
			}
		} catch (e: Exception) {
			Logger.e(e, e.message ?: "")
		}
	}

	fun onDispose() = Unit
	fun onErrorHandler() = Unit

	fun Fragment.getColor(i: Int): Int = ContextCompat.getColor(requireContext(), i)

	fun AppCompatImageView.loadImageResourceWithGlide(resId: Int) =
		Glide.with(this).load(resId).placeholder(circularProgressDrawable)
			.fallback(R.drawable.vtr_logo).into(this)

	fun AppCompatImageView.loadImageResourceWithGlide(url: String) = try {
		Glide.with(this).load(GlideUrl(url)).centerCrop()
			.fallback(R.drawable.vtr_logo).placeholder(circularProgressDrawable)
			.diskCacheStrategy(DiskCacheStrategy.ALL).into(this)
	} catch (e: Exception) {
		Logger.e(e, e.message ?: "")
	}

	fun View.snackBar(
		text: String, duration: Int = Snackbar.LENGTH_SHORT,
	): Snackbar = Snackbar.make(this, text, duration).apply { show() }

	fun View.snackBar(
		@StringRes text: Int,
		duration: Int = Snackbar.LENGTH_SHORT,
	): Snackbar = snackBar(text = context.getString(text), duration = duration)

}