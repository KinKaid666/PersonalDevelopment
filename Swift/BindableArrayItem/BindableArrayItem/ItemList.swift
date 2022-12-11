//
//  OOArray.swift
//  BindableArrayItem
//
//  Created by Eric Ferguson on 6/25/22.
//

import Foundation

struct ItemDetails {
    var name: String
}

class Item : ObservableObject, Identifiable, Equatable {
    static func == (lhs: Item, rhs: Item) -> Bool {
        return lhs.id == rhs.id
    }
    
    var id = UUID()
    @Published var itemDetails: ItemDetails
    
    init(itemDetails: ItemDetails) {
        self.itemDetails = itemDetails
    }
}

class ItemList : ObservableObject {
    @Published var items = [Item]()
    
    func addFakeItems(_ count: Int) async {
        for x in 0..<count {
            items.append(Item(itemDetails: ItemDetails(name: "Item \(x)")))
        }
    }
    
    func reset() {
        self.items = []
    }
}
