//
//  InfoSheetView.swift
//  InfoSheetView
//
//  Created by Eric Ferguson on 8/15/22.
//  Credit: https://github.com/dwancin/BottomSheet

import UIKit
import SwiftUI

@available(iOS 15.0, *)
public extension View {
    func infoSheet<Content: View>(isPresented: Binding<Bool>, prefersGrabberVisible: Bool, prefersEdgeAttachedInCompactHeight: Bool, widthFollowsPreferredContentSizeWhenEdgeAttached: Bool, prefersScrollingExpandsWhenScrolledToEdge: Bool, preferredCornerRadius: CGFloat, detents: [UISheetPresentationController.Detent], @ViewBuilder content: @escaping () -> Content) -> some View {
        self
            .background(InfoSheetView(isPresented: isPresented, prefersGrabberVisible: prefersGrabberVisible, prefersEdgeAttachedInCompactHeight: prefersEdgeAttachedInCompactHeight, widthFollowsPreferredContentSizeWhenEdgeAttached: widthFollowsPreferredContentSizeWhenEdgeAttached, prefersScrollingExpandsWhenScrolledToEdge: prefersScrollingExpandsWhenScrolledToEdge, preferredCornerRadius: preferredCornerRadius, detents: detents, content: content))
    }
}

@available(iOS 15.0, *)
struct InfoSheetView<Content: View>: UIViewControllerRepresentable {
    @Binding var isPresented: Bool
    let prefersGrabberVisible: Bool
    let prefersEdgeAttachedInCompactHeight: Bool
    let widthFollowsPreferredContentSizeWhenEdgeAttached: Bool
    let prefersScrollingExpandsWhenScrolledToEdge: Bool
    let preferredCornerRadius: CGFloat
    let detents: [UISheetPresentationController.Detent]
    @ViewBuilder let content: Content
    
    func makeCoordinator() -> Coordinator {
        return Coordinator(parent: self)
    }
    
    func makeUIViewController(context: Context) -> UIViewController {
        let controller = UIViewController()
        return controller
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        if isPresented {
            let sheetController = InfoSheetController(rootView: content,
                                                      prefersGrabberVisible: prefersGrabberVisible,
                                                      prefersEdgeAttachedInCompactHeight: prefersEdgeAttachedInCompactHeight,
                                                      widthFollowsPreferredContentSizeWhenEdgeAttached: widthFollowsPreferredContentSizeWhenEdgeAttached,
                                                      prefersScrollingExpandsWhenScrolledToEdge: prefersScrollingExpandsWhenScrolledToEdge,
                                                      preferredCornerRadius: preferredCornerRadius, detents: detents)
            
            sheetController.presentationController?.delegate = context.coordinator
            DispatchQueue.main.async {
                uiViewController.present(sheetController, animated: true)
                isPresented.toggle()
            }
        }
    }
    
    final class Coordinator: NSObject, UISheetPresentationControllerDelegate {
        private let parent: InfoSheetView
        init(parent: InfoSheetView) {
            self.parent = parent
        }
    }
}
