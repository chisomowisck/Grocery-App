package za.co.dubedivine.groceryapp.data.remote

import retrofit2.http.*
import za.co.dubedivine.groceryapp.model.GroceryItem
import za.co.dubedivine.groceryapp.model.responseModel.StatusResponseEntity

interface GroceryService {

    @GET("groceries")
    suspend fun getGroceriesList(): MutableIterable<GroceryItem>

    @PUT("groceries")
    suspend fun addGroceryItem(@Body groceryItem: GroceryItem): StatusResponseEntity<GroceryItem>

    @DELETE("groceries/{id}")
    suspend fun deleteGroceryItem(@Path("id") id: Long): StatusResponseEntity<Boolean>

    @POST("groceries/available")
    suspend fun toggleGroceryItemAvailability(@Body availability: Boolean, @Query("id") id: Long): StatusResponseEntity<Boolean>
}