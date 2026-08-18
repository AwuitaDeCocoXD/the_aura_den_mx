import SwiftUI

/// One continuous finger stroke of the signature.
nonisolated struct SignatureStroke: Shape {
    let points: [CGPoint]

    func path(in rect: CGRect) -> Path {
        var path = Path()
        guard let first = points.first else { return path }
        if points.count == 1 {
            path.addEllipse(in: CGRect(x: first.x - 1.5, y: first.y - 1.5, width: 3, height: 3))
            return path
        }
        path.move(to: first)
        for point in points.dropFirst() {
            path.addLine(to: point)
        }
        return path
    }
}

/// Finger drawing surface used to capture the specialist's signature.
/// Strokes live in the parent so it can clear them and know whether anything was drawn.
struct SignaturePad: View {
    @Binding var strokes: [[CGPoint]]
    var inkColor: Color = AuraPalette.navy
    var lineWidth: CGFloat = 3

    @State private var current: [CGPoint] = []

    private var style: StrokeStyle {
        StrokeStyle(lineWidth: lineWidth, lineCap: .round, lineJoin: .round)
    }

    var body: some View {
        ZStack {
            Color.clear
            ForEach(Array(strokes.enumerated()), id: \.offset) { _, stroke in
                SignatureStroke(points: stroke)
                    .stroke(style: style)
                    .foregroundStyle(inkColor)
            }
            SignatureStroke(points: current)
                .stroke(style: style)
                .foregroundStyle(inkColor)
        }
        .contentShape(.rect)
        .gesture(
            DragGesture(minimumDistance: 0)
                .onChanged { value in
                    current.append(value.location)
                }
                .onEnded { _ in
                    if current.count > 1 {
                        strokes.append(current)
                    }
                    current = []
                }
        )
        .onChange(of: strokes.isEmpty) { _, isEmpty in
            if isEmpty { current = [] }
        }
    }
}
