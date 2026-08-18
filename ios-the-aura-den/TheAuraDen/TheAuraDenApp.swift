//
//  TheAuraDenApp.swift
//  TheAuraDen
//
//  Created by Rork on August 17, 2026.
//

import SwiftUI

@main
struct TheAuraDenApp: App {
    init() {
        FontRegistrar.registerBundledFonts()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
