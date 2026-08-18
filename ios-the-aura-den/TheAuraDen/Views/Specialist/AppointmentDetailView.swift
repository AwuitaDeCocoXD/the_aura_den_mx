import SwiftUI

/// Appointment detail with reschedule and cancel actions.
struct AppointmentDetailView: View {
    let appointmentID: String
    let onBack: () -> Void

    @Environment(DemoStore.self) private var store

    @State private var showCancelDialog = false
    @State private var showReschedule = false
    @State private var newTime: String = ""

    private var appointment: Appointment? { store.appointment(id: appointmentID) }

    var body: some View {
        Group {
            if let appointment {
                content(for: appointment)
            } else {
                AuraPlainScaffold {
                    AuraHeader(title: "Detalle de cita", onBack: onBack)
                } content: {
                    AuraEmptyState(
                        title: "Cita no disponible",
                        message: "Esta cita ya no está en tu agenda.",
                        symbol: "calendar"
                    )
                }
            }
        }
        .onAppear {
            if newTime.isEmpty { newTime = appointment?.time ?? DemoData.timeSlots[0] }
        }
    }

    @ViewBuilder
    private func content(for appointment: Appointment) -> some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Detalle de cita",
                eyebrow: appointment.dateLabel,
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Spacer().frame(height: 22)

                    Text(appointment.clientName)
                        .font(AuraFont.displaySmall())
                        .foregroundStyle(AuraPalette.navy)
                        .padding(.bottom, 6)

                    Text(appointment.service)
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(AuraPalette.blue)
                        .padding(.bottom, 14)

                    AppointmentStatusPill(status: appointment.status)

                    Spacer().frame(height: 22)

                    VStack(spacing: 0) {
                        InfoRow(label: "Fecha", value: appointment.dateLabel, symbol: "calendar")
                        AuraDivider().padding(.horizontal, 18)
                        InfoRow(label: "Hora", value: appointment.time, symbol: "clock")
                        AuraDivider().padding(.horizontal, 18)
                        InfoRow(label: "Mesa", value: appointment.stationName, symbol: "chair.lounge.fill")
                    }
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))

                    if let notes = appointment.notes {
                        Spacer().frame(height: 16)
                        HStack(alignment: .top, spacing: 12) {
                            Image(systemName: "note.text")
                                .font(.system(size: 18))
                                .foregroundStyle(AuraPalette.sand)
                            VStack(alignment: .leading, spacing: 4) {
                                EyebrowText(text: "Notas", color: AuraPalette.sand)
                                Text(notes)
                                    .font(AuraFont.bodyLarge())
                                    .foregroundStyle(AuraPalette.ink)
                                    .fixedSize(horizontal: false, vertical: true)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .padding(18)
                        .frame(maxWidth: .infinity)
                        .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
                    }

                    if showReschedule {
                        Spacer().frame(height: 22)

                        Text("Nuevo horario")
                            .font(AuraFont.titleLarge())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.bottom, 6)

                        Text("Elige otra franja disponible para \(appointment.clientName).")
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(AuraPalette.inkMuted)
                            .fixedSize(horizontal: false, vertical: true)
                            .padding(.bottom, 12)

                        let rows = stride(from: 0, to: DemoData.timeSlots.count, by: 2).map { index in
                            Array(DemoData.timeSlots[index..<min(index + 2, DemoData.timeSlots.count)])
                        }
                        VStack(spacing: 10) {
                            ForEach(Array(rows.enumerated()), id: \.offset) { _, row in
                                HStack(spacing: 10) {
                                    ForEach(row, id: \.self) { slot in
                                        TimeChip(
                                            title: slot,
                                            isSelected: slot == newTime,
                                            action: { newTime = slot }
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

                    Spacer().frame(height: 28)
                }
                .padding(.horizontal, 20)
            }
        } bottomAction: {
            VStack(spacing: 4) {
                AuraSecondaryButton(
                    title: showReschedule ? "Guardar nuevo horario" : "Reprogramar"
                ) {
                    if showReschedule {
                        store.rescheduleAppointment(
                            id: appointment.id,
                            time: newTime,
                            dayID: appointment.dayID
                        )
                        onBack()
                    } else {
                        withAnimation(.easeInOut(duration: 0.24)) { showReschedule = true }
                    }
                }

                Button("Cancelar cita") {
                    AuraHaptics.tap()
                    showCancelDialog = true
                }
                .font(AuraFont.labelLarge())
                .foregroundStyle(AuraPalette.red)
                .frame(maxWidth: .infinity)
                .frame(height: 44)
            }
        }
        .confirmationDialog(
            "¿Seguro que quieres cancelar esta cita?",
            isPresented: $showCancelDialog,
            titleVisibility: .visible
        ) {
            Button("Sí, cancelar", role: .destructive) {
                store.cancelAppointment(id: appointment.id)
                onBack()
            }
            Button("No, mantener", role: .cancel) {}
        } message: {
            Text("\(appointment.clientName) recibirá un aviso de que su cita de \(appointment.service) fue cancelada.")
        }
    }
}
