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
import ext.observeAsState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import native.NativeMapView
import nav.TopAppBarProvider
import org.brightify.hyperdrive.multiplatformx.BaseViewModel
import org.brightify.hyperdrive.multiplatformx.ViewModel
import org.brightify.hyperdrive.multiplatformx.property.asFlow
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

        val allCities by screenModel.observableCities.observeAsState()
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

@ViewModel
class MapViewScreenModel : BaseViewModel(), ScreenModel {
    private val _selectedMarker = MutableSharedFlow<KMPMapMarker>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val selectedMarker = _selectedMarker.asSharedFlow()

    val observableCoordinates by observe(::coordinates)
    var coordinates: KMPCoordinates? by published(null)

    val observableCities by observe(::cities)
    var cities: List<KMPCity> by published(emptyList())
        private set

    val observableMarkers by observe(::markers)
    var markers: List<KMPMapMarker> by published(emptyList())
        private set

    init {
        screenModelScope.launch {
            val citiesResult = LocalMapDataDatasource.cities()
            val currentCity = citiesResult.firstOrNull()

            cities = citiesResult
            if (currentCity != null) {
                coordinates = currentCity.coordinates
                markers = LocalMapDataDatasource.allData(currentCity)
            }
        }
        screenModelScope.launch {
            observableCoordinates.asFlow().collect {
                println("New coordinates: $it")
            }
        }
    }

    fun goToCity(city: KMPCity) {
        coordinates = city.coordinates
        screenModelScope.launch {
            markers = LocalMapDataDatasource.allData(city)
        }
    }

    fun onMarkerClick(marker: KMPMapMarker) {
        _selectedMarker.tryEmit(marker)
    }
}
