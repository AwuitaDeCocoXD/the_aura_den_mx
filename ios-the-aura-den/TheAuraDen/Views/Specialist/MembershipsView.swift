import SwiftUI

/// The three monthly memberships, with Residente as the recommended plan.
struct MembershipsView: View {
    let onBack: () -> Void
    let onChoosePlan: (String) -> Void

    @Environment(DemoStore.self) private var store

    var body: some View {
        AuraPlainScaffold {
            AuraHeader(
                title: "Membresías",
                eyebrow: "Tu espacio, tus reglas",
                subtitle: "Elige el ritmo que acompaña tu talento",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(spacing: 0) {
                    Spacer().frame(height: 18)

                    ForEach(DemoData.plans) { plan in
                        PlanCard(
                            plan: plan,
                            isActive: plan.id == store.activePlanID,
                            onChoose: { onChoosePlan(plan.id) }
                        )
                        .padding(.bottom, 14)
                    }

                    Spacer().frame(height: 4)

                    Text("Puedes cambiar de plan cuando lo necesites.")
                        .font(AuraFont.bodyMedium())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, 16)

                    Spacer().frame(height: 28)
                }
                .padding(.horizontal, 20)
            }
        }
    }
}

private struct PlanCard: View {
    let plan: MembershipPlan
    let isActive: Bool
    let onChoose: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack(alignment: .top, spacing: 12) {
                VStack(alignment: .leading, spacing: 0) {
                    if plan.recommended {
                        StatusPill(
                            text: "Recomendada",
                            foreground: AuraPalette.navy,
                            background: AuraPalette.yellow,
                            symbol: "star.fill"
                        )
                        .padding(.bottom, 12)
                    } else if isActive {
                        StatusPill(
                            text: "Tu plan actual",
                            foreground: AuraPalette.white,
                            background: AuraPalette.blue
                        )
                        .padding(.bottom, 12)
                    }

                    EyebrowText(text: "Membresía")
                        .padding(.bottom, 4)

                    Text(plan.name)
                        .font(AuraFont.displaySmall())
                        .foregroundStyle(AuraPalette.navy)
                        .padding(.bottom, 6)

                    HStack(alignment: .lastTextBaseline, spacing: 5) {
                        Text(Money.format(plan.price))
                            .font(AuraFont.headlineLarge())
                            .foregroundStyle(AuraPalette.blue)
                        Text("/ mes")
                            .font(AuraFont.bodyMedium())
                            .foregroundStyle(AuraPalette.inkMuted)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                if plan.recommended {
                    AuraCardMark()
                        .frame(width: 56)
                }
            }
            .padding(.bottom, 16)

            ForEach(plan.perks, id: \.self) { perk in
                HStack(spacing: 10) {
                    Image(systemName: "checkmark.circle.fill")
                        .font(.system(size: 15))
                        .foregroundStyle(AuraPalette.blue)
                    Text(perk)
                        .font(AuraFont.bodyLarge())
                        .fontWeight(plan.recommended ? .medium : .regular)
                        .foregroundStyle(AuraPalette.ink)
                        .fixedSize(horizontal: false, vertical: true)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.vertical, 4)
            }

            Group {
                if isActive {
                    AuraSecondaryButton(title: "Renovar este plan", action: onChoose)
                } else {
                    AuraPrimaryButton(title: "Elegir plan", action: onChoose)
                }
            }
            .padding(.top, 18)
        }
        .padding(20)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            plan.recommended ? AuraPalette.cream : AuraPalette.white,
            in: .rect(cornerRadius: AuraRadius.card)
        )
        .overlay {
            if plan.recommended {
                RoundedRectangle(cornerRadius: AuraRadius.card)
                    .strokeBorder(AuraPalette.yellow, lineWidth: 1.5)
            }
        }
    }
}
