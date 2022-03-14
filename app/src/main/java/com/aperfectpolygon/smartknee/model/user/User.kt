package com.aperfectpolygon.smartknee.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.DefaultValue
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class User(
	@Id var id: Long = 0,
	@Expose @DefaultValue("") @SerializedName("token") var token: String? = "GooglePlay",
	@Expose @DefaultValue("") @SerializedName("first_name") var firstName: String? = null,
	@Expose @DefaultValue("") @SerializedName("last_name") var lastName: String? = null,
	@Expose @DefaultValue("") @SerializedName("mobile_number") var mobileNumber: String? = null,
	@Expose @DefaultValue("") @SerializedName("username") var username: String? = null,
	@Expose @DefaultValue("") @SerializedName("email") var email: String? = null,
	@Expose @SerializedName("auth_mobile_number") var authMobileNumber: Boolean = false,
	@Expose @SerializedName("auth_email") var authEmail: Boolean = false,
	@Expose @DefaultValue("") @SerializedName("mazdax_api_key") var mazdaxApiKey: String? = null,
	@Expose @DefaultValue("") @SerializedName("nobitex_api_key") var nobitexApiKey: String? = null,
	@Expose @SerializedName("exchange_enabled") var exchangeEnabled: Boolean = true,
	@Expose @SerializedName("risk_level") var riskLevel: Int? = null,
	@Expose @SerializedName("birth_date") var birthDate: Long? = null,
	@Expose @DefaultValue("") @SerializedName("avatar") var avatar: String? = null,
	@Expose @SerializedName("subscription_code") var subscriptionCode: Int? = null,
	@Expose @SerializedName("subscription_period") var subscriptionPeriod: Long? = null,
	@Expose @SerializedName("subscription_start") var subscriptionStart: Long? = null,
	@Expose @SerializedName("subscription_end") var subscriptionEnd: Long? = null,
	@Expose @SerializedName("show_subscriptions") var showSubscriptions: Boolean? = null,
	@Expose @SerializedName("curr_version") var currVersion: Int? = null,
	@Expose @SerializedName("force_version") var forceVersion: Int? = null,
	@Expose @SerializedName("unread_notification_count") var unreadCount: Int? = null,
	@Expose @SerializedName("limit_mw_count") var limitMwCount: Int? = null,
	@Expose @SerializedName("limit_mw_symbols_count") var limitMwSymbolsCount: Int? = null,
	@Expose @SerializedName("limit_strategy_count") var limitStrategyCount: Int? = null,
	@Expose @SerializedName("limit_portfolio_count") var limitPortfolioCount: Int? = null,
	@Expose @SerializedName("show_mazdax") var showMazdax: Boolean = true,
	@Expose @SerializedName("show_exir") var showExir: Boolean = true,
	@Expose @SerializedName("show_nobitex") var showNobitex: Boolean = true,
	@Expose @SerializedName("limit_markets") var limitMarket: List<String>? = null,
	@Expose @SerializedName("ir_access") var isIr: Boolean = false,
	@Expose @SerializedName("fx_access") var isFx: Boolean = false,
	@Expose @SerializedName("notification_token_app") val notificationTokenApp: Boolean = false,
	@Expose @SerializedName("exir_api_key") val exirApiKey: String? = null,
	@Expose @SerializedName("exir_api_secret") val exirApiSecret: String? = null,
	@Expose @SerializedName("lang") val lang: String = "",
	@Expose @SerializedName("referral_link") val referralLink: String? = null,
	@Expose @DefaultValue("") @SerializedName("template") val template: String? = null,
	@Expose @SerializedName("show_exchanges") val showExchanges: List<String> = emptyList(),
	@Expose @DefaultValue("") @SerializedName("signup_from") val signupFrom: String? = null,
	@Expose @DefaultValue("") @SerializedName("status") val status: String? = null,
	var itemType: String? = null,
	var originalJson: String? = null,
	var signature: String? = null,
)