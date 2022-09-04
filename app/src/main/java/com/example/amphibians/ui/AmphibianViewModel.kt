package com.example.amphibians.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amphibians.network.Amphibian
import com.example.amphibians.network.AmphibianApi
import kotlinx.coroutines.launch
import java.lang.Exception

enum class AmphibianApiStatus {LOADING, ERROR, DONE}

class AmphibianViewModel : ViewModel() {

    // Property to represent MutableLiveData and LiveData for the API status
    // using backing property
    private val _status = MutableLiveData<AmphibianApiStatus>()
    val status: LiveData<AmphibianApiStatus> = _status


    // Property to represent MutableLiveData and LiveData for a list of amphibian objects
    // using backing property
    private val _amphibians = MutableLiveData<List<Amphibian>>()
    val amphibians : LiveData<List<Amphibian>> = _amphibians


    // Property to represent MutableLiveData and LiveData for a single amphibian object.
    //  This will be used to display the details of an amphibian when a list item is clicked
    private val _amphibian = MutableLiveData<Amphibian>()
    val amphibian : LiveData<Amphibian> = _amphibian

    // initializer block
    init{
        getAmphibianList()
    }

    // A function that gets a list of amphibians from the api service and sets the
    //  status via a Coroutine
    private fun getAmphibianList() {

        // launch launches a new coroutine concurrently with the rest of the code,
        // which continues to work independently.
        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING

            // Exception Handling using try-catch block
            try {
                _amphibians.value = AmphibianApi.retrofitService.getListOfAmphibian()
                _status.value = AmphibianApiStatus.DONE
            } catch ( e : Exception){
                _status.value = AmphibianApiStatus.ERROR
                _amphibians.value = listOf()
            }
        }
    }


    fun onAmphibianClicked(amphibian: Amphibian) {
        // Set the amphibian object
        viewModelScope.launch {
            _status.value = AmphibianApiStatus.LOADING
            try {
                _amphibian.value = amphibian
                _status.value = AmphibianApiStatus.DONE
            } catch ( e : Exception){
                _status.value = AmphibianApiStatus.ERROR
                _amphibian.value = Amphibian("","","")
            }
        }

    }
}