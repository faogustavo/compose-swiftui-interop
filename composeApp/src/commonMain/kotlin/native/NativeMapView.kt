package native

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vm.MapViewViewModel

@Composable
expect fun NativeMapView(
    modifier: Modifier = Modifier,
    viewModel: MapViewViewModel,
)
