import SwiftUI

/// The Parisienne logotype, used on brand moments only.
struct LogoWordmark: View {
    var size: CGFloat = 46
    var color: Color = AuraPalette.white

    var body: some View {
        Text(AuraCopy.brandName)
            .font(AuraFont.logo(size))
            .foregroundStyle(color)
            .accessibilityAddTraits(.isHeader)
    }
}

/// The half-ellipse brand arc that crowns the logotype.
nonisolated struct AuraArcShape: Shape {
    var lineWidth: CGFloat = 2

    func path(in rect: CGRect) -> Path {
        let inset = lineWidth / 2
        let width = max(rect.width - lineWidth, 1)
        let height = max(rect.height - inset, 1)

        var unit = Path()
        unit.addArc(
            center: .zero,
            radius: 0.5,
            startAngle: .degrees(180),
            endAngle: .degrees(360),
            clockwise: false
        )

        let transform = CGAffineTransform(translationX: rect.midX, y: rect.maxY)
            .scaledBy(x: width, y: height * 2)
        return unit.applying(transform)
    }
}

/// Stroked brand arc, sized by its frame.
struct AuraArc: View {
    var color: Color = AuraPalette.yellow
    var lineWidth: CGFloat = 2

    var body: some View {
        AuraArcShape(lineWidth: lineWidth)
            .stroke(color, style: StrokeStyle(lineWidth: lineWidth, lineCap: .round))
    }
}

enum LogoSize {
    case large
    case medium
    case compact

    var script: CGFloat {
        switch self {
        case .large: 58
        case .medium: 38
        case .compact: 26
        }
    }

    var arcWidth: CGFloat {
        switch self {
        case .large: 86
        case .medium: 58
        case .compact: 40
        }
    }

    var theSize: CGFloat {
        switch self {
        case .large: 16
        case .medium: 12
        case .compact: 9
        }
    }

    var denSize: CGFloat {
        switch self {
        case .large: 14
        case .medium: 11
        case .compact: 8
        }
    }

    var arcSpacing: CGFloat {
        self == .large ? 10 : 4
    }

    var arcStroke: CGFloat {
        self == .large ? 2.5 : 1.5
    }
}

/// "The Aura Den" logotype: brand arc over the script wordmark, exactly as on Android.
struct AuraLogo: View {
    var size: LogoSize = .large
    var textColor: Color = AuraPalette.white
    var scriptColor: Color = AuraPalette.yellow
    var arcColor: Color = AuraPalette.yellow

    var body: some View {
        VStack(spacing: 0) {
            AuraArc(color: arcColor, lineWidth: size.arcStroke)
                .frame(width: size.arcWidth, height: size.arcWidth / 2.6)
                .padding(.bottom, size.arcSpacing)

            Text("The")
                .font(AuraFont.displaySmall(size.theSize))
                .foregroundStyle(textColor)

            Text("Aura")
                .font(AuraFont.logo(size.script))
                .foregroundStyle(scriptColor)
                .padding(.vertical, -size.script * 0.16)

            Text("D E N")
                .font(AuraFont.labelMedium(size.denSize))
                .tracking(size.denSize * 0.35)
                .foregroundStyle(textColor)
        }
        .accessibilityElement(children: .ignore)
        .accessibilityLabel(AuraCopy.brandName)
        .accessibilityAddTraits(.isHeader)
    }
}

/// Very subtle dotted texture inspired by the brand board.
nonisolated struct BrandTexture: View {
    var color: Color = AuraPalette.navy
    var opacity: Double = 0.35

    var body: some View {
        Canvas { context, size in
            let step = size.width / 7
            let dot: CGFloat = 0.8
            var y = step * 0.6
            while y < size.height {
                var x = step * 0.5
                while x < size.width {
                    let rect = CGRect(
                        x: x - dot,
                        y: y - dot,
                        width: dot * 2,
                        height: dot * 2
                    )
                    context.fill(Path(ellipseIn: rect), with: .color(color.opacity(opacity)))
                    x += step
                }
                y += step
            }
        }
        .allowsHitTesting(false)
    }
}

/// The signature arch shape from the brand board — a rounded top on a straight base.
nonisolated struct ArchShape: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        let radius = rect.width / 2
        path.move(to: CGPoint(x: rect.minX, y: rect.maxY))
        path.addLine(to: CGPoint(x: rect.minX, y: rect.minY + radius))
        path.addArc(
            center: CGPoint(x: rect.midX, y: rect.minY + radius),
            radius: radius,
            startAngle: .degrees(180),
            endAngle: .degrees(0),
            clockwise: false
        )
        path.addLine(to: CGPoint(x: rect.maxX, y: rect.maxY))
        path.closeSubpath()
        return path
    }
}

/// Brand header used at the top of every screen: deep blue block with soft rounded bottom,
/// optional back arrow and optional trailing action.
struct AuraHeader<Trailing: View, Content: View>: View {
    let title: String
    var eyebrow: String?
    var subtitle: String?
    var onBack: (() -> Void)?
    var containerColor: Color = AuraPalette.blue
    var titleColor: Color = AuraPalette.white
    @ViewBuilder var trailing: Trailing
    @ViewBuilder var content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .center, spacing: 0) {
                if let onBack {
                    Button {
                        AuraHaptics.tap()
                        onBack()
                    } label: {
                        Image(systemName: "arrow.left")
                            .font(.system(size: 20, weight: .medium))
                            .foregroundStyle(titleColor)
                            .frame(width: 48, height: 48)
                            .contentShape(.rect)
                    }
                    .buttonStyle(.plain)
                    .accessibilityLabel("Regresar")
                } else {
                    Spacer().frame(width: 8)
                }

                VStack(alignment: .leading, spacing: 0) {
                    if let eyebrow {
                        Text(eyebrow.uppercased())
                            .font(AuraFont.labelSmall())
                            .tracking(1.4)
                            .foregroundStyle(AuraPalette.yellow)
                            .padding(.bottom, 4)
                    }
                    Text(title)
                        .font(AuraFont.headlineMedium())
                        .foregroundStyle(titleColor)
                        .fixedSize(horizontal: false, vertical: true)
                    if let subtitle {
                        Text(subtitle)
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(titleColor.opacity(0.82))
                            .fixedSize(horizontal: false, vertical: true)
                            .padding(.top, 3)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                trailing
                    .padding(.leading, 12)
            }

            content
                .padding(.horizontal, 8)
                .padding(.top, 18)
        }
        .padding(.leading, 8)
        .padding(.trailing, 16)
        .padding(.top, 8)
        .padding(.bottom, 22)
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.top, safeAreaTop)
        .background(
            containerColor,
            in: .rect(bottomLeadingRadius: 28, bottomTrailingRadius: 28)
        )
    }

    private var safeAreaTop: CGFloat {
        #if canImport(UIKit)
        let scenes = UIApplication.shared.connectedScenes
        let window = scenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap(\.windows)
            .first { $0.isKeyWindow }
        return window?.safeAreaInsets.top ?? 47
        #else
        return 47
        #endif
    }
}

extension AuraHeader where Trailing == EmptyView, Content == EmptyView {
    init(
        title: String,
        eyebrow: String? = nil,
        subtitle: String? = nil,
        onBack: (() -> Void)? = nil,
        containerColor: Color = AuraPalette.blue,
        titleColor: Color = AuraPalette.white
    ) {
        self.init(
            title: title,
            eyebrow: eyebrow,
            subtitle: subtitle,
            onBack: onBack,
            containerColor: containerColor,
            titleColor: titleColor,
            trailing: { EmptyView() },
            content: { EmptyView() }
        )
    }
}

extension AuraHeader where Trailing == EmptyView {
    init(
        title: String,
        eyebrow: String? = nil,
        subtitle: String? = nil,
        onBack: (() -> Void)? = nil,
        containerColor: Color = AuraPalette.blue,
        titleColor: Color = AuraPalette.white,
        @ViewBuilder content: () -> Content
    ) {
        self.init(
            title: title,
            eyebrow: eyebrow,
            subtitle: subtitle,
            onBack: onBack,
            containerColor: containerColor,
            titleColor: titleColor,
            trailing: { EmptyView() },
            content: content
        )
    }
}

extension AuraHeader where Content == EmptyView {
    init(
        title: String,
        eyebrow: String? = nil,
        subtitle: String? = nil,
        onBack: (() -> Void)? = nil,
        containerColor: Color = AuraPalette.blue,
        titleColor: Color = AuraPalette.white,
        @ViewBuilder trailing: () -> Trailing
    ) {
        self.init(
            title: title,
            eyebrow: eyebrow,
            subtitle: subtitle,
            onBack: onBack,
            containerColor: containerColor,
            titleColor: titleColor,
            trailing: trailing,
            content: { EmptyView() }
        )
    }
}

/// Small decorative arch + wordmark for card corners, matching the membership cards.
struct AuraCardMark: View {
    var tint: Color = AuraPalette.blue

    var body: some View {
        VStack(spacing: 0) {
            AuraArc(color: AuraPalette.yellow, lineWidth: 1.5)
                .frame(width: 34, height: 13)
                .padding(.bottom, 3)
            Text("The")
                .font(AuraFont.displaySmall(9))
                .foregroundStyle(tint)
            Text("Aura")
                .font(AuraFont.logo(22))
                .foregroundStyle(tint)
                .padding(.vertical, -4)
            Text("D E N")
                .font(AuraFont.labelMedium(7))
                .tracking(2.4)
                .foregroundStyle(tint)
        }
    }
}

/// Deep navy header with the arch motif behind the content.
struct AuraBrandHeader<Content: View>: View {
    var showsLogo: Bool = true
    @ViewBuilder var content: Content

    var body: some View {
        ZStack(alignment: .top) {
            LinearGradient(
                colors: [AuraPalette.blue, AuraPalette.navyDeep],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .overlay(alignment: .topTrailing) {
                ArchShape()
                    .fill(AuraPalette.white.opacity(0.06))
                    .frame(width: 190, height: 240)
                    .offset(x: 60, y: -70)
                    .allowsHitTesting(false)
            }
            .overlay(alignment: .bottomLeading) {
                Circle()
                    .fill(AuraPalette.yellow.opacity(0.14))
                    .frame(width: 150, height: 150)
                    .offset(x: -60, y: 70)
                    .allowsHitTesting(false)
            }

            VStack(alignment: .leading, spacing: 14) {
                if showsLogo {
                    LogoWordmark(size: 34)
                }
                content
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(.horizontal, 20)
            .padding(.top, 12)
            .padding(.bottom, 26)
        }
        .clipShape(.rect(bottomLeadingRadius: 30, bottomTrailingRadius: 30))
        .ignoresSafeArea(edges: .top)
    }
}

/// Small "demo" marker so nobody mistakes the prototype for the shipped product.
struct DemoBadge: View {
    var body: some View {
        Text("Demo")
            .font(AuraFont.labelSmall())
            .tracking(1.2)
            .foregroundStyle(AuraPalette.navy)
            .padding(.horizontal, 10)
            .padding(.vertical, 5)
            .background(AuraPalette.yellow, in: .capsule)
    }
}

/// Circular remote avatar with a warm placeholder.
struct AuraAvatar: View {
    let imageURL: String
    var size: CGFloat = 52
    var ringColor: Color = AuraPalette.white

    var body: some View {
        Circle()
            .fill(AuraPalette.sandSoft)
            .frame(width: size, height: size)
            .overlay {
                AsyncImage(url: URL(string: imageURL)) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    default:
                        Image(systemName: "person.fill")
                            .font(.system(size: size * 0.42))
                            .foregroundStyle(AuraPalette.sand)
                    }
                }
                .allowsHitTesting(false)
            }
            .clipShape(.circle)
            .overlay {
                Circle().strokeBorder(ringColor.opacity(0.7), lineWidth: 2)
            }
    }
}
