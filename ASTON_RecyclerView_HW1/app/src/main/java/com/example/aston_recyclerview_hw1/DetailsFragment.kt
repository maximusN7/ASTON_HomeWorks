package com.example.aston_recyclerview_hw1

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.aston_recyclerview_hw1.utils.Contact
import com.squareup.picasso.Picasso


private const val ARG_CONTACT_DETAILS = "detailsList"
private const val ARG_CONTACT_NUMBER = "number"

class DetailsFragment : Fragment() {

    private var number: Int? = null
    private var detailsList: Array<String>? = null
    private var dualMode: Boolean = false

    interface OnSaveChangesListener {
        fun saveChanges(number: Int, details: Contact)
    }

    private var saveChangesListener: OnSaveChangesListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            number = it.getInt(ARG_CONTACT_NUMBER)
            detailsList = it.getStringArray(ARG_CONTACT_DETAILS)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        val detailsFrame: View? = activity.findViewById(R.id.fragment_container_view2)
        dualMode = (detailsFrame != null
                && detailsFrame.visibility == View.VISIBLE)
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
        val editViewAvatar = view.findViewById<EditText>(R.id.editTextAvatar)
        val imageViewAvatar = view.findViewById<ImageView>(R.id.imageViewAvatar)

        editViewName.setText(detailsList?.get(1) ?: "")
        editViewLastName.setText(detailsList?.get(2) ?: "")
        editViewNumber.setText(detailsList?.get(3) ?: "")
        editViewDetails.setText(detailsList?.get(4) ?: "")
        editViewAvatar.setText(detailsList?.get(5) ?: "")
        Picasso.with(context)
            .load(detailsList?.get(5))
            .into(imageViewAvatar)


        val buttonSave: Button = view.findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveChangesListener?.saveChanges(
                number!!, Contact(
                    detailsList?.get(0)!!.toInt(),
                    editViewName.text.toString(),
                    editViewLastName.text.toString(),
                    editViewNumber.text.toString(),
                    editViewDetails.text.toString(),
                    editViewAvatar.text.toString(),
                    dualMode
                )
            )
        }

        editViewAvatar.setOnEditorActionListener(TextView.OnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val picasso = Picasso.Builder(context)
                    .listener { _, _, _ ->
                        imageViewAvatar.setImageResource(android.R.drawable.ic_delete)
                    }
                    .build()
                picasso.load(editViewAvatar.text.toString())
                    .into(imageViewAvatar)

                return@OnEditorActionListener true
            }
            false
        })

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