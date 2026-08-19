import Foundation

/// Generated brand imagery used across the demo.
nonisolated enum AuraImages {
    static let stationNails2 =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/e206f3d9-f8c9-42be-8e4f-60ccb339d73b.png"
    static let stationNails1 =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/6ee43916-14e1-4ee9-a1f6-edbbefec1e66.png"
    static let stationLashes =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/0ea01544-244d-437d-96a1-1b7498a39e60.png"
    static let juanita =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/bfc828fe-f1b2-46fb-a888-b80e0189af58.png"
    static let renata =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/5d7cb0e7-d501-4b4c-9597-3deb1486a380.png"
    static let camila =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/1c1e2596-a6c0-4144-998f-7cd070b83420.png"
    static let studio =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/af8c7c99-618c-425e-9124-1ecdfa7b6347.png"
}

/// Static copy shared across screens.
nonisolated enum AuraCopy {
    static let brandName = "The Aura Den"
    static let tagline = "Beauty coworking · CDMX"
    static let addressLine1 = "Cuauhtémoc #1473, Piso 2"
    static let addressLine2 = "Benito Juárez, CDMX"
    static let neighborhood = "Benito Juárez"
    static let openingHours = "Lun a sáb · 9:00 am – 8:00 pm"
    static let todayLabel = "Martes 18 de agosto"
    static let currentUser = "Juanita Cruz"
    static let clientUser = "Lucía Gómez"
    static let legalUpdated = "Última actualización: 1 de agosto de 2025"
    static let contractDate = "18 de agosto de 2026"
    static let demoNotice = "Demo visual · los datos son de ejemplo"
}

nonisolated enum DemoData {

    static let weekDays: [DaySlot] = [
        DaySlot(id: "mon", weekdayShort: "Lun", dayNumber: "17", fullLabel: "Lunes 17 de agosto"),
        DaySlot(id: "tue", weekdayShort: "Mar", dayNumber: "18", fullLabel: "Martes 18 de agosto"),
        DaySlot(id: "wed", weekdayShort: "Mié", dayNumber: "19", fullLabel: "Miércoles 19 de agosto"),
        DaySlot(id: "thu", weekdayShort: "Jue", dayNumber: "20", fullLabel: "Jueves 20 de agosto"),
        DaySlot(id: "fri", weekdayShort: "Vie", dayNumber: "21", fullLabel: "Viernes 21 de agosto"),
        DaySlot(id: "sat", weekdayShort: "Sáb", dayNumber: "22", fullLabel: "Sábado 22 de agosto"),
        DaySlot(id: "sun", weekdayShort: "Dom", dayNumber: "23", fullLabel: "Domingo 23 de agosto")
    ]

    static let timeSlots: [String] = ["10:00 am", "12:30 pm", "2:00 pm", "4:00 pm", "6:30 pm"]

    static let specialties: [String] = ["Uñas", "Pestañas / cejas", "Maquillaje", "Podología"]

    static let services: [ClientService] = [
        ClientService(id: "s1", name: "Manicure gel", durationLabel: "1 h 15 min", price: 450),
        ClientService(id: "s2", name: "Pedicure spa", durationLabel: "1 h 30 min", price: 620),
        ClientService(id: "s3", name: "Uñas acrílicas", durationLabel: "2 h", price: 850),
        ClientService(id: "s4", name: "Pestañas / cejas", durationLabel: "1 h", price: 700)
    ]

    static let stations: [Station] = [
        Station(
            id: "st2",
            name: "Mesa de uñas 2",
            kind: "Mesa de uñas",
            status: .available,
            scheduleLabel: "10:00 am – 2:00 pm",
            amenities: ["Luz natural", "Lámpara profesional", "Toma corriente"],
            description: "Tu lugar para crear con calma. La estación junto a la ventana, "
                + "ideal para manicure y uñas acrílicas.",
            imageURL: AuraImages.stationNails2,
            hourlyRate: 120
        ),
        Station(
            id: "st1",
            name: "Mesa de uñas 1",
            kind: "Mesa de uñas",
            status: .occupied,
            scheduleLabel: "Ocupada hasta las 6:00 pm",
            amenities: ["Lámpara profesional", "Extractor de polvo"],
            description: "Estación central con extractor de polvo integrado y buena "
                + "circulación, cómoda para jornadas largas.",
            imageURL: AuraImages.stationNails1,
            occupiedBy: "Camila Ortiz",
            nextAvailability: "Próxima disponibilidad: 6:30 pm",
            hourlyRate: 120
        ),
        Station(
            id: "st3",
            name: "Mesa de uñas 3",
            kind: "Mesa de uñas",
            status: .occupied,
            scheduleLabel: "Ocupada hasta las 2:00 pm",
            amenities: ["Luz natural", "Locker cercano"],
            description: "Estación amplia al fondo del estudio, con locker a un paso y "
                + "espacio extra para materiales.",
            imageURL: AuraImages.stationNails1,
            occupiedBy: "Mariana Ríos",
            nextAvailability: "Próxima disponibilidad: 2:30 pm",
            hourlyRate: 120
        ),
        Station(
            id: "st4",
            name: "Mesa de pestañas 1",
            kind: "Pestañas",
            status: .available,
            scheduleLabel: "Disponible todo el día",
            amenities: ["Camilla acojinada", "Aro de luz", "Cortina de privacidad"],
            description: "Cabina serena con camilla acojinada y cortina de privacidad para "
                + "servicios de pestañas y cejas.",
            imageURL: AuraImages.stationLashes,
            hourlyRate: 140
        )
    ]

    static let plans: [MembershipPlan] = [
        MembershipPlan(
            id: "turista",
            name: "Turista",
            price: 2100,
            hours: 32,
            locker: "Locker incluido",
            perks: ["32 horas totales", "Locker incluido", "Reserva con 24h de anticipación"]
        ),
        MembershipPlan(
            id: "residente",
            name: "Residente",
            price: 3200,
            hours: 64,
            locker: "Locker incluido",
            perks: ["64 horas totales", "Locker incluido", "Kit de bienvenida"],
            recommended: true
        ),
        MembershipPlan(
            id: "anchor",
            name: "Anchor",
            price: 5500,
            hours: 128,
            locker: "Locker grande",
            perks: ["128 horas totales", "Locker grande", "Prioridad de reserva 48h antes"]
        )
    ]

    static let paymentMethods: [PaymentMethodOption] = [
        PaymentMethodOption(
            id: "card",
            name: "Tarjeta guardada",
            detail: "Visa terminación 4242",
            settlesImmediately: true
        ),
        PaymentMethodOption(
            id: "spei",
            name: "Transferencia SPEI",
            detail: "Se confirma en 1 día hábil",
            settlesImmediately: false
        ),
        PaymentMethodOption(
            id: "mercadopago",
            name: "Mercado Pago",
            detail: "Saldo o tarjeta asociada",
            settlesImmediately: true
        )
    ]

    static let appointments: [Appointment] = [
        Appointment(
            id: "a1",
            clientName: "Lucía Gómez",
            specialistName: "Juanita Cruz",
            service: "Manicure gel",
            dayID: "tue",
            dateLabel: "Martes 18 de agosto",
            time: "10:00 am",
            stationName: "Mesa de uñas 2",
            status: .confirmed,
            notes: "Diseño francés en tonos blanco y dorado",
            price: 450
        ),
        Appointment(
            id: "a2",
            clientName: "Mariana López",
            specialistName: "Juanita Cruz",
            service: "Pedicure spa",
            dayID: "tue",
            dateLabel: "Martes 18 de agosto",
            time: "12:30 pm",
            stationName: "Mesa de uñas 2",
            status: .confirmed,
            notes: "Prefiere tono nude rosado",
            price: 620
        ),
        Appointment(
            id: "a3",
            clientName: "Sofía Ramírez",
            specialistName: "Juanita Cruz",
            service: "Uñas acrílicas",
            dayID: "tue",
            dateLabel: "Martes 18 de agosto",
            time: "4:00 pm",
            stationName: "Mesa de uñas 3",
            status: .confirmed,
            notes: nil,
            price: 850
        ),
        Appointment(
            id: "a4",
            clientName: "Ana Torres",
            specialistName: "Juanita Cruz",
            service: "Manicure gel",
            dayID: "wed",
            dateLabel: "Miércoles 19 de agosto",
            time: "11:00 am",
            stationName: "Mesa de uñas 2",
            status: .confirmed,
            notes: "Trae inspiración en su celular",
            price: 450
        ),
        Appointment(
            id: "a5",
            clientName: "Lucía Gómez",
            specialistName: "Juanita Cruz",
            service: "Pedicure spa",
            dayID: "wed",
            dateLabel: "Miércoles 19 de agosto",
            time: "3:00 pm",
            stationName: "Mesa de uñas 2",
            status: .confirmed,
            notes: nil,
            price: 620
        ),
        Appointment(
            id: "a6",
            clientName: "Mariana López",
            specialistName: "Juanita Cruz",
            service: "Uñas acrílicas",
            dayID: "thu",
            dateLabel: "Jueves 20 de agosto",
            time: "10:00 am",
            stationName: "Mesa de uñas 3",
            status: .confirmed,
            notes: nil,
            price: 850
        ),
        Appointment(
            id: "a7",
            clientName: "Sofía Ramírez",
            specialistName: "Juanita Cruz",
            service: "Manicure gel",
            dayID: "fri",
            dateLabel: "Viernes 21 de agosto",
            time: "12:30 pm",
            stationName: "Mesa de uñas 2",
            status: .confirmed,
            notes: "Quiere probar tono vino",
            price: 450
        ),
        Appointment(
            id: "a8",
            clientName: "Ana Torres",
            specialistName: "Juanita Cruz",
            service: "Pestañas / cejas",
            dayID: "sat",
            dateLabel: "Sábado 22 de agosto",
            time: "11:30 am",
            stationName: "Mesa de pestañas 1",
            status: .confirmed,
            notes: nil,
            price: 700
        )
    ]

    /// Lucía Gómez is the demo client.
    static let clientHistory: [Appointment] = [
        Appointment(
            id: "c1",
            clientName: "Lucía Gómez",
            specialistName: "Juanita Cruz",
            service: "Manicure gel",
            dayID: "past",
            dateLabel: "1 de agosto",
            time: "10:00 am",
            stationName: "Mesa de uñas 2",
            status: .completed,
            notes: nil,
            price: 450
        ),
        Appointment(
            id: "c2",
            clientName: "Lucía Gómez",
            specialistName: "Camila Ortiz",
            service: "Uñas acrílicas",
            dayID: "past",
            dateLabel: "12 de julio",
            time: "1:00 pm",
            stationName: "Mesa de uñas 1",
            status: .completed,
            notes: nil,
            price: 850
        ),
        Appointment(
            id: "c3",
            clientName: "Lucía Gómez",
            specialistName: "Renata Fuentes",
            service: "Pestañas / cejas",
            dayID: "past",
            dateLabel: "28 de junio",
            time: "5:00 pm",
            stationName: "Mesa de pestañas 1",
            status: .cancelled,
            notes: nil,
            price: 700
        )
    ]

    static let checkIns: [CheckIn] = [
        CheckIn(
            id: "k1",
            clientName: "Lucía Gómez",
            specialistName: "Juanita Cruz",
            time: "10:00 am",
            service: "Manicure gel",
            status: .waiting
        ),
        CheckIn(
            id: "k2",
            clientName: "Mariana López",
            specialistName: "Juanita Cruz",
            time: "12:30 pm",
            service: "Pedicure spa",
            status: .pending
        ),
        CheckIn(
            id: "k3",
            clientName: "Valeria Núñez",
            specialistName: "Camila Ortiz",
            time: "2:00 pm",
            service: "Uñas acrílicas",
            status: .attended
        ),
        CheckIn(
            id: "k4",
            clientName: "Sofía Ramírez",
            specialistName: "Juanita Cruz",
            time: "4:00 pm",
            service: "Uñas acrílicas",
            status: .pending
        ),
        CheckIn(
            id: "k5",
            clientName: "Ana Torres",
            specialistName: "Renata Fuentes",
            time: "6:30 pm",
            service: "Pestañas / cejas",
            status: .pending
        )
    ]

    static let paymentHistory: [PaymentRecord] = [
        PaymentRecord(
            id: "p0",
            folio: "AD-2025-0918",
            concept: "Membresía Residente · septiembre",
            dateLabel: "15 de septiembre",
            amount: 3200,
            status: .pending,
            method: "Transferencia SPEI",
            kind: .membership,
            payerName: "Juanita Cruz"
        ),
        PaymentRecord(
            id: "p1",
            folio: "AD-2025-0815",
            concept: "Membresía Residente · agosto",
            dateLabel: "15 de agosto",
            amount: 3200,
            status: .paid,
            method: "Tarjeta guardada",
            kind: .membership,
            invoiceRequested: true,
            payerName: "Juanita Cruz"
        ),
        PaymentRecord(
            id: "p2",
            folio: "AD-2025-0812",
            concept: "Membresía Residente · agosto",
            dateLabel: "12 de agosto",
            amount: 3200,
            status: .failed,
            method: "Tarjeta guardada",
            kind: .membership,
            payerName: "Juanita Cruz"
        ),
        PaymentRecord(
            id: "p3",
            folio: "AD-2025-0715",
            concept: "Membresía Residente · julio",
            dateLabel: "15 de julio",
            amount: 3200,
            status: .paid,
            method: "Mercado Pago",
            kind: .membership,
            payerName: "Juanita Cruz"
        ),
        PaymentRecord(
            id: "p4",
            folio: "AD-2025-0615",
            concept: "Membresía Turista · junio",
            dateLabel: "15 de junio",
            amount: 2100,
            status: .paid,
            method: "Tarjeta guardada",
            kind: .membership,
            payerName: "Juanita Cruz"
        )
    ]

    static let reviews: [Review] = [
        Review(
            id: "r1",
            author: "Lucía Gómez",
            rating: 5,
            comment: "Manicure impecable y trato increíble",
            timeAgo: "Hace 3 días"
        ),
        Review(
            id: "r2",
            author: "Mariana López",
            rating: 5,
            comment: "Súper puntual y el diseño quedó tal cual lo pedí",
            timeAgo: "Hace 1 semana"
        ),
        Review(
            id: "r3",
            author: "Ana Torres",
            rating: 4,
            comment: "Muy buen ambiente en el estudio, volveré",
            timeAgo: "Hace 2 semanas"
        )
    ]

    static let currentSpecialist = SpecialistProfile(
        id: "sp1",
        name: "Juanita Cruz",
        specialty: "Uñas",
        phone: "+52 55 1234 5678",
        email: "juanita@correo.com",
        since: "Especialista desde 2024",
        rating: 4.8,
        reviewCount: 36,
        imageURL: AuraImages.juanita,
        planID: "residente",
        hoursUsed: 42,
        paymentStatus: .paid,
        contractSigned: true,
        contractFolio: "AD-2026-0087",
        contractDateLabel: "14 de enero de 2026"
    )

    /// Contract already on file for the demo specialist who signs in.
    static let signedContract = SignedContract(
        folio: "AD-2026-0087",
        signerName: AuraCopy.currentUser,
        dateLabel: "14 de enero de 2026",
        timeLabel: "9:12 h"
    )

    /// Folio assigned to a contract signed during the demo.
    static let newContractFolio = "AD-2026-0142"

    static let specialists: [SpecialistProfile] = [
        SpecialistProfile(
            id: "sp1",
            name: "Juanita Cruz",
            specialty: "Uñas",
            phone: "+52 55 1234 5678",
            email: "juanita@correo.com",
            since: "Especialista desde 2024",
            rating: 4.8,
            reviewCount: 36,
            imageURL: AuraImages.juanita,
            planID: "residente",
            hoursUsed: 42,
            paymentStatus: .paid,
            nextServiceLabel: "Disponible hoy a las 2:00 pm",
            contractSigned: true,
            contractFolio: "AD-2026-0087",
            contractDateLabel: "14 de enero de 2026"
        ),
        SpecialistProfile(
            id: "sp2",
            name: "Renata Fuentes",
            specialty: "Pestañas / cejas",
            phone: "+52 55 4455 8899",
            email: "renata@correo.com",
            since: "Especialista desde 2023",
            rating: 4.9,
            reviewCount: 52,
            imageURL: AuraImages.renata,
            planID: "anchor",
            hoursUsed: 96,
            paymentStatus: .paid,
            nextServiceLabel: "Disponible hoy a las 5:00 pm",
            contractSigned: true,
            contractFolio: "AD-2026-0031",
            contractDateLabel: "3 de marzo de 2026"
        ),
        SpecialistProfile(
            id: "sp3",
            name: "Camila Ortiz",
            specialty: "Uñas acrílicas",
            phone: "+52 55 7788 1122",
            email: "camila@correo.com",
            since: "Especialista desde 2025",
            rating: 4.7,
            reviewCount: 18,
            imageURL: AuraImages.camila,
            planID: "turista",
            hoursUsed: 28,
            paymentStatus: .pending,
            nextServiceLabel: "Disponible mañana a las 11:00 am",
            contractSigned: false
        )
    ]

    static let monthMetrics: [ReportMetric] = [
        ReportMetric(label: "Ingresos del mes", value: "$186,400", delta: "+12% vs julio", positive: true),
        ReportMetric(label: "Ocupación promedio", value: "78%", delta: "+6 pts vs julio", positive: true),
        ReportMetric(label: "Especialistas activas", value: "14", delta: "+2 nuevas rentistas", positive: true),
        ReportMetric(label: "Citas del mes", value: "312", delta: "-4% vs julio", positive: false)
    ]

    static let serviceDemand: [ServiceDemand] = [
        ServiceDemand(service: "Manicure gel", count: 128, share: 1.0),
        ServiceDemand(service: "Uñas acrílicas", count: 86, share: 0.67),
        ServiceDemand(service: "Pedicure spa", count: 61, share: 0.48),
        ServiceDemand(service: "Pestañas / cejas", count: 37, share: 0.29)
    ]

    static let revenueByMonth: [MonthRevenue] = [
        MonthRevenue(month: "Mar", amount: 98400, share: 0.53),
        MonthRevenue(month: "Abr", amount: 112300, share: 0.60),
        MonthRevenue(month: "May", amount: 128000, share: 0.69),
        MonthRevenue(month: "Jun", amount: 142500, share: 0.76),
        MonthRevenue(month: "Jul", amount: 166200, share: 0.89),
        MonthRevenue(month: "Ago", amount: 186400, share: 1.0)
    ]

    /// Recurring monthly rent split by membership tier. 5 + 6 + 3 = 14 especialistas.
    static let revenueByPlan: [PlanRevenue] = [
        PlanRevenue(planName: "Residente", specialists: 6, amount: 19200, share: 1.0),
        PlanRevenue(planName: "Anchor", specialists: 3, amount: 16500, share: 0.86),
        PlanRevenue(planName: "Turista", specialists: 5, amount: 10500, share: 0.55)
    ]

    static let topSpecialists: [TopSpecialist] = [
        TopSpecialist(
            position: 1,
            name: "Juanita Cruz",
            specialty: "Uñas",
            imageURL: AuraImages.juanita,
            appointments: 38,
            revenue: 17100,
            share: 1.0
        ),
        TopSpecialist(
            position: 2,
            name: "Renata Fuentes",
            specialty: "Pestañas / cejas",
            imageURL: AuraImages.renata,
            appointments: 31,
            revenue: 21700,
            share: 0.82
        ),
        TopSpecialist(
            position: 3,
            name: "Camila Ortiz",
            specialty: "Uñas acrílicas",
            imageURL: AuraImages.camila,
            appointments: 24,
            revenue: 20400,
            share: 0.63
        )
    ]

    static let pendingCharges: [PendingCharge] = [
        PendingCharge(
            id: "pc1",
            specialistName: "Camila Ortiz",
            planName: "Turista",
            amount: 2100,
            dueLabel: "Venció hace 4 días",
            overdue: true
        ),
        PendingCharge(
            id: "pc2",
            specialistName: "Mariana Ríos",
            planName: "Residente",
            amount: 3200,
            dueLabel: "Venció ayer",
            overdue: true
        ),
        PendingCharge(
            id: "pc3",
            specialistName: "Valeria Núñez",
            planName: "Turista",
            amount: 2100,
            dueLabel: "Vence en 3 días",
            overdue: false
        )
    ]

    static let overdueTotal: Int = pendingCharges.filter(\.overdue).reduce(0) { $0 + $1.amount }

    static let pendingTotal: Int = pendingCharges.reduce(0) { $0 + $1.amount }

    // MARK: - Notices

    static let specialistNotifications: [AppNotification] = [
        AppNotification(
            id: "n1",
            kind: .appointment,
            title: "Lucía Gómez confirmó su cita",
            body: "Manicure gel hoy a las 10:00 am en Mesa de uñas 2.",
            timeAgo: "Hace 8 min",
            unread: true
        ),
        AppNotification(
            id: "n2",
            kind: .payment,
            title: "Tu membresía de septiembre está por vencer",
            body: "Residente · $3,200. Puedes pagarla desde la pestaña de Pagos.",
            timeAgo: "Hace 2 h",
            unread: true
        ),
        AppNotification(
            id: "n3",
            kind: .studio,
            title: "Mesa de uñas 1 se liberó",
            body: "Quedó libre a partir de las 6:30 pm por si quieres reservarla.",
            timeAgo: "Hace 5 h"
        ),
        AppNotification(
            id: "n4",
            kind: .contract,
            title: "Tu contrato quedó firmado",
            body: "Folio AD-2026-0087. Puedes consultarlo desde tu perfil.",
            timeAgo: "Ayer"
        )
    ]

    static let clientNotifications: [AppNotification] = [
        AppNotification(
            id: "cn1",
            kind: .appointment,
            title: "Tu cita está confirmada",
            body: "Manicure gel con Juanita Cruz, hoy a las 10:00 am.",
            timeAgo: "Hace 15 min",
            unread: true
        ),
        AppNotification(
            id: "cn2",
            kind: .studio,
            title: "Cómo llegar a The Aura Den",
            body: "\(AuraCopy.addressLine1), \(AuraCopy.addressLine2). Toca el timbre del piso 2.",
            timeAgo: "Hace 1 h",
            unread: true
        ),
        AppNotification(
            id: "cn3",
            kind: .payment,
            title: "Tu pago se aplicó correctamente",
            body: "Recibimos $450 por tu último servicio. Ya puedes descargar tu comprobante.",
            timeAgo: "Hace 3 días"
        )
    ]

    static let receptionNotifications: [AppNotification] = [
        AppNotification(
            id: "rn1",
            kind: .appointment,
            title: "Lucía Gómez llegó al estudio",
            body: "Está en espera para Manicure gel con Juanita Cruz.",
            timeAgo: "Hace 4 min",
            unread: true
        ),
        AppNotification(
            id: "rn2",
            kind: .studio,
            title: "Walk-in registrada",
            body: "Valeria Núñez fue asignada a Mesa de uñas 1 con Camila Ortiz.",
            timeAgo: "Hace 40 min"
        )
    ]

    static let adminNotifications: [AppNotification] = [
        AppNotification(
            id: "an1",
            kind: .payment,
            title: "2 membresías vencidas",
            body: "Camila Ortiz y Mariana Ríos suman $5,300 por cobrar.",
            timeAgo: "Hace 30 min",
            unread: true
        ),
        AppNotification(
            id: "an2",
            kind: .studio,
            title: "Ocupación al 78%",
            body: "3 de 4 estaciones están en uso en este momento.",
            timeAgo: "Hace 1 h",
            unread: true
        ),
        AppNotification(
            id: "an3",
            kind: .contract,
            title: "Nueva rentista dada de alta",
            body: "Camila Ortiz completó su registro con el plan Turista.",
            timeAgo: "Ayer"
        )
    ]

    static func notifications(for role: UserRole) -> [AppNotification] {
        switch role {
        case .specialist: specialistNotifications
        case .client: clientNotifications
        case .reception: receptionNotifications
        case .admin: adminNotifications
        }
    }

    static let todayKpis: [KpiTile] = [
        KpiTile(value: "78%", label: "Ocupación"),
        KpiTile(value: "$8,400", label: "Ingresos del día"),
        KpiTile(value: "12", label: "Citas totales"),
        KpiTile(value: "6", label: "Especialistas activas")
    ]

    static func plan(id: String) -> MembershipPlan {
        plans.first { $0.id == id } ?? plans.first { $0.recommended } ?? plans[1]
    }

    static func station(id: String) -> Station {
        stations.first { $0.id == id } ?? stations[0]
    }

    static func specialist(id: String) -> SpecialistProfile {
        specialists.first { $0.id == id } ?? currentSpecialist
    }

    static func service(named name: String) -> ClientService {
        services.first { $0.name == name } ?? services[0]
    }

    static func service(id: String) -> ClientService {
        services.first { $0.id == id } ?? services[0]
    }

    static func day(id: String) -> DaySlot {
        weekDays.first { $0.id == id } ?? weekDays[1]
    }
}
