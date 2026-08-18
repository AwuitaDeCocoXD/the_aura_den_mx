import Foundation

/// Placeholder rental agreement shown before a specialist can rent a station.
/// The wording is sample text: it has no legal validity and must be replaced by
/// the contract reviewed by the coworking's lawyer.
nonisolated enum ContractCopy {

    static let title = "Contrato de Renta de Espacio"
    static let subtitle = "The Aura Den · Beauty coworking"

    static let parties = """
    Celebrado entre THE AURA DEN, con domicilio en Av. Álvaro Obregón 128, Roma Norte, Ciudad \
    de México, en adelante "el coworking", y la especialista cuyo nombre y firma constan al \
    calce, en adelante "la especialista", al tenor de las declaraciones y cláusulas siguientes.
    """

    static let disclaimer = """
    Texto de ejemplo. Este contrato es una simulación visual para la demostración de la app y \
    no tiene validez legal. La versión definitiva deberá ser redactada y revisada por un \
    abogado antes de ponerse en operación.
    """

    static let closing = """
    Leído que fue el presente contrato por ambas partes y enteradas de su contenido y alcance \
    legal, lo firman de conformidad en la Ciudad de México, en la fecha señalada al momento de \
    la firma electrónica.
    """

    static let sections: [ContractSection] = [
        ContractSection(
            number: "1",
            title: "Objeto del contrato",
            body: """
            El coworking otorga a la especialista el uso temporal de una estación de trabajo \
            dentro de sus instalaciones, en la modalidad y por las horas contratadas en su \
            membresía vigente. El presente instrumento no constituye una relación laboral, de \
            asociación ni de subordinación entre las partes, por lo que cada una conserva su \
            independencia operativa, fiscal y administrativa.

            La especialista atiende a su propia clientela, fija sus propios precios y responde \
            por los servicios que presta. El coworking únicamente pone a disposición el \
            inmueble, el mobiliario y los servicios descritos en la membresía contratada.
            """
        ),
        ContractSection(
            number: "2",
            title: "Obligaciones de la especialista",
            body: """
            La especialista se obliga a utilizar la estación asignada exclusivamente para la \
            prestación de servicios de belleza, conservándola en el mismo estado en que le fue \
            entregada y reportando cualquier desperfecto de forma inmediata.

            Deberá contar con la documentación, certificaciones y materiales propios de su \
            especialidad, cumplir con las normas de higiene y manejo de residuos aplicables, \
            respetar los horarios reservados y liberar la estación al término de los mismos.

            Queda prohibido subarrendar, ceder o compartir el espacio con terceros sin \
            autorización previa y por escrito del coworking.
            """
        ),
        ContractSection(
            number: "3",
            title: "Condiciones de pago",
            body: """
            La especialista cubrirá el importe de su membresía por mensualidades adelantadas, \
            dentro de los primeros cinco días naturales de cada periodo, a través de los medios \
            de pago habilitados en la aplicación.

            El retraso en el pago genera la suspensión temporal del acceso a las estaciones \
            hasta regularizar el adeudo. Las horas no utilizadas dentro del periodo contratado \
            no son acumulables ni reembolsables, salvo pacto distinto por escrito.

            La especialista podrá solicitar comprobante fiscal digital (CFDI) por cada pago, \
            proporcionando sus datos de facturación dentro del mes en curso.
            """
        ),
        ContractSection(
            number: "4",
            title: "Terminación",
            body: """
            Cualquiera de las partes podrá dar por terminado el presente contrato mediante \
            aviso por escrito con treinta días naturales de anticipación, sin responsabilidad \
            alguna, siempre que no existan adeudos pendientes.

            El coworking podrá rescindir el contrato de forma inmediata en caso de \
            incumplimiento reiterado de las obligaciones de pago, daño doloso al mobiliario, \
            conductas que afecten la convivencia del estudio o el incumplimiento de las normas \
            sanitarias aplicables.

            Al término del contrato la especialista deberá retirar sus materiales y liberar el \
            locker asignado dentro de los cinco días hábiles siguientes.
            """
        ),
        ContractSection(
            number: "5",
            title: "Confidencialidad y uso de imagen",
            body: """
            Las partes se obligan a guardar confidencialidad respecto de la información \
            comercial, de clientela y de operación a la que tengan acceso con motivo del \
            presente contrato.

            La especialista autoriza al coworking a difundir fotografías de los trabajos \
            realizados dentro del estudio con fines de promoción, otorgando el crédito \
            correspondiente. Esta autorización podrá revocarse en cualquier momento mediante \
            aviso por escrito.
            """
        ),
        ContractSection(
            number: "6",
            title: "Firma electrónica",
            body: """
            Las partes reconocen la validez de la firma electrónica capturada dentro de la \
            aplicación como manifestación de su voluntad, y aceptan que el folio generado, la \
            fecha y la hora de firma constituyen evidencia de la celebración del presente \
            contrato.

            Para la interpretación y cumplimiento del mismo, las partes se someten a las leyes \
            y tribunales competentes de la Ciudad de México, renunciando a cualquier otro fuero \
            que pudiera corresponderles.
            """
        )
    ]
}
