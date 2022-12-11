//
//  GolfSwingProgressView.swift
//  GolfSwingProgressView
//
//  Created by Eric Ferguson on 7/25/22.
//

import SwiftUI
import Combine

class RotatingImageViewModel: ObservableObject {
    private var cancellable: AnyCancellable?
    private let timer = Timer.publish(every: 0.25, on: .main, in: .common).autoconnect()
    private var filenames: [String]
    private var index: Int = 0
    @Published var image: Image?
    
    init(_ filenames: [String]) {
        self.filenames = filenames
    }
    
    func start() {
        cancellable = timer.map { currentDate in
            self.index += 1
            return Image(self.filenames[self.index % self.filenames.count])
        }.assign(to: \.image, on: self)
    }
    
    func stop() {
        cancellable?.cancel()
    }
}

struct RotatingImageView: View {
    @StateObject var viewModel = RotatingImageViewModel(["BF_GolfSwing-1",
                                                         "BF_GolfSwing-2",
                                                         "BF_GolfSwing-3",
                                                         "BF_GolfSwing-4",
                                                         "BF_GolfSwing-5",
                                                         "BF_GolfSwing-6",
                                                         "BF_GolfSwing-7",
                                                         "BF_GolfSwing-8",
                                                        ])

    var body: some View {
        HStack {
            Button(action: viewModel.start) {
                Image("play")
            }
            Button(action: viewModel.stop) {
                Image("stop")
            }
        }
        if let image = viewModel.image {
            image
                .resizable()
                .scaledToFill()
                .foregroundColor(.indigo)
        }
    }
}

struct RotatingImageView_Previews: PreviewProvider {
    static var previews: some View {
        RotatingImageView()
    }
}
