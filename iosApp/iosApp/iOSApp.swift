import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    @State private var name: String = ""
	var body: some Scene {
		WindowGroup {
            VStack {
                Text("Kotin inside SwiftUI").font(.title)
                
                NameView(name: name).frame(height: 30)
                HStack {
                    Button(action: { self.name = getRandom(except: name) }) {
                        Text("Toggle name")
                    }
                    
                    Button(action: { self.name = "" }) {
                        Text("Clear name").foregroundStyle(.red)
                    }
                }
                
                Divider().padding()
                
                Text("SwiftUI inside Kotlin").font(.title)
                
                InverseNameView()
                    .frame(height: 200)
            }
		}
	}
}

struct NameView: UIViewControllerRepresentable {
    @State private var state = TextViewController.State()
    var name: String
    
    func makeUIViewController(context: Context) -> UIViewController {
        TextViewController.shared.create(state: state)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        state.name.value = name
    }
}

struct InverseNameView : UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        TextViewController.shared.createReverse { state in
            let myView = MyInverseTextView(sharedState: state)
            return UIHostingController(rootView: myView)
        }
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct MyInverseTextView : View {
    let sharedState: TextViewController.State
    @State private var name: String = ""
    
    var body: some View {
        VStack {
            if name.isEmpty {
                Text("Fill your name")
            } else {
                Text("Hello \(name)!")
            }
        }.onAppear(perform: {
            Task {
                for await value in sharedState.name {
                    self.name = value
                }
            }
        })
    }
}
