import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    @StateObject
    private var lifecycleManager = LifecycleManager()
    
    private let rootViewModel = RootViewModel()
    
    var body: some Scene {
        WindowGroup {
            Group {
                MainView(rootViewModel: rootViewModel).ignoresSafeArea(.keyboard)
                    .attach(viewModel: rootViewModel)
            }.environmentObject(lifecycleManager)
        }
    }
}

struct MainView : UIViewControllerRepresentable {
    @ObservedObject
    var rootViewModel: RootViewModel
    
    func makeUIViewController(context: Context) -> some UIViewController {
        MainViewController(nativeViewFactory: iOSNativeViewFactory.shared, rootViewModel: rootViewModel)
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
}
