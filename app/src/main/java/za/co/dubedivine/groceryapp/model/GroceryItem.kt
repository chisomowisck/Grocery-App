package za.co.dubedivine.groceryapp.model

import com.google.gson.annotations.SerializedName

data class GroceryItem(var name: String,
                       @SerializedName("available") var isAvailable: Boolean) {

    var id: Long = 0L

    constructor(): this ("", false)
}