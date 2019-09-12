package za.co.dubedivine.groceryapp.activity.main

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.co.dubedivine.groceryapp.R
import za.co.dubedivine.groceryapp.data.remote.GroceryServiceFactory
import za.co.dubedivine.groceryapp.model.GroceryItem
import za.co.dubedivine.groceryapp.model.responseModel.StatusResponseEntity

class MainActivity : AppCompatActivity() {

    val groceryItemAdapter = GroceryListAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val dialog = setUpAddNewGroceryListItemDialog()
        fab.setOnClickListener {
            dialog.show()
        }

        swipe_refresh_grocery_items.isEnabled = true
        recycler_view_grocery_items.layoutManager = LinearLayoutManager(this)
        recycler_view_grocery_items.adapter = groceryItemAdapter
        setUpAddNewGroceryListItemDialog()
        loadGroceriesList()
    }

    private fun setUpAddNewGroceryListItemDialog(): AlertDialog {
        val view = LayoutInflater.from(this).inflate(R.layout.alert_create_new_grocery_item, null)
        val adb = AlertDialog.Builder(this)
            .setTitle("Add A New Grocery Item")
            .setPositiveButton("OK") { dialog, _ -> postNewGroceryItemToServer(dialog, view) }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        val dialog = adb.setView(view).create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    private fun postNewGroceryItemToServer(dialog: DialogInterface, view: View) {
        val etItemName = view.findViewById<EditText>(R.id.et_grocery_item_name)
        val checkBoxIsAvailable = view.findViewById<CheckBox>(R.id.checkbox_grocery_item_is_available)
        val groceryItem = GroceryItem(etItemName.text.toString(), checkBoxIsAvailable.isChecked)

        swipe_refresh_grocery_items.isRefreshing = true

        GroceryServiceFactory.makeService().addGroceryItem(groceryItem).enqueue(object: Callback<StatusResponseEntity<GroceryItem>?> {
            override fun onFailure(call: Call<StatusResponseEntity<GroceryItem>?>, t: Throwable) {
                swipe_refresh_grocery_items.isRefreshing = false
                Toast.makeText(this@MainActivity, "Opps somethng went wrong please check your internet connection", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<StatusResponseEntity<GroceryItem>?>,
                response: Response<StatusResponseEntity<GroceryItem>?>
            ) {
                swipe_refresh_grocery_items.isRefreshing = false
                if (response.body() != null && response.body()?.status == true) { // our business rule is that if status is true then entity is not null
                    groceryItemAdapter.add(response.body()!!.entity!!)
                } else {
                    Toast.makeText(this@MainActivity, "Something went terribly wrong", Toast.LENGTH_LONG).show()
                }
            }
        })
        dialog.dismiss()
    }

    private fun loadGroceriesList() {
            swipe_refresh_grocery_items.isRefreshing = true
            GroceryServiceFactory.makeService().getGroceriesList().enqueue(object: retrofit2.Callback<List<GroceryItem>?> {
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
                    groceryItemAdapter.addAll(items)
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
