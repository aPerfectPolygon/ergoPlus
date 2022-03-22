package com.aperfectpolygon.ergoplus.helper.abstracts

import com.aperfectpolygon.ergoplus.helper.api.APIClient
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.stream.JsonReader
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.io.StringReader
import java.util.*

abstract class AbstractPresenter<L, T> {

	val TAG by lazy { javaClass.simpleName + "_CTAG" }

	// with on day interval
	val ts by lazy { Calendar.getInstance(TimeZone.getTimeZone("GMT")).timeInMillis / 1000 + 86400 }

	var headers: HashMap<String, String> = HashMap()

	private var disposable: Disposable? = null
	var listener: L? = null

	open fun onAttach(listener: L) {
		Logger.i(TAG, "attached")
		listener.also { this.listener = it }
	}

	open fun onDetach() {
		Logger.i(TAG, "detached")
		onDispose()
	}

	open fun onDispose() {
		Logger.i(TAG, "disposed")
		disposable?.dispose()
	}

	val Response<T>.errorBody: String
		get() = errorBody()?.charStream()?.readText() ?: ""

	val Response<T>.body: T?
		get() = body()

	val Response<T>.responseCode: Int
		get() = code()

	val Response<T>.headers: String
		get() = "${headers()}"

	val Response<T>.message: String?
		get() = message()

	val contents: JsonObject by lazy { JsonObject() }

	fun content(content: JsonObject.() -> Unit) {
		content(contents)
	}

	val String.reader: JsonReader
		get() = JsonReader(StringReader(this))
	private val gsonBuilder by lazy {
		GsonBuilder().setLenient().serializeNulls().serializeSpecialFloatingPointValues()
			.setPrettyPrinting().registerTypeAdapter(
				Double::class.java,
				JsonSerializer<Double?> { src, _, _ -> JsonPrimitive(src.toBigDecimal()) }
			).create()
	}

	interface OnRequest<R> {
		fun onSuccessful(response: R?)
		fun onFailure(response: R?)
		fun onError(e: Throwable)
	}

	var onResponse: OnResponse<T>? = null
	var onFailure: OnFailure<T>? = null
	var onError: OnError? = null

	interface OnResponse<R> {
		fun onResponse(response: Response<R>)
	}

	interface OnFailure<R> {
		fun onFailure(response: String)
	}

	interface OnError {
		fun onError(e: Throwable)
	}

	fun onResponse(onResponse: Response<T>.() -> Unit) = object : OnResponse<T> {
		override fun onResponse(response: Response<T>) = onResponse(response)
	}.also { this.onResponse = it }

	fun onFailure(onFailure: String?.() -> Unit) = object : OnFailure<T> {
		override fun onFailure(response: String) = onFailure(response)
	}.also { this.onFailure = it }

	fun onError(onError: Throwable.() -> Unit) = object : OnError {
		override fun onError(e: Throwable) = onError(e)
	}.also { this.onError = it }

	private fun Retrofit.sendReq(
		onRequestListener: OnRequest<Response<T>>,
		observable: Retrofit.() -> Observable<Response<T>?>?,
	): Job = CoroutineScope(Dispatchers.IO).launch {
		runCatching {
			observable(this@sendReq)?.observeOn(Schedulers.newThread())
				?.observeOn(AndroidSchedulers.mainThread())?.subscribeWith(
					object : DisposableObserver<Response<T>?>() {
						override fun onNext(response: Response<T>) = with(onRequestListener) {
							if (response.isSuccessful) onSuccessful(response) else onFailure(response)
						}

						override fun onError(e: Throwable) = onRequestListener.onError(e)

						override fun onComplete() = onDispose()
					}
				).also { disposable = it }
		}.onFailure { onRequestListener.onError(it) }
	}

	fun safeRequest(url: String = "", apiInterface: Retrofit.() -> Observable<Response<T>?>?) {
		sendReq(apiInterface, url).start()
	}

	private fun sendReq(apiInterface: Retrofit.() -> Observable<Response<T>?>?, ip: String): Job =
		APIClient.defaultClient(ip).sendReq(
			object : OnRequest<Response<T>> {
				override fun onSuccessful(response: Response<T>?) {
					response?.also { res ->
						onResponse?.onResponse(res)
						Logger.json("${res.body}")
					}
				}

				override fun onFailure(response: Response<T>?) {
					response?.errorBody?.also { res ->
						onFailure?.onFailure(res)
						Logger.e(TAG, "request was Unsuccessful -> $res")
					}
				}

				override fun onError(e: Throwable) {
					Logger.e(TAG, e)
					onError?.onError(e)
				}
			}
		) { apiInterface(APIClient.defaultClient(ip)) }
}