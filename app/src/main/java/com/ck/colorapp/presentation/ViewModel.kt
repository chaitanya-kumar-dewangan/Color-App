package com.ck.colorapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ck.colorapp.data.ColorEntity
import com.ck.colorapp.data.ColorRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ViewModel(private val repo: ColorRepo) : ViewModel() {

    private val firebaseDB = FirebaseDatabase.getInstance().getReference("colors")

    val colors = MutableStateFlow<List<ColorEntity>>(emptyList())

    val _count = MutableStateFlow<Int>(colors.value.size)

    val count =_count.asStateFlow()

    init {
        getColors()
//        getColorCount()
    }

    private fun getColors() {
        viewModelScope.launch {
            repo.getColors().collectLatest {

                println(it.toString())
                colors.value = it
            }
        }
            getColorCount()
    }

    fun addListOfColorToFirebase() {
        viewModelScope.launch {

            firebaseDB.setValue(colors.value)

        }
        getColorCount()
    }


    private fun addColor(color: String) {
        viewModelScope.launch {
            repo.addColor(ColorEntity(color = color))
        }
        getColors()
    }

    private fun getColorCount() {
        viewModelScope.launch {
            firebaseDB.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val colorsCount = snapshot.childrenCount.toInt() // Count of child nodes

                    println("count xxxxxxxxxxxxxxxxxxxxxx")
                    println(colorsCount)
                    println("count xxxxxxxxxxxxxxxxxxxxxx")
                    println(count.value)
                    println(colors.value.size)
                    _count.value = colors.value.size -  colorsCount

                }

                override fun onCancelled(error: DatabaseError) {


                }

            })

        }
    }
        fun generateColor() {
            val chars =
                arrayOf(
                    '0',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9',
                    'A',
                    'B',
                    'C',
                    'D',
                    'E',
                    'F'
                )
            var color = "#"
            for (i in 0..5) {

                val char = chars.random()
                color = "$color$char"

            }
            addColor(color)


        }}
