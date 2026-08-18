import SwiftUI

// MARK: - Surfaces

/// Soft white card used for nearly every block of content.
struct AuraCard<Content: View>: View {
    var background: Color = AuraPalette.surface
    var padding: CGFloat = 18
    @ViewBuilder var content: Content

    var borderColor: Color?

    var body: some View {
        content
            .padding(padding)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(background, in: .rect(cornerRadius: AuraRadius.card))
            .overlay {
                if let borderColor {
                    RoundedRectangle(cornerRadius: AuraRadius.card)
                        .strokeBorder(borderColor, lineWidth: 1.5)
                }
            }
    }
}

/// Section heading used between blocks of content.
struct SectionHeading: View {
    let text: String
    var actionLabel: String?
    var action: (() -> Void)?

    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            Text(text)
                .font(AuraFont.titleLarge())
                .foregroundStyle(AuraPalette.navy)
                .frame(maxWidth: .infinity, alignment: .leading)
            if let actionLabel, let action {
                Button {
                    AuraHaptics.tap()
                    action()
                } label: {
                    Text(actionLabel)
                        .font(AuraFont.labelLarge())
                        .foregroundStyle(AuraPalette.blue)
                }
                .buttonStyle(.plain)
            }
        }
        .frame(maxWidth: .infinity)
    }
}

/// Eyebrow + serif title, with an optional trailing action.
struct SectionHeader: View {
    let eyebrow: String
    let title: String
    var actionLabel: String?
    var action: (() -> Void)?

    var body: some View {
        HStack(alignment: .firstTextBaseline) {
            VStack(alignment: .leading, spacing: 4) {
                EyebrowText(text: eyebrow)
                Text(title)
                    .font(AuraFont.titleLarge())
                    .foregroundStyle(AuraPalette.navy)
            }
            Spacer()
            if let actionLabel, let action {
                Button(actionLabel, action: action)
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(AuraPalette.blue)
            }
        }
    }
}

// MARK: - Status

nonisolated enum AuraTone {
    case positive
    case warning
    case danger
    case neutral
    case brand

    var foreground: Color {
        switch self {
        case .positive: AuraPalette.green
        case .warning: AuraPalette.amber
        case .danger: AuraPalette.red
        case .neutral: AuraPalette.grey
        case .brand: AuraPalette.blue
        }
    }

    var background: Color {
        switch self {
        case .positive: AuraPalette.greenSoft
        case .warning: AuraPalette.amberSoft
        case .danger: AuraPalette.redSoft
        case .neutral: AuraPalette.greySoft
        case .brand: AuraPalette.blueSoft
        }
    }
}

nonisolated extension PaymentStatus {
    var tone: AuraTone {
        switch self {
        case .paid: .positive
        case .pending: .warning
        case .failed: .danger
        }
    }
}

nonisolated extension AppointmentStatus {
    var tone: AuraTone {
        switch self {
        case .confirmed: .positive
        case .completed: .neutral
        case .cancelled: .danger
        }
    }
}

nonisolated extension CheckInStatus {
    /// Waiting reads as the brand yellow pill on Android, so it gets its own colours.
    var pillForeground: Color {
        switch self {
        case .attended: AuraPalette.green
        case .waiting: AuraPalette.navy
        case .pending: AuraPalette.grey
        }
    }

    var pillBackground: Color {
        switch self {
        case .attended: AuraPalette.greenSoft
        case .waiting: AuraPalette.yellow
        case .pending: AuraPalette.greySoft
        }
    }
}

nonisolated extension AppointmentStatus {
    var pillForeground: Color {
        switch self {
        case .confirmed: AuraPalette.navy
        case .completed: AuraPalette.green
        case .cancelled: AuraPalette.red
        }
    }

    var pillBackground: Color {
        switch self {
        case .confirmed: AuraPalette.yellow
        case .completed: AuraPalette.greenSoft
        case .cancelled: AuraPalette.redSoft
        }
    }
}

struct StatusPill: View {
    let text: String
    var foreground: Color = AuraPalette.grey
    var background: Color = AuraPalette.greySoft
    var symbol: String?

    init(
        text: String,
        foreground: Color = AuraPalette.grey,
        background: Color = AuraPalette.greySoft,
        symbol: String? = nil
    ) {
        self.text = text
        self.foreground = foreground
        self.background = background
        self.symbol = symbol
    }

    init(text: String, tone: AuraTone, symbol: String? = nil) {
        self.text = text
        self.foreground = tone.foreground
        self.background = tone.background
        self.symbol = symbol
    }

    var body: some View {
        HStack(spacing: 5) {
            if let symbol {
                Image(systemName: symbol)
                    .font(.system(size: 12, weight: .semibold))
            }
            Text(text)
                .font(AuraFont.labelMedium())
                .fontWeight(.semibold)
        }
        .foregroundStyle(foreground)
        .padding(.horizontal, 12)
        .padding(.vertical, 6)
        .background(background, in: .capsule)
    }
}

struct PaymentStatusPill: View {
    let status: PaymentStatus

    var body: some View {
        StatusPill(text: status.label, tone: status.tone)
    }
}

struct CheckInStatusPill: View {
    let status: CheckInStatus

    var body: some View {
        StatusPill(
            text: status.label,
            foreground: status.pillForeground,
            background: status.pillBackground
        )
    }
}

struct AppointmentStatusPill: View {
    let status: AppointmentStatus

    var body: some View {
        StatusPill(
            text: status.label,
            foreground: status.pillForeground,
            background: status.pillBackground
        )
    }
}

// MARK: - Buttons

struct AuraPrimaryButton: View {
    let title: String
    var symbol: String?
    var tint: Color = AuraPalette.blue
    var foreground: Color = AuraPalette.white
    var isEnabled: Bool = true
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(spacing: 8) {
                if let symbol {
                    Image(systemName: symbol)
                        .font(.system(size: 15, weight: .semibold))
                }
                Text(title)
                    .font(AuraFont.labelLarge())
            }
            .frame(maxWidth: .infinity)
            .frame(height: 56)
            .foregroundStyle(foreground)
            .background(
                isEnabled ? tint : AuraPalette.inkFaint.opacity(0.35),
                in: .rect(cornerRadius: AuraRadius.button)
            )
        }
        .buttonStyle(PressableButtonStyle())
        .disabled(!isEnabled)
    }
}

struct AuraSecondaryButton: View {
    let title: String
    var symbol: String?
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(spacing: 8) {
                if let symbol {
                    Image(systemName: symbol)
                        .font(.system(size: 15, weight: .semibold))
                }
                Text(title)
                    .font(AuraFont.labelLarge())
            }
            .frame(maxWidth: .infinity)
            .frame(height: 54)
            .foregroundStyle(AuraPalette.blue)
            .overlay {
                RoundedRectangle(cornerRadius: AuraRadius.button)
                    .strokeBorder(AuraPalette.blue, lineWidth: 1.5)
            }
        }
        .buttonStyle(PressableButtonStyle())
    }
}

/// Subtle press feedback used across the demo.
nonisolated struct PressableButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.97 : 1)
            .opacity(configuration.isPressed ? 0.9 : 1)
            .animation(.spring(response: 0.28, dampingFraction: 0.7), value: configuration.isPressed)
    }
}

nonisolated enum AuraHaptics {
    @MainActor
    static func tap() {
        #if canImport(UIKit)
        UIImpactFeedbackGenerator(style: .light).impactOccurred()
        #endif
    }

    @MainActor
    static func success() {
        #if canImport(UIKit)
        UINotificationFeedbackGenerator().notificationOccurred(.success)
        #endif
    }
}

// MARK: - Chips

/// Filter chip used in browse and operations screens.
struct AuraFilterChip: View {
    let title: String
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            Text(title)
                .font(AuraFont.labelMedium())
                .fontWeight(.medium)
                .foregroundStyle(isSelected ? AuraPalette.white : AuraPalette.inkMuted)
                .padding(.horizontal, 16)
                .padding(.vertical, 10)
                .background(isSelected ? AuraPalette.blue : AuraPalette.white, in: .capsule)
                .overlay {
                    Capsule()
                        .strokeBorder(
                            isSelected ? AuraPalette.blue : AuraPalette.divider,
                            lineWidth: 1
                        )
                }
        }
        .buttonStyle(PressableButtonStyle())
    }
}

/// Selectable time slot chip.
struct TimeChip: View {
    let title: String
    let isSelected: Bool
    var isEnabled: Bool = true
    let action: () -> Void

    private var container: Color {
        if !isEnabled { return AuraPalette.greySoft }
        return isSelected ? AuraPalette.yellow : AuraPalette.white
    }

    private var content: Color {
        if !isEnabled { return AuraPalette.inkFaint }
        return isSelected ? AuraPalette.navy : AuraPalette.ink
    }

    var body: some View {
        Button {
            guard isEnabled else { return }
            AuraHaptics.tap()
            action()
        } label: {
            Text(title)
                .font(AuraFont.labelLarge())
                .foregroundStyle(content)
                .padding(.horizontal, 18)
                .padding(.vertical, 12)
                .background(container, in: .rect(cornerRadius: 18))
                .overlay {
                    RoundedRectangle(cornerRadius: 18)
                        .strokeBorder(
                            isSelected ? AuraPalette.yellow : AuraPalette.divider,
                            lineWidth: 1
                        )
                }
        }
        .buttonStyle(PressableButtonStyle())
        .disabled(!isEnabled)
    }
}

/// Horizontal date strip shared by reservation, agenda and scheduling flows.
struct DayPill: View {
    let weekday: String
    let day: String
    let isSelected: Bool
    var hasDot: Bool = false
    let action: () -> Void

    private var dotColor: Color {
        if !hasDot { return .clear }
        return isSelected ? AuraPalette.navy : AuraPalette.blue
    }

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            VStack(spacing: 0) {
                Text(weekday)
                    .font(AuraFont.labelSmall())
                    .tracking(0.6)
                    .foregroundStyle(isSelected ? AuraPalette.navy : AuraPalette.inkMuted)
                    .padding(.bottom, 4)
                Text(day)
                    .font(AuraFont.titleLarge())
                    .foregroundStyle(isSelected ? AuraPalette.navy : AuraPalette.ink)
                    .padding(.bottom, 6)
                Circle()
                    .fill(dotColor)
                    .frame(width: 5, height: 5)
            }
            .padding(.vertical, 12)
            .frame(width: 62)
            .background(
                isSelected ? AuraPalette.yellow : AuraPalette.white,
                in: .rect(cornerRadius: 18)
            )
        }
        .buttonStyle(PressableButtonStyle())
        .animation(.easeInOut(duration: 0.22), value: isSelected)
    }
}

/// Star rating row.
struct RatingStars: View {
    let rating: Double
    var starSize: CGFloat = 16
    var tint: Color = AuraPalette.yellow

    var body: some View {
        HStack(spacing: 1) {
            ForEach(0..<5, id: \.self) { index in
                Image(systemName: "star.fill")
                    .font(.system(size: starSize * 0.85))
                    .foregroundStyle(Double(index) < rating.rounded(.down) ? tint : AuraPalette.divider)
            }
        }
    }
}

// MARK: - Rows

/// Labelled row inside detail cards.
struct InfoRow: View {
    let label: String
    let value: String
    var symbol: String?
    var showsChevron: Bool = false
    var action: (() -> Void)?

    var body: some View {
        let row = HStack(alignment: .center, spacing: 0) {
            if let symbol {
                ZStack {
                    RoundedRectangle(cornerRadius: 13)
                        .fill(AuraPalette.sandSoft)
                        .frame(width: 42, height: 42)
                    Image(systemName: symbol)
                        .font(.system(size: 18))
                        .foregroundStyle(AuraPalette.navy)
                }
                .padding(.trailing, 14)
            }
            VStack(alignment: .leading, spacing: 2) {
                Text(label)
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                Text(value)
                    .font(AuraFont.bodyLarge())
                    .fontWeight(.medium)
                    .foregroundStyle(AuraPalette.ink)
                    .fixedSize(horizontal: false, vertical: true)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            if showsChevron {
                Image(systemName: "chevron.right")
                    .font(.system(size: 15, weight: .medium))
                    .foregroundStyle(AuraPalette.inkFaint)
            }
        }
        .padding(.horizontal, 18)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity)
        .contentShape(.rect)

        if let action {
            Button {
                AuraHaptics.tap()
                action()
            } label: {
                row
            }
            .buttonStyle(.plain)
        } else {
            row
        }
    }
}

struct AuraDivider: View {
    var body: some View {
        Rectangle()
            .fill(AuraPalette.divider)
            .frame(height: 1)
    }
}

// MARK: - Fields

/// Labelled text field matching the brand forms.
struct AuraTextField: View {
    let title: String
    var placeholder: String = ""
    @Binding var text: String
    var keyboard: UIKeyboardType = .default
    var isSecure: Bool = false
    var minLines: Int = 1

    @FocusState private var isFocused: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: 7) {
            EyebrowText(text: title)
            Group {
                if isSecure {
                    SecureField("", text: $text, prompt: promptText)
                } else if minLines > 1 {
                    TextField("", text: $text, prompt: promptText, axis: .vertical)
                        .lineLimit(minLines...(minLines + 3))
                } else {
                    TextField("", text: $text, prompt: promptText)
                }
            }
            .font(AuraFont.bodyLarge())
            .foregroundStyle(AuraPalette.ink)
            .tint(AuraPalette.blue)
            .focused($isFocused)
            .keyboardType(keyboard)
            .textInputAutocapitalization(keyboard == .emailAddress ? .never : .sentences)
            .autocorrectionDisabled(keyboard == .emailAddress)
            .padding(.horizontal, 16)
            .padding(.vertical, 17)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.control))
            .overlay {
                RoundedRectangle(cornerRadius: AuraRadius.control)
                    .strokeBorder(
                        isFocused ? AuraPalette.blue : AuraPalette.divider,
                        lineWidth: isFocused ? 2 : 1
                    )
            }
        }
    }

    private var promptText: Text {
        Text(placeholder)
            .font(AuraFont.bodyLarge())
            .foregroundColor(AuraPalette.inkFaint)
    }
}

// MARK: - Empty state

/// Empty state with a light hand-drawn arch illustration.
struct AuraEmptyState: View {
    let title: String
    let message: String
    var symbol: String?
    var actionLabel: String?
    var action: (() -> Void)?

    var body: some View {
        VStack(spacing: 0) {
            ZStack {
                Circle()
                    .fill(AuraPalette.blueSoft)
                    .frame(width: 96, height: 96)
                VStack(spacing: 8) {
                    AuraArc(color: AuraPalette.blue, lineWidth: 2)
                        .frame(width: 46, height: 18)
                    if let symbol {
                        Image(systemName: symbol)
                            .font(.system(size: 20))
                            .foregroundStyle(AuraPalette.blue)
                    }
                }
            }
            .padding(.bottom, 18)

            Text(title)
                .font(AuraFont.headlineSmall())
                .foregroundStyle(AuraPalette.navy)
                .multilineTextAlignment(.center)
                .padding(.bottom, 6)

            Text(message)
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.inkMuted)
                .multilineTextAlignment(.center)
                .fixedSize(horizontal: false, vertical: true)

            if let actionLabel, let action {
                AuraSecondaryButton(title: actionLabel, action: action)
                    .frame(width: 240)
                    .padding(.top, 20)
            }
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 24)
        .padding(.vertical, 34)
    }
}

// MARK: - Progress

/// Animated hours progress bar for memberships.
struct AuraProgressBar: View {
    let progress: Double
    var tint: Color = AuraPalette.blue
    var trackColor: Color = AuraPalette.sandSoft
    var height: CGFloat = 9

    @State private var animatedProgress: Double = 0

    var body: some View {
        GeometryReader { proxy in
            ZStack(alignment: .leading) {
                Capsule()
                    .fill(trackColor)
                Capsule()
                    .fill(tint)
                    .frame(width: proxy.size.width * animatedProgress)
            }
        }
        .frame(height: height)
        .onAppear {
            withAnimation(.spring(response: 0.7, dampingFraction: 0.85).delay(0.1)) {
                animatedProgress = max(0, min(progress, 1))
            }
        }
        .onChange(of: progress) { _, newValue in
            withAnimation(.spring(response: 0.5, dampingFraction: 0.85)) {
                animatedProgress = max(0, min(newValue, 1))
            }
        }
    }
}

/// Dropdown selector styled like the brand fields.
struct AuraDropdownField: View {
    let title: String
    @Binding var selection: String
    let options: [String]

    var body: some View {
        VStack(alignment: .leading, spacing: 7) {
            EyebrowText(text: title)
            Menu {
                ForEach(options, id: \.self) { option in
                    Button {
                        AuraHaptics.tap()
                        selection = option
                    } label: {
                        if option == selection {
                            Label(option, systemImage: "checkmark")
                        } else {
                            Text(option)
                        }
                    }
                }
            } label: {
                HStack(spacing: 8) {
                    Text(selection)
                        .font(AuraFont.bodyLarge())
                        .foregroundStyle(AuraPalette.ink)
                        .multilineTextAlignment(.leading)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Image(systemName: "chevron.down")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AuraPalette.inkMuted)
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 18)
                .frame(maxWidth: .infinity)
                .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.control))
                .overlay {
                    RoundedRectangle(cornerRadius: AuraRadius.control)
                        .strokeBorder(AuraPalette.divider, lineWidth: 1)
                }
            }
        }
    }
}

// MARK: - Tiles

/// Quick access tile used on the specialist dashboard.
struct QuickActionTile: View {
    let title: String
    let subtitle: String
    let symbol: String
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            VStack(alignment: .leading, spacing: 0) {
                Image(systemName: symbol)
                    .font(.system(size: 24))
                    .foregroundStyle(AuraPalette.blue)
                    .padding(.bottom, 14)
                Text(title)
                    .font(AuraFont.titleMedium())
                    .fontWeight(.semibold)
                    .foregroundStyle(AuraPalette.navy)
                    .multilineTextAlignment(.leading)
                    .padding(.bottom, 3)
                Text(subtitle)
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
            }
            .padding(18)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
        }
        .buttonStyle(PressableButtonStyle())
    }
}

/// Compact metric tile for operations and reports.
struct MetricTile: View {
    let value: String
    let label: String
    var caption: String?
    var captionColor: Color = AuraPalette.inkMuted
    var background: Color = AuraPalette.white

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Text(value)
                .font(AuraFont.headlineMedium())
                .foregroundStyle(AuraPalette.navy)
                .padding(.bottom, 4)
            Text(label)
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
            if let caption {
                Text(caption)
                    .font(AuraFont.labelSmall())
                    .tracking(0.6)
                    .foregroundStyle(captionColor)
                    .padding(.top, 6)
            }
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(background, in: .rect(cornerRadius: AuraRadius.card))
    }
}

/// Simple horizontal bar used in the admin reports screen.
struct BarRow: View {
    let label: String
    let value: String
    let share: Double
    var barColor: Color = AuraPalette.blue

    var body: some View {
        VStack(spacing: 8) {
            HStack(spacing: 8) {
                Text(label)
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.ink)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text(value)
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(AuraPalette.navy)
            }
            AuraProgressBar(
                progress: share,
                tint: barColor,
                trackColor: AuraPalette.cream,
                height: 10
            )
        }
        .frame(maxWidth: .infinity)
    }
}

/// Vertical bar chart column for monthly revenue.
struct ChartColumn: View {
    let label: String
    let share: Double
    let isHighlighted: Bool

    @State private var animated: Double = 0.05

    var body: some View {
        VStack(spacing: 8) {
            Spacer(minLength: 0)
            RoundedRectangle(cornerRadius: 10)
                .fill(isHighlighted ? AuraPalette.blue : AuraPalette.blueSoft)
                .frame(width: 26, height: 120 * animated)
            Text(label)
                .font(AuraFont.labelSmall())
                .tracking(0.6)
                .foregroundStyle(isHighlighted ? AuraPalette.navy : AuraPalette.inkMuted)
        }
        .frame(maxHeight: .infinity, alignment: .bottom)
        .onAppear {
            withAnimation(.easeOut(duration: 0.9)) {
                animated = min(max(share, 0.05), 1)
            }
        }
    }
}

// MARK: - Screen chrome

/// Warm canvas used behind every scrolling screen.
struct AuraCanvas<Content: View>: View {
    @ViewBuilder var content: Content

    var body: some View {
        ZStack {
            AuraPalette.canvas.ignoresSafeArea()
            content
        }
    }
}
