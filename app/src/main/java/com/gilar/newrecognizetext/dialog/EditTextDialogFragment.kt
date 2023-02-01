package com.gilar.newrecognizetext.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gilar.newrecognizetext.R
import com.gilar.newrecognizetext.data.TextData
import com.gilar.newrecognizetext.databinding.DialogFragmentEditTextdataBinding
import com.gilar.newrecognizetext.viewmodel.ListDataTextViewModel

class EditTextDialogFragment(private val textData: TextData) : DialogFragment() {

    private lateinit var viewModel: ListDataTextViewModel

    private var _binding: DialogFragmentEditTextdataBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogFragmentEditTextdataBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // mendapatkan viewmodel dari class AuthorViewModels
        viewModel = ViewModelProvider(this).get(ListDataTextViewModel::class.java)
        return root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // styling pop up add author
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_MinWidth)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.etText.setText(textData.text)

        // define result on button add author clicked
        viewModel.result.observe(viewLifecycleOwner, Observer {
            val message = if (it == null) {
                "Data  Diperbarui!"
            } else {
                getString(R.string.error, it.message)
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            dismiss()
        })

        // on click
        binding.btnEditData.setOnClickListener {
            val  texts = binding.etText.text.toString().trim()
            if (texts.isEmpty()){
                binding.inputLayoutName.error = "Text Kosong"
                return@setOnClickListener
            }
            textData.text = texts
            viewModel.updateAuthor(textData)
        }
    }

}
