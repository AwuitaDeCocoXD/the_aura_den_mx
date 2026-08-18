import SwiftUI

/// Demo-only role switcher. Not part of the final product: it exists so the four
/// perspectives can be shown quickly in a client presentation without signing out.
struct RoleSwitcherSheet: View {
    let currentRole: UserRole
    let onSelect: (UserRole) -> Void

    @Environment(\.dismiss) private var dismiss

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            Capsule()
                .fill(AuraPalette.divider)
                .frame(width: 42, height: 4)
                .frame(maxWidth: .infinity)
                .padding(.top, 14)
                .padding(.bottom, 8)

            VStack(alignment: .leading, spacing: 0) {
                HStack(spacing: 12) {
                    Text("Cambiar de vista")
                        .font(AuraFont.headlineSmall())
                        .foregroundStyle(AuraPalette.navy)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    StatusPill(
                        text: "Solo demo",
                        foreground: AuraPalette.navy,
                        background: AuraPalette.yellow
                    )
                }
                .padding(.bottom, 6)

                Text("Atajo para la presentación: cambia de perspectiva sin cerrar sesión.")
                    .font(AuraFont.bodyMedium())
                    .foregroundStyle(AuraPalette.inkMuted)
                    .fixedSize(horizontal: false, vertical: true)
                    .padding(.bottom, 18)

                ForEach(UserRole.allCases) { role in
                    roleRow(role)
                        .padding(.bottom, 10)
                }
            }
            .padding(.horizontal, 20)
            .padding(.bottom, 20)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .background(AuraPalette.white)
        .presentationDetents([.height(470)])
        .presentationDragIndicator(.hidden)
    }

    private func roleRow(_ role: UserRole) -> some View {
        let isSelected = role == currentRole

        return Button {
            AuraHaptics.tap()
            onSelect(role)
            dismiss()
        } label: {
            HStack(spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 14)
                        .fill(isSelected ? AuraPalette.blue : AuraPalette.sandSoft)
                        .frame(width: 44, height: 44)
                    Image(systemName: role.symbol)
                        .font(.system(size: 19))
                        .foregroundStyle(isSelected ? AuraPalette.white : AuraPalette.navy)
                }

                VStack(alignment: .leading, spacing: 2) {
                    Text(role.label)
                        .font(AuraFont.titleMedium())
                        .fontWeight(.semibold)
                        .foregroundStyle(AuraPalette.ink)
                    Text(role.summary)
                        .font(AuraFont.bodySmall())
                        .foregroundStyle(AuraPalette.inkMuted)
                        .multilineTextAlignment(.leading)
                        .fixedSize(horizontal: false, vertical: true)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                if isSelected {
                    Image(systemName: "checkmark")
                        .font(.system(size: 16, weight: .semibold))
                        .foregroundStyle(AuraPalette.blue)
                }
            }
            .padding(14)
            .frame(maxWidth: .infinity)
            .background(
                isSelected ? AuraPalette.blueSoft : AuraPalette.white,
                in: .rect(cornerRadius: 18)
            )
            .overlay {
                RoundedRectangle(cornerRadius: 18)
                    .strokeBorder(isSelected ? AuraPalette.blue : AuraPalette.divider, lineWidth: 1)
            }
        }
        .buttonStyle(PressableButtonStyle())
    }
}
