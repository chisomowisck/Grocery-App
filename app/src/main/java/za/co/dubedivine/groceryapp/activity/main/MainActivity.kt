package za.co.dubedivine.groceryapp.activity.main

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Response
import za.co.dubedivine.groceryapp.R
import za.co.dubedivine.groceryapp.data.remote.GroceryServiceFactory
import za.co.dubedivine.groceryapp.model.GroceryItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        swipe_refresh_grocery_items.isEnabled = true
        recycler_view_grocery_items.layoutManager = LinearLayoutManager(this)

        loadGroceriesList()
    }

    private fun loadGroceriesList() {
            swipe_refresh_grocery_items.isRefreshing = true
            GroceryServiceFactory.makeservice().getGroceriesList().enqueue(object: retrofit2.Callback<List<GroceryItem>?> {
            override fun onFailure(call: Call<List<GroceryItem>?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong please check your network", Toast.LENGTH_LONG).show()
                swipe_refresh_grocery_items.isRefreshing = false
            }

            override fun onResponse(
                call: Call<List<GroceryItem>?>,
                response: Response<List<GroceryItem>?>
            ) {
                swipe_refresh_grocery_items.isRefreshing = false
                val items = response.body()
                if (items != null)  {
                    recycler_view_grocery_items.adapter = GroceryListAdapter(items)
                }
            }
        } )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
