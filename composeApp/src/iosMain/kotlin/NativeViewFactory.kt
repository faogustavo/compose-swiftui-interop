import platform.UIKit.UIViewController
import tabs.MapViewScreenModel

interface NativeViewFactory {
    fun createMapView(screenState: MapViewScreenModel): UIViewController
}


