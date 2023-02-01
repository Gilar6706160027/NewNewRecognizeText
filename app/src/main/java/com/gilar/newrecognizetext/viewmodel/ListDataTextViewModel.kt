package com.gilar.newrecognizetext.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gilar.newrecognizetext.data.NODE_DATABASE
import com.gilar.newrecognizetext.data.TextData
import com.google.firebase.database.*

class ListDataTextViewModel : ViewModel() {

    private val dbDataTexts = FirebaseDatabase.getInstance(NODE_DATABASE)
    @SuppressLint("StaticFieldLeak")
    private lateinit var mContext: Context

    private val dataRef = dbDataTexts.getReference(getDeviceName().toString())

    private val _datatexts = MutableLiveData<List<TextData>>()
    val dataTexts: LiveData<List<TextData>>
        get() = _datatexts

    // create single author
    private val _datatext = MutableLiveData<TextData>()
    val dataText: LiveData<TextData>
        get() = _datatext

    private val _textResultBefore = MutableLiveData<String>()
    val textResultBefore: LiveData<String>
        get() = _textResultBefore

    // create result
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    // fungsi untuk menambahkan data ke firebase realtime database
    fun addAuthor(textData: TextData , context: Context) {
        textData.id = dataRef.push().key
        dataRef.child(textData.id!!).setValue(textData).addOnCompleteListener {
            if(it.isSuccessful) {
                Toast.makeText(context,"Successfully Saved", Toast.LENGTH_SHORT).show()
                _textResultBefore.value = textData.text.toString()
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    // buat event untuk perubahan data untuk realtime update
    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) { }

        override fun onChildMoved(snapshot: DataSnapshot, p1: String?) { }

        // update data otomatis ketika data di edit
        override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
            val texData = snapshot.getValue(TextData::class.java)
            texData?.id = snapshot.key
            _datatext.value = texData!!
        }

        // update data otomatis ketika data ditambahkan
        override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
            val texData = snapshot.getValue(TextData::class.java)
            texData?.id = snapshot.key
            _datatext.value = texData!!
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val texData = snapshot.getValue(TextData::class.java)
            texData?.id = snapshot.key
            texData?.isDeleted = true
            _datatext.value = texData!!
        }
    }

    // buat fungsi get realtimeupdate
    fun getRealtimeUpdates() {
        dataRef.addChildEventListener(childEventListener)
    }

    // buat event untuk menampilkan data di firebase dengan metode fetching
    private val valueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) { }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val textData = mutableListOf<TextData>()
                for (textDataSnapshot in snapshot.children) {
                    val dataTexts = textDataSnapshot.getValue(TextData::class.java)
                    dataTexts?.id = textDataSnapshot.key
                    dataTexts?.let { textData.add(it) }
                }
                _datatexts.value = textData
            }
        }
    }

    // fetch author untuk menampilkan data di firebase
    fun fetchTextdata() {
        dataRef.addListenerForSingleValueEvent(valueEventListener)
    }

    // fungsi update
    fun updateAuthor(textData: TextData) {
        dataRef.child(textData.id!!).setValue(textData).addOnCompleteListener {
            if(it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    // fungsi delete
    fun deleteAuthor(textData: TextData) {
        dataRef.child(textData.id!!).setValue(null).addOnCompleteListener {
            if(it.isSuccessful) {
                _result.value = null
            } else {
                _result.value = it.exception
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dataRef.removeEventListener(childEventListener)
    }

    fun getDeviceName(): String? {
        return (Build.MANUFACTURER
                + " " + Build.MODEL + " ")
    }
}