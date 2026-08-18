import SwiftUI

/// Station detail with date and time selection.
struct ReserveSpaceView: View {
    let stationID: String
    let onBack: () -> Void
    let onConfirm: (SuccessKind) -> Void

    @Environment(DemoStore.self) private var store
    @State private var selectedDay: String = "tue"
    @State private var selectedTime: String = DemoData.timeSlots[0]

    private var station: Station { DemoData.station(id: stationID) }
    private var isAvailable: Bool { station.status == .available }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Reservar espacio",
                eyebrow: station.kind,
                subtitle: station.name,
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    AuraPalette.sandSoft
                        .frame(height: 214)
                        .overlay {
                            AsyncImage(url: URL(string: station.imageURL)) { phase in
                                if case .success(let image) = phase {
                                    image.resizable().aspectRatio(contentMode: .fill)
                                } else {
                                    AuraPalette.sandSoft
                                }
                            }
                            .allowsHitTesting(false)
                        }
                        .clipShape(.rect(cornerRadius: 24))
                        .overlay(alignment: .topLeading) {
                            StatusPill(
                                text: isAvailable ? "Disponible ahora" : "Ocupada",
                                foreground: isAvailable ? AuraPalette.green : AuraPalette.grey,
                                background: isAvailable ? AuraPalette.greenSoft : AuraPalette.greySoft
                            )
                            .padding(14)
                        }
                        .padding(.horizontal, 20)
                        .padding(.vertical, 16)

                    VStack(alignment: .leading, spacing: 0) {
                        Text(station.name)
                            .font(AuraFont.headlineMedium())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.bottom, 8)

                        Text(station.description)
                            .font(AuraFont.bodyLarge())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .fixedSize(horizontal: false, vertical: true)
                            .padding(.bottom, 16)

                        AuraCard(background: AuraPalette.cream, padding: 16) {
                            VStack(alignment: .leading, spacing: 0) {
                                EyebrowText(text: "Incluye")
                                    .padding(.bottom, 10)
                                ForEach(station.amenities, id: \.self) { amenity in
                                    HStack(spacing: 9) {
                                        Image(systemName: "checkmark.circle.fill")
                                            .font(.system(size: 14))
                                            .foregroundStyle(AuraPalette.blue)
                                        Text(amenity)
                                            .font(AuraFont.bodyMedium())
                                            .foregroundStyle(AuraPalette.ink)
                                    }
                                    .padding(.vertical, 3)
                                }
                                HStack(spacing: 7) {
                                    Image(systemName: "clock")
                                        .font(.system(size: 13))
                                        .foregroundStyle(AuraPalette.navy)
                                    Text(station.scheduleLabel)
                                        .font(AuraFont.titleSmall())
                                        .foregroundStyle(AuraPalette.navy)
                                }
                                .padding(.top, 12)
                            }
                        }

                        Text("Elige tu fecha")
                            .font(AuraFont.titleLarge())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.top, 24)
                            .padding(.bottom, 12)
                    }
                    .padding(.horizontal, 20)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 10) {
                            ForEach(DemoData.weekDays.prefix(5)) { day in
                                DayPill(
                                    weekday: day.weekdayShort,
                                    day: day.dayNumber,
                                    isSelected: day.id == selectedDay,
                                    action: { selectedDay = day.id }
                                )
                            }
                        }
                        .padding(.horizontal, 20)
                    }

                    VStack(alignment: .leading, spacing: 0) {
                        Text("Horarios disponibles")
                            .font(AuraFont.titleLarge())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.top, 24)
                            .padding(.bottom, 12)

                        TimeSlotGrid(
                            slots: DemoData.timeSlots,
                            selected: selectedTime,
                            onSelect: { selectedTime = $0 }
                        )

                        Text("Renta por hora: $\(station.hourlyRate) MXN · Se descuenta de tus horas de membresía.")
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .fixedSize(horizontal: false, vertical: true)
                            .padding(.top, 18)
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 20)
                }
            }
        } bottomAction: {
            AuraPrimaryButton(
                title: isAvailable ? "Confirmar reserva" : "Avisarme cuando se libere"
            ) {
                store.reserveStation(
                    stationName: station.name,
                    dayID: selectedDay,
                    time: selectedTime
                )
                onConfirm(isAvailable ? .reservation : .notify)
            }
        }
    }
}

private struct TimeSlotGrid: View {
    let slots: [String]
    let selected: String
    let onSelect: (String) -> Void

    var body: some View {
        let rows = stride(from: 0, to: slots.count, by: 2).map { index in
            Array(slots[index..<min(index + 2, slots.count)])
        }

        VStack(spacing: 10) {
            ForEach(Array(rows.enumerated()), id: \.offset) { _, row in
                HStack(spacing: 10) {
                    ForEach(row, id: \.self) { slot in
                        TimeChip(
                            title: slot,
                            isSelected: slot == selected,
                            action: { onSelect(slot) }
                        )
                        .frame(maxWidth: .infinity)
                    }
                    if row.count == 1 {
                        Spacer().frame(maxWidth: .infinity)
                    }
                }
            }
        }
    }
}
