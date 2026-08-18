package com.rork.theauraden.data

/** A titled block of legal text. */
data class LegalSection(val title: String, val body: String)

/**
 * Placeholder legal copy for the demo. The wording is illustrative only and has to be
 * replaced by the text the client's lawyer approves before publishing.
 */
object LegalCopy {

    const val DISCLAIMER =
        "Texto de ejemplo para la demo. El contenido final debe ser revisado y " +
            "aprobado por el área legal de The Aura Den antes de publicar la app."

    val terms: List<LegalSection> = listOf(
        LegalSection(
            "1. Sobre este documento",
            "Estos términos regulan el uso de la aplicación de The Aura Den, un espacio " +
                "de coworking de belleza ubicado en Av. Álvaro Obregón 128, Roma Norte, " +
                "Ciudad de México. Al crear una cuenta aceptas lo aquí descrito."
        ),
        LegalSection(
            "2. Quién puede usar la app",
            "Pueden registrarse especialistas mayores de edad que renten un espacio en el " +
                "estudio, así como clientas que deseen agendar un servicio. Cada persona es " +
                "responsable de la veracidad de los datos que registra."
        ),
        LegalSection(
            "3. Membresías y renta de espacio",
            "Las especialistas contratan una membresía mensual que da derecho a un número " +
                "determinado de horas de uso de estación. La membresía se renueva cada mes y " +
                "puede cancelarse con aviso previo conforme al reglamento interno."
        ),
        LegalSection(
            "4. Citas y cancelaciones",
            "Las citas agendadas pueden reprogramarse o cancelarse desde la app. Las " +
                "cancelaciones con menos de 24 horas de anticipación pueden estar sujetas a " +
                "una política de penalización definida por cada especialista."
        ),
        LegalSection(
            "5. Pagos",
            "La app muestra el estado de cada cargo: pagado, pendiente o fallido. Los pagos " +
                "por transferencia pueden tardar hasta un día hábil en confirmarse. La " +
                "facturación CFDI se emite a solicitud con los datos fiscales registrados."
        ),
        LegalSection(
            "6. Conducta dentro del estudio",
            "Todas las personas usuarias se comprometen a mantener un trato respetuoso, " +
                "cuidar el mobiliario y respetar los horarios reservados por las demás " +
                "especialistas."
        ),
        LegalSection(
            "7. Cambios a los términos",
            "The Aura Den puede actualizar estos términos. Cualquier cambio relevante será " +
                "avisado dentro de la app con antelación razonable."
        )
    )

    val privacy: List<LegalSection> = listOf(
        LegalSection(
            "1. Responsable de tus datos",
            "The Aura Den, con domicilio en Av. Álvaro Obregón 128, Roma Norte, Ciudad de " +
                "México, es responsable del tratamiento de los datos personales que nos " +
                "compartes a través de esta aplicación."
        ),
        LegalSection(
            "2. Qué datos recopilamos",
            "Nombre, correo electrónico, número de celular, especialidad, historial de citas " +
                "y registros de pago. No almacenamos números de tarjeta: los cobros se " +
                "procesan a través de proveedores de pago certificados."
        ),
        LegalSection(
            "3. Para qué los usamos",
            "Para crear y administrar tu cuenta, agendar y confirmar citas, gestionar " +
                "membresías y pagos, emitir comprobantes y enviarte avisos relacionados con " +
                "tu actividad en el estudio."
        ),
        LegalSection(
            "4. Con quién los compartimos",
            "Únicamente con proveedores necesarios para operar el servicio, como el " +
                "procesador de pagos y el proveedor de facturación electrónica. No vendemos " +
                "tus datos a terceros."
        ),
        LegalSection(
            "5. Tus derechos ARCO",
            "Puedes acceder, rectificar, cancelar u oponerte al tratamiento de tus datos " +
                "escribiendo a hola@theauraden.mx. También puedes eliminar tu cuenta desde " +
                "la sección de perfil dentro de la app."
        ),
        LegalSection(
            "6. Conservación",
            "Conservamos tu información mientras tu cuenta esté activa y por el plazo que " +
                "exija la normativa fiscal aplicable para los comprobantes emitidos."
        ),
        LegalSection(
            "7. Contacto",
            "Para cualquier duda sobre privacidad puedes escribir a hola@theauraden.mx o " +
                "acercarte a recepción en el estudio."
        )
    )

    /** What a person loses when deleting the account — shown in the confirmation modal. */
    val deletionLosses: List<String> = listOf(
        "Tu perfil, tus fotos y tus reseñas",
        "Tu historial de citas y de servicios",
        "Tus comprobantes y facturas descargables",
        "Tu membresía activa y las horas que te quedan"
    )
}
