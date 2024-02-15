package com.compose.sample.composeui.stringresource

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface StringResource {
    fun resolve(context: Context): String

    data class Text(val text: String) : StringResource {
        override fun resolve(context: Context): String {
            return text
        }
    }

    data class ResId(@StringRes val stringId: Int) : StringResource {
        override fun resolve(context: Context): String {
            return context.getString(stringId)
        }
    }

    data class ResIdWitParams(@StringRes val stringId: Int, val params: List<Any>) :
        StringResource {
        override fun resolve(context: Context): String {
            return context.getString(stringId, *params.toTypedArray())
        }
    }

    @Composable
    fun resolve(): String {
        return when (this) {
            is ResId -> stringResource(id = stringId)
            is ResIdWitParams -> stringResource(id = stringId, *params.toTypedArray())
            is Text -> text
        }
    }

}