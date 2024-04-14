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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import data.KMPCity
import data.KMPCoordinates
import data.KMPMapMarker
import datasource.LocalMapDataDatasource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import native.NativeMapView
import nav.TopAppBarProvider
import screen.LocationDetails

object MapViewInterop : Tab, TopAppBarProvider {
    private val screenModel: MapViewScreenModel
        @Composable
        get() = rememberScreenModel { MapViewScreenModel() }

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
        val screenModel = screenModel
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        NativeMapView(
            modifier = Modifier.fillMaxSize(),
            screenModel = screenModel,
        )

        LaunchedEffect(Unit) {
            screenModel.selectedMarker.collect {
                bottomSheetNavigator.show(LocationDetails(it))
            }
        }
    }

    @Composable
    override fun TopBar() {
        val screenModel = screenModel

        val allCities by screenModel.allCities.collectAsState()
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
                                    screenModel.goToCity(city)
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

class MapViewScreenModel : ScreenModel {
    private val _allCities = MutableStateFlow<List<KMPCity>>(emptyList())
    private val _pins = MutableStateFlow<List<KMPMapMarker>>(emptyList())
    private val _currentCoordinates = MutableSharedFlow<KMPCoordinates>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    private val _selectedMarker = MutableSharedFlow<KMPMapMarker>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    val allCities = _allCities.asStateFlow()
    val pins = _pins.asStateFlow()
    val selectedMarker = _selectedMarker.asSharedFlow()
    val currentCoordinates = _currentCoordinates.asSharedFlow()

    init {
        screenModelScope.launch {
            val citiesResult = LocalMapDataDatasource.cities()
            val currentCity = citiesResult.firstOrNull()

            _allCities.value = citiesResult
            if (currentCity != null) {
                _currentCoordinates.tryEmit(currentCity.coordinates)
                _pins.value = LocalMapDataDatasource.allData(currentCity)
            }
        }
    }

    fun goToCity(city: KMPCity) {
        _currentCoordinates.tryEmit(city.coordinates)
        screenModelScope.launch {
            _pins.value = LocalMapDataDatasource.allData(city)
        }
    }

    fun onMarkerClick(marker: KMPMapMarker) {
        _selectedMarker.tryEmit(marker)
    }
}
