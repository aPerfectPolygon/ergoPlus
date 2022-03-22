package com.aperfectpolygon.ergoplus.helper

import android.app.Activity
import android.content.Intent
import com.orhanobut.logger.Logger

/**
 * Moving & Transportation between Activities
 */
object ActivityHelper {

	private const val TAG = "ActivityHelper_CTAG"

	/**
	 * Move From Current Activity to Another one
	 *
	 * @param from                Current Activity You are in
	 * @param to Destination Activity You want to move to
	 * @param finishCurrent       Finish Current Activity? Current Activity will Finish if `True`
	 */
	fun moveTo(
		from: Activity,
		to: Activity,
		finishCurrent: Boolean = true,
		targetId: Int? = null,
	) = Intent(from, to::class.java).apply {
		Logger.i("moving to target from ${from.javaClass.simpleName} to ${to.javaClass.simpleName}")
		addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		targetId?.let { putExtra(TARGET, it) }
		if (finishCurrent) from.finishAfterTransition()
		from.startActivity(this)
	}

	const val TARGET: String = "TARGET"
	private const val exitRequestCode: Int = 100

	// region targetId's
	const val idSignIn = 1
	const val idSignUp = 2
	//endregion
}