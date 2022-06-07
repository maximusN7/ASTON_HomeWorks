package com.example.aston_recyclerview_hw1.utils

import androidx.recyclerview.widget.DiffUtil

class ContactsDiffUtilCallback(private val oldList: List<Contact>, private val newList: MutableList<Contact>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact: Contact = oldList[oldItemPosition]
        val newContact: Contact = newList[newItemPosition]
        return oldContact.id == newContact.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldContact: Contact = oldList[oldItemPosition]
        val newContact: Contact = newList[newItemPosition]

        return (oldContact.name == newContact.name
                && oldContact.lastName == newContact.lastName
                && oldContact.phoneNumber == newContact.phoneNumber
                && oldContact.avatarUrl == newContact.avatarUrl
                && oldContact.colored == newContact.colored)
    }
}
