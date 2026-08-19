import Foundation

/// A titled block of legal text.
nonisolated struct LegalSection: Identifiable, Hashable {
    var id: String { title }
    let title: String
    let body: String
}

/// Terms of service and privacy notice shown inside the app.
nonisolated enum LegalCopy {

    static let terms: [LegalSection] = [
        LegalSection(
            title: "1. Sobre este documento",
            body: "Estos términos regulan el uso de la aplicación de The Aura Den, un espacio " +
                "de coworking de belleza ubicado en Cuauhtémoc #1473, Piso 2, Benito " +
                "Juárez, Ciudad de México. Al crear una cuenta aceptas lo aquí descrito."
        ),
        LegalSection(
            title: "2. Quién puede usar la app",
            body: "Pueden registrarse especialistas mayores de edad que renten un espacio en " +
                "el estudio, así como clientas que deseen agendar un servicio. Cada persona " +
                "es responsable de la veracidad de los datos que registra."
        ),
        LegalSection(
            title: "3. Membresías y renta de espacio",
            body: "Las especialistas contratan una membresía mensual que da derecho a un " +
                "número determinado de horas de uso de estación. La membresía se renueva " +
                "cada mes y puede cancelarse con aviso previo conforme al reglamento interno."
        ),
        LegalSection(
            title: "4. Citas y cancelaciones",
            body: "Las citas agendadas pueden reprogramarse o cancelarse desde la app. Las " +
                "cancelaciones con menos de 24 horas de anticipación pueden estar sujetas a " +
                "una política de penalización definida por cada especialista."
        ),
        LegalSection(
            title: "5. Pagos",
            body: "La app muestra el estado de cada cargo: pagado, pendiente o fallido. Los " +
                "pagos por transferencia pueden tardar hasta un día hábil en confirmarse. La " +
                "facturación CFDI se emite a solicitud con los datos fiscales registrados."
        ),
        LegalSection(
            title: "6. Conducta dentro del estudio",
            body: "Todas las personas usuarias se comprometen a mantener un trato " +
                "respetuoso, cuidar el mobiliario y respetar los horarios reservados por las " +
                "demás especialistas."
        ),
        LegalSection(
            title: "7. Cambios a los términos",
            body: "The Aura Den puede actualizar estos términos. Cualquier cambio relevante " +
                "será avisado dentro de la app con antelación razonable."
        )
    ]

    static let privacy: [LegalSection] = [
        LegalSection(
            title: "1. Responsable de tus datos",
            body: "The Aura Den, con domicilio en Cuauhtémoc #1473, Piso 2, Benito Juárez, Ciudad " +
                "de México, es responsable del tratamiento de los datos personales que nos " +
                "compartes a través de esta aplicación."
        ),
        LegalSection(
            title: "2. Qué datos recopilamos",
            body: "Nombre, correo electrónico, número de celular, especialidad, historial de " +
                "citas y registros de pago. No almacenamos números de tarjeta: los cobros se " +
                "procesan a través de proveedores de pago certificados."
        ),
        LegalSection(
            title: "3. Para qué los usamos",
            body: "Para crear y administrar tu cuenta, agendar y confirmar citas, gestionar " +
                "membresías y pagos, emitir comprobantes y enviarte avisos relacionados con " +
                "tu actividad en el estudio."
        ),
        LegalSection(
            title: "4. Con quién los compartimos",
            body: "Únicamente con proveedores necesarios para operar el servicio, como el " +
                "procesador de pagos y el proveedor de facturación electrónica. No vendemos " +
                "tus datos a terceros."
        ),
        LegalSection(
            title: "5. Tus derechos ARCO",
            body: "Puedes acceder, rectificar, cancelar u oponerte al tratamiento de tus " +
                "datos escribiendo a hola@theauraden.mx. También puedes eliminar tu cuenta " +
                "desde la sección de perfil dentro de la app."
        ),
        LegalSection(
            title: "6. Conservación",
            body: "Conservamos tu información mientras tu cuenta esté activa y por el plazo " +
                "que exija la normativa fiscal aplicable para los comprobantes emitidos."
        ),
        LegalSection(
            title: "7. Contacto",
            body: "Para cualquier duda sobre privacidad puedes escribir a " +
                "hola@theauraden.mx o acercarte a recepción en el estudio."
        )
    ]

    /// What a person loses when deleting the account — shown in the confirmation modal.
    static let deletionLosses: [String] = [
        "Tu perfil, tus fotos y tus reseñas",
        "Tu historial de citas y de servicios",
        "Tus comprobantes y facturas descargables",
        "Tu membresía activa y las horas que te quedan"
    ]
}
