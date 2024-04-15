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
    @ObservedObject var viewModel: MapViewViewModel
    private let position: Binding<MapCameraPosition>
    
    init(viewModel: MapViewViewModel) {
        self.viewModel = viewModel
        self.position = Binding(
            get: {
                guard let coordinates = viewModel.coordinates else { return .automatic }
                return .region(
                    MKCoordinateRegion(
                        center: CLLocationCoordinate2D(latitude: coordinates.lat, longitude: coordinates.lng),
                        span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
                    )
                )
            }, set: { newValue, _ in
                guard let region = newValue.region else { return }
                viewModel.coordinates = KMPCoordinates(lat: region.center.latitude, lng: region.center.longitude)
            }
        )
    }
    
    var body: some View {
        MapReader { reader in
            Map(position: position) {
                ForEach(self.viewModel.markers) { (pin) in
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
                            self.viewModel.onMarkerClick(marker: pin)
                        }
                    }
                }
            }
            .mapControls {
                MapCompass()
                MapScaleView()
            }
        }
    }
}

extension KMPMapMarker : Identifiable {}

