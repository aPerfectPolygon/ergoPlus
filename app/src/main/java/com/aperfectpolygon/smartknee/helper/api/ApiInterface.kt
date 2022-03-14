package com.aperfectpolygon.smartknee.helper.api

import com.aperfectpolygon.smartknee.model.user.User
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiInterface {

	companion object {
		const val token = "token"
		const val CODE_400 = 400                                    // BAD REQUEST
		const val CODE_401 = 401                                    // UNAUTHORIZED
		const val CODE_402 = 402                                    // PAYMENT REQUIRED
		const val CODE_403 = 403                                    // FORBIDDEN
		const val CODE_404 = 404                                    // NOT FOUND
		const val CODE_406 = 406                                    // NOT ACCEPTABLE
		const val CODE_409 = 409                                    // CONFLICT
		const val CODE_426 = 426                                    // UPGRADE REQUIRED

		// region main
		const val referralNotFound = "[REFERRAL NOT FOUND]"
		const val outOfDate = "[OUT OF DATE]"
		const val badToken = "[BAD TOKEN]"
		const val inactiveUser = "[INACTIVE_USER]"
		const val suspendedUser = "[SUSPENDED_USER]"
		const val subscriptionExpired = "[SUBSCRIPTION_EXPIRED]"
		// endregion

		//region userMain
		const val firstName = "first_name"
		const val lastName = "last_name"
		const val mobile_number = "mobile_number"
		const val username = "username"
		const val password = "password"
		const val email = "email"
		const val status = "status"
		const val isActive = "is_active"
		//endregion

		const val profile = "profile"
		const val state = "state"
		const val riskLevel = "risk_level"
		const val created = "created"
		//endregion

		//region subscription
		const val code = "code"
		const val start = "start"
		const val end = "end"
		//endregion

		//region sbiIndicators
		const val sbi = "sbi"

		//indicators

		const val rsi = "rsi"
		const val stochastic = "stochastic"
		const val cci = "cci"
		const val bbands = "bbands"
		const val sma = "sma"
		const val adx = "adx"
		const val ichimoku = "ichimoku"
		const val macd = "macd"
		const val ema = "ema"
		const val psar = "psar"
		//endregion

		//region msIndicators
		const val candlestick = "candlestick"
		const val candlestickPattern = "candlestick_pattern"
		const val harmonicPattern = "harmonic_pattern"
		const val trend = "trend"
		const val fibonacci = "fibonacci"
		const val elliottWave = "elliott_wave"
		//endregion
	}

	val header: HashMap<String, String>
		get() = HashMap<String, String>().apply { set("Content-Type", "application/json") }

	// region requests

	//region user

	//region signupSeries
	@POST("User/SignupSeries/signup")
	fun signUp(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/SignupSeries/MobileNumber/verify")
	fun verify(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/SignupSeries/MobileNumber/re_send")
	fun verifyReSend(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?
	//endregion signupSeries

	//region ForgetPasswordSeries
	@POST("User/ForgetPasswordSeries/send")
	fun forgetPasswordSend(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/change_password")
	fun changePass(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/ForgetPasswordSeries/verify")
	fun forgotPasswordVerify(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/ForgetPasswordSeries/change")
	fun forgotPasswordChange(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/resend_email_verification")
	fun emailVerification(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/exchange_order")
	fun exchangeActivity(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?
	//endregion ForgetPasswordSeries

	@POST("User/login")
	fun signIn(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<User>?>?

	@POST("User/resign")
	fun resign(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/info")
	fun getProfileInfo(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/logout")
	fun signOut(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	//endregion user

	//region symbols
	@POST("Static/symbols")
	fun symbolsGetAll(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Static/symbols_candle_nerkh")
	fun candleNerkhSymbols(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Static/exchange_signup")
	fun exchangeSignup(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	//endregion

	//region marketWatch
	@POST("MarketWatch/create")
	fun createMarketWatch(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("MarketWatch/read")
	fun readMarketWatch(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("MarketWatch/update")
	fun updateMarketWatch(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("MarketWatch/delete")
	fun deleteMarketWatch(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?
	//endregion

	//region strategies
	@POST("Strategy/create")
	fun strategyCreate(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Strategy/read")
	fun strategyRead(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Strategy/update")
	fun strategyEdit(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Strategy/delete")
	fun strategyDelete(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	//endregion

	//region services
	@POST("Static/subscriptions")
	fun subscription(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Services/supervisor_message")
	fun getSB(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Services/ads")
	fun getAds(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?
	//endregion

	@POST("Static/force")
	fun forceUpdate(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("News/preview_v2")
	fun getNews(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("News/more")
	fun getMoreNews(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("News/categories")
	fun getNewsCategories(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Symbol/technical_analysis_v2")
	fun technical(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Symbol/chart")
	fun chart(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Scan/strategy")
	fun strategyScan(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Scan/filter")
	fun filterScan(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("analysis/Scan/Offer")
	fun signal(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/Avatar/update")
	fun editProfileAvatar(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/Avatar/delete")
	fun deleteProfileAvatar(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/update")
	fun editProfile(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("User/set_risk_test_results")
	fun examination(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Payment/create")
	fun paymentCreate(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Payment/check_code")
	fun checkDiscountCode(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Payment/my_payments")
	fun getPaymentList(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Payment/callback_myket")
	fun myketCallback(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Payment/my_myket_payments")
	fun myketOrders(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Notification/set_token")
	fun fcmToken(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Notification/select_choice")
	fun choice(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Notification/SettingsV2/update")
	fun notificationSet(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Notification/SettingsV2/read")
	fun notificationGet(
		@HeaderMap headers: Map<String, String> = header,
		@Body body: JsonObject?,
	): Observable<Response<String?>?>?

	@POST("Notification/Inventory/get_v2")
	fun message(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Notification/Inventory/seen")
	fun seen(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Notification/Inventory/seen_all")
	fun seenAll(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/preview")
	fun preview(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/read")
	fun portfolioRead(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/mark")
	fun mark(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/unmark")
	fun unmark(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/Allocated/read")
	fun allocateRead(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/Allocated/test")
	fun allocateTest(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/Allocated/save")
	fun allocateSave(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Portfolio/Allocated/remove")
	fun allocateRemove(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Scan/offer_v2")
	fun scanOffer(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/dashboard")
	fun getDashboard(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/Rewards/read")
	fun clubRewardRead(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/Rewards/get")
	fun claimReward(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/Reports/reward")
	fun getReportRewards(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/discount_codes")
	fun getClubDiscountCodes(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/Reports/activity")
	fun getReportActivities(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/Reports/referral")
	fun getReportReferral(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	@POST("Club/activities")
	fun readActivities(
		@HeaderMap headers: HashMap<String, String> = header,
		@Body sendJsonObject: JsonObject,
	): Observable<Response<String?>?>?

	//endregion
}
