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

        viewModel.dataEmpty.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
            Snackbar.make(this.requireView(), "Data is Empty", Snackbar.LENGTH_SHORT).show()
        })

        viewModel.dataTexts.observe(viewLifecycleOwner, Observer {
            binding.layoutEmpty.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            adapter.setDataTexts(it)
            Snackbar.make(this.requireView(), "Data successfully retrieved!", Snackbar.LENGTH_SHORT).show()
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
}
