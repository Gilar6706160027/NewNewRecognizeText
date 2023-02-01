package com.gilar.newrecognizetext.recyclerview

import android.view.View
import com.gilar.newrecognizetext.data.TextData

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, textData: TextData)
}