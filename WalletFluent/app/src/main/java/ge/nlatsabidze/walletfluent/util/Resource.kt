package ge.nlatsabidze.walletfluent


sealed class Resource<T>(val data: T? = null, val message: String? = null, val empty: Int? = null) {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class EmptyData<T>(private val emptyState: Int? = null) : Resource<T>(empty = emptyState)
}