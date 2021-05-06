//
//  ContentView.swift
//  TabViewSelectedBug
//
//  Created by Eric Ferguson on 5/6/21.
//
// Output of program when selecting tab2 then tab1 is as follows
// -------------------------------
// Tab: Tab1 selection: 1
//     selected will be set: 2
//     selected was set: 2
// Tab: Tab2 selection: 2
// Tab: Tab1 selection: 2     <<<<< BUG
//     selected will be set: 1
//     selected was set: 1

import SwiftUI

// Simple View so I can see when onAppear is called
struct TabViewExample : View {
    var tabName: String
    @ObservedObject var selection: Selection
    
    var body: some View {
        Text(tabName).onAppear(perform: {
            print("Tab:", tabName, "selection:", selection.selected)
        })
    }
}

// Simple wrapper so I can see when TabView is updating the selection
class Selection: ObservableObject {
    internal init(value: Int) {
        self.selected = value
    }
    
    @Published var selected: Int {
        willSet(newValue) {
            print("\tselected will be set:", newValue)
        }
        didSet {
            print("\tselected was set:", selected)
        }
    }
}

struct ContentView: View {
    @StateObject var selection: Selection = Selection(value: 1)
    var body: some View {
        TabView(selection: $selection.selected) {
            TabViewExample(tabName: "Tab1", selection: selection)
                .tabItem {
                Text("Tab1")
            }.tag(1)
            TabViewExample(tabName: "Tab2", selection: selection)
                .tabItem {
                Text("Tab2")
            }.tag(2)
        }
    }
}
