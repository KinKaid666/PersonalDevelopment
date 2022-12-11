//
//  RefreshControl.swift
//  RefreshableView
//
//  Created by Eric Ferguson on 6/13/22.
//

import SwiftUI

import SwiftUI

struct RefreshControl: View {
    var coordinateSpace: CoordinateSpace
    var onRefresh: ()->Void
    @State var refresh: Bool = false
    var body: some View {
        GeometryReader { geo in
            Text("\(geo.frame(in: coordinateSpace).midY)")
            if (geo.frame(in: coordinateSpace).midY > 50) || refresh {
                HStack {
                    Spacer()
                    ProgressView()
                        .onAppear {
                            if refresh == false {
                                onRefresh() ///call refresh once if pulled more than 50px
                            }
                            refresh = true
                            DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
                                refresh = false
                                print("refresh = false")
                            }
                        }
                    Spacer()
                }
            }
        }
    }
}

struct PullToRefreshDemo: View {
    var body: some View {
        ScrollView {
            RefreshControl(coordinateSpace: .named("RefreshControl")) {
                // reload the view model
                print("refresh = true")
            }
            Text("Some view...")
        }.coordinateSpace(name: "RefreshControl")
    }
}

struct PullToRefreshDemo_Previews: PreviewProvider {
    static var previews: some View {
        PullToRefreshDemo()
    }
}
