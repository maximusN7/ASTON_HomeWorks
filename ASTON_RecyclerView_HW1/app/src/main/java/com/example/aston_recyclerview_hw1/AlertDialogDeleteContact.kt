package com.example.aston_recyclerview_hw1

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_recyclerview_hw1.MainActivity.Companion.contactsList
import java.lang.NullPointerException


class AlertDialogDeleteContact(
    private val textFromSearch: String,
    private val recyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?,
    private val fManager: FragmentManager,
    private val detailsList: Array<String>
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_text)
                .setPositiveButton(R.string.delete) { dialog, _ ->

                    val fragmentContacts: ContactsFragment? =
                        parentFragment as ContactsFragment?
                    val oldList = contactsList.map { it.copy() }
                    var needToDelete = 0
                    for (i in 0 until contactsList.size) {
                        if (detailsList[0].toInt() == contactsList[i].id) needToDelete = i
                    }

                    contactsList.remove(contactsList[needToDelete])
                    fragmentContacts?.updateContactToRecyclerView(oldList)
                    fragmentContacts?.filter(textFromSearch)
                    recyclerAdapter?.notifyItemRangeChanged(0, contactsList.size)

                    val detailsFrame: View? = activity?.findViewById(R.id.fragment_container_view2)
                    val dualMode = (detailsFrame != null
                            && detailsFrame.visibility == View.VISIBLE)
                    if (dualMode) {
                        try {
                            val fragmentDetails: Fragment =
                                fManager.findFragmentByTag("DetailsFragment") as Fragment
                            val fTrans = fManager.beginTransaction()

                            fTrans.remove(fragmentDetails)
                            fTrans.commit()
                        } catch (e: NullPointerException) {

                        }
                    }

                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}