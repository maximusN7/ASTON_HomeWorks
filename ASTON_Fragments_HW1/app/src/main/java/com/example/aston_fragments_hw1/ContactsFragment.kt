package com.example.aston_fragments_hw1

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aston_fragments_hw1.MainActivity.Companion.contactsMap
import com.google.android.material.card.MaterialCardView

private const val ARG_PACKAGENAME = "packageName"

class ContactsFragment: Fragment(R.layout.fragment_contacts) {

    private var packageName: String? = null

    interface OnOpenDetailsListener {
        fun openDetails(number: Int?, detailsList: Array<String>?)
    }

    var openDetailsListener: OnOpenDetailsListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            packageName = it.getString(ARG_PACKAGENAME)
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        openDetailsListener = try {
            activity as OnOpenDetailsListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement onSomeEventListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadContactsToScroll(view)

    }

    private fun loadContactsToScroll(view: View) {
        for ((number, infoList) in contactsMap) {
            val textNamePlusLastName: TextView = view.findViewById(resources.getIdentifier("txt_contact_name$number", "id", packageName))
            val namePlusLastName = "${infoList?.get(0)} ${infoList?.get(1)}"
            textNamePlusLastName.text = namePlusLastName

            val textNumber: TextView = view.findViewById(resources.getIdentifier("txt_phone$number", "id", packageName))
             textNumber.text = infoList?.get(2) ?: ""

            val contactCell: MaterialCardView = view.findViewById(resources.getIdentifier("contact_card$number", "id", packageName))
            contactCell.setOnClickListener {
                openDetailsListener?.openDetails(number, contactsMap.get(number))
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(packageName: String) =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PACKAGENAME, packageName)
                }
            }
    }


}