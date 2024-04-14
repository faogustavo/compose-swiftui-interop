package data

data class KMPMapMarker(
    val id: String,
    val title: String,
    val description: String,
    val location: KMPCoordinates,
    val image: String,
    val monogram: String,
)
