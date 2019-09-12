package za.co.dubedivine.groceryapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient

object GroceryServiceFactory {

    private const val API_BASE_URL = "http://10.72.78.124:8080/"

    // add the logging interceptor

    fun makeService(): GroceryService {
        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        return builder
            .client(httpClient.build())
            .build().create(GroceryService::class.java)
    }
}