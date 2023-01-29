package com.gilar.newrecognizetext.ui.dashboard

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gilar.newrecognizetext.MainActivity
import com.gilar.newrecognizetext.databinding.FragmentUpdateDataBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query


class UpdateDataFragment : Fragment() {

    private var _binding: FragmentUpdateDataBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var dataArrayList: ArrayList<String>

    private var database =
        FirebaseDatabase.getInstance("https://recognizetext-e8304-default-rtdb.firebaseio.com/")


    private lateinit var dataRef: DatabaseReference
    private lateinit var query: Query

    var count = 0
    var position = 0
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateDataBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.textResult.setText("hjsadbawdhalwda")
        binding.btnUpdate.setOnClickListener {
            val textData = binding.textResult.text.toString()

            updateData(textData)

        }
        return root
    }

    private fun updateData(textData: String) {

        dataRef = database.getReference((activity as MainActivity?)!!.getDeviceName().toString())
        val user = mapOf(
            "Text" to textData,
        )

        dataRef.child(position.toString()).updateChildren(user).addOnSuccessListener {
            Toast.makeText(requireContext(),"Successfuly Updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Failed to Update", Toast.LENGTH_SHORT).show()
        }
    }
}