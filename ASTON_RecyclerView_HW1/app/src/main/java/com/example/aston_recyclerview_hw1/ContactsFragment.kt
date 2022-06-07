package com.example.aston_recyclerview_hw1

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_recyclerview_hw1.MainActivity.Companion.contactsList
import com.example.aston_recyclerview_hw1.utils.Contact
import com.example.aston_recyclerview_hw1.utils.ContactsAdapter
import com.example.aston_recyclerview_hw1.utils.ContactsDiffUtilCallback
import com.example.aston_recyclerview_hw1.utils.RecyclerDecoration
import java.util.*


class ContactsFragment : Fragment(R.layout.fragment_contacts), ContactsAdapter.CellClickListener,
    ContactsAdapter.TaskListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactsAdapter
    var textFromSearch: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_contacts)
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.divider_drawable
            )!!
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        val sidePadding = 10
        val topPadding = 5
        recyclerView.addItemDecoration(RecyclerDecoration(sidePadding, topPadding))

        adapter = ContactsAdapter((activity as AppCompatActivity), contactsList, this, this)
        recyclerView.adapter = adapter

        val oldList = contactsList.map { it.copy() }
        for (i in 0 until contactsList.size) {
            contactsList[i].colored = false
        }
        updateContactToRecyclerView(oldList)

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                textFromSearch = query.toString()
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                textFromSearch = query.toString()
                filter(query.toString())
                return false
            }
        })
    }

    fun filter(text: String) {
        val filteredNames: ArrayList<Contact> = ArrayList()

        for (i in 0 until contactsList.size) {
            val namePlusLastname = "${contactsList[i].name} ${contactsList[i].lastName}"
            if (namePlusLastname.lowercase().contains(text.lowercase())) {
                filteredNames.add(contactsList[i])
            }
        }

        adapter.filterList(filteredNames)
    }

    override fun onCellClickListener(
        number: Int,
        detailsList: Array<String>?,
        coloredLayout: LinearLayout
    ) {
        val fragmentDetails = DetailsFragment.newInstance(number, detailsList)

        val fTrans = parentFragmentManager.beginTransaction()


        val detailsFrame: View? = activity?.findViewById(R.id.fragment_container_view2)
        val dualMode = (detailsFrame != null
                && detailsFrame.visibility == View.VISIBLE)
        val oldList = contactsList.map { it.copy() }
        if (dualMode) {

            for (i in 0 until contactsList.size) {
                val list = contactsList[i]
                list.colored = false
                contactsList[i] = list
            }
            val list = contactsList[number]
            list.colored = true
            contactsList[number] = list

            updateContactToRecyclerView(oldList)

            coloredLayout.setBackgroundColor(context?.getColor(R.color.pressed_cell) ?: 1)
            if (list.colored) {
                coloredLayout.setBackgroundColor(context?.getColor(R.color.pressed_cell) ?: 1)
            } else {
                coloredLayout.setBackgroundColor(context?.getColor(R.color.white_cell) ?: 1)
            }

            fTrans.replace(R.id.fragment_container_view2, fragmentDetails, "DetailsFragment")
            fTrans.commit()
        } else {
            updateContactToRecyclerView(oldList)

            val contactFragment: Fragment =
                parentFragmentManager.findFragmentByTag("ContactsFragment") as Fragment
            fTrans.hide(contactFragment)
            fTrans.add(R.id.fragment_container_view, fragmentDetails, "DetailsFragment")
            fTrans.addToBackStack(null)
            fTrans.commit()
        }
    }

    override fun onLongPressed(view: View?, number: Int, detailsList: Array<String>) {
        //Log.d("Check111", "Long pressed: ${detailsList[1]}")
        val myDialogFragment =
            AlertDialogDeleteContact(
                textFromSearch,
                recyclerView.adapter,
                parentFragmentManager,
                detailsList
            )
        myDialogFragment.show(childFragmentManager, "DeleteDialog")
    }

    fun updateContactToRecyclerView(oldList: List<Contact>) {
        val productDiffUtilCallback = ContactsDiffUtilCallback(oldList, contactsList)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
        recyclerView.adapter?.let { productDiffResult.dispatchUpdatesTo(it) }
    }

}