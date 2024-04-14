package ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import effect.ObservableEffect
import org.brightify.hyperdrive.multiplatformx.property.ObservableProperty

/**
 * Observe a view model property as it changes to update the view.
 *
 * Equivalent to [collectAsState] for [ObservableProperty].
 */
@Composable
internal fun <T> ObservableProperty<T>.observeAsState(): State<T> {
    val result = remember(this) { mutableStateOf(value, neverEqualPolicy()) }
    ObservableEffect(this) { _, newValue ->
        result.value = newValue
    }
    return result
}
