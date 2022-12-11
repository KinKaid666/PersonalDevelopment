//
//  ContentView.swift
//  MapView
//
//  Created by Eric Ferguson on 7/17/22.
//

import SwiftUI
import CoreLocationUI
import MapKit

extension CLLocationCoordinate2D: Equatable {
    public static func == (lhs: CLLocationCoordinate2D, rhs: CLLocationCoordinate2D) -> Bool {
        lhs.latitude == rhs.latitude && lhs.longitude == rhs.longitude
    }
}

extension CLLocationCoordinate2D {
    public func asString() -> String {
        "(latitude: \(self.latitude), longitude: \(self.longitude)"
    }
}

extension MKCoordinateSpan: Equatable {
    public static func == (lhs: MKCoordinateSpan, rhs: MKCoordinateSpan) -> Bool {
        lhs.latitudeDelta == rhs.latitudeDelta && lhs.longitudeDelta == rhs.longitudeDelta
    }
}

extension MKCoordinateRegion: Equatable {
    public static func == (lhs: MKCoordinateRegion, rhs: MKCoordinateRegion) -> Bool {
        lhs.center == rhs.center && lhs.span == rhs.span
    }
}

class MapViewModel: ObservableObject {
    @Published var coordinateRegion = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 41.914101, longitude: -87.676348),
        span: MKCoordinateSpan(latitudeDelta: 0.03, longitudeDelta: 0.03))
    @Published var userTrackingMode: MapUserTrackingMode = .follow
    @Published var locationManager = LocationManager()
    
}

struct ContentView: View {
    @StateObject var viewModel = MapViewModel()
    
    var body: some View {
        Text(viewModel.locationManager.statusString)
        Text(viewModel.locationManager.lastLocation?.coordinate.asString() ?? "0,0")
        ZStack {
            Map(coordinateRegion: $viewModel.coordinateRegion,
                interactionModes: .all,
                showsUserLocation: true,
                userTrackingMode: $viewModel.userTrackingMode)
            .onChange(of: viewModel.coordinateRegion) { newValue in
                    print("Changed", newValue)
                }
            .onChange(of: viewModel.locationManager.lastLocation?.coordinate) { newValue in
                    print("Changed", newValue)
                }
            Circle()
                .stroke(lineWidth: 5)
                .frame(width: 100, height: 100)
                .offset(x: 0, y: 0)
        }
    }
}
