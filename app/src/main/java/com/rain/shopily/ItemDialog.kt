package com.rain.shopily

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.rain.shopily.data.ShoppingItem
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.shopping_item.*

class ItemDialog : DialogFragment() {

    interface ItemHandler{
        fun itemCreated(item: ShoppingItem)
        fun itemUpdated(item: ShoppingItem)
    }

    lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(
                "The activity isn't implementing the ItemHandler interface!"
            )
        }
    }

    lateinit var etItemName: EditText
    lateinit var etItemDescription: EditText
    //lateinit var cbItemBought: CheckBox
    lateinit var spinnerCategory: Spinner
    lateinit var etItemPrice : EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Add Item")
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.item_dialog, null
        )

        etItemName = dialogView.etItemName
        etItemPrice = dialogView.etItemPrice
        etItemDescription = dialogView.etItemDescription
        //cbItemBought = dialogView.cbBought
        spinnerCategory = dialogView.spinnerCategory

        var categoryAdapter = ArrayAdapter.createFromResource(
            context!!,
            R.array.categories,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerCategory.adapter = categoryAdapter
        spinnerCategory.setSelection(1)

        dialogBuilder.setView(dialogView)

        val arguments = this.arguments

        // if we are in EDIT mode
        if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_EDIT)) {
            val Item = arguments.getSerializable(ScrollingActivity.KEY_EDIT) as ShoppingItem

            etItemName.setText(Item.itemName)
            etItemDescription.setText(Item.description)
            etItemPrice.setText(Item.estimatedPrice.toString())
            spinnerCategory.setSelection(Item.category)
            //cbBought.isChecked = Item.bought

            dialogBuilder.setTitle("Edit Item")
        }

        dialogBuilder.setPositiveButton("Ok") {
                dialog, which ->
        }
        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }

        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()
        //TODO: ADD ALL FIELDS AS MUST WRITE, CANNOT BE NULL OR ELSE CRASH
        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etItemName.text.isNotEmpty() && etItemPrice.text.isNotEmpty()) {
                val arguments = this.arguments
                // IF EDIT MODE
                if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog!!.dismiss()
            } else {
                if (etItemPrice.text.isEmpty())
                    etItemName.error = "This field can not be empty"
                else if (etItemPrice.text.isEmpty())
                    etItemPrice.error = "This field can not be empty"
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
            ShoppingItem(null,
                etItemName.text.toString(),
                spinnerCategory.selectedItemPosition,
                etItemDescription.text.toString(),
                Integer.parseInt(etItemPrice.getText().toString()),
                false)
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_EDIT
        ) as ShoppingItem
        itemToEdit.itemName = etItemName.text.toString()
        itemToEdit.description = etItemDescription.text.toString()
        //itemToEdit.bought = cbBought.isChecked
        itemToEdit.estimatedPrice = Integer.parseInt(etItemPrice.getText().toString())
        itemToEdit.category = spinnerCategory.selectedItemPosition

        itemHandler.itemUpdated(itemToEdit)
    }
}