package com.dc.todomvvmroomcoroutine.utils

sealed class GeneralState{
    data class Success(
        val message: String? = ""
    ) : GeneralState()

    data class Error(
        val message: String? = null
    ) : GeneralState()

    object Loading : GeneralState()
}
