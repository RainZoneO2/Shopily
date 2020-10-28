package com.rain.shopily.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rain.shopily.R
import com.rain.shopily.ScrollingActivity
import com.rain.shopily.data.AppDatabase
import com.rain.shopily.data.ShoppingItem
import com.rain.shopily.interaction.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.shopping_item.view.*
import java.util.*

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>, ItemTouchHelperCallback {

    var shoppingItems = mutableListOf<ShoppingItem>()
    val context: Context

    constructor(context: Context, listTodos: List<ShoppingItem>) {
        this.context = context
        shoppingItems.addAll(listTodos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.shopping_item, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = shoppingItems[position]

        holder.tvName.text = currentItem.itemName
        holder.tvDescription.text = currentItem.description
        holder.tvPrice.text = currentItem.estimatedPrice.toString() + "HUF"
        holder.cbBought.isChecked = currentItem.bought

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditItemDialog(
                shoppingItems[holder.adapterPosition], holder.adapterPosition
            )
        }

        holder.cbBought.setOnClickListener {
            shoppingItems[holder.adapterPosition].bought = holder.cbBought.isChecked
            Thread {
                AppDatabase.getInstance(context).itemDao().updateItem(shoppingItems[holder.adapterPosition])
            }.start()
        }

        if (shoppingItems[holder.adapterPosition].category == 0) {
            holder.ivIcon.setImageResource(R.drawable.food)
            holder.itemView.card_view.setCardBackgroundColor(Color.CYAN)
        } else if (shoppingItems[holder.adapterPosition].category == 1) {
            holder.ivIcon.setImageResource(R.drawable.books)
            holder.itemView.card_view.setCardBackgroundColor(Color.GREEN)
        } else if (shoppingItems[holder.adapterPosition].category == 2) {
            holder.ivIcon.setImageResource(R.drawable.electronics)
            holder.itemView.card_view.setCardBackgroundColor(Color.MAGENTA)
        } else if (shoppingItems[holder.adapterPosition].category == 3) {
            holder.ivIcon.setImageResource(R.drawable.t_shirt)
            holder.itemView.card_view.setCardBackgroundColor(Color.YELLOW)
        }
    }

    private fun deleteItem(position: Int) {
        Thread {
            AppDatabase.getInstance(context).itemDao().deleteItem(
                shoppingItems.get(position)
            )
            (context as ScrollingActivity).runOnUiThread {
                shoppingItems.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    fun addItem(item: ShoppingItem) {
        shoppingItems.add(item)
        notifyItemInserted(shoppingItems.lastIndex)
    }

    fun updateItem(item: ShoppingItem, editIndex: Int) {
        shoppingItems[editIndex] = item
        notifyItemChanged(editIndex)
    }

    fun clearItems() {
//        Thread {
//            AppDatabase.getInstance(context).itemDao().
//        }

        shoppingItems.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.tvName
        val cbBought = itemView.cbBought
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val ivIcon = itemView.ivIcon
        val tvPrice = itemView.tvPrice
        val tvDescription = itemView.tvDescription

    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}