package com.gilar.newrecognizetext.ui.dashboard

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.gilar.newrecognizetext.R
import com.gilar.newrecognizetext.adapter.TextDataAdapter
import com.gilar.newrecognizetext.data.TextData
import com.gilar.newrecognizetext.databinding.FragmentListDataTextBinding
import com.gilar.newrecognizetext.dialog.EditTextDialogFragment
import com.gilar.newrecognizetext.recyclerview.RecyclerViewClickListener
import com.gilar.newrecognizetext.viewmodel.ListDataTextViewModel
import com.google.android.material.snackbar.Snackbar

class ListDataTextFragment : Fragment(),
    RecyclerViewClickListener {

    companion object {
        fun newInstance() = ListDataTextFragment()
    }

    private lateinit var viewModel: ListDataTextViewModel
    private val adapter =
        TextDataAdapter()

    private var _binding: FragmentListDataTextBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListDataTextBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel = ViewModelProvider(this).get(ListDataTextViewModel::class.java)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // set listener on this fragment
        adapter.listener = this
        // set adapter untuk recycleview
        binding.recyclerView.adapter = adapter

        // panggil fun yg di view model untuk fetch data
        viewModel.fetchTextdata()

        // panggil fun get realtimeupdate
        viewModel.getRealtimeUpdates()

        viewModel.dataTexts.observe(viewLifecycleOwner, Observer {
            adapter.setDataTexts(it)
            Snackbar.make(this.requireView(), "Author fetched!", Snackbar.LENGTH_SHORT).show()
        })

        viewModel.dataText.observe(viewLifecycleOwner, Observer {
            adapter.addAuthor(it)
        })
    }

    override fun onRecyclerViewItemClicked(view: View, textData: TextData) {
        when (view.id) {
            R.id.button_edit -> {
                EditTextDialogFragment(
                    textData
                ).show(childFragmentManager, "")
            }
            R.id.button_delete -> {
                AlertDialog.Builder(requireContext()).also {
                    it.setTitle("Apakah Anda yakin ingin menghapusnya?")
                    it.setPositiveButton("Ya") { _, _ ->
                        viewModel.deleteAuthor(textData)
                    }
                }.create().show()
            }
        }
    }

//    private var _binding: FragmentListTextDataBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    private lateinit var dataArrayList : ArrayList<String>
//
//    private var database = FirebaseDatabase.getInstance("https://recognizetext-e8304-default-rtdb.firebaseio.com/")
//
//
//    private lateinit var dataRef: DatabaseReference
//    private lateinit var query: Query
//
//    var count = 0
//    var position = 0
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentListTextDataBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//        dataArrayList = arrayListOf()
//        getUserData()
//        return root
//    }
//
//    override fun onResume() {
//        super.onResume()
//        getUserData()
//    }
//
//    private fun getUserData() {
//        dataRef = database.getReference((activity as MainActivity?)!!.getDeviceName().toString())
//        query = dataRef.orderByKey()
//        query.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    dataArrayList.clear()
//                    for (childSnapshot in snapshot.children) {
//                        dataRef.child(childSnapshot.key!!).get().addOnSuccessListener {
//                            if (it.exists()){
//                                val text = it.child("Text").value
//                                dataArrayList.add(text.toString())
//                                Log.d("DATA" , "==$dataArrayList")
//                            }else{
//                                Toast.makeText(requireContext(),"User Doesn't Exist", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
////                    val textDataAdapter = TextDataAdapter(dataArrayList,requireContext())
////                    binding.recyclerview.apply {
////                        layoutManager = LinearLayoutManager(requireContext())
////                        setHasFixedSize(true)
////                        adapter = textDataAdapter
////                    }
////                    textDataAdapter.notifyDataSetChanged()
//                }else{
//                    Toast.makeText(requireContext(),"User Doesn't Exist", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun onClickUpdate(position: Int, data: String) {
//
//        dataRef = database.getReference((activity as MainActivity?)!!.getDeviceName().toString())
//        val user = mapOf(
//            "Text" to data,
//        )
//
////        dataRef.child(position.toString()).updateChildren(user).addOnSuccessListener {
////            Toast.makeText(requireContext(),"Successfuly Updated",Toast.LENGTH_SHORT).show()
////            val textDataAdapter = TextDataAdapter(dataArrayList,requireContext())
////            textDataAdapter.notifyDataSetChanged() // notify the changed
////        }.addOnFailureListener{
////            Toast.makeText(requireContext(),"Failed to Update",Toast.LENGTH_SHORT).show()
////        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}
