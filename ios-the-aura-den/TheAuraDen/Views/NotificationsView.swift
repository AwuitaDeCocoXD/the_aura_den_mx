import SwiftUI

/// Notice centre opened from the bell. Reading the list marks everything as seen.
struct NotificationsView: View {
    let onBack: () -> Void

    @Environment(DemoStore.self) private var store

    var body: some View {
        let notices = store.notifications

        AuraPlainScaffold {
            AuraHeader(
                title: "Avisos",
                eyebrow: "The Aura Den",
                subtitle: "Lo que pasó mientras no estabas",
                onBack: onBack
            )
        } content: {
            ScrollView {
                VStack(spacing: 12) {
                    if notices.isEmpty {
                        Spacer().frame(height: 28)
                        AuraEmptyState(
                            title: "Todo tranquilo",
                            message: "Aquí aparecerán tus confirmaciones, recordatorios de pago "
                                + "y novedades del estudio.",
                            symbol: "bell"
                        )
                    } else {
                        Spacer().frame(height: 18)
                        ForEach(notices) { notice in
                            NotificationRow(notice: notice)
                        }
                    }
                    Spacer().frame(height: 28)
                }
                .padding(.horizontal, 20)
            }
        }
        .onAppear { store.markNotificationsRead() }
    }
}

private struct NotificationRow: View {
    let notice: AppNotification

    private var accent: Color {
        switch notice.kind {
        case .payment: AuraPalette.amber
        case .appointment: AuraPalette.blue
        case .contract: AuraPalette.green
        case .studio: AuraPalette.navy
        }
    }

    var body: some View {
        HStack(alignment: .top, spacing: 13) {
            ZStack {
                RoundedRectangle(cornerRadius: 14)
                    .fill(accent.opacity(0.13))
                    .frame(width: 42, height: 42)
                Image(systemName: notice.kind.symbol)
                    .font(.system(size: 17))
                    .foregroundStyle(accent)
            }

            VStack(alignment: .leading, spacing: 0) {
                HStack(alignment: .top, spacing: 8) {
                    Text(notice.title)
                        .font(AuraFont.titleMedium())
                        .foregroundStyle(AuraPalette.navy)
                        .fixedSize(horizontal: false, vertical: true)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    if notice.unread {
                        Circle()
                            .fill(AuraPalette.yellow)
                            .frame(width: 8, height: 8)
                            .padding(.top, 6)
                    }
                }
                .padding(.bottom, 4)

                Text(notice.body)
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.ink)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.bottom, 8)

                Text(notice.timeAgo)
                    .font(AuraFont.labelSmall())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 3)
                    .background(
                        AuraPalette.sandSoft.opacity(0.55),
                        in: .rect(cornerRadius: 9)
                    )
            }
        }
        .padding(16)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            notice.unread ? AuraPalette.cream : AuraPalette.white,
            in: .rect(cornerRadius: AuraRadius.card)
        )
    }
}
