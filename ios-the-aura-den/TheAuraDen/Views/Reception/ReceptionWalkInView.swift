import SwiftUI

/// Quick intake for a client who arrives without an appointment.
struct ReceptionWalkInView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void
    let onRegistered: () -> Void

    @Environment(DemoStore.self) private var store

    @State private var clientName: String = ""
    @State private var service: String = DemoData.services[0].name
    @State private var specialist: String = DemoData.specialists[0].name
    @State private var station: String = ""

    private var freeStations: [Station] {
        DemoData.stations.filter { $0.status == .available }
    }

    var body: some View {
        AuraTabScaffold(
            role: .reception,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Registrar walk-in",
                eyebrow: "Recepción",
                subtitle: "Clienta sin cita previa"
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Spacer().frame(height: 18)

                    HStack(spacing: 13) {
                        ZStack {
                            RoundedRectangle(cornerRadius: 13)
                                .fill(AuraPalette.sand.opacity(0.3))
                                .frame(width: 42, height: 42)
                            Image(systemName: "chair.lounge.fill")
                                .font(.system(size: 17))
                                .foregroundStyle(AuraPalette.navy)
                        }
                        VStack(alignment: .leading, spacing: 2) {
                            Text("\(freeStations.count) estaciones libres ahora")
                                .font(AuraFont.titleMedium())
                                .fontWeight(.semibold)
                                .foregroundStyle(AuraPalette.navy)
                            Text(freeStations.map(\.name).joined(separator: " · "))
                                .font(AuraFont.bodySmall())
                                .foregroundStyle(AuraPalette.inkMuted)
                                .fixedSize(horizontal: false, vertical: true)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                    Spacer().frame(height: 20)

                    AuraTextField(
                        title: "Nombre de la clienta",
                        placeholder: "Valeria Núñez",
                        text: $clientName
                    )

                    Spacer().frame(height: 18)

                    AuraDropdownField(
                        title: "Servicio",
                        selection: $service,
                        options: DemoData.services.map(\.name)
                    )

                    Spacer().frame(height: 18)

                    AuraDropdownField(
                        title: "Especialista disponible",
                        selection: $specialist,
                        options: DemoData.specialists.map(\.name)
                    )

                    Spacer().frame(height: 18)

                    AuraDropdownField(
                        title: "Estación",
                        selection: $station,
                        options: freeStations.map(\.name)
                    )

                    Spacer().frame(height: 20)

                    HStack(spacing: 8) {
                        StatusPill(
                            text: "Entra en espera",
                            foreground: AuraPalette.green,
                            background: AuraPalette.greenSoft,
                            symbol: "checkmark.circle.fill"
                        )
                        EyebrowText(text: "Se agrega al check-in de hoy")
                    }

                    Spacer().frame(height: 22)

                    AuraPrimaryButton(title: "Dar de alta") {
                        store.registerWalkIn(
                            clientName: clientName,
                            service: service,
                            specialist: specialist,
                            station: station
                        )
                        onRegistered()
                    }

                    Spacer().frame(height: 26)
                }
                .padding(.horizontal, 20)
            }
        }
        .onAppear {
            if station.isEmpty { station = freeStations.first?.name ?? "" }
        }
    }
}
