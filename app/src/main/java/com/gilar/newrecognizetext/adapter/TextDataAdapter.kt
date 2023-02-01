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


//    (private val textDataList: ArrayList<String> , context: Context) : RecyclerView.Adapter<TextDataAdapter.TextViewHolder>() {
//
//    var mFragment: DashboardFragment? = null
//    var mContext = context
//
//    private var database = FirebaseDatabase.getInstance("https://recognizetext-e8304-default-rtdb.firebaseio.com/")
//    private lateinit var dataRef: DatabaseReference
//    private lateinit var query: Query
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(
//            R.layout.item_text_data,
//            parent,false)
//        return TextViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
//        val currentItem = textDataList[position]
//        holder.tvTextDataa.text = currentItem
//        holder.btnUpdate.setOnClickListener{
//            mFragment?.onClickUpdate(position, currentItem)
//        }
//        holder.btnDelete.setOnClickListener {
//            showSimpleAlert("Delete Data", "Apakah anda yakin ?" , position)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return textDataList.size
//    }
//
//    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val tvTextDataa : TextView = itemView.findViewById(R.id.tv_data_text)
//        val btnUpdate : ImageButton = itemView.findViewById(R.id.btn_update)
//        val btnDelete : ImageButton = itemView.findViewById(R.id.btn_hapus)
//    }
//
//    private fun showSimpleAlert(title: String, pesan: String , position: Int) {
//        AlertDialog.Builder(mContext)
//            .setTitle(title)
//            .setMessage(pesan)
//            .setPositiveButton("OK") { dialog, which ->
//                onClickDelete(position)
//            }
//            .setNegativeButton("Cancel") { dialog, which ->
//                Toast.makeText(mContext, "Cancel is pressed", Toast.LENGTH_LONG).show()
//            }
//            .show()
//    }
//
//    private fun onClickDelete(position: Int) {
//
//        dataRef = database.getReference((mContext as MainActivity?)!!.getDeviceName().toString())
//        query = dataRef.child(position.toString())
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // remove the value at reference
//                dataSnapshot.ref.removeValue()
//                textDataList.removeAt(position)
//                Toast.makeText(mContext, "OK is pressed", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                Log.e("TAG", "onCancelled", databaseError.toException())
//                Toast.makeText(mContext, "NOT OK is pressed", Toast.LENGTH_LONG).show()
//            }
//        })
//
//    }
//}