package com.compose.sample.composeui.visualTransformation

class PhoneNumberVisualTransformation : GenericSeparatorVisualTransformation() {
    override fun transform(input: CharSequence): CharSequence {
        val trimmed = if (input.length >= 16) input.substring(0..15) else input
        return trimmed.chunked(4).joinToString("-")
    }

    override fun isSeparator(char: Char): Boolean {
        return !Character.isDigit(char)
    }

}
