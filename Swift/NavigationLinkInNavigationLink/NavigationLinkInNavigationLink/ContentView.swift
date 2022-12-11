//
//  ContentView.swift
//  NavigationLinkInNavigationLink
//
//  Created by Eric Ferguson on 7/23/22.
//

import SwiftUI

struct InnerView: View {
    @State var notifications: [Int] = [1,2,3,4,5]
    @State var selection: Int? = nil

    var body: some View {
        List {
            ForEach(notifications, id: \.self) { notification in
                ZStack {
                    NavigationLink(destination: Text("Page \(notification)"),
                                   tag: notification,
                                   selection: $selection) {
                        EmptyView()
                    }.hidden()
                    
                    Button(action: {
                        self.selection = notification
                    })
                    {
                        HStack {
                            Text("Pick \(notification)")
                                .padding()
                        }
                    }.background(Color.gray)
                        .buttonStyle(PlainButtonStyle())
                }
            }
        }.listStyle(.plain)
         .navigationBarTitle("Notifications", displayMode: .inline)
         .task {
            selection = 2
        }.onChange(of: selection) { newValue in
            print("selection update to \(newValue)")
        }
    }
}


struct ContentView: View {
    @State var selection: Int?
    var body: some View {
        NavigationView {
            NavigationLink(destination: InnerView(), tag: 1, selection: $selection) {
                Text("InnerView")
            }
        }//.navigationViewStyle(.stack)
    }
}
