import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import composition.LocalNativeViewFactory
import vm.RootViewModel

fun MainViewController(
    nativeViewFactory: NativeViewFactory,
    rootViewModel: RootViewModel,
) = ComposeUIViewController {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        MainView(rootViewModel)
    }
}
