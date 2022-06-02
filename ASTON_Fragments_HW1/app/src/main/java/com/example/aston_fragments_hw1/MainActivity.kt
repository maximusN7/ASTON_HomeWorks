package com.example.aston_fragments_hw1

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity(), ContactsFragment.OnOpenDetailsListener,
    DetailsFragment.OnSaveChangesListener {

    private lateinit var fragmentContacts: Fragment
    private lateinit var fragmentDetails: Fragment
    private lateinit var fTrans: FragmentTransaction
    private var dualMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val detailsFrame: View? = findViewById(R.id.fragment_container_view2)
        dualMode = (detailsFrame != null
                && detailsFrame.visibility == View.VISIBLE)

        fragmentContacts = ContactsFragment.newInstance(packageName)

        fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fragment_container_view, fragmentContacts)
        fTrans.commit()

    }

    override fun openDetails(number: Int?, detailsList: Array<String>?) {
        fragmentDetails = DetailsFragment.newInstance(number, detailsList)

        fTrans = supportFragmentManager.beginTransaction()

        if (dualMode) {
            colorizeCell(number)
            fTrans.replace(R.id.fragment_container_view2, fragmentDetails)
            fTrans.commit()
        } else {
            fTrans.replace(R.id.fragment_container_view, fragmentDetails)
            fTrans.addToBackStack(null)
            fTrans.commit()
        }
    }

    override fun saveChanges(number: Int?, detailsList: Array<String>?) {
        contactsMap[number!!] = detailsList
        if (dualMode) {
            val namePlusLast = "${detailsList?.get(0)} ${detailsList?.get(1)}"
            (fragmentContacts.requireView().findViewById<TextView?>(
                resources.getIdentifier(
                    "txt_contact_name$number",
                    "id",
                    packageName
                )
            )).text =
                namePlusLast
            (fragmentContacts.requireView().findViewById<TextView?>(
                resources.getIdentifier(
                    "txt_phone$number",
                    "id",
                    packageName
                )
            )).text =
                detailsList?.get(2) ?: ""

            fragmentDetails = DetailsFragment.newInstance(number, detailsList)
            fTrans = supportFragmentManager.beginTransaction()
            fTrans.replace(R.id.fragment_container_view2, fragmentDetails)
            fTrans.commit()
        } else {
            onBackPressed()
        }

    }


    private fun colorizeCell(number: Int?) {
        for (i in 1..contactsMap.size) {
            (fragmentContacts.requireView().findViewById<LinearLayout?>(
                resources.getIdentifier(
                    "colored_layout$i",
                    "id",
                    packageName
                )
            )).setBackgroundColor(getColor(R.color.white_cell))
        }
        (fragmentContacts.requireView().findViewById<LinearLayout?>(
            resources.getIdentifier(
                "colored_layout$number",
                "id",
                packageName
            )
        )).setBackgroundColor(getColor(R.color.pressed_cell))
    }

    companion object {
        var contactsMap = mutableMapOf<Int, Array<String>?>(
            1 to arrayOf("Liara", "T'Soni", "89211105040", "Archaeologist and protean specialist"),
            2 to arrayOf("Commander", "Shepherd", "89213334151", "Saviour of the galaxy"),
            3 to arrayOf("Garrus", "Vakarian", "89215325431", "Rogue C-Sec operative"),
            4 to arrayOf("Tali'Zorah", "nar Rayya", "89601214536", "Gifted engineer"),
            5 to arrayOf("Kaidan", "Alenko", "89316627623", " Systems Alliance Marine"),
            6 to arrayOf("Ashley", "Williams", "89992317694", " Systems Alliance Marine"),
            7 to arrayOf("Rex", "Urdnot", "89416388285", " Krogan mercenary and bounty hunter"),
            8 to arrayOf("Karin", "Chakwas", "89994127594", "Flight medic")
        )
    }
}