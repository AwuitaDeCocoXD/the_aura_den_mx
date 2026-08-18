import SwiftUI

/// The Aura Den brand palette. Exact hex values from the brand board.
nonisolated enum AuraPalette {
    // Core brand
    static let blue = Color(hex: 0x004FA7)
    static let navy = Color(hex: 0x0A3A6E)
    static let navyDeep = Color(hex: 0x072B52)
    static let yellow = Color(hex: 0xF8F101)
    static let sand = Color(hex: 0xD4B896)
    static let white = Color(hex: 0xFFFFFF)
    static let black = Color(hex: 0x000000)

    // Warm neutrals for cards, canvases and soft surfaces
    static let cream = Color(hex: 0xFBF6EE)
    static let sandSoft = Color(hex: 0xEFE3D2)
    static let canvas = Color(hex: 0xF4F5F7)
    static let surface = Color(hex: 0xFFFFFF)
    static let blueSoft = Color(hex: 0xE8F0FA)
    static let blueTint = Color(hex: 0xD3E2F4)

    // Text tones
    static let ink = Color(hex: 0x0A3A6E)
    static let inkMuted = Color(hex: 0x6B7A8F)
    static let inkFaint = Color(hex: 0x9AA6B6)
    static let divider = Color(hex: 0xE4E7EC)

    // Status tones — sober, never neon
    static let green = Color(hex: 0x12855C)
    static let greenSoft = Color(hex: 0xD8F1E4)
    static let amber = Color(hex: 0x9A6B00)
    static let amberSoft = Color(hex: 0xFBEECB)
    static let red = Color(hex: 0xC0362C)
    static let redSoft = Color(hex: 0xFAE4E1)
    static let grey = Color(hex: 0x6B7A8F)
    static let greySoft = Color(hex: 0xEAEDF2)
}

nonisolated extension Color {
    /// Creates a color from a packed 24-bit RGB literal such as `0x004FA7`.
    init(hex: UInt32) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xFF) / 255.0,
            green: Double((hex >> 8) & 0xFF) / 255.0,
            blue: Double(hex & 0xFF) / 255.0,
            opacity: 1.0
        )
    }
}

nonisolated enum AuraRadius {
    static let card: CGFloat = 22
    static let control: CGFloat = 16
    static let button: CGFloat = 30
    static let chip: CGFloat = 999
}
