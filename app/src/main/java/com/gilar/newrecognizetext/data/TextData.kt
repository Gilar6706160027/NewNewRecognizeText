package com.gilar.newrecognizetext.data

import com.google.firebase.database.Exclude

data class TextData (
    @get:Exclude
    var id: String? = null,
    var text: String? = null,
    @get:Exclude
    var isDeleted: Boolean = false

) {
    override fun equals(other: Any?): Boolean {
        return if (other is TextData) {
            other.id == id
        } else false
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (text?.hashCode() ?: 0)
        return result
    }
}