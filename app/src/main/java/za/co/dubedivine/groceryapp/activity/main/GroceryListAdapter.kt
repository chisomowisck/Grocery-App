package za.co.dubedivine.groceryapp.activity.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import za.co.dubedivine.groceryapp.R
import za.co.dubedivine.groceryapp.model.GroceryItem

class GroceryListAdapter(private val groceryItems: List<GroceryItem>): RecyclerView.Adapter<GroceryListAdapter.GroceryItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery_item, parent, false)
        return GroceryItemViewHolder(view)
    }

    override fun getItemCount() = groceryItems.size

    override fun onBindViewHolder(holder: GroceryItemViewHolder, position: Int) {
        holder.bind(groceryItems[position])
    }

    class GroceryItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        private val tvGroceryItemName = view.findViewById<TextView>(R.id.tv_grocery_item_name)
        private val tvGroceryItemIsAvailable = view.findViewById<TextView>(R.id.tv_is_available)
        private val viewIsAvailableBar = view.findViewById<TextView>(R.id.tv_bar)


        fun bind(groceryItem: GroceryItem) {
            tvGroceryItemName.text = groceryItem.name
            tvGroceryItemIsAvailable.text = if (groceryItem.isAvailable) "Available" else "Finished"
            if (!groceryItem.isAvailable) viewIsAvailableBar.setBackgroundColor(ResourcesCompat.getColor(itemView.context.resources, R.color.colorAccent, null))
        }
    }
}