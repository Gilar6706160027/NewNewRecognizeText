package com.gilar.newrecognizetext.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gilar.newrecognizetext.MainActivity
import com.gilar.newrecognizetext.R
import com.gilar.newrecognizetext.ui.dashboard.DashboardFragment
import com.google.firebase.database.*


class TextDataAdapter(private val textDataList: ArrayList<String> , context: Context) : RecyclerView.Adapter<TextDataAdapter.TextViewHolder>() {

    var mFragment: DashboardFragment? = null
    var mContext = context

    private var database = FirebaseDatabase.getInstance("https://recognizetext-e8304-default-rtdb.firebaseio.com/")
    private lateinit var dataRef: DatabaseReference
    private lateinit var query: Query

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_text_data,
            parent,false)
        return TextViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val currentItem = textDataList[position]
        holder.tvTextDataa.text = currentItem
        holder.btnUpdate.setOnClickListener{
            mFragment?.onClickUpdate(position, currentItem)
        }
        holder.btnDelete.setOnClickListener {
            showSimpleAlert("Delete Data", "Apakah anda yakin ?" , position)
        }
    }

    override fun getItemCount(): Int {
        return textDataList.size
    }

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTextDataa : TextView = itemView.findViewById(R.id.tv_data_text)
        val btnUpdate : ImageButton = itemView.findViewById(R.id.btn_update)
        val btnDelete : ImageButton = itemView.findViewById(R.id.btn_hapus)
    }

    private fun showSimpleAlert(title: String, pesan: String , position: Int) {
        AlertDialog.Builder(mContext)
            .setTitle(title)
            .setMessage(pesan)
            .setPositiveButton("OK") { dialog, which ->
                onClickDelete(position)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(mContext, "Cancel is pressed", Toast.LENGTH_LONG).show()
            }
            .show()
    }

    private fun onClickDelete(position: Int) {

        dataRef = database.getReference((mContext as MainActivity?)!!.getDeviceName().toString())
        query = dataRef.child(position.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // remove the value at reference
                dataSnapshot.ref.removeValue()
                textDataList.removeAt(position)
                Toast.makeText(mContext, "OK is pressed", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException())
                Toast.makeText(mContext, "NOT OK is pressed", Toast.LENGTH_LONG).show()
            }
        })

    }
}