package com.example.aston_fragments_hw1

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

private const val ARG_CONTACT_DETAILS = "detailsList"
private const val ARG_CONTACT_NUMBER = "number"

class DetailsFragment: Fragment() {

    private var number: Int? = null
    private var detailsList: Array<String>? = null


    interface OnSaveChangesListener {
        fun saveChanges(number: Int?, detailsList: Array<String>?)
    }
    var saveChangesListener: OnSaveChangesListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            number = it.getInt(ARG_CONTACT_NUMBER)
            detailsList = it.getStringArray(ARG_CONTACT_DETAILS)
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        saveChangesListener = try {
            activity as OnSaveChangesListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSaveChangesListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)
        val editViewName = view.findViewById<EditText>(R.id.editTextName)
        val editViewLastName = view.findViewById<EditText>(R.id.editTextLastName)
        val editViewNumber = view.findViewById<EditText>(R.id.editTextNumber)
        val editViewDetails = view.findViewById<EditText>(R.id.editTextDetails)

        editViewName.setText(detailsList?.get(0) ?: "")
        editViewLastName.setText(detailsList?.get(1) ?: "")
        editViewNumber.setText(detailsList?.get(2) ?: "")
        editViewDetails.setText(detailsList?.get(3) ?: "")

        // Inflate the layout for this fragment
        val buttonSave: Button = view.findViewById<View>(R.id.buttonSave) as Button
        buttonSave.setOnClickListener {
            val changedInfoList = arrayOf(editViewName.text.toString(), editViewLastName.text.toString(),
            editViewNumber.text.toString(), editViewDetails.text.toString())
            saveChangesListener?.saveChanges(number, changedInfoList)
        }

        return view
    }


    companion object {

        @JvmStatic
        fun newInstance(number: Int?, detailsList: Array<String>?) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CONTACT_NUMBER, number!!)
                    putStringArray(ARG_CONTACT_DETAILS, detailsList)
                }
            }
    }
}