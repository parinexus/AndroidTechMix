package android.tech.mix.data.extention

import com.google.gson.Gson

/**
 * Converts any object to its JSON string representation.
 * @return The JSON string representing the object.
 */
fun Any.toJsonString(): String {
    return Gson().toJson(this)
}

/**
 * Converts a JSON string to an object of the specified type.
 * @return An object of type [T], or null if the conversion fails.
 */
inline fun <reified T> String.toObjectFromJsonString(): T? {
    return try {
        Gson().fromJson(this, T::class.java)
    } catch (exception: Exception) {
        null
    }
}
