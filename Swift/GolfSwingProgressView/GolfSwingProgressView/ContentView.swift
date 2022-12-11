//
//  ContentView.swift
//  GolfSwingProgressView
//
//  Created by Eric Ferguson on 7/25/22.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        RotatingImageView()
            .frame(width: 200,height: 200)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
