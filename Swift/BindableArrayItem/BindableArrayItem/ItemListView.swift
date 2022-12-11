//
//  ItemListView.swift
//  BindableArrayItem
//
//  Created by Eric Ferguson on 6/25/22.
//

import SwiftUI

struct ItemListView: View {
    @StateObject private var itemList = ItemList()
    
    var body: some View {
        NavigationView {
            List {
                ForEach(itemList.items) { item in
                    ItemSummaryView(item: item)
                }
            }
        }.task {
            // Add some dummy items
            try? await Task.sleep(nanoseconds: 2_000_000_000)
            await itemList.addFakeItems(Int.random(in: 1...10))
        }.refreshable {
            // Creating this to show that it's been updated
            itemList.reset()
            await itemList.addFakeItems(Int.random(in: 1...10))
        }
    }
}

struct ItemListView_Previews: PreviewProvider {
    static var previews: some View {
        ItemListView()
    }
}
