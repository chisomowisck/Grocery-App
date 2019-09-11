package za.co.dubedivine.groceryapp.data.remote

import retrofit2.Call
import retrofit2.http.*
import za.co.dubedivine.groceryapp.model.GroceryItem
import za.co.dubedivine.groceryapp.model.responseModel.StatusResponseEntity

interface GroceryService {

    @GET("groceries")
    fun getGroceriesList(): Call<List<GroceryItem>>

    @PUT("groceries")
    fun addGroceryItem(@Body groceryItem: GroceryItem): Call<StatusResponseEntity<GroceryItem>>

    @DELETE("groceries/{id}")
    fun deleteGroceryItem(@Path("id") id: Long): Call<StatusResponseEntity<Boolean>>

    @POST("groceries/available")
    fun toggleGroceryItemAvailability(@Body availability: Boolean, @Query("id") id: Long): Call<StatusResponseEntity<Boolean>>
}