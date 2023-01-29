package com.gilar.newrecognizetext.ui.home

import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.gilar.newrecognizetext.MainActivity
import com.gilar.newrecognizetext.databinding.FragmentHomeBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.firebase.database.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var database = FirebaseDatabase.getInstance("https://recognizetext-e8304-default-rtdb.firebaseio.com/")


    private lateinit var dataRef: DatabaseReference
    private lateinit var query: Query

    private var FOTO_URI : Uri? = null
    private var bitmap : Bitmap? = null
    var count = 0
    private var edTextResult : String = ""
    private var edTextResultBefore : String = ""

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    FOTO_URI = data?.data!!
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, FOTO_URI)
                    binding.imageView.visibility = View.GONE
                    binding.textView.visibility = View.GONE
                    getTextFromImage(bitmap!!)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnTakeImg.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
        binding.btnSave.setOnClickListener {
            saveData()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configSave(count: Int){
        dataRef.child(count.toString()).child("Text").setValue(edTextResult).addOnSuccessListener {
            Toast.makeText(requireContext(),"Successfully Saved",Toast.LENGTH_SHORT).show()
            edTextResultBefore = edTextResult
        }.addOnFailureListener{
            Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData(){
        if(edTextResult.isEmpty()){
            Toast.makeText(requireContext(),"Your data is empty",Toast.LENGTH_SHORT).show()
        }else if(edTextResult == edTextResultBefore){
            Toast.makeText(requireContext(),"Your data is still the same",Toast.LENGTH_SHORT).show()
        }else{
            dataRef = database.getReference((activity as MainActivity?)!!.getDeviceName().toString())
            query = dataRef.orderByKey().limitToLast(1)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        for (childSnapshot in snapshot.children) {
                            count = childSnapshot.key!!.toInt()
                            count++
                        }
                        configSave(count)
                    }else{
                        configSave(count)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    private fun getTextFromImage(bitmap: Bitmap){
        val recognizer = TextRecognizer.Builder(requireContext()).build()
        if (!recognizer.isOperational){
            Toast.makeText(requireContext(),"Failed load the Text",Toast.LENGTH_SHORT).show()
        } else {
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val textBlockSparseArray = recognizer.detect(frame)
            val stringBuilder = StringBuilder()
            for (i in 0 until textBlockSparseArray.size()){
                val textBlock = textBlockSparseArray.valueAt(i)
                stringBuilder.append(textBlock.value)
                stringBuilder.append("\n")
            }
            binding.textResult.visibility = View.VISIBLE
            binding.textResult.setText(stringBuilder.toString())
            edTextResult = stringBuilder.toString()
        }
    }
}