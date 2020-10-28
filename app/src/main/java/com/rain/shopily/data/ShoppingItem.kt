package com.rain.shopily.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "itemName") var itemName: String,
    @ColumnInfo(name = "category") var category: Int,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "estimatedPrice") var estimatedPrice: Int,
    @ColumnInfo(name = "bought") var bought: Boolean
) : Serializable