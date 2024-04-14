package datasource

import data.KMPCity
import data.KMPCoordinates
import data.KMPMapMarker
import kotlinx.coroutines.delay

object LocalMapDataDatasource {
    private val data = mapOf(
        KMPCity("Copenhagen", KMPCoordinates(55.637848, 12.580427)) to listOf(
            KMPMapMarker(
                id = "001",
                title = "Bella Center",
                description = "Bella Center (abbreviated BC) is Scandinavia's second largest exhibition and conference center (after Messecenter Herning), and is located in Copenhagen, Denmark. Located in Ørestad between the city centre and Copenhagen Airport, it offers an indoor area of 121,800 square metres (1,311,000 sq ft) and has a capacity of 30,000 people.",
                location = KMPCoordinates(55.6380111, 12.5788393),
                image = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/db/Bella_Center_indgang_vest.JPG/1024px-Bella_Center_indgang_vest.JPG",
                monogram = "🏢",
            ),
            KMPMapMarker(
                id = "002",
                title = "Royal Arena",
                description = "The Royal Arena is a multi-use indoor arena in the Ørestad South area of Copenhagen, Denmark. The ground was broken for construction on 26 June 2013 and the arena opened in February 2017. It has a capacity of 13,000 for sporting events and up to 16,000 (either sitting or standing) for concerts.",
                location = KMPCoordinates(55.6267574, 12.5639331),
                image = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/60/Construction_of_the_Royal_Arena_in_%C3%98restad_Syd_-_panoramio.jpg/1024px-Construction_of_the_Royal_Arena_in_%C3%98restad_Syd_-_panoramio.jpg",
                monogram = "👑",
            ),
        ),
        KMPCity("New York", KMPCoordinates(40.7503587,-74.0016948)) to listOf()
    )

    suspend fun cities(): List<KMPCity> {
        delay(500)
        return data.keys.toList()
    }

    suspend fun allData(city: KMPCity): List<KMPMapMarker> {
        delay(500)
        return data[city].orEmpty()
    }

    suspend fun getMarkerInfo(id: String): KMPMapMarker? {
        delay(500)
        return data
            .flatMap { it.value }
            .find { it.id == id }
    }
}
