package com.rain.shopily

import android.content.Intent
import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.rain.shopily.adapter.ShoppingAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import com.rain.shopily.data.AppDatabase
import com.rain.shopily.data.ShoppingItem
import com.rain.shopily.interaction.ItemRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), ItemDialog.ItemHandler {
//TODO: CHANGE ADD ITEM TO BE A DIFFERENT ACTIVITY AND EDIT TO BE A DIALOG
//TODO: ADD FILTER FEATURE AND SEARCH FEATURE
//TODO: ADD TABLAYOUT AND VIEWPAGER FOR CURRENT ITEMS AND FINISHED ITEMS (ANIMATION: THE SLIDING ITEM WHEN DONE IS CLICKED)

    lateinit var shoppingAdapter : ShoppingAdapter
    lateinit var newItem : Intent
    companion object {
        const val KEY_EDIT = "KEY_EDIT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        Thread {
            var itemList = AppDatabase.getInstance(this).itemDao().getAllItems()

            runOnUiThread {
                shoppingAdapter = ShoppingAdapter(this, itemList)
                recyclerList.adapter = shoppingAdapter

                val touchCallbackList = ItemRecyclerTouchCallback(shoppingAdapter)
                val itemTouchHelper = ItemTouchHelper(touchCallbackList)
                itemTouchHelper.attachToRecyclerView(recyclerList)
            }
        }.start()


        //newItem = Intent(this, R.id.itemScreen::class.java)
    }

    fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, "Dialog")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.addItem ->
                showAddItemDialog()
                //startActivity(newItem);
            R.id.clearList -> shoppingAdapter.clearItems()
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun itemCreated(item: ShoppingItem) {
        saveItem(item)
    }

    private fun saveItem(item: ShoppingItem) {
        Thread {
            AppDatabase.getInstance(this).itemDao().insertItem(item)

            runOnUiThread {
                shoppingAdapter.addItem(item)
            }
        }.start()
    }

    var editIndex : Int = -1

    fun showEditItemDialog(itemToEdit: ShoppingItem, index: Int) {
        editIndex = index
        val editItemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT, itemToEdit)
        editItemDialog.arguments = bundle
        editItemDialog.show(supportFragmentManager, "EDITDIALOG")
    }

    override fun itemUpdated(item: ShoppingItem) {
        Thread {
            AppDatabase.getInstance(this).itemDao().updateItem(item)

            runOnUiThread {
                shoppingAdapter.updateItem(item, editIndex)
            }
        }.start()
    }
}