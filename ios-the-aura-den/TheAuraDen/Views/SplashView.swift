import SwiftUI

/// Opening animation: the brand arch draws itself, then the wordmark fades up.
/// Hands over to the welcome screen on its own after roughly two seconds.
struct SplashView: View {
    let onFinished: () -> Void

    @State private var arcSweep: CGFloat = 0
    @State private var wordmarkOpacity: Double = 0
    @State private var wordmarkOffset: CGFloat = 10
    @State private var taglineOpacity: Double = 0

    var body: some View {
        ZStack {
            LinearGradient(
                stops: [
                    .init(color: AuraPalette.blue, location: 0),
                    .init(color: AuraPalette.blue, location: 0.5),
                    .init(color: AuraPalette.navyDeep, location: 1)
                ],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()

            VStack(spacing: 0) {
                DrawingArc(sweep: arcSweep)
                    .stroke(
                        AuraPalette.yellow,
                        style: StrokeStyle(lineWidth: 2.6, lineCap: .round)
                    )
                    .frame(width: 96, height: 38)
                    .padding(.bottom, 12)

                Text("The")
                    .font(AuraFont.displaySmall(17))
                    .foregroundStyle(AuraPalette.white)

                Text("Aura")
                    .font(AuraFont.logo(62))
                    .foregroundStyle(AuraPalette.yellow)

                Text("D E N")
                    .font(AuraFont.labelLarge(15))
                    .tracking(5.2)
                    .foregroundStyle(AuraPalette.white)
            }
            .opacity(wordmarkOpacity)
            .offset(y: wordmarkOffset)
            .overlay(alignment: .bottom) {
                Text(AuraCopy.tagline)
                    .font(AuraFont.labelMedium())
                    .tracking(2.4)
                    .foregroundStyle(AuraPalette.white.opacity(0.7))
                    .opacity(taglineOpacity)
                    .offset(y: 62)
            }
        }
        .task { await runIntro() }
    }

    private func runIntro() async {
        withAnimation(.easeOut(duration: 0.76)) { arcSweep = 1 }
        try? await Task.sleep(for: .milliseconds(560))
        withAnimation(.easeOut(duration: 0.56)) {
            wordmarkOpacity = 1
            wordmarkOffset = 0
        }
        try? await Task.sleep(for: .milliseconds(480))
        withAnimation(.easeOut(duration: 0.42)) { taglineOpacity = 1 }
        try? await Task.sleep(for: .milliseconds(620))
        onFinished()
    }
}

/// Half arch that draws itself from left to right as `sweep` goes from 0 to 1.
private struct DrawingArc: Shape {
    var sweep: CGFloat

    var animatableData: CGFloat {
        get { sweep }
        set { sweep = newValue }
    }

    func path(in rect: CGRect) -> Path {
        var path = Path()
        let radius = rect.width / 2
        path.addArc(
            center: CGPoint(x: rect.midX, y: rect.maxY),
            radius: radius - 1.3,
            startAngle: .degrees(180),
            endAngle: .degrees(180 + 180 * Double(sweep)),
            clockwise: false
        )
        return path
    }
}
