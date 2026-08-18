import CoreText
import SwiftUI

/// Brand type system.
///
/// Pending for the production app: Marcella (headings) and Century Gothic (UI) are licensed
/// typefaces. This demo uses the closest free equivalents — Playfair Display for the editorial
/// serif voice and Jost for the geometric sans voice. Parisienne is already libre and is the
/// real logotype face.
nonisolated enum AuraFont {
    private static let logoFace = "Parisienne-Regular"
    private static let displayFace = "PlayfairDisplay-Regular"
    private static let bodyFace = "Jost-Regular"

    // Logotype
    static func logo(_ size: CGFloat = 46) -> Font {
        .custom(logoFace, size: size, relativeTo: .largeTitle)
    }

    // Editorial serif voice
    static func displayLarge(_ size: CGFloat = 44) -> Font {
        .custom(displayFace, size: size, relativeTo: .largeTitle)
    }

    static func displayMedium(_ size: CGFloat = 36) -> Font {
        .custom(displayFace, size: size, relativeTo: .largeTitle)
    }

    static func displaySmall(_ size: CGFloat = 30) -> Font {
        .custom(displayFace, size: size, relativeTo: .title)
    }

    static func headlineLarge(_ size: CGFloat = 28) -> Font {
        .custom(displayFace, size: size, relativeTo: .title)
    }

    static func headlineMedium(_ size: CGFloat = 24) -> Font {
        .custom(displayFace, size: size, relativeTo: .title2)
    }

    static func headlineSmall(_ size: CGFloat = 20) -> Font {
        .custom(displayFace, size: size, relativeTo: .title3)
    }

    static func titleLarge(_ size: CGFloat = 20) -> Font {
        .custom(displayFace, size: size, relativeTo: .title3)
    }

    // Geometric sans voice
    static func titleMedium(_ size: CGFloat = 16) -> Font {
        .custom(bodyFace, size: size, relativeTo: .headline)
    }

    static func titleSmall(_ size: CGFloat = 14) -> Font {
        .custom(bodyFace, size: size, relativeTo: .subheadline)
    }

    static func bodyLarge(_ size: CGFloat = 16) -> Font {
        .custom(bodyFace, size: size, relativeTo: .body)
    }

    static func bodyMedium(_ size: CGFloat = 14) -> Font {
        .custom(bodyFace, size: size, relativeTo: .callout)
    }

    static func bodySmall(_ size: CGFloat = 12) -> Font {
        .custom(bodyFace, size: size, relativeTo: .footnote)
    }

    static func labelLarge(_ size: CGFloat = 15) -> Font {
        .custom(bodyFace, size: size, relativeTo: .subheadline)
    }

    static func labelMedium(_ size: CGFloat = 12) -> Font {
        .custom(bodyFace, size: size, relativeTo: .caption)
    }

    static func labelSmall(_ size: CGFloat = 11) -> Font {
        .custom(bodyFace, size: size, relativeTo: .caption2)
    }
}

/// Registers the bundled brand faces so `Font.custom` can resolve them.
nonisolated enum FontRegistrar {
    private static let faces = [
        "Parisienne-Regular",
        "PlayfairDisplay-Regular",
        "Jost-Regular"
    ]

    static func registerBundledFonts() {
        for face in faces {
            guard let url = Bundle.main.url(forResource: face, withExtension: "ttf") else {
                continue
            }
            var error: Unmanaged<CFError>?
            CTFontManagerRegisterFontsForURL(url as CFURL, .process, &error)
            error?.release()
        }
    }
}

/// Uppercase eyebrow label used above section content.
nonisolated struct EyebrowText: View {
    let text: String
    var color: Color = AuraPalette.inkFaint

    var body: some View {
        Text(text.uppercased())
            .font(AuraFont.labelSmall())
            .tracking(1.4)
            .foregroundStyle(color)
    }
}
