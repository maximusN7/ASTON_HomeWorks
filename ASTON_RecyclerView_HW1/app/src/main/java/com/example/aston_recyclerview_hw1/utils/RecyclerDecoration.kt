package com.example.aston_recyclerview_hw1.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerDecoration(val sidePadding: Int, val topPadding: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = topPadding
        outRect.top = topPadding

        outRect.left = sidePadding
        outRect.right = sidePadding
    }
}