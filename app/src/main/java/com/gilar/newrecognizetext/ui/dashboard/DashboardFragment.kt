package com.gilar.newrecognizetext.ui.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gilar.newrecognizetext.MainActivity
import com.gilar.newrecognizetext.adapter.TextDataAdapter
import com.gilar.newrecognizetext.databinding.FragmentDashboardBinding
import com.google.firebase.database.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dataArrayList : ArrayList<String>

    private var database = FirebaseDatabase.getInstance("https://recognizetext-e8304-default-rtdb.firebaseio.com/")


    private lateinit var dataRef: DatabaseReference
    private lateinit var query: Query

    var count = 0
    var position = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        dataArrayList = arrayListOf()
        getUserData()
        return root
    }

    override fun onResume() {
        super.onResume()
        getUserData()
    }

    private fun getUserData() {
        dataRef = database.getReference((activity as MainActivity?)!!.getDeviceName().toString())
        query = dataRef.orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    dataArrayList.clear()
                    for (childSnapshot in snapshot.children) {
                        dataRef.child(childSnapshot.key!!).get().addOnSuccessListener {
                            if (it.exists()){
                                val text = it.child("Text").value
                                dataArrayList.add(text.toString())
                                Log.d("DATA" , "==$dataArrayList")
                            }else{
                                Toast.makeText(requireContext(),"User Doesn't Exist",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    val textDataAdapter = TextDataAdapter(dataArrayList,requireContext())
                    binding.recyclerview.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        setHasFixedSize(true)
                        adapter = textDataAdapter
                    }
                    textDataAdapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(requireContext(),"User Doesn't Exist",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onClickUpdate(position: Int, data: String) {

        dataRef = database.getReference((activity as MainActivity?)!!.getDeviceName().toString())
        val user = mapOf(
            "Text" to data,
        )

        dataRef.child(position.toString()).updateChildren(user).addOnSuccessListener {
            Toast.makeText(requireContext(),"Successfuly Updated",Toast.LENGTH_SHORT).show()
            val textDataAdapter = TextDataAdapter(dataArrayList,requireContext())
            textDataAdapter.notifyDataSetChanged() // notify the changed
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Failed to Update",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}