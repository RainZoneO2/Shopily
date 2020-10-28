package com.rain.shopily.data

import androidx.room.*

@Dao
interface ShoppingItemDAO {
    @Query("SELECT * FROM items")
    fun getAllItems(): List<ShoppingItem>

    @Insert
    fun insertItem(todo: ShoppingItem) : Long

    @Delete
    fun deleteItem(todo: ShoppingItem)

    @Update
    fun updateItem(todo: ShoppingItem)

}