package android.tech.mix.domain.extentions

import android.tech.mix.domain.util.MAX_QUERY_LENGTH

fun String?.isValidQueryString(): Boolean = !isNullOrBlank() && trim().length >= MAX_QUERY_LENGTH
