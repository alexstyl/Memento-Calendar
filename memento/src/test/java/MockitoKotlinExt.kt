import org.mockito.Mockito

inline fun <T> anyObject(): T {
    return Mockito.anyObject<T>()
}