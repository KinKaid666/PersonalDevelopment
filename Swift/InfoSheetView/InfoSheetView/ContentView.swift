//
//  ContentView.swift
//  InfoSheetView
//
//  Created by Eric Ferguson on 8/15/22.
//

import SwiftUI

struct ContentView: View {
    @State private var isPresented = false
    
    var imageSize: CGFloat = 20
    
    var body: some View {
        VStack {
            Text("Hello, world!")
                .padding()
            Button("Show Info") {
                isPresented = true
            }
        }.infoSheet(
            isPresented: $isPresented,
            prefersGrabberVisible: true,
            prefersEdgeAttachedInCompactHeight: true,
            widthFollowsPreferredContentSizeWhenEdgeAttached: true,
            prefersScrollingExpandsWhenScrolledToEdge: true,
            preferredCornerRadius: 23,
            detents: [.medium()]) {
                VStack {
//                    Spacer()
                    Text("You can edit scorecards!")
                        .font(.system(size:24))
                        .bold()
                        .padding()
                    VStack(spacing: 15) {
                        HStack {
                            Image(systemName: "pencil")
                                .resizable()
                                .frame(width: imageSize, height: imageSize)
                            VStack(alignment: .leading) {
                                Text("Message 1 something something").bold()
                                Text("Longer message with some extra listings about what is happening")
                            }.padding(5)
                        }
                        HStack {
                            Image(systemName: "pencil")
                                .resizable()
                                .frame(width: imageSize, height: imageSize)
                            VStack(alignment: .leading) {
                                Text("Message 2 something something").bold()
                                Text("Longer message with some extra listings about what is happening")
                            }.padding(5)
                        }
                        HStack {
                            Image(systemName: "pencil")
                                .resizable()
                                .frame(width: imageSize, height: imageSize)
                            VStack(alignment: .leading) {
                                Text("Message 3 something something").bold()
                                Text("Longer message with some extra listings about what is happening")
                            }.padding(5)
                        }
                    }
                }
            }
            .onChange(of: isPresented) { newValue in
                print("Changed to \(newValue)")
            }
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
//        ContentView().preferredColorScheme(.dark)
        ContentView().preferredColorScheme(.light)
    }
}


