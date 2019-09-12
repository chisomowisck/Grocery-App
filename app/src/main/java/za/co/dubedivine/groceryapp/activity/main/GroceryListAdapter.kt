package za.co.dubedivine.groceryapp.activity.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.dubedivine.groceryapp.R
import za.co.dubedivine.groceryapp.data.remote.GroceryServiceFactory
import za.co.dubedivine.groceryapp.model.GroceryItem
import za.co.dubedivine.groceryapp.model.responseModel.StatusResponseEntity

class GroceryListAdapter(private val groceryItems: ArrayList<GroceryItem>): RecyclerView.Adapter<GroceryListAdapter.GroceryItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery_item, parent, false)
        return GroceryItemViewHolder(view)
    }

    override fun getItemCount() = groceryItems.size

    override fun onBindViewHolder(holder: GroceryItemViewHolder, position: Int) {
        holder.bind(groceryItems[position])
    }

    class GroceryItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val tvGroceryItemName = view.findViewById<TextView>(R.id.tv_grocery_item_name)
        private val checkBoxIsAvailable = view.findViewById<CheckBox>(R.id.checkbox_is_available)
        private val viewIsAvailableBar = view.findViewById<TextView>(R.id.tv_bar)


        fun bind(groceryItem: GroceryItem) {
            tvGroceryItemName.text = groceryItem.name
            checkBoxIsAvailable.isChecked = groceryItem.isAvailable
            checkBoxIsAvailable.text = if (groceryItem.isAvailable) "Finished" else "Available"
            if (!groceryItem.isAvailable)
                viewIsAvailableBar.setBackgroundColor(ResourcesCompat.getColor(itemView.context.resources, R.color.colorAccent, null))
            else
                viewIsAvailableBar.setBackgroundColor(ResourcesCompat.getColor(itemView.context.resources, R.color.colorPrimary, null))

            checkBoxIsAvailable.setOnCheckedChangeListener { _, checked ->
                GroceryServiceFactory.makeService().toggleGroceryItemAvailability(checked, groceryItem.id).enqueue(object: Callback<StatusResponseEntity<Boolean>?> {
                    override fun onFailure(call: Call<StatusResponseEntity<Boolean>?>, t: Throwable) {
                        Toast.makeText(itemView.context, "Failed to update item", Toast.LENGTH_LONG).show()
                        checkBoxIsAvailable.isChecked = !checked
                    }

                    override fun onResponse(
                        call: Call<StatusResponseEntity<Boolean>?>,
                        response: Response<StatusResponseEntity<Boolean>?>
                    ) {
                        if(response.body() != null) {
                            Toast.makeText(itemView.context, response.body()?.message, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(itemView.context, "Failed to update item", Toast.LENGTH_LONG).show()
                            checkBoxIsAvailable.isChecked = !checked
                        }
                    }
                })
            }
        }
    }

    fun addAll(groceryItems: List<GroceryItem>) {
        this.groceryItems.addAll(groceryItems)
        notifyDataSetChanged()
    }

    fun clear() {
        this.groceryItems.clear()
        notifyDataSetChanged()
    }

    fun add(item: GroceryItem) {
        this.groceryItems.add(item)
        notifyDataSetChanged()
    }
}