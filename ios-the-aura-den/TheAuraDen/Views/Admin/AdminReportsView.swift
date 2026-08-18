import SwiftUI

/// Monthly business report for the owner.
struct AdminReportsView: View {
    let currentRoute: AuraTabRoute
    let onTabSelected: (AuraTabRoute) -> Void

    private var recurringRent: Int {
        DemoData.revenueByPlan.reduce(0) { $0 + $1.amount }
    }

    var body: some View {
        let metricRows = stride(from: 0, to: DemoData.monthMetrics.count, by: 2).map { index in
            Array(DemoData.monthMetrics[index..<min(index + 2, DemoData.monthMetrics.count)])
        }

        AuraTabScaffold(
            role: .admin,
            currentRoute: currentRoute,
            onTabSelected: onTabSelected
        ) {
            AuraHeader(
                title: "Reportes",
                eyebrow: "Modo administrador",
                subtitle: "Agosto 2025 · cierre parcial"
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 18)

                    VStack(spacing: 12) {
                        ForEach(Array(metricRows.enumerated()), id: \.offset) { _, row in
                            HStack(alignment: .top, spacing: 12) {
                                ForEach(row) { metric in
                                    MetricTile(
                                        value: metric.value,
                                        label: metric.label,
                                        caption: metric.delta,
                                        captionColor: metric.positive
                                            ? AuraPalette.green
                                            : AuraPalette.red,
                                        background: metric.label == "Ingresos del mes"
                                            ? AuraPalette.cream
                                            : AuraPalette.white
                                    )
                                }
                                if row.count == 1 {
                                    Spacer().frame(maxWidth: .infinity)
                                }
                            }
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 22)

                    sectionTitle("Tendencia de ingresos", subtitle: "Últimos 6 meses")

                    Spacer().frame(height: 12)

                    revenueTrendCard
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 22)

                    sectionTitle(
                        "Ingresos por membresía",
                        subtitle: "Renta fija mensual · \(Money.format(recurringRent)) MXN"
                    )

                    Spacer().frame(height: 12)

                    VStack(spacing: 18) {
                        ForEach(DemoData.revenueByPlan) { plan in
                            PlanRevenueRow(plan: plan)
                        }
                    }
                    .padding(20)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 22)

                    sectionTitle("Top especialistas", subtitle: "Más citas atendidas en agosto")

                    Spacer().frame(height: 12)

                    VStack(spacing: 10) {
                        ForEach(DemoData.topSpecialists) { specialist in
                            TopSpecialistRow(specialist: specialist)
                        }
                    }
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 22)

                    SectionHeading(text: "Servicios más solicitados")
                        .padding(.horizontal, 20)

                    Spacer().frame(height: 12)

                    VStack(spacing: 16) {
                        ForEach(DemoData.serviceDemand) { demand in
                            BarRow(
                                label: demand.service,
                                value: "\(demand.count) citas",
                                share: demand.share
                            )
                        }
                    }
                    .padding(20)
                    .frame(maxWidth: .infinity)
                    .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 22)

                    VStack(alignment: .leading, spacing: 0) {
                        EyebrowText(text: "Ocupación por horario", color: AuraPalette.sand)
                            .padding(.bottom, 10)

                        Text("La tarde es tu hora pico")
                            .font(AuraFont.headlineSmall())
                            .foregroundStyle(AuraPalette.navy)
                            .padding(.bottom, 14)

                        VStack(spacing: 14) {
                            BarRow(label: "10:00 am – 1:00 pm", value: "64%", share: 0.64)
                            BarRow(label: "1:00 pm – 4:00 pm", value: "88%", share: 0.88)
                            BarRow(label: "4:00 pm – 8:00 pm", value: "72%", share: 0.72)
                        }
                    }
                    .padding(20)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(AuraPalette.cream, in: .rect(cornerRadius: AuraRadius.card))
                    .padding(.horizontal, 20)

                    Spacer().frame(height: 28)
                }
            }
        }
    }

    private func sectionTitle(_ title: String, subtitle: String) -> some View {
        VStack(alignment: .leading, spacing: 4) {
            SectionHeading(text: title)
            Text(subtitle)
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 20)
    }

    private var revenueTrendCard: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .center) {
                VStack(alignment: .leading, spacing: 5) {
                    EyebrowText(text: "Agosto")
                    Text("$186,400")
                        .font(AuraFont.displaySmall())
                        .foregroundStyle(AuraPalette.navy)
                }
                Spacer()
                HStack(spacing: 5) {
                    Image(systemName: "arrow.up.right")
                        .font(.system(size: 12, weight: .bold))
                    Text("+89% vs marzo")
                        .font(AuraFont.labelMedium())
                        .fontWeight(.semibold)
                }
                .foregroundStyle(AuraPalette.green)
                .padding(.horizontal, 12)
                .padding(.vertical, 7)
                .background(AuraPalette.green.opacity(0.12), in: .capsule)
            }
            .padding(.bottom, 20)

            HStack(alignment: .bottom, spacing: 0) {
                ForEach(Array(DemoData.revenueByMonth.enumerated()), id: \.element.id) { index, month in
                    ChartColumn(
                        label: month.month,
                        share: month.share,
                        isHighlighted: index == DemoData.revenueByMonth.count - 1
                    )
                    .frame(maxWidth: .infinity)
                }
            }
            .frame(height: 148)
            .padding(.bottom, 18)

            AuraDivider()
                .padding(.bottom, 14)

            Text("Seis meses consecutivos al alza. Agosto cerró en $186,400 MXN, el mejor mes del año.")
                .font(AuraFont.bodyMedium())
                .foregroundStyle(AuraPalette.inkMuted)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding(20)
        .frame(maxWidth: .infinity)
        .background(AuraPalette.white, in: .rect(cornerRadius: AuraRadius.card))
    }
}

private struct PlanRevenueRow: View {
    let plan: PlanRevenue

    private var barColor: Color {
        switch plan.planName {
        case "Anchor": AuraPalette.navy
        case "Turista": AuraPalette.sand
        default: AuraPalette.blue
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 9) {
            HStack(alignment: .center) {
                VStack(alignment: .leading, spacing: 2) {
                    Text("Plan \(plan.planName)")
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.ink)
                    Text("\(plan.specialists) especialistas")
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                }
                Spacer()
                Text(Money.format(plan.amount))
                    .font(AuraFont.titleMedium())
                    .foregroundStyle(AuraPalette.navy)
            }
            AuraProgressBar(
                progress: plan.share,
                tint: barColor,
                trackColor: AuraPalette.cream,
                height: 10
            )
        }
        .frame(maxWidth: .infinity)
    }
}

private struct TopSpecialistRow: View {
    let specialist: TopSpecialist

    private var isFirst: Bool { specialist.position == 1 }

    var body: some View {
        HStack(spacing: 14) {
            AuraAvatar(imageURL: specialist.imageURL, size: 56, ringColor: .clear)
                .overlay(alignment: .bottomLeading) {
                    ZStack {
                        Circle()
                            .fill(isFirst ? AuraPalette.yellow : AuraPalette.white)
                            .frame(width: 22, height: 22)
                        Text("\(specialist.position)")
                            .font(AuraFont.labelMedium())
                            .fontWeight(.bold)
                            .foregroundStyle(AuraPalette.navy)
                    }
                }

            VStack(alignment: .leading, spacing: 0) {
                HStack(spacing: 6) {
                    Text(specialist.name)
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.ink)
                    if isFirst {
                        Image(systemName: "trophy.fill")
                            .font(.system(size: 13))
                            .foregroundStyle(AuraPalette.sand)
                    }
                }
                .padding(.bottom, 2)

                Text(specialist.specialty)
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .padding(.bottom, 9)

                AuraProgressBar(
                    progress: specialist.share,
                    tint: isFirst ? AuraPalette.blue : AuraPalette.blue.opacity(0.55),
                    trackColor: AuraPalette.sandSoft,
                    height: 7
                )
            }
            .frame(maxWidth: .infinity, alignment: .leading)

            VStack(alignment: .trailing, spacing: 0) {
                Text("\(specialist.appointments)")
                    .font(AuraFont.headlineSmall())
                    .foregroundStyle(AuraPalette.navy)
                Text("citas")
                    .font(AuraFont.bodySmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                Text(Money.format(specialist.revenue))
                    .font(AuraFont.labelMedium())
                    .foregroundStyle(AuraPalette.blue)
                    .padding(.top, 4)
            }
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            isFirst ? AuraPalette.cream : AuraPalette.white,
            in: .rect(cornerRadius: AuraRadius.card)
        )
    }
}
