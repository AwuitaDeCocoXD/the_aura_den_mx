import SwiftUI

/// Lightweight confirmation the client opens from her link — no full account needed.
struct ClientAppointmentView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onExplore: () -> Void
    let onPayService: (String, String) -> Void
    let onOpenRoleSwitcher: () -> Void

    @Environment(DemoStore.self) private var store
    @State private var showCancelDialog = false

    private let specialist = DemoData.currentSpecialist

    var body: some View {
        let appointment = store.clientUpcoming.first

        AuraTabScaffold(
            role: .client,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            ClientTopBar(onOpenRoleSwitcher: onOpenRoleSwitcher)
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    if let appointment {
                        Spacer().frame(height: 14)

                        Text("Tu cita está confirmada")
                            .font(AuraFont.headlineMedium())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.bottom, 6)

                        Text("Guarda los datos y llega cinco minutos antes.")
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .fixedSize(horizontal: false, vertical: true)

                        Spacer().frame(height: 18)

                        appointmentCard(appointment)

                        Spacer().frame(height: 16)

                        locationCard

                        Spacer().frame(height: 20)

                        AuraPrimaryButton(
                            title: "Añadir a mi calendario",
                            tint: AuraPalette.yellow,
                            foreground: AuraPalette.navy,
                            action: {}
                        )

                        Spacer().frame(height: 10)

                        AuraSecondaryButton(
                            title: "Pagar mi servicio",
                            symbol: "creditcard.fill"
                        ) {
                            let serviceID = DemoData.services
                                .first { $0.name == appointment.service }?.id
                                ?? DemoData.services[0].id
                            onPayService(DemoData.currentSpecialist.id, serviceID)
                        }

                        Button("Cancelar mi cita") {
                            AuraHaptics.tap()
                            showCancelDialog = true
                        }
                        .font(AuraFont.labelLarge())
                        .foregroundStyle(AuraPalette.red)
                        .frame(maxWidth: .infinity)
                        .frame(height: 48)
                        .padding(.top, 4)

                        Spacer().frame(height: 20)
                    } else {
                        AuraEmptyState(
                            title: "Aún no tienes citas",
                            message: "Explora a nuestras especialistas y agenda tu próximo servicio en The Aura Den.",
                            symbol: "calendar.badge.checkmark",
                            actionLabel: "Explorar especialistas",
                            action: onExplore
                        )
                        Spacer().frame(height: 20)
                    }
                }
                .padding(.horizontal, 20)
            }
        }
        .confirmationDialog(
            "¿Seguro que quieres cancelar tu cita?",
            isPresented: $showCancelDialog,
            titleVisibility: .visible
        ) {
            Button("Sí, cancelar", role: .destructive) {
                if let id = store.clientAppointment?.id {
                    store.cancelAppointment(id: id)
                }
            }
            Button("No, mantener", role: .cancel) {}
        } message: {
            Text("Le avisaremos a tu especialista y liberaremos el horario.")
        }
    }

    private func appointmentCard(_ appointment: Appointment) -> some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 16) {
                AuraAvatar(imageURL: specialist.imageURL, size: 76, ringColor: .clear)
                VStack(alignment: .leading, spacing: 0) {
                    EyebrowText(text: "Con")
                        .padding(.bottom, 3)
                    Text(specialist.name)
                        .font(AuraFont.headlineSmall())
                        .foregroundStyle(AuraPalette.navy)
                        .padding(.bottom, 6)
                    HStack(spacing: 6) {
                        RatingStars(rating: specialist.rating, starSize: 14)
                        Text(String(format: "%.1f", specialist.rating))
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(.bottom, 18)

            StatusPill(
                text: appointment.service,
                foreground: AuraPalette.blue,
                background: AuraPalette.blueSoft
            )
            .padding(.bottom, 16)

            DetailLine(symbol: "calendar", text: appointment.dateLabel)
                .padding(.bottom, 9)
            DetailLine(symbol: "clock", text: appointment.time)
                .padding(.bottom, 9)
            DetailLine(symbol: "chair.lounge.fill", text: appointment.stationName)
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }

    private var locationCard: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .top, spacing: 10) {
                Image(systemName: "mappin.circle.fill")
                    .font(.system(size: 18))
                    .foregroundStyle(AuraPalette.sand)
                VStack(alignment: .leading, spacing: 2) {
                    Text(AuraCopy.addressLine1)
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(AuraPalette.navy)
                    Text(AuraCopy.addressLine2)
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
            }
            .padding(.bottom, 14)

            StylizedMap()
                .frame(height: 132)
                .clipShape(.rect(cornerRadius: 18))
                .padding(.bottom, 10)

            Text("The Aura Den · Roma Norte")
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
    }
}

/// White top bar with the centred logotype, used across the client role.
struct ClientTopBar: View {
    var onOpenRoleSwitcher: (() -> Void)?

    var body: some View {
        HStack(spacing: 0) {
            Spacer().frame(width: 44)
            AuraLogo(
                size: .compact,
                textColor: AuraPalette.navy,
                scriptColor: AuraPalette.blue,
                arcColor: AuraPalette.yellow
            )
            .frame(maxWidth: .infinity)

            if let onOpenRoleSwitcher {
                Button {
                    AuraHaptics.tap()
                    onOpenRoleSwitcher()
                } label: {
                    ZStack {
                        Circle()
                            .fill(AuraPalette.sandSoft)
                            .frame(width: 40, height: 40)
                        Text("LG")
                            .font(AuraFont.titleSmall())
                            .foregroundStyle(AuraPalette.navy)
                    }
                    .frame(width: 44, height: 44)
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Cambiar de vista")
            } else {
                Spacer().frame(width: 44)
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 14)
        .padding(.top, safeAreaTop)
        .frame(maxWidth: .infinity)
        .background(AuraPalette.white)
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

private struct DetailLine: View {
    let symbol: String
    let text: String

    var body: some View {
        HStack(spacing: 9) {
            Image(systemName: symbol)
                .font(.system(size: 14))
                .foregroundStyle(AuraPalette.blue)
                .frame(width: 18)
            Text(text)
                .font(AuraFont.bodyLarge())
                .foregroundStyle(AuraPalette.ink)
        }
    }
}

/// Stylized, non-interactive location sketch in brand colors.
nonisolated struct StylizedMap: View {
    var body: some View {
        Canvas { context, size in
            let blockColor = AuraPalette.sandSoft.opacity(0.55)

            for fraction in [0.18, 0.62] {
                let rect = CGRect(
                    x: size.width * fraction,
                    y: size.height * 0.12,
                    width: size.width * 0.22,
                    height: size.height * 0.3
                )
                context.fill(Path(rect), with: .color(blockColor))
            }

            context.fill(
                Path(CGRect(
                    x: size.width * 0.35,
                    y: size.height * 0.62,
                    width: size.width * 0.4,
                    height: size.height * 0.26
                )),
                with: .color(blockColor)
            )

            drawStreet(
                context: context,
                from: CGPoint(x: 0, y: size.height * 0.52),
                to: CGPoint(x: size.width, y: size.height * 0.46),
                width: 6
            )
            drawStreet(
                context: context,
                from: CGPoint(x: size.width * 0.3, y: 0),
                to: CGPoint(x: size.width * 0.36, y: size.height),
                width: 4.5
            )
            drawStreet(
                context: context,
                from: CGPoint(x: size.width * 0.78, y: 0),
                to: CGPoint(x: size.width * 0.72, y: size.height),
                width: 3.5
            )

            let marker = CGPoint(x: size.width * 0.47, y: size.height * 0.45)
            drawDot(context: context, at: marker, radius: 17, color: AuraPalette.blue.opacity(0.18))
            drawDot(context: context, at: marker, radius: 7.5, color: AuraPalette.blue)
            drawDot(context: context, at: marker, radius: 3, color: AuraPalette.yellow)
        }
        .background(AuraPalette.blueSoft)
    }

    private func drawStreet(
        context: GraphicsContext,
        from start: CGPoint,
        to end: CGPoint,
        width: CGFloat
    ) {
        var path = Path()
        path.move(to: start)
        path.addLine(to: end)
        context.stroke(
            path,
            with: .color(AuraPalette.white),
            style: StrokeStyle(lineWidth: width, lineCap: .round)
        )
    }

    private func drawDot(
        context: GraphicsContext,
        at point: CGPoint,
        radius: CGFloat,
        color: Color
    ) {
        let rect = CGRect(
            x: point.x - radius,
            y: point.y - radius,
            width: radius * 2,
            height: radius * 2
        )
        context.fill(Path(ellipseIn: rect), with: .color(color))
    }
}
