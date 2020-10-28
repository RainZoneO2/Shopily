package com.rain.shopily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.rain.shopily.data.ShoppingItem

class NewItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        var btnAdd = findViewById(R.id.ok_button) as Button
        var btnCancel = findViewById(R.id.cancel_button) as Button

        btnAdd.setOnClickListener {
            val intent = Intent(this, ScrollingActivity::class.java)
            startActivity(intent)
        }

        btnCancel.setOnClickListener {
            val intent = Intent(this, ScrollingActivity::class.java)
            startActivity(intent)
        }
    }

    //TODO: Implement functions for adding and cancelling the action
}