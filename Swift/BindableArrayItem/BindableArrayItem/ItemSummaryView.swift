//
//  ItemSummaryView.swift
//  BindableArrayItem
//
//  Created by Eric Ferguson on 6/25/22.
//

import SwiftUI

struct ItemSummaryView: View {
    @ObservedObject var item: Item
    
    var body: some View {
        NavigationLink(destination: ItemView(item: item)) {
            Text(item.itemDetails.name)
        }
    }
}
