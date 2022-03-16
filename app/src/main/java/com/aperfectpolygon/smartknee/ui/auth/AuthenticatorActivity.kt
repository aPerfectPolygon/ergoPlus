package com.aperfectpolygon.smartknee.ui.auth

import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.Calendar.*
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.smartknee.database.user.UserRepository.user
import com.aperfectpolygon.smartknee.databinding.ActivityAutherticatorBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.moveTo
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.aperfectpolygon.smartknee.ui.dashboard.DashboardActivity
import com.aperfectpolygon.smartknee.utils.snackBar
import com.github.dhaval2404.imagepicker.ImagePicker


class AuthenticatorActivity : AbstractActivity() {

	private lateinit var binding: ActivityAutherticatorBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAutherticatorBinding.inflate(layoutInflater).apply {
			setContentView(root)
			edtBirthDate
			hideKeyboard
			vBirthDate.setOnClickListener {
				Calendar.getInstance().apply {
					get(YEAR).also { year ->
						get(MONTH).also { month ->
							get(DAY_OF_MONTH).also { day ->
								DatePickerDialog(
									this@AuthenticatorActivity, { _, y, moy, dom ->
										"$dom, $moy, $y".also { edtBirthDate.setText(it) }
									}, year, month, day
								).show()
							}
						}
					}
				}
			}
			imgAvatar.setOnClickListener {
				ImagePicker.with(this@AuthenticatorActivity)
					.crop().compress(1024).maxResultSize(1080, 1080)
					.createIntent { startForProfileImageResult.launch(it) }
			}
			btnSignup.setOnClickListener {
				if (edtUsername.text.isNullOrEmpty()) {
					snackBar(root, "You have not entered your username yet")
					return@setOnClickListener
				}
				if (edtMail.text.isNullOrEmpty()) {
					snackBar(root, "You have not entered your email yet")
					return@setOnClickListener
				}
				if (edtPassword.text.isNullOrEmpty()) {
					snackBar(root, "You have not entered your password yet")
					return@setOnClickListener
				}
				if (!checkbox.isChecked) {
					snackBar(root, "You have not accepted the term's of using (this App)")
					return@setOnClickListener
				}
				user = user.apply {
					username = edtUsername.text.toString()
					birthDate = edtBirthDate.text.toString()
					email = edtMail.text.toString()
				}
				moveTo(this@AuthenticatorActivity, DashboardActivity())
			}
		}
	}

	private val startForProfileImageResult =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
			when (result.resultCode) {
				Activity.RESULT_OK -> result.data?.data?.also { fileUri ->
					user.avatar = fileUri.toString()
					binding.imgAvatar.setImageURI(fileUri)
				}
				ImagePicker.RESULT_ERROR -> snackBar(binding.root, ImagePicker.getError(result.data)).show()
				else -> snackBar(binding.root, "Task Cancelled").show()
			}
		}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}