import SwiftUI

private let paymentFilters = ["Todas", "Al corriente", "Con adeudo"]

/// Roster of renters: plan, payment health and hours consumed, plus what is still owed.
struct AdminSpecialistsView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void

    @State private var filter: String = paymentFilters[0]

    private var specialists: [SpecialistProfile] {
        DemoData.specialists.filter { specialist in
            switch filter {
            case "Al corriente": specialist.paymentStatus == .paid
            case "Con adeudo": specialist.paymentStatus != .paid
            default: true
            }
        }
    }

    private var monthlyIncome: Int {
        DemoData.specialists.reduce(0) { $0 + DemoData.plan(id: $1.planID).price }
    }

    var body: some View {
        AuraTabScaffold(
            role: .admin,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Rentistas",
                eyebrow: "Modo administrador",
                subtitle: "\(DemoData.specialists.count) especialistas activas",
                content: {
                    StatusPill(
                        text: "Renta comprometida \(Money.format(monthlyIncome)) / mes",
                        foreground: AuraPalette.navy,
                        background: AuraPalette.yellow
                    )
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 16)

                    PendingPaymentsSection()
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 24)

                    SectionHeading(text: "Todas las rentistas")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 12)

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 9) {
                            ForEach(paymentFilters, id: \.self) { option in
                                AuraFilterChip(
                                    title: option,
                                    isSelected: option == filter,
                                    action: { filter = option }
                                )
                            }
                        }
                        .padding(.horizontal, 20)
                    }

                    Spacer().frame(height: 14)

                    VStack(spacing: 12) {
                        ForEach(specialists) { specialist in
                            SpecialistRow(specialist: specialist)
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 26)
                }
            }
        }
    }
}

/// Highlighted block with everything the coworking still has to collect this month.
private struct PendingPaymentsSection: View {
    var body: some View {
        let overdue = DemoData.pendingCharges.filter(\.overdue)

        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 10) {
                ZStack {
                    RoundedRectangle(cornerRadius: 10)
                        .fill(AuraPalette.redSoft)
                        .frame(width: 30, height: 30)
                    Image(systemName: "exclamationmark")
                        .font(.system(size: 15, weight: .bold))
                        .foregroundStyle(AuraPalette.red)
                }
                Text("Pagos pendientes")
                    .font(AuraFont.titleLarge())
                    .foregroundStyle(AuraPalette.navy)
                    .frame(maxWidth: .infinity, alignment: .leading)
            }
            .padding(.bottom, 12)

            HStack(alignment: .top, spacing: 12) {
                AmountTile(
                    amount: DemoData.overdueTotal,
                    label: "Vencido",
                    caption: "\(overdue.count) rentistas",
                    background: AuraPalette.redSoft,
                    valueColor: AuraPalette.red
                )
                AmountTile(
                    amount: DemoData.pendingTotal - DemoData.overdueTotal,
                    label: "Por vencer",
                    caption: "Esta semana",
                    background: AuraPalette.cream,
                    valueColor: AuraPalette.navy
                )
            }
            .padding(.bottom, 12)

            VStack(spacing: 0) {
                ForEach(Array(DemoData.pendingCharges.enumerated()), id: \.element.id) { index, charge in
                    PendingChargeRow(charge: charge)
                    if index != DemoData.pendingCharges.count - 1 {
                        AuraDivider().padding(.horizontal, 16)
                    }
                }
            }
            .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
        }
    }
}

private struct AmountTile: View {
    let amount: Int
    let label: String
    let caption: String
    let background: Color
    let valueColor: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            EyebrowText(text: label, color: valueColor)
                .padding(.bottom, 8)
            Text(Money.format(amount))
                .font(AuraFont.headlineMedium())
                .foregroundStyle(valueColor)
                .padding(.bottom, 4)
            Text(caption)
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(background, in: .rect(cornerRadius: AuraRadius.card))
    }
}

private struct PendingChargeRow: View {
    let charge: PendingCharge

    private var initials: String {
        charge.specialistName
            .split(separator: " ")
            .prefix(2)
            .compactMap(\.first)
            .map(String.init)
            .joined()
    }

    var body: some View {
        HStack(spacing: 13) {
            ZStack {
                Circle()
                    .fill(charge.overdue ? AuraPalette.redSoft : AuraPalette.sandSoft)
                    .frame(width: 40, height: 40)
                Text(initials)
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(charge.overdue ? AuraPalette.red : AuraPalette.navy)
            }

            VStack(alignment: .leading, spacing: 2) {
                Text(charge.specialistName)
                    .font(AuraFont.titleMedium())
                    .fontWeight(.semibold)
                    .foregroundStyle(AuraPalette.ink)
                HStack(spacing: 6) {
                    Text("Membresía \(charge.planName)")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                    Circle()
                        .fill(AuraPalette.inkMuted)
                        .frame(width: 3, height: 3)
                    Text(charge.dueLabel)
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(charge.overdue ? AuraPalette.red : AuraPalette.amber)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            VStack(alignment: .trailing, spacing: 5) {
                Text(Money.format(charge.amount))
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(charge.overdue ? AuraPalette.red : AuraPalette.navy)
                HStack(spacing: 4) {
                    Image(systemName: "bell.badge.fill")
                        .font(.system(size: 11))
                    Text("Recordar")
                        .font(AuraFont.labelSmall())
                        .tracking(0.6)
                }
                .foregroundStyle(AuraPalette.blue)
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .frame(maxWidth: .infinity)
    }
}

/// Contract health of a renter: signed with folio, or still pending signature.
private struct ContractStatusRow: View {
    let specialist: SpecialistProfile

    private var isSigned: Bool { specialist.contractSigned }

    var body: some View {
        HStack(spacing: 9) {
            Image(systemName: isSigned ? "checkmark.seal.fill" : "signature")
                .font(.system(size: 15))
                .foregroundStyle(isSigned ? AuraPalette.green : AuraPalette.amber)
            VStack(alignment: .leading, spacing: 1) {
                Text(isSigned ? "Contrato firmado" : "Pendiente de firma")
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(isSigned ? AuraPalette.green : AuraPalette.amber)
                Text(
                    isSigned
                        ? "Folio \(specialist.contractFolio ?? "") · \(specialist.contractDateLabel ?? "")"
                        : "No puede reservar estaciones hasta firmar"
                )
                .font(AuraFont.bodySmall())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding(.horizontal, 13)
        .padding(.vertical, 11)
        .frame(maxWidth: .infinity)
        .background(
            isSigned ? AuraPalette.greenSoft.opacity(0.6) : AuraPalette.amberSoft,
            in: .rect(cornerRadius: 16)
        )
    }
}

private struct SpecialistRow: View {
    let specialist: SpecialistProfile

    var body: some View {
        let plan = DemoData.plan(id: specialist.planID)
        let owes = specialist.paymentStatus != .paid
        let hoursShare = plan.hours == 0
            ? 0
            : Double(specialist.hoursUsed) / Double(plan.hours)

        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 14) {
                AuraAvatar(imageURL: specialist.imageURL, size: 54, ringColor: .clear)
                VStack(alignment: .leading, spacing: 3) {
                    Text(specialist.name)
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(AuraPalette.ink)
                    Text(specialist.specialty)
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                StatusPill(
                    text: owes ? "Con adeudo" : "Al corriente",
                    foreground: owes ? AuraPalette.red : AuraPalette.green,
                    background: owes ? AuraPalette.redSoft : AuraPalette.greenSoft,
                    symbol: owes ? "exclamationmark.circle.fill" : nil
                )
            }
            .padding(.bottom, 14)

            ContractStatusRow(specialist: specialist)
                .padding(.bottom, 16)

            HStack(spacing: 11) {
                ZStack {
                    RoundedRectangle(cornerRadius: 11)
                        .fill(AuraPalette.sandSoft)
                        .frame(width: 34, height: 34)
                    Image(systemName: "creditcard.fill")
                        .font(.system(size: 14))
                        .foregroundStyle(AuraPalette.navy)
                }
                VStack(alignment: .leading, spacing: 2) {
                    EyebrowText(text: "Plan \(plan.name)")
                    Text(owes ? "Adeuda \(Money.format(plan.price)) de este mes" : "Pagado este mes")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(owes ? AuraPalette.red : AuraPalette.inkMuted)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                Text("\(Money.format(plan.price)) / mes")
                    .font(AuraFont.titleSmall())
                    .foregroundStyle(AuraPalette.blue)
            }
            .padding(.bottom, 16)

            HStack(spacing: 6) {
                Image(systemName: "clock")
                    .font(.system(size: 12))
                    .foregroundStyle(AuraPalette.inkMuted)
                Text("Horas del plan")
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text("\(specialist.hoursUsed) / \(plan.hours) h")
                    .font(AuraFont.labelLarge())
                    .foregroundStyle(AuraPalette.navy)
            }
            .padding(.bottom, 9)

            AuraProgressBar(
                progress: hoursShare,
                tint: hoursShare >= 0.9
                    ? AuraPalette.amber
                    : (owes ? AuraPalette.red : AuraPalette.blue)
            )
            .padding(.bottom, 8)

            Text(hoursCaption(for: hoursShare))
                .font(AuraFont.bodySmall())
                .foregroundStyle(hoursShare >= 0.9 ? AuraPalette.amber : AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
        .overlay {
            if owes {
                RoundedRectangle(cornerRadius: AuraRadius.card)
                    .strokeBorder(AuraPalette.red.opacity(0.45), lineWidth: 1)
            }
        }
    }

    private func hoursCaption(for share: Double) -> String {
        if share >= 0.9 {
            return "Casi agota su plan, buen momento para subirla de nivel"
        }
        if share <= 0.5 {
            return "Usa menos de la mitad de sus horas"
        }
        return "Ritmo de uso saludable"
    }
}
