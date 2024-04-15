package vm

import org.brightify.hyperdrive.multiplatformx.BaseViewModel

class RootViewModel : BaseViewModel() {
    val mapViewModel: MapViewViewModel by managed(MapViewViewModel())
}
