//
//  NativeViewFactory.swift
//  iosApp
//
//  Created by Gustavo Valvassori on 13/04/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

class iOSNativeViewFactory : NativeViewFactory {
    static var shared = iOSNativeViewFactory()
    
    func createMapView(viewModel: MapViewViewModel) -> UIViewController {
        let view = NativeMapView(viewModel: viewModel)
        return UIHostingController(rootView: view)
    }
}
