//
//  NativeMapView.swift
//  iosApp
//
//  Created by Gustavo Valvassori on 13/04/24.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp
import MapKit

struct NativeMapView : View {
    var screenState: MapViewScreenModel
    
    @State private var pins: [KMPMapMarker] = []
    @State private var position: MapCameraPosition = .automatic
    
    var body: some View {
        MapReader { reader in
            Map(position: $position) {
                ForEach(pins) { (pin) in
                    Annotation(
                        pin.title,
                        coordinate: CLLocationCoordinate2D(
                            latitude: pin.location.lat,
                            longitude: pin.location.lng
                        )
                    ) {
                        ZStack {
                            RoundedRectangle(cornerRadius: 5)
                                .fill(.background)
                                .stroke(.secondary, lineWidth: 2)
                            Text(pin.monogram).padding(8)
                        }.onTapGesture {
                            self.screenState.onMarkerClick(marker: pin)
                        }
                    }
                }
            }
            .mapControls {
                MapCompass()
                MapScaleView()
            }
        }
        .task { await collectCoordinates() }
        .task { await collectPins() }
    }
    
    private func collectCoordinates() async {
        for await coordinates in self.screenState.currentCoordinates {
            withAnimation {
                position = .region(
                    MKCoordinateRegion(
                        center: CLLocationCoordinate2D(latitude: coordinates.lat, longitude: coordinates.lng),
                        span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
                    )
                )
            }
        }
    }
    
    private func collectPins() async {
        for await pins in self.screenState.pins {
            self.pins = pins
        }
    }
}

extension KMPMapMarker : Identifiable {
    
}
