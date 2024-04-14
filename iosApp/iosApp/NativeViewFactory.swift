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
    
    func createMapView(screenState: MapViewScreenModel) -> UIViewController {
        let view = NativeMapView(screenState: screenState)
        return UIHostingController(rootView: view)
    }
}
