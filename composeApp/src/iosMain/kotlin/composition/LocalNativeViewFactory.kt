package composition

import NativeViewFactory
import androidx.compose.runtime.compositionLocalOf

val LocalNativeViewFactory = compositionLocalOf<NativeViewFactory> {
    error("You need to provide it")
}
