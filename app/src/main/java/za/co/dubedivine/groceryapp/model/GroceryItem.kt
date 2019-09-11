package za.co.dubedivine.groceryapp.model

data class GroceryItem(var name: String, var isAvailable: Boolean) {

    var id: Long = 0L

    constructor(): this ("", false)
}