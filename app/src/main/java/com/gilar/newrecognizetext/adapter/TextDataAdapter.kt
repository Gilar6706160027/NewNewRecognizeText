package com.gilar.newrecognizetext.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gilar.newrecognizetext.R
import com.gilar.newrecognizetext.data.TextData
import com.gilar.newrecognizetext.recyclerview.RecyclerViewClickListener


class TextDataAdapter : RecyclerView.Adapter<TextDataAdapter.TextDataViewModel>() {
    private var dataTexts = mutableListOf<TextData>()
    var listener: RecyclerViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TextDataViewModel(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_data_texts, parent, false)
        )
    override fun getItemCount() = dataTexts.size

    override fun onBindViewHolder(holder: TextDataViewModel, position: Int) {

        holder.tvTextDataa.text = dataTexts[position].text
        holder.btnUpdate.setOnClickListener {
            listener?.onRecyclerViewItemClicked(it, dataTexts[position])
        }
        holder.btnDelete.setOnClickListener {
            listener?.onRecyclerViewItemClicked(it, dataTexts[position])
        }
    }

    fun setDataTexts(dataText: List<TextData>) {
        this.dataTexts = dataText as MutableList<TextData>
        notifyDataSetChanged()
    }

    fun addAuthor(textData: TextData) {
        // untuk nambah data
        if (!dataTexts.contains(textData)){
            dataTexts.add(textData)
        } else { // untuk update data
            // buat index untuk data yang dihapus atau edit
            val index = dataTexts.indexOf(textData)
            if (textData.isDeleted) {
                dataTexts.removeAt(index)
            } else {
                dataTexts[index] = textData
            }
        }
        notifyDataSetChanged()
    }
    class TextDataViewModel(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTextDataa : TextView = itemView.findViewById(R.id.tv_texts)
        val btnUpdate : ImageButton = itemView.findViewById(R.id.button_edit)
        val btnDelete : ImageButton = itemView.findViewById(R.id.button_delete)
    }
}