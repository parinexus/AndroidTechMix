package android.tech.mix.domain.model

/**
 * A generic class that holds a value along with its loading status.
 * @param <Data> The type of the value being held.
 */
sealed class DataResult<out Data> {

    data class Success<out T>(val data: T) : DataResult<T>()

    data class Failure(val exception: Exception) : DataResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Failure -> "Failure[exception=$exception]"
        }
    }
}