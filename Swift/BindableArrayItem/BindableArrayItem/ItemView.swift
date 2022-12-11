//
//  ItemView.swift
//  BindableArrayItem
//
//  Created by Eric Ferguson on 6/25/22.
//

import SwiftUI

struct ItemView: View {
    @ObservedObject var item: Item
    
    var body: some View {
        HStack {
            Text(item.itemDetails.name)
            Spacer()
            Button(action: {
                item.itemDetails.name = "update via name"
                print("updating via name")
            }) {
                Text("update via name")
            }.buttonStyle(BorderlessButtonStyle())
            Spacer()
            Button(action: {
                item.itemDetails = ItemDetails(name: "update via details")
                print("updating via details")
            }) {
                Text("update via details")
            }.buttonStyle(BorderlessButtonStyle())
        }
    }
}
