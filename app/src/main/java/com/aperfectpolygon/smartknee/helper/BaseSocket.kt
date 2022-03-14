package com.aperfectpolygon.smartknee.helper

import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
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

data class SocketOutput(
	@Expose @SerializedName("type") val type: String? = null,
	@Expose @SerializedName("data") val data: ArrayList<Data>? = null,
)

data class Data(@Expose val status: String)

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
		fun onSend(message: String)
		fun onReceive(message: String)
		fun onSymbolsReceived(symbols: ArrayList<Data>)
		fun onSymbolUpdated(symbols: ArrayList<Data>)
		fun onIndexesReceived(indexes: ArrayList<Data>)
		fun onIndexesUpdated(indexes: ArrayList<Data>)
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

	private interface OnReceiveListener {
		fun onReceive(socket: BaseSocket, message: String)
	}

	private interface OnSymbolsReceivedListener {
		fun onSymbolsReceived(socket: BaseSocket, symbols: ArrayList<Data>)
	}

	private interface OnIndexesReceivedListener {
		fun onIndexesReceived(socket: BaseSocket, indexes: ArrayList<Data>)
	}

	private interface OnIndexesUpdatedListener {
		fun onIndexesUpdated(socket: BaseSocket, indexes: ArrayList<Data>)
	}

	private interface OnSymbolUpdatedListener {
		fun onSymbolUpdated(socket: BaseSocket, symbols: ArrayList<Data>)
	}

	private var onSocketEventListener = object : OnSocketEventListener {
		override fun onClose() {
			Logger.e("onClose")
			onCloseListener?.onClose(this@BaseSocket)
		}

		override fun onConnect() {
			Logger.e("onConnect")
			onConnectListener?.onConnect(this@BaseSocket)
		}

		override fun onDisconnect() {
			Logger.e("onDisconnect")
			onDisconnectListener?.onDisconnect(this@BaseSocket)
		}

		override fun onSend(message: String) {
			onSendListener?.onSend(this@BaseSocket, message)
		}

		override fun onReceive(message: String) {
			onReceiveListener?.onReceive(this@BaseSocket, message)
		}

		override fun onSymbolsReceived(symbols: ArrayList<Data>) {
			onSymbolsReceivedListener?.onSymbolsReceived(this@BaseSocket, symbols)
		}

		override fun onIndexesReceived(indexes: ArrayList<Data>) {
			onIndexesReceivedListener?.onIndexesReceived(this@BaseSocket, indexes)
		}

		override fun onIndexesUpdated(indexes: ArrayList<Data>) {
			onIndexesUpdatedListener?.onIndexesUpdated(this@BaseSocket, indexes)
		}

		override fun onSymbolUpdated(symbols: ArrayList<Data>) {
			onSymbolUpdatedListener?.onSymbolUpdated(this@BaseSocket, symbols)
		}
	}
	private var onConnectListener: OnConnectListener? = null
	private var onCloseListener: OnCloseListener? = null
	private var onDisconnectListener: OnDisconnectListener? = null
	private var onReceiveListener: OnReceiveListener? = null
	private var onSendListener: OnSendListener? = null
	private var onSymbolsReceivedListener: OnSymbolsReceivedListener? = null
	private var onIndexesReceivedListener: OnIndexesReceivedListener? = null
	private var onIndexesUpdatedListener: OnIndexesUpdatedListener? = null
	private var onSymbolUpdatedListener: OnSymbolUpdatedListener? = null

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

	fun onSymbolsReceived(cb: BaseSocket.(symbols: ArrayList<Data>) -> Unit) {
		object : OnSymbolsReceivedListener {
			override fun onSymbolsReceived(socket: BaseSocket, symbols: ArrayList<Data>) =
				cb.invoke(socket, symbols)
		}.also { this.onSymbolsReceivedListener = it }
	}

	fun onIndexesReceived(cb: BaseSocket.(symbols: ArrayList<Data>) -> Unit) {
		object : OnIndexesReceivedListener {
			override fun onIndexesReceived(socket: BaseSocket, indexes: ArrayList<Data>) =
				cb.invoke(socket, indexes)
		}.also { this.onIndexesReceivedListener = it }
	}

	fun onIndexesUpdated(cb: BaseSocket.(symbols: ArrayList<Data>) -> Unit) {
		object : OnIndexesUpdatedListener {
			override fun onIndexesUpdated(socket: BaseSocket, indexes: ArrayList<Data>) =
				cb.invoke(socket, indexes)
		}.also { this.onIndexesUpdatedListener = it }
	}

	fun onSymbolUpdated(cb: BaseSocket.(symbols: ArrayList<Data>) -> Unit) {
		object : OnSymbolUpdatedListener {
			override fun onSymbolUpdated(socket: BaseSocket, symbols: ArrayList<Data>) =
				cb.invoke(socket, symbols)
		}.also { this.onSymbolUpdatedListener = it }
	}

	fun connect() {
		if (isClosed) runCatching {
			runBlocking {
				_socket = Socket("192.168.1.35", 8888).apply { soTimeout = 86_400_000 }
			}
			outPrintWriter = PrintWriter(outputStream!!)
			// setTime
			CoroutineScope(Dispatchers.IO).launch { _receive() }
		}
		_isClosed = false
		Logger.i("isClosed : $_isClosed")
		Logger.i(_socket.toString())
		// onSocketEventListener.onConnect()
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

	val String.convertSocketLine: SocketOutput
		@Throws(JsonSyntaxException::class)
		get() = gsonBuilder.fromJson(this.trim(), object : TypeToken<SocketOutput>() {}.type)

	private var x = 1000L
	private fun _receive() {
		if (_socket == null) {
			CoroutineScope(Dispatchers.IO).launch {
				Logger.e(
					Exception("SHOULD NOT HAPPEN", Throwable("_SOCKET IS NULL!!")),
					"SHOULD NOT HAPPEN",
				)
				Logger.i("retrying")
				x += x * 2
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
							Logger.i("line: $line")
						}.onFailure { Logger.e(it, it.message ?: "") }
					}
				}
			}
		}.onFailure { Logger.e(it, it.message ?: "") }
		_close()
	}

	private fun String._send() {
		if (outPrintWriter == null) {
			CoroutineScope(Dispatchers.IO).launch {
				Logger.e(
					Exception("SHOULD NOT HAPPEN", Throwable("_SOCKET IS NULL!!")), "SHOULD NOT HAPPEN",
				)
				Logger.i("retrying")
				x += x * 2
				delay(x)
				connect()
			}
			return
		}
		x = 1000L
		runCatching {
			runBlocking {
				try {
					outPrintWriter!!.print(this@_send)
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

	val setTime: String
		get() = "time${System.currentTimeMillis().div(1000)}"

	val readAndFlush: String
		get() = "readAndFlush"

	val readVoltage: String
		get() = "readVoltage"

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