import SwiftUI

/// Quick booking form for the specialist's own client.
struct ScheduleClientView: View {
    let onBack: () -> Void
    let onConfirm: () -> Void

    @Environment(DemoStore.self) private var store

    @State private var clientName: String = "Mariana López"
    @State private var service: String = DemoData.services[0].name
    @State private var selectedDay: String = "tue"
    @State private var selectedTime: String = "12:30 pm"
    @State private var notes: String = "Prefiere tono nude rosado"

    private var suggestedStation: String {
        service == "Pestañas / cejas" ? "Mesa de pestañas 1" : "Mesa de uñas 2"
    }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Agendar clienta",
                eyebrow: "Nueva cita",
                subtitle: "Para \(store.profile.name)",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    VStack(alignment: .leading, spacing: 0) {
                        Spacer().frame(height: 22)

                        AuraTextField(
                            title: "Nombre de la clienta",
                            placeholder: "Mariana López",
                            text: $clientName
                        )

                        Spacer().frame(height: 18)

                        AuraDropdownField(
                            title: "Servicio",
                            selection: $service,
                            options: DemoData.services.map(\.name)
                        )

                        Spacer().frame(height: 22)

                        EyebrowText(text: "Fecha")
                            .padding(.bottom, 10)
                    }
                    .padding(.horizontal, 20)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 10) {
                            ForEach(DemoData.weekDays) { day in
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
                        Spacer().frame(height: 22)

                        EyebrowText(text: "Hora")
                            .padding(.bottom, 10)

                        let rows = stride(from: 0, to: DemoData.timeSlots.count, by: 2).map { index in
                            Array(DemoData.timeSlots[index..<min(index + 2, DemoData.timeSlots.count)])
                        }
                        VStack(spacing: 10) {
                            ForEach(Array(rows.enumerated()), id: \.offset) { _, row in
                                HStack(spacing: 10) {
                                    ForEach(row, id: \.self) { slot in
                                        TimeChip(
                                            title: slot,
                                            isSelected: slot == selectedTime,
                                            action: { selectedTime = slot }
                                        )
                                        .frame(maxWidth: .infinity)
                                    }
                                    if row.count == 1 {
                                        Spacer().frame(maxWidth: .infinity)
                                    }
                                }
                            }
                        }

                        Spacer().frame(height: 22)

                        HStack(spacing: 14) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 14)
                                    .fill(AuraPalette.sand.opacity(0.32))
                                    .frame(width: 44, height: 44)
                                Image(systemName: "chair.lounge.fill")
                                    .font(.system(size: 18))
                                    .foregroundStyle(AuraPalette.navy)
                            }
                            VStack(alignment: .leading, spacing: 3) {
                                EyebrowText(text: "Mesa asignada", color: AuraPalette.sand)
                                Text(suggestedStation)
                                    .font(AuraFont.titleMedium())
                                    .fontWeight(.semibold)
                                    .foregroundStyle(AuraPalette.navy)
                                HStack(spacing: 5) {
                                    Image(systemName: "checkmark.circle.fill")
                                        .font(.system(size: 12))
                                    Text("Disponible para tu cita")
                                        .font(AuraFont.bodySmall())
                                }
                                .foregroundStyle(AuraPalette.green)
                                .padding(.top, 1)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .padding(18)
                        .frame(maxWidth: .infinity)
                        .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))

                        Spacer().frame(height: 20)

                        AuraTextField(
                            title: "Notas opcionales",
                            placeholder: "Preferencias, alergias, inspiración…",
                            text: $notes,
                            minLines: 3
                        )

                        Spacer().frame(height: 12)

                        Text("La clienta recibirá un enlace con los datos de su cita.")
                            .font(AuraFont.bodySmall())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .fixedSize(horizontal: false, vertical: true)

                        Spacer().frame(height: 24)
                    }
                    .padding(.horizontal, 20)
                }
            }
        } bottomAction: {
            AuraPrimaryButton(title: "Confirmar cita") {
                store.addAppointment(
                    clientName: clientName,
                    service: service,
                    dayID: selectedDay,
                    time: selectedTime,
                    stationName: suggestedStation,
                    notes: notes
                )
                onConfirm()
            }
        }
    }
}
