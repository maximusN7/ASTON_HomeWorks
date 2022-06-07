package com.example.aston_recyclerview_hw1.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.aston_recyclerview_hw1.R
import com.squareup.picasso.Picasso


class ContactsAdapter(
    private var appCompatActivity: AppCompatActivity,
    private var contactsList: MutableList<Contact>,
    private val cellClickListener: CellClickListener,
    private val taskListener: TaskListener
) : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    interface CellClickListener {
        fun onCellClickListener(
            number: Int,
            detailsList: Array<String>?,
            coloredLayout: LinearLayout
        )
    }

    interface TaskListener {

        fun onLongPressed(view: View?, number: Int, detailsList: Array<String>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val h = MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_cell, parent, false)
        )
        h.listen { pos, _ ->
            val adapterPosition: Int = h.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                cellClickListener.onCellClickListener(
                    pos,
                    contactsList[pos].toStringArray(),
                    h.coloredLayout
                )
            }
        }

        return h
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val array = contactsList[position]
        val namePlusLastName = "${array.name} ${array.lastName}"
        holder.txtContactName.text = namePlusLastName
        holder.txtPhone.text = array.phoneNumber
        Picasso.with(appCompatActivity)
            .load(array.avatarUrl)
            .into(holder.imageViewAvatar)

        if (array.colored) {
            holder.coloredLayout.setBackgroundColor(appCompatActivity.getColor(R.color.pressed_cell))
        } else {
            holder.coloredLayout.setBackgroundColor(appCompatActivity.getColor(R.color.white_cell))
        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun filterList(filteredList: ArrayList<Contact>) {
        this.contactsList = filteredList
        notifyDataSetChanged()
    }

    private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        itemView.setOnLongClickListener { view: View? ->
            taskListener.onLongPressed(view, position, contactsList[position].toStringArray())
            true
        }
        return this
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var txtContactName: TextView
        var txtPhone: TextView
        var imageViewAvatar: ImageView
        var coloredLayout: LinearLayout

        override fun onClick(view: View) {
        }

        init {
            txtContactName = itemView.findViewById(R.id.txt_contact_name)
            txtPhone = itemView.findViewById(R.id.txt_phone)
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar)
            coloredLayout = itemView.findViewById(R.id.colored_layout)
            itemView.setOnClickListener(this)
        }
    }

}