//
//  ContentView.swift
//  ShapeTest
//
//  Created by Eric Ferguson on 3/19/21.
//

import SwiftUI

struct ShrinkingSquares: Shape {
    let size: CGSize

    func path(in rect: CGRect) -> Path {
//        var path = Path()

//        for i in stride(from: 1, through: 100, by: 5.0) {
//            let rect = CGRect(x: 0, y: 0, width: rect.width, height: rect.height)
//            let insetRect = rect.insetBy(dx: CGFloat(i), dy: CGFloat(i))
//            path.addRect(insetRect)
//        }

        var path = Rectangle().path(in: rect)

        let origin = CGPoint(x: rect.minX - size.width / 2, y: rect.minY - size.height / 2)
        path.addRect(CGRect(origin: origin, size: size))
    
        return path
    }
}

struct ContentView: View {
    static var size: Int = 200
    var frameWidth:CGFloat = CGFloat(size)
    var frameHeight:CGFloat = CGFloat(size)
    var body: some View {
        ZStack {
            Rectangle()
                .stroke()
                .frame(width: frameWidth*1.2, height: frameHeight*1.2)
            Rectangle()
                .stroke()
                .frame(width: frameWidth, height: frameHeight)
            Rectangle()
                .fill(Color.white)
//                .stroke()
                .frame(width: frameWidth, height: frameHeight)
                .overlay(
            GeometryReader { geometry in
                // All geography is relative to the frame
                let segments:CGFloat = 4.0
                Path { path in
                    let widthSegmentSize  = geometry.size.width  / segments
                    let heightSegmentSize = geometry.size.height / segments
                    
                    path.addLines([
                        CGPoint(x:heightSegmentSize*1, y:0),
                        CGPoint(x:heightSegmentSize*2, y:0),
                        CGPoint(x:0, y:widthSegmentSize*2),
                        CGPoint(x:0, y:widthSegmentSize*1),
                    ])
                    path.addLines([
                        CGPoint(x:heightSegmentSize*3, y:0),
                        CGPoint(x:heightSegmentSize*4, y:0),
                        CGPoint(x:0, y:widthSegmentSize*4),
                        CGPoint(x:0, y:widthSegmentSize*3),
                    ])
                    path.addLines([
                        CGPoint(x:heightSegmentSize*4, y:widthSegmentSize*1),
                        CGPoint(x:heightSegmentSize*4, y:widthSegmentSize*2),
                        CGPoint(x:heightSegmentSize*2, y:widthSegmentSize*4),
                        CGPoint(x:heightSegmentSize*1, y:widthSegmentSize*4),
                    ])
                    path.addLines([
                        CGPoint(x:heightSegmentSize*4, y:widthSegmentSize*3),
                        CGPoint(x:heightSegmentSize*4, y:widthSegmentSize*4),
                        CGPoint(x:heightSegmentSize*4, y:widthSegmentSize*4),
                        CGPoint(x:heightSegmentSize*3, y:widthSegmentSize*4),
                    ])
                }.fill(Color.black.opacity(0.25))
            }.aspectRatio(1, contentMode: .fit))
            Text("5").font(.system(size: CGFloat(ContentView.size)))
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
