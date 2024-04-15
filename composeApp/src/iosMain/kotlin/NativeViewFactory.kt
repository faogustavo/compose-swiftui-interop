import platform.UIKit.UIViewController
import vm.MapViewViewModel

interface NativeViewFactory {
    fun createMapView(viewModel: MapViewViewModel): UIViewController
}


