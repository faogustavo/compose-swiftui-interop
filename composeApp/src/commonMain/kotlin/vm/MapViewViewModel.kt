package vm

import data.KMPCity
import data.KMPCoordinates
import data.KMPMapMarker
import datasource.LocalMapDataDatasource
import org.brightify.hyperdrive.multiplatformx.BaseViewModel
import org.brightify.hyperdrive.multiplatformx.ViewModel

@ViewModel
class MapViewViewModel : BaseViewModel() {
    val observableCoordinates by observe(::coordinates)
    var coordinates: KMPCoordinates? by published(null)

    val observableCities by observe(::cities)
    var cities: List<KMPCity> by published(emptyList())
        private set

    val observableMarkers by observe(::markers)
    var markers: List<KMPMapMarker> by published(emptyList())
        private set

    val observableDetailsViewModel by observe(::detailsViewModel)
    var detailsViewModel: DetailsViewModel? by managed(null)
        private set

    init {
        lifecycle.whileAttached {
            val citiesResult = LocalMapDataDatasource.cities()
            val currentCity = citiesResult.firstOrNull()

            cities = citiesResult
            if (currentCity != null) {
                coordinates = currentCity.coordinates
                markers = LocalMapDataDatasource.allData(currentCity)
            }
        }
    }

    fun goToCity(city: KMPCity) {
        coordinates = city.coordinates
        instanceLock.runExclusively {
            markers = LocalMapDataDatasource.allData(city)
        }
    }

    fun onMarkerClick(marker: KMPMapMarker) {
        detailsViewModel = DetailsViewModel(marker) {
            detailsViewModel = null
        }
    }
}
