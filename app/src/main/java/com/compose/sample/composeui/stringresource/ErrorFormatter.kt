package com.compose.sample.composeui.stringresource

import com.compose.sample.composeui.R
import java.net.SocketTimeoutException

interface ErrorFormatter {
    fun format(throwable: Throwable?): StringResource
}

class ErrorFormatDefault : ErrorFormatter {
    override fun format(throwable: Throwable?): StringResource {
        return when (throwable) {
            is SocketTimeoutException -> StringResource.ResId(stringId = R.string.not_connected_to_internet)
            else -> StringResource.Text("Something went wrong!")
        }
    }
}