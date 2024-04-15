package native

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import composition.LocalNativeViewFactory
import kotlinx.cinterop.ExperimentalForeignApi
import vm.MapViewViewModel

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeMapView(
    modifier: Modifier,
    viewModel: MapViewViewModel,
) {
    val factory = LocalNativeViewFactory.current

    UIKitViewController(
        modifier = modifier,
        factory = { factory.createMapView(viewModel) },
        update = {},
    )
}
