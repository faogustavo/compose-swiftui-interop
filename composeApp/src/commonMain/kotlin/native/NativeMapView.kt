package native

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tabs.MapViewScreenModel

@Composable
expect fun NativeMapView(
    modifier: Modifier = Modifier,
    screenModel: MapViewScreenModel,
)
