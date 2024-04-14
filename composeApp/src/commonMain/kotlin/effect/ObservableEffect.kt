package effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import org.brightify.hyperdrive.multiplatformx.property.ObservableProperty

@Composable
fun <T> ObservableEffect(
    observable: ObservableProperty<T>,
    willChange: (oldValue: T, newValue: T) -> Unit = { _, _ -> },
    didChange: (oldValue: T, newValue: T) -> Unit = { _, _ -> },
) {
    val listener = remember {
        object : ObservableProperty.Listener<T> {
            override fun valueWillChange(oldValue: T, newValue: T) {
                willChange(oldValue, newValue)
            }

            override fun valueDidChange(oldValue: T, newValue: T) {
                didChange(oldValue, newValue)
            }
        }
    }

    DisposableEffect(observable) {
        val token = observable.addListener(listener)
        onDispose { token.cancel() }
    }
}
