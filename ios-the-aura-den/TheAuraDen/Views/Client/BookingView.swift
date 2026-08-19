import SwiftUI

/// The guest closes her own booking: service, day and time with the specialist
/// she picked in Explorar. Everything stays in memory.
struct BookingView: View {
    let specialistID: String
    let onBack: () -> Void
    let onConfirm: (ClientService, String, String) -> Void

    @State private var serviceID: String = ""
    @State private var dayID: String = DemoData.weekDays[1].id
    @State private var time: String?

    private var specialist: SpecialistProfile { DemoData.specialist(id: specialistID) }

    private var services: [ClientService] {
        let matching = DemoData.services.filter { service in
            switch specialist.specialty {
            case "Pestañas / cejas": service.name.contains("Pestañas")
            case "Uñas acrílicas": service.name.contains("acrílicas") || service.name.contains("Manicure")
            default: !service.name.contains("Pestañas")
            }
        }
        return matching.isEmpty ? DemoData.services : matching
    }

    private var selectedService: ClientService {
        services.first { $0.id == serviceID } ?? services[0]
    }

    private var selectedDay: DaySlot { DemoData.day(id: dayID) }

    var body: some View {
        AuraDetailScaffold {
            AuraHeader(
                title: "Agendar",
                eyebrow: "Con \(specialist.name)",
                subtitle: "Elige servicio, día y hora",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    Spacer().frame(height: 18)

                    specialistCard
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 24)

                    SectionHeading(text: "1 · Servicio")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 12)

                    VStack(spacing: 10) {
                        ForEach(services) { option in
                            ServiceOption(
                                service: option,
                                isSelected: option.id == selectedService.id
                            ) {
                                serviceID = option.id
                            }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 24)

                    SectionHeading(text: "2 · Día")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 12)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 10) {
                            ForEach(DemoData.weekDays) { day in
                                DayPill(
                                    weekday: day.weekdayShort,
                                    day: day.dayNumber,
                                    isSelected: day.id == dayID
                                ) {
                                    dayID = day.id
                                    time = nil
                                }
                            }
                        }
                        .padding(.horizontal, 20)
                    }

                    Spacer().frame(height: 24)

                    SectionHeading(text: "3 · Hora")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 12)

                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 10) {
                        ForEach(DemoData.timeSlots, id: \.self) { slot in
                            TimeSlotChip(label: slot, isSelected: slot == time) {
                                time = slot
                            }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 18)

                    HStack(alignment: .top, spacing: 10) {
                        Image(systemName: "clock")
                            .font(.system(size: 15))
                            .foregroundStyle(AuraPalette.blue)
                        Text(
                            "Llega cinco minutos antes. Puedes reprogramar sin costo hasta "
                                + "24 horas antes."
                        )
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .fixedSize(horizontal: false, vertical: true)
                    }
                    .padding(16)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 28)
                }
            }
        } bottomAction: {
            VStack(alignment: .leading, spacing: 0) {
                Text(
                    time == nil
                        ? "Elige un horario"
                        : "\(selectedDay.fullLabel) · \(time ?? "")"
                )
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)

                Text("\(Money.format(selectedService.price)) MXN")
                    .font(AuraFont.titleLarge())
                    .foregroundStyle(AuraPalette.navy)
                    .padding(.bottom, 10)

                AuraPrimaryButton(
                    title: "Confirmar mi cita",
                    isEnabled: time != nil
                ) {
                    guard let time else { return }
                    onConfirm(selectedService, dayID, time)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .onAppear {
            if serviceID.isEmpty { serviceID = services[0].id }
        }
    }

    private var specialistCard: some View {
        HStack(spacing: 14) {
            AuraAvatar(imageURL: specialist.imageURL, size: 58, ringColor: .clear)
            VStack(alignment: .leading, spacing: 3) {
                Text(specialist.name)
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(AuraPalette.navy)
                Text(specialist.specialty)
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                RatingStars(rating: specialist.rating, starSize: 13)
                    .padding(.top, 3)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}

private struct ServiceOption: View {
    let service: ClientService
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button {
            AuraHaptics.tap()
            action()
        } label: {
            HStack(spacing: 10) {
                VStack(alignment: .leading, spacing: 3) {
                    Text(service.name)
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(isSelected ? AuraPalette.white : AuraPalette.ink)
                    Text(service.durationLabel)
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(
                            isSelected
                                ? AuraPalette.white.opacity(0.78)
                                : AuraPalette.inkMuted
                        )
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Text(Money.format(service.price))
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(isSelected ? AuraPalette.yellow : AuraPalette.navy)

                if isSelected {
                    ZStack {
                        Circle()
                            .fill(AuraPalette.yellow)
                            .frame(width: 22, height: 22)
                        Image(systemName: "checkmark")
                            .font(.system(size: 12, weight: .bold))
                            .foregroundStyle(AuraPalette.navy)
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 15)
            .frame(maxWidth: .infinity)
            .background(
                isSelected ? AuraPalette.blue : AuraPalette.white,
                in: .rect(cornerRadius: 20)
            )
            .overlay {
                RoundedRectangle(cornerRadius: 20)
                    .stroke(isSelected ? AuraPalette.blue : AuraPalette.sandSoft, lineWidth: 1)
            }
            .contentShape(.rect)
        }
        .buttonStyle(.plain)
        .animation(.easeInOut(duration: 0.2), value: isSelected)
    }
}

private struct TimeSlotChip: View {
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
                .foregroundStyle(isSelected ? AuraPalette.navy : AuraPalette.ink)
                .frame(maxWidth: .infinity)
                .frame(height: 48)
                .background(
                    isSelected ? AuraPalette.yellow : AuraPalette.white,
                    in: .rect(cornerRadius: 16)
                )
                .overlay {
                    RoundedRectangle(cornerRadius: 16)
                        .stroke(isSelected ? AuraPalette.yellow : AuraPalette.sandSoft, lineWidth: 1)
                }
                .contentShape(.rect)
        }
        .buttonStyle(.plain)
        .animation(.easeInOut(duration: 0.2), value: isSelected)
    }
}
