package tabs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import ext.observeAsState
import kotlinx.coroutines.flow.filterNotNull
import native.NativeMapView
import nav.TopAppBarProvider
import org.brightify.hyperdrive.multiplatformx.property.asFlow
import screen.LocationDetails
import vm.MapViewViewModel

class MapViewInterop(
    private val viewModel: MapViewViewModel,
) : Tab, TopAppBarProvider {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Map)
            return remember {
                TabOptions(0u, "Map", icon)
            }
        }

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        NativeMapView(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
        )

        LaunchedEffect(Unit) {
            viewModel.observableDetailsViewModel
                .asFlow()
                .filterNotNull()
                .collect {
                    bottomSheetNavigator.show(LocationDetails(it))
                }
        }
    }

    @Composable
    override fun TopBar() {
        val allCities by viewModel.observableCities.observeAsState()
        var menuOpen by remember { mutableStateOf(false) }

        TopAppBar(
            title = { Text("Platform Map View") },
            actions = {
                if (allCities.isEmpty()) return@TopAppBar

                IconButton(onClick = { menuOpen = true }) {
                    Icon(Icons.Default.MoreVert, null)
                }

                DropdownMenu(
                    expanded = menuOpen,
                    onDismissRequest = { menuOpen = false },
                    content = {
                        allCities.forEach { city ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.goToCity(city)
                                    menuOpen = false
                                }
                            ) {
                                Text(city.name)
                            }
                        }
                    }
                )
            }
        )
    }
}
