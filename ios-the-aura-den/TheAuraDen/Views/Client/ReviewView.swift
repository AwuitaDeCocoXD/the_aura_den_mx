import SwiftUI

private let ratingLabels = [
    "Cuéntanos cómo te fue",
    "No fue lo que esperaba",
    "Pudo estar mejor",
    "Estuvo bien",
    "Muy buen servicio",
    "¡Quedé encantada!"
]

private let quickTags = [
    "Puntual",
    "Trato cálido",
    "Diseño impecable",
    "Espacio limpio",
    "Volvería"
]

/// The guest rates a finished visit. Visual only: nothing is published.
struct ReviewView: View {
    let appointmentID: String
    let onBack: () -> Void
    let onSubmit: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var rating: Int = 0
    @State private var comment: String = ""
    @State private var tags: Set<String> = []

    var body: some View {
        if let appointment = store.appointment(id: appointmentID) {
            let specialist = DemoData.specialists
                .first { $0.name == appointment.specialistName } ?? DemoData.currentSpecialist

            AuraDetailScaffold {
                AuraHeader(
                    title: "Califica tu visita",
                    eyebrow: appointment.dateLabel,
                    subtitle: "\(appointment.service) · \(appointment.specialistName)",
                    onBack: onBack
                )
            } content: {
                ScrollView {
                    VStack(spacing: 0) {
                        Spacer().frame(height: 24)

                        AuraAvatar(imageURL: specialist.imageURL, size: 88, ringColor: .clear)
                            .padding(.bottom, 14)

                        Text(specialist.name)
                            .font(AuraFont.headlineSmall())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.bottom, 4)

                        EyebrowText(text: specialist.specialty)

                        Spacer().frame(height: 26)

                        HStack(spacing: 8) {
                            ForEach(1...5, id: \.self) { value in
                                RatingStar(isFilled: value <= rating) {
                                    withAnimation(.spring(response: 0.32, dampingFraction: 0.45)) {
                                        rating = value
                                    }
                                }
                            }
                        }

                        Spacer().frame(height: 14)

                        Text(ratingLabels[rating])
                            .font(AuraFont.titleMedium())
                            .foregroundStyle(rating > 0 ? AuraPalette.navy : AuraPalette.inkMuted)
                            .multilineTextAlignment(.center)

                        Spacer().frame(height: 26)

                        LazyVGrid(
                            columns: [GridItem(.flexible()), GridItem(.flexible())],
                            spacing: 8
                        ) {
                            ForEach(quickTags, id: \.self) { tag in
                                TagChip(label: tag, isSelected: tags.contains(tag)) {
                                    if tags.contains(tag) {
                                        tags.remove(tag)
                                    } else {
                                        tags.insert(tag)
                                    }
                                }
                            }
                        }

                        Spacer().frame(height: 18)

                        AuraTextField(
                            title: "Cuéntale a otras invitadas (opcional)",
                            placeholder: "El diseño quedó justo como lo pedí…",
                            text: $comment,
                            minLines: 3
                        )

                        Spacer().frame(height: 18)

                        Text(
                            "Tu reseña ayuda a que otras invitadas encuentren a la especialista "
                                + "indicada para ellas."
                        )
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .fixedSize(horizontal: false, vertical: true)
                        .padding(16)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                        Spacer().frame(height: 24)
                    }
                    .padding(.horizontal, 20)
                }
            } bottomAction: {
                VStack(spacing: 0) {
                    AuraPrimaryButton(title: "Enviar reseña", isEnabled: rating > 0) {
                        AuraHaptics.success()
                        onSubmit()
                    }
                    Button("Ahora no", action: onBack)
                        .font(AuraFont.labelLarge())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .frame(maxWidth: .infinity)
                        .padding(.top, 10)
                }
            }
        }
    }
}

private struct RatingStar: View {
    let isFilled: Bool
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            Image(systemName: isFilled ? "star.fill" : "star")
                .font(.system(size: 36))
                .foregroundStyle(isFilled ? AuraPalette.yellow : AuraPalette.divider)
                .scaleEffect(isFilled ? 1 : 0.86)
                .frame(width: 52, height: 52)
                .contentShape(.rect)
        }
        .buttonStyle(.plain)
    }
}

private struct TagChip: View {
    let label: String
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            Text(label)
                .font(AuraFont.labelLarge())
                .foregroundStyle(isSelected ? AuraPalette.white : AuraPalette.navy)
                .frame(maxWidth: .infinity)
                .frame(height: 42)
                .background(
                    isSelected ? AuraPalette.blue : AuraPalette.white,
                    in: .rect(cornerRadius: 14)
                )
                .overlay {
                    RoundedRectangle(cornerRadius: 14)
                        .stroke(isSelected ? AuraPalette.blue : AuraPalette.sandSoft, lineWidth: 1)
                }
                .contentShape(.rect)
        }
        .buttonStyle(.plain)
        .animation(.easeInOut(duration: 0.18), value: isSelected)
    }
}

/// Compact prompt shown inside the history list for a visit that has no rating yet.
struct ReviewPromptCard: View {
    let appointment: Appointment
    let onReview: () -> Void

    private var firstName: String {
        appointment.specialistName.split(separator: " ").first.map(String.init)
            ?? appointment.specialistName
    }

    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: "star.fill")
                .font(.system(size: 20))
                .foregroundStyle(AuraPalette.yellow)

            VStack(alignment: .leading, spacing: 2) {
                Text("¿Cómo te fue con \(firstName)?")
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(AuraPalette.navy)
                    .fixedSize(horizontal: false, vertical: true)
                Text("\(appointment.service) · \(appointment.dateLabel)")
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            Button("Calificar") {
                AuraHaptics.tap()
                onReview()
            }
            .font(AuraFont.labelLarge())
            .foregroundStyle(AuraPalette.blue)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
    }
}
