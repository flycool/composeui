package com.compose.sample.composeui.visualTransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(
                text = LazyCreditCardFormatter.format(text).toString(),
            ),
            offsetMapping = creditCardOffsetTranslator
        )
    }

}

val creditCardOffsetTranslator = object : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        if (offset <= 3) return offset
        if (offset <= 7) return offset + 1
        if (offset <= 11) return offset + 2
        if (offset <= 16) return offset + 3
        return 19
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= 4) return offset
        if (offset <= 9) return offset - 1
        if (offset <= 14) return offset - 2
        if (offset <= 19) return offset - 3
        return 16
    }
}


object LazyCreditCardFormatter : Formatter {
    override fun format(input: CharSequence): CharSequence {
        val trimmed = if (input.length >= 16) input.substring(0..15) else input
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += "-"
        }
        return out
    }
}


interface Formatter {
    fun format(input: CharSequence): CharSequence
}
