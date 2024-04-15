package vm

import data.KMPMapMarker
import org.brightify.hyperdrive.multiplatformx.BaseViewModel

class DetailsViewModel(
    marker: KMPMapMarker,
    private val onDismiss: () -> Unit,
) : BaseViewModel() {
    val marker by published(marker)
    fun onDetailsDismissed() = instanceLock.runExclusively(onDismiss)
}
