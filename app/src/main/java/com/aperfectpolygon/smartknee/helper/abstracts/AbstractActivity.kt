@file:Suppress("DEPRECATION")

package com.aperfectpolygon.smartknee.helper.abstracts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.aperfectpolygon.smartknee.helper.router.OnNavigate
import com.aperfectpolygon.smartknee.helper.sharedPrefrences.ShpHelper.darkMode
import com.orhanobut.logger.Logger

abstract class AbstractActivity : AppCompatActivity(), AbstractFrgInterface {

	fun navigate(targetId: Int) = (this as? OnNavigate)?.apply { navigateTo(targetId) }

	val hideKeyboard: Boolean
		get() = try {
			(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
				.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
		} catch (e: RuntimeException) {
			false
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		when {
			applicationContext.darkMode -> AppCompatDelegate.MODE_NIGHT_YES
			else -> AppCompatDelegate.MODE_NIGHT_NO
		}.also { AppCompatDelegate.setDefaultNightMode(it) }
	}

	val Int.resultLauncher: ActivityResultLauncher<Intent>
		get() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
			Logger.d("result $result")
		}

	fun removeFragment(@IdRes fragmentId: Int) = try {
		Logger.i("removing fragment")
		supportFragmentManager.apply {
			beginTransaction().apply {
				setCustomAnimations(
					android.R.anim.fade_in,  // enter
					android.R.anim.fade_out, // exit
					android.R.anim.fade_in,  // popEnter
					android.R.anim.fade_out  // popExit
				)
				if (findFragmentById(fragmentId) != null) remove(findFragmentById(fragmentId)!!)
				commit()
				popBackStack()
			}
		}
	} catch (e: Exception) {
		Logger.e(e, "error message : ${e.message}", e)
	}

	fun addFragment(@IdRes fragmentId: Int, fragment: AbstractFragment) = try {
		supportFragmentManager.apply {
			beginTransaction().apply {
				if (findFragmentById(fragmentId) == null) {
					Logger.i("adding fragment fragmentName : ${fragment::class.java.simpleName}")
					setCustomAnimations(
						android.R.anim.fade_in,   // enter
						android.R.anim.fade_out,  // exit
						android.R.anim.fade_in,   // popEnter
						android.R.anim.fade_out   // popExit
					)
					add(fragmentId, fragment, fragment::class.java.simpleName)
				} else {
					Logger.i("replacing ${javaClass.simpleName} with ${fragment::class.java.simpleName}")
					setCustomAnimations(
						android.R.anim.fade_in,   // enter
						android.R.anim.fade_out,  // exit
						android.R.anim.fade_in,   // popEnter
						android.R.anim.fade_out   // popExit
					)
					replace(fragmentId, fragment, fragment::class.java.simpleName)
				}
				disallowAddToBackStack()
				commitNow()
			}
		}
	} catch (e: Exception) {
		Logger.e(e, "error message : ${e.message}", e)
	}

	override fun recreate() {
		finish()
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
		startActivity(intent)
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
	}
}