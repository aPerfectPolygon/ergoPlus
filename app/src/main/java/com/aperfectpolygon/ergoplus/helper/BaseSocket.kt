package com.aperfectpolygon.ergoplus.helper

import android.os.Handler
import android.os.Looper
import com.aperfectpolygon.ergoplus.model.event.Event
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket
import java.nio.channels.SocketChannel
import java.util.*

object BaseSocket : Socket() {

	const val TAG = "BaseSocket_CTAG"
	private const val tagEnd = "[END]"

	private var _socket: Socket? = null
	private var outPrintWriter: PrintWriter? = null
	private var _isClosed = false

	private interface OnSocketEventListener {
		fun onClose()
		fun onConnect()
		fun onDisconnect()
		fun onRetry()
		fun onSend(message: String)
		fun onReceive(message: String)
		fun onEventReceived(event: Event)
		fun onLogsReceived(event: Event)
	}

	interface OnCloseListener {
		fun onClose(socket: BaseSocket)
	}

	private interface OnConnectListener {
		fun onConnect(socket: BaseSocket)
	}

	private interface OnSendListener {
		fun onSend(socket: BaseSocket, message: String)
	}

	private interface OnDisconnectListener {
		fun onDisconnect(socket: BaseSocket)
	}

	private interface OnRetryListener {
		fun onRetry(socket: BaseSocket)
	}

	private interface OnReceiveListener {
		fun onReceive(socket: BaseSocket, message: String)
	}

	private interface OnEventReceivedListener {
		fun onEventReceived(socket: BaseSocket, event: Event)
	}

	private interface OnLogsReceivedListener {
		fun onLogsReceived(socket: BaseSocket, event: Event)
	}

	private var onSocketEventListener = object : OnSocketEventListener {
		override fun onClose() {
			Logger.e("Closed")
			onCloseListener?.onClose(this@BaseSocket)
		}

		override fun onConnect() {
			Logger.i("Connected")
			onConnectListener?.onConnect(this@BaseSocket)
		}

		override fun onDisconnect() {
			Logger.e("Disconnected")
			onDisconnectListener?.onDisconnect(this@BaseSocket)
		}

		override fun onRetry() {
			Logger.e("Disconnected")
			onRetryListener?.onRetry(this@BaseSocket)
		}

		override fun onSend(message: String) {
			Logger.i("Sent $message")
			onSendListener?.onSend(this@BaseSocket, message)
		}

		override fun onReceive(message: String) {
			Logger.i("Received $message")
			onReceiveListener?.onReceive(this@BaseSocket, message)
		}

		override fun onEventReceived(event: Event) {
			Logger.i("Event received $event")
			onEventReceivedListener?.onEventReceived(this@BaseSocket, event)
		}

		override fun onLogsReceived(event: Event) {
			Logger.i("Logs received $event")
			onLogsReceivedListener?.onLogsReceived(this@BaseSocket, event)
		}
	}
	private var onConnectListener: OnConnectListener? = null
	private var onCloseListener: OnCloseListener? = null
	private var onDisconnectListener: OnDisconnectListener? = null
	private var onRetryListener: OnRetryListener? = null
	private var onReceiveListener: OnReceiveListener? = null
	private var onSendListener: OnSendListener? = null
	private var onEventReceivedListener: OnEventReceivedListener? = null
	private var onLogsReceivedListener: OnLogsReceivedListener? = null

	fun onConnect(cb: BaseSocket.() -> Unit) {
		object : OnConnectListener {
			override fun onConnect(socket: BaseSocket) = cb.invoke(socket)
		}.also { this.onConnectListener = it }
	}

	fun onClose(cb: BaseSocket.() -> Unit) = object : OnCloseListener {
		override fun onClose(socket: BaseSocket) = cb(socket)
	}.also { this.onCloseListener = it }

	fun onDisconnect(cb: BaseSocket.() -> Unit) {
		object : OnDisconnectListener {
			override fun onDisconnect(socket: BaseSocket) = cb(socket)
		}.also { this.onDisconnectListener = it }
	}

	fun onRetry(cb: BaseSocket.() -> Unit) {
		object : OnRetryListener {
			override fun onRetry(socket: BaseSocket) = cb(socket)
		}.also { this.onRetryListener = it }
	}

	fun onSend(cb: BaseSocket.(message: String) -> Unit) {
		object : OnSendListener {
			override fun onSend(socket: BaseSocket, message: String) = cb(socket, message)
		}.also { this.onSendListener = it }
	}

	fun onReceive(cb: (socket: BaseSocket, message: String) -> Unit) {
		object : OnReceiveListener {
			override fun onReceive(socket: BaseSocket, message: String) = cb.invoke(socket, message)
		}.also { this.onReceiveListener = it }
	}

	fun onEventReceived(cb: BaseSocket.(event: Event) -> Unit) {
		object : OnEventReceivedListener {
			override fun onEventReceived(socket: BaseSocket, event: Event) =
				cb.invoke(socket, event)
		}.also { this.onEventReceivedListener = it }
	}

	fun onLogsReceived(cb: BaseSocket.(event: Event) -> Unit) {
		object : OnLogsReceivedListener {
			override fun onLogsReceived(socket: BaseSocket, event: Event) =
				cb.invoke(socket, event)
		}.also { this.onLogsReceivedListener = it }
	}

	var host: String = ""

	fun connect() {
		if (isClosed) try {
			runBlocking { _socket = Socket(host, 8888).apply { soTimeout = 86_400_000 } }
			outPrintWriter = PrintWriter(outputStream!!)
			CoroutineScope(Dispatchers.IO).launch { _receive() }
		} catch (e: Exception) {
			Logger.e(e, e.message ?: "")
		} finally {
			Handler(Looper.getMainLooper()).postDelayed({
				if (_socket != null) {
					_isClosed = false
					onSocketEventListener.onConnect()
				}
			}, 500)
		}
	}

	private val gsonBuilder by lazy {
		GsonBuilder().apply {
			serializeNulls()
			setLenient()
			setPrettyPrinting()
			serializeSpecialFloatingPointValues()
			enableComplexMapKeySerialization()
			registerTypeAdapter(
				Double::class.java,
				JsonSerializer<Double?> { src, _, _ -> JsonPrimitive(src.toBigDecimal()) }
			)
		}.create()
	}

	val String.convertSocketLine: Event
		@Throws(JsonSyntaxException::class)
		get() = gsonBuilder.fromJson(this.trim(), object : TypeToken<Event>() {}.type)

	private var x = 1000L
	private fun _receive() {
		if (_socket == null) {
			CoroutineScope(Dispatchers.IO).launch {
				onSocketEventListener.onRetry()
				Logger.e(
					Exception("SHOULD NOT HAPPEN", Throwable("_SOCKET IS NULL!!")),
					"SHOULD NOT HAPPEN",
				)
				x += x * 2
				Logger.i("retrying in $x milliseconds")
				delay(x)
				connect()
			}
			return
		}
		x = 1000L
		runCatching {
			Scanner(inputStream).apply {
				while (hasNextLine()) nextLine().apply {
					if (!isNullOrEmpty()) removeSuffix(tagEnd).also { line ->
						runCatching {
							line.convertSocketLine.also {
								when (it.type) {
									"event" -> onSocketEventListener.onEventReceived(it)
									"logs" -> onSocketEventListener.onLogsReceived(it)
									else -> onSocketEventListener.onReceive(line)
								}
							}
						}
					}
				}
			}
		}.onFailure { Logger.e(it, it.message ?: "") }
		_close()
	}

	private fun String._send() {
		if (outPrintWriter == null) {
			CoroutineScope(Dispatchers.IO).launch {
				onSocketEventListener.onRetry()
				Logger.e(
					Exception("SHOULD NOT HAPPEN", Throwable("_SOCKET IS NULL!!")), "SHOULD NOT HAPPEN",
				)
				x += x * 2
				Logger.i("retrying in $x milliseconds")
				delay(x)
				connect()
			}
			return
		}
		x = 1000L
		runCatching {
			runBlocking {
				try {
					outPrintWriter!!.print(this@_send + '\n')
					outPrintWriter!!.flush()
					onSocketEventListener.onSend(this@_send)
				} catch (e: IOException) {
					Logger.e(e, e.message ?: "")
					_close()
				}
			}
		}
	}

	private val String.convertMessage: String
		get() = this.also { Logger.json(this) }

	fun String.send() = _send()

	private fun _close(): Boolean {
		Logger.e("closed")
		Logger.e("isConnected : $isConnected")
		runCatching {
			if (isConnected) {
				outPrintWriter?.close()
				_socket?.close()
				if (!_isClosed) {
					_isClosed = true
					outPrintWriter = null
					onSocketEventListener.onClose()
				}
				return true
			}
		}.onFailure { Logger.e(it, it.message ?: "") }
		return false
	}

	fun disconnect() {
		if (_close()) {
			onSocketEventListener.onDisconnect()
		}
	}

	val setTime: Unit get() = "time${System.currentTimeMillis().div(1000)}".send()

	val event0: Unit get() = "event0".send()

	val event1: Unit get() = "event1".send()

	val readAndFlush: Unit get() = "readAndFlush".send()

	val read: Unit get() = "read".send()

	val flush: Unit get() = "flush".send()

	val readVoltage: Unit get() = "readVoltage".send()

	override fun isClosed(): Boolean = _socket?.isClosed ?: true
	override fun isConnected(): Boolean = !isClosed
	override fun isBound(): Boolean = _socket?.isBound ?: false
	override fun isInputShutdown(): Boolean = _socket?.isInputShutdown ?: true
	override fun isOutputShutdown(): Boolean = _socket?.isOutputShutdown ?: true
	override fun getKeepAlive(): Boolean = _socket?.keepAlive ?: false
	override fun getLocalAddress(): InetAddress? = _socket?.localAddress
	override fun getOutputStream(): OutputStream? = _socket?.getOutputStream()
	override fun getInputStream(): InputStream? = _socket?.getInputStream()
	override fun getLocalPort(): Int = _socket?.localPort ?: 0
	override fun getInetAddress(): InetAddress? = _socket?.inetAddress
	override fun getChannel(): SocketChannel? = _socket?.channel
	override fun getSoTimeout(): Int = _socket?.soTimeout ?: 0
	override fun getPort(): Int = _socket?.port ?: 0
	override fun toString() = _socket.toString()
}