//
//  ContentView.swift
//  PhoneNumberTextField
//
//  Created by Eric Ferguson on 8/15/21.
//

import SwiftUI


struct ContentView: View {
    @State private var phoneNumber:String = ""
    
    var body: some View {
        TextField("(123) 456-7890",
                  text: $phoneNumber)
            .onChange(of: phoneNumber) { phoneNumber in
                print("before: '\(self.phoneNumber)'")
                self.phoneNumber = phoneNumber.applyPatternOnNumbers(pattern: "(###) ###-####", replacementCharacter: "#")
                print("after: '\(self.phoneNumber)'")
            }
            .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
