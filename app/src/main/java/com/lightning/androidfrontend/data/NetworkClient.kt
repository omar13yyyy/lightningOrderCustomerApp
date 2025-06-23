import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object NetworkClient {

    private val client = OkHttpClient()
    val gson = Gson()
    private suspend fun Call.await(): Response = suspendCancellableCoroutine { cont ->
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (cont.isCancelled) return
                cont.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                cont.resume(response)
            }
        })

        cont.invokeOnCancellation {
            try {
                cancel()
            } catch (_: Throwable) {}
        }
    }

    // الآن دالة suspend تقوم بالطلب
    suspend  fun<T,  U>  makeRequest(
        method: String, // "GET" or "POST"
        url: String,
        bodyData: T? = null,
        headers: Map<String, String> = emptyMap() ,
        responseType: Type
    ): U? {
        try {
            val bodyJson: String = gson.toJson(bodyData)
            val requestBody = bodyJson?.let {
                it.toRequestBody("application/json".toMediaTypeOrNull())
            }
            val host = "http://192.168.110.167:8000/api/v1/$url"
            Log.d("debugTag", "bodyJson : $bodyJson")
            Log.d("debugTag", "headers $headers")

            val requestBuilder = Request.Builder()
                .url(host)


            when (method.uppercase()) {
                "POST" -> requestBuilder.post(requestBody ?: emptyBody())
                "GET" -> requestBuilder.get()
                else -> throw IllegalArgumentException("Unsupported method: $method")
            }

            for ((key, value) in headers) {
                requestBuilder.addHeader(key, value)
            }

            val request = requestBuilder.build()

            Log.d("debugTag",
                "sending request ${request.method} to: ${request.url}")

            val response = client.newCall(request).await()

            Log.d("debugTag", "after request send")

            val responseBody = response.body?.string()
            if (responseBody != null) {
                Log.d("debugTag", responseBody)
            }
            return if (responseBody != null) {
                gson.fromJson(responseBody, responseType)
            } else null

        } catch (e: Exception) {
            Log.e("debugTag", "Request failed: ${e.message}", e)
            e.printStackTrace()
            return null
        }
    }

    fun emptyBody(): RequestBody =
        RequestBody.create(null, ByteArray(0))
}
