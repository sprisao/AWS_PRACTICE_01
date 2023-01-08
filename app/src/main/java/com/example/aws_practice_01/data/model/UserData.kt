package com.example.aws_practice_01.data.model

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aws_practice_01.data.model.UserData.notifyObserver

object UserData {
    private const val TAG = "UserData"

    //
    // observable properties
    //

    // signed in status
    private val _isSigenedIn = MutableLiveData<Boolean>(false)
    var isSignedIn: LiveData<Boolean> = _isSigenedIn

    fun setSigendIn(newValue: Boolean) {
        // use postvalue() to make the assignation on the main (UI) thread
        _isSigenedIn.postValue(newValue)
    }

    // the notes
    private val _notes = MutableLiveData<MutableList<Note>>(mutableListOf())

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.postValue(this.value)
    }

    fun notifyObserver() {
        this._notes.notifyObserver()
    }

    fun notes(): LiveData<MutableList<Note>> = _notes
    fun addNote(n: Note) {
        val notes = _notes.value
        if (notes != null) {
            notes.add(n)
            _notes.notifyObserver()
        } else {
            Log.e(TAG, "addNote: note collection is null !!")
        }
    }

    fun deleteNote(at: Int): Note? {
        val note = _notes.value?.removeAt(at)
        _notes.notifyObserver()
        return note
    }

    fun resetNotes() {
        this._notes.value?.clear()
        _notes.notifyObserver()
    }

    // a note data class
    data class Note(
        val id: String,
        val name: String,
        val description: String,
        var imageName: String? = null
    ) {
        override fun toString(): String = name

        // bitmap image
        var image: Bitmap? = null
    }

}
