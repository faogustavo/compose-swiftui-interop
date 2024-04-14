import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import composition.LocalNativeViewFactory

fun MainViewController(nativeViewFactory: NativeViewFactory) = ComposeUIViewController {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        MainView()
    }
}
