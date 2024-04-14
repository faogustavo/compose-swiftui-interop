Sample to update SwiftUI state from compose and vice versa.

### Relevant files: 

Kotlin
* [NativeViewFactory](composeApp/src/iosMain/kotlin/NativeViewFactory.kt) - Interface with methods that can be called to create native SwiftUI views
* [NativeMapView](composeApp/src/iosMain/kotlin/native/NativeMapView.ios.kt) - iOS Implementation to use native MapView
* [MapViewInterop](composeApp/src/commonMain/kotlin/tabs/MapViewInterop.kt) - Screen that uses the NativeMapView component
* [MainViewController](composeApp/src/iosMain/kotlin/MainViewController.kt) - Renders the MainView to Swift

Swift:
* [iOSApp](iosApp/iosApp/iOSApp.swift) - Instantiate MainViewController
* [NativeMapView](iosApp/iosApp/NativeMapView.swift) - MapView implementation in Swift
* [NativeViewFactory](iosApp/iosApp/NativeViewFactory.swift) - Implementation of the kmp `NativeViewFactory`

### Notes

1. As Compose must know how to create the SwiftUI views, I created the factory class for that
   1. This class is sent as parameter in the initial Compose ViewController
   2. I created a Composition Local for it, so we don't need to pass that value down (only call `.current`)
2. To test the native view as "part" of the compose app, I added Voyager for navigation 
   1. Something I noticed is that native views are fully recreated when tabs change
   2. This seems to be a behavior from the `UIKitViewController` composable
      1. Can validate adding `onRelease = { println("NativeMapViewRelease") }` to the composable 
3. For this example, I'm considering to have on screen fully native that consumes a shared state, and it's embedded in Compose
   1. For the sake of simplicity, the state is stored in the [MapViewScreenModel](composeApp/src/commonMain/kotlin/tabs/MapViewInterop.kt#L105)
   2. This ViewModel collect the state with SKIE in the [NativeMapView](iosApp/iosApp/NativeMapView.swift#L50)
