package com.rork.theauraden.data

/** Generated brand imagery used across the demo. */
object AuraImages {
    const val STATION_NAILS_2 =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/e206f3d9-f8c9-42be-8e4f-60ccb339d73b.png"
    const val STATION_NAILS_1 =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/6ee43916-14e1-4ee9-a1f6-edbbefec1e66.png"
    const val STATION_LASHES =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/0ea01544-244d-437d-96a1-1b7498a39e60.png"
    const val JUANITA =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/bfc828fe-f1b2-46fb-a888-b80e0189af58.png"
    const val RENATA =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/5d7cb0e7-d501-4b4c-9597-3deb1486a380.png"
    const val CAMILA =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/1c1e2596-a6c0-4144-998f-7cd070b83420.png"
    const val STUDIO =
        "https://r2-pub.rork.com/projects/hik0iv6rdfhnc5wzj5h1i/assets/af8c7c99-618c-425e-9124-1ecdfa7b6347.png"
}

/** Static copy shared across screens. */
object AuraCopy {
    const val BRAND_NAME = "The Aura Den"
    const val TAGLINE = "Beauty coworking · CDMX"
    const val ADDRESS_LINE_1 = "Cuauhtémoc #1473, Piso 2"
    const val ADDRESS_LINE_2 = "Benito Juárez, CDMX"
    const val NEIGHBORHOOD = "Benito Juárez"
    const val OPENING_HOURS = "Lun a sáb · 9:00 am – 8:00 pm"
    const val TODAY_LABEL = "Martes 18 de agosto"
    const val CURRENT_USER = "Juanita Cruz"
    const val CLIENT_USER = "Lucía Gómez"
    const val LEGAL_UPDATED = "Última actualización: 1 de agosto de 2025"
    const val CONTRACT_DATE = "18 de agosto de 2026"
}

object DemoData {

    val weekDays: List<DaySlot> = listOf(
        DaySlot("mon", "Lun", "17", "Lunes 17 de agosto"),
        DaySlot("tue", "Mar", "18", "Martes 18 de agosto"),
        DaySlot("wed", "Mié", "19", "Miércoles 19 de agosto"),
        DaySlot("thu", "Jue", "20", "Jueves 20 de agosto"),
        DaySlot("fri", "Vie", "21", "Viernes 21 de agosto"),
        DaySlot("sat", "Sáb", "22", "Sábado 22 de agosto"),
        DaySlot("sun", "Dom", "23", "Domingo 23 de agosto")
    )

    val timeSlots: List<String> = listOf("10:00 am", "12:30 pm", "2:00 pm", "4:00 pm", "6:30 pm")

    val specialties: List<String> = listOf("Uñas", "Pestañas / cejas", "Maquillaje", "Podología")

    val services: List<ClientService> = listOf(
        ClientService("s1", "Manicure gel", "1 h 15 min", 450),
        ClientService("s2", "Pedicure spa", "1 h 30 min", 620),
        ClientService("s3", "Uñas acrílicas", "2 h", 850),
        ClientService("s4", "Pestañas / cejas", "1 h", 700)
    )

    val stations: List<Station> = listOf(
        Station(
            id = "st2",
            name = "Mesa de uñas 2",
            kind = "Mesa de uñas",
            status = StationStatus.AVAILABLE,
            scheduleLabel = "10:00 am – 2:00 pm",
            amenities = listOf("Luz natural", "Lámpara profesional", "Toma corriente"),
            description = "Tu lugar para crear con calma. La estación junto a la ventana, " +
                "ideal para manicure y uñas acrílicas.",
            imageUrl = AuraImages.STATION_NAILS_2,
            hourlyRate = 120
        ),
        Station(
            id = "st1",
            name = "Mesa de uñas 1",
            kind = "Mesa de uñas",
            status = StationStatus.OCCUPIED,
            scheduleLabel = "Ocupada hasta las 6:00 pm",
            amenities = listOf("Lámpara profesional", "Extractor de polvo"),
            description = "Estación central con extractor de polvo integrado y buena " +
                "circulación, cómoda para jornadas largas.",
            imageUrl = AuraImages.STATION_NAILS_1,
            occupiedBy = "Camila Ortiz",
            nextAvailability = "Próxima disponibilidad: 6:30 pm",
            hourlyRate = 120
        ),
        Station(
            id = "st3",
            name = "Mesa de uñas 3",
            kind = "Mesa de uñas",
            status = StationStatus.OCCUPIED,
            scheduleLabel = "Ocupada hasta las 2:00 pm",
            amenities = listOf("Luz natural", "Locker cercano"),
            description = "Estación amplia al fondo del estudio, con locker a un paso y " +
                "espacio extra para materiales.",
            imageUrl = AuraImages.STATION_NAILS_1,
            occupiedBy = "Mariana Ríos",
            nextAvailability = "Próxima disponibilidad: 2:30 pm",
            hourlyRate = 120
        ),
        Station(
            id = "st4",
            name = "Mesa de pestañas 1",
            kind = "Pestañas",
            status = StationStatus.AVAILABLE,
            scheduleLabel = "Disponible todo el día",
            amenities = listOf("Camilla acojinada", "Aro de luz", "Cortina de privacidad"),
            description = "Cabina serena con camilla acojinada y cortina de privacidad para " +
                "servicios de pestañas y cejas.",
            imageUrl = AuraImages.STATION_LASHES,
            hourlyRate = 140
        )
    )

    val plans: List<MembershipPlan> = listOf(
        MembershipPlan(
            id = "turista",
            name = "Turista",
            price = 2100,
            hours = 32,
            locker = "Locker incluido",
            perks = listOf("32 horas totales", "Locker incluido", "Reserva con 24h de anticipación")
        ),
        MembershipPlan(
            id = "residente",
            name = "Residente",
            price = 3200,
            hours = 64,
            locker = "Locker incluido",
            perks = listOf("64 horas totales", "Locker incluido", "Kit de bienvenida"),
            recommended = true
        ),
        MembershipPlan(
            id = "anchor",
            name = "Anchor",
            price = 5500,
            hours = 128,
            locker = "Locker grande",
            perks = listOf(
                "128 horas totales",
                "Locker grande",
                "Prioridad de reserva 48h antes"
            )
        )
    )

    val paymentMethods: List<PaymentMethodOption> = listOf(
        PaymentMethodOption("card", "Tarjeta guardada", "Visa terminación 4242", true),
        PaymentMethodOption("spei", "Transferencia SPEI", "Se confirma en 1 día hábil", false),
        PaymentMethodOption("mercadopago", "Mercado Pago", "Saldo o tarjeta asociada", true)
    )

    val appointments: List<Appointment> = listOf(
        Appointment(
            id = "a1",
            clientName = "Lucía Gómez",
            specialistName = "Juanita Cruz",
            service = "Manicure gel",
            dayId = "tue",
            dateLabel = "Martes 18 de agosto",
            time = "10:00 am",
            stationName = "Mesa de uñas 2",
            status = AppointmentStatus.CONFIRMED,
            notes = "Diseño francés en tonos blanco y dorado",
            price = 450
        ),
        Appointment(
            id = "a2",
            clientName = "Mariana López",
            specialistName = "Juanita Cruz",
            service = "Pedicure spa",
            dayId = "tue",
            dateLabel = "Martes 18 de agosto",
            time = "12:30 pm",
            stationName = "Mesa de uñas 2",
            status = AppointmentStatus.CONFIRMED,
            notes = "Prefiere tono nude rosado",
            price = 620
        ),
        Appointment(
            id = "a3",
            clientName = "Sofía Ramírez",
            specialistName = "Juanita Cruz",
            service = "Uñas acrílicas",
            dayId = "tue",
            dateLabel = "Martes 18 de agosto",
            time = "4:00 pm",
            stationName = "Mesa de uñas 3",
            status = AppointmentStatus.CONFIRMED,
            notes = null,
            price = 850
        ),
        Appointment(
            id = "a4",
            clientName = "Ana Torres",
            specialistName = "Juanita Cruz",
            service = "Manicure gel",
            dayId = "wed",
            dateLabel = "Miércoles 19 de agosto",
            time = "11:00 am",
            stationName = "Mesa de uñas 2",
            status = AppointmentStatus.CONFIRMED,
            notes = "Trae inspiración en su celular",
            price = 450
        ),
        Appointment(
            id = "a5",
            clientName = "Lucía Gómez",
            specialistName = "Juanita Cruz",
            service = "Pedicure spa",
            dayId = "wed",
            dateLabel = "Miércoles 19 de agosto",
            time = "3:00 pm",
            stationName = "Mesa de uñas 2",
            status = AppointmentStatus.CONFIRMED,
            notes = null,
            price = 620
        ),
        Appointment(
            id = "a6",
            clientName = "Mariana López",
            specialistName = "Juanita Cruz",
            service = "Uñas acrílicas",
            dayId = "thu",
            dateLabel = "Jueves 20 de agosto",
            time = "10:00 am",
            stationName = "Mesa de uñas 3",
            status = AppointmentStatus.CONFIRMED,
            notes = null,
            price = 850
        ),
        Appointment(
            id = "a7",
            clientName = "Sofía Ramírez",
            specialistName = "Juanita Cruz",
            service = "Manicure gel",
            dayId = "fri",
            dateLabel = "Viernes 21 de agosto",
            time = "12:30 pm",
            stationName = "Mesa de uñas 2",
            status = AppointmentStatus.CONFIRMED,
            notes = "Quiere probar tono vino",
            price = 450
        ),
        Appointment(
            id = "a8",
            clientName = "Ana Torres",
            specialistName = "Juanita Cruz",
            service = "Pestañas / cejas",
            dayId = "sat",
            dateLabel = "Sábado 22 de agosto",
            time = "11:30 am",
            stationName = "Mesa de pestañas 1",
            status = AppointmentStatus.CONFIRMED,
            notes = null,
            price = 700
        )
    )

    /** Lucía Gómez is the demo client. */
    val clientHistory: List<Appointment> = listOf(
        Appointment(
            id = "c1",
            clientName = "Lucía Gómez",
            specialistName = "Juanita Cruz",
            service = "Manicure gel",
            dayId = "past",
            dateLabel = "1 de agosto",
            time = "10:00 am",
            stationName = "Mesa de uñas 2",
            status = AppointmentStatus.COMPLETED,
            notes = null,
            price = 450
        ),
        Appointment(
            id = "c2",
            clientName = "Lucía Gómez",
            specialistName = "Camila Ortiz",
            service = "Uñas acrílicas",
            dayId = "past",
            dateLabel = "12 de julio",
            time = "1:00 pm",
            stationName = "Mesa de uñas 1",
            status = AppointmentStatus.COMPLETED,
            notes = null,
            price = 850
        ),
        Appointment(
            id = "c3",
            clientName = "Lucía Gómez",
            specialistName = "Renata Fuentes",
            service = "Pestañas / cejas",
            dayId = "past",
            dateLabel = "28 de junio",
            time = "5:00 pm",
            stationName = "Mesa de pestañas 1",
            status = AppointmentStatus.CANCELLED,
            notes = null,
            price = 700
        )
    )

    val checkIns: List<CheckIn> = listOf(
        CheckIn("k1", "Lucía Gómez", "Juanita Cruz", "10:00 am", "Manicure gel", CheckInStatus.WAITING),
        CheckIn("k2", "Mariana López", "Juanita Cruz", "12:30 pm", "Pedicure spa", CheckInStatus.PENDING),
        CheckIn("k3", "Valeria Núñez", "Camila Ortiz", "2:00 pm", "Uñas acrílicas", CheckInStatus.ATTENDED),
        CheckIn("k4", "Sofía Ramírez", "Juanita Cruz", "4:00 pm", "Uñas acrílicas", CheckInStatus.PENDING),
        CheckIn("k5", "Ana Torres", "Renata Fuentes", "6:30 pm", "Pestañas / cejas", CheckInStatus.PENDING)
    )

    val paymentHistory: List<PaymentRecord> = listOf(
        PaymentRecord(
            id = "p0",
            folio = "AD-2025-0918",
            concept = "Membresía Residente · septiembre",
            dateLabel = "15 de septiembre",
            amount = 3200,
            status = PaymentStatus.PENDING,
            method = "Transferencia SPEI",
            kind = PaymentKind.MEMBERSHIP,
            payerName = "Juanita Cruz"
        ),
        PaymentRecord(
            id = "p1",
            folio = "AD-2025-0815",
            concept = "Membresía Residente · agosto",
            dateLabel = "15 de agosto",
            amount = 3200,
            status = PaymentStatus.PAID,
            method = "Tarjeta guardada",
            kind = PaymentKind.MEMBERSHIP,
            invoiceRequested = true,
            payerName = "Juanita Cruz"
        ),
        PaymentRecord(
            id = "p2",
            folio = "AD-2025-0812",
            concept = "Membresía Residente · agosto",
            dateLabel = "12 de agosto",
            amount = 3200,
            status = PaymentStatus.FAILED,
            method = "Tarjeta guardada",
            kind = PaymentKind.MEMBERSHIP,
            payerName = "Juanita Cruz"
        ),
        PaymentRecord(
            id = "p3",
            folio = "AD-2025-0715",
            concept = "Membresía Residente · julio",
            dateLabel = "15 de julio",
            amount = 3200,
            status = PaymentStatus.PAID,
            method = "Mercado Pago",
            kind = PaymentKind.MEMBERSHIP,
            payerName = "Juanita Cruz"
        ),
        PaymentRecord(
            id = "p4",
            folio = "AD-2025-0615",
            concept = "Membresía Turista · junio",
            dateLabel = "15 de junio",
            amount = 2100,
            status = PaymentStatus.PAID,
            method = "Tarjeta guardada",
            kind = PaymentKind.MEMBERSHIP,
            payerName = "Juanita Cruz"
        )
    )

    val reviews: List<Review> = listOf(
        Review("r1", "Lucía Gómez", 5, "Manicure impecable y trato increíble", "Hace 3 días"),
        Review("r2", "Mariana López", 5, "Súper puntual y el diseño quedó tal cual lo pedí", "Hace 1 semana"),
        Review("r3", "Ana Torres", 4, "Muy buen ambiente en el estudio, volveré", "Hace 2 semanas")
    )

    val currentSpecialist = SpecialistProfile(
        id = "sp1",
        name = "Juanita Cruz",
        specialty = "Uñas",
        phone = "+52 55 1234 5678",
        email = "juanita@correo.com",
        since = "Especialista desde 2024",
        rating = 4.8,
        reviewCount = 36,
        imageUrl = AuraImages.JUANITA,
        planId = "residente",
        hoursUsed = 42,
        paymentStatus = PaymentStatus.PAID,
        contractSigned = true,
        contractFolio = "AD-2026-0087",
        contractDateLabel = "14 de enero de 2026"
    )

    /** Contract already on file for the demo specialist who signs in. */
    val signedContract = SignedContract(
        folio = "AD-2026-0087",
        signerName = AuraCopy.CURRENT_USER,
        dateLabel = "14 de enero de 2026",
        timeLabel = "9:12 h"
    )

    /** Folio assigned to a contract signed during the demo. */
    const val NEW_CONTRACT_FOLIO = "AD-2026-0142"

    val specialists: List<SpecialistProfile> = listOf(
        currentSpecialist.copy(nextServiceLabel = "Disponible hoy a las 2:00 pm"),
        SpecialistProfile(
            id = "sp2",
            name = "Renata Fuentes",
            specialty = "Pestañas / cejas",
            phone = "+52 55 4455 8899",
            email = "renata@correo.com",
            since = "Especialista desde 2023",
            rating = 4.9,
            reviewCount = 52,
            imageUrl = AuraImages.RENATA,
            planId = "anchor",
            hoursUsed = 96,
            paymentStatus = PaymentStatus.PAID,
            nextServiceLabel = "Disponible hoy a las 5:00 pm",
            contractSigned = true,
            contractFolio = "AD-2026-0031",
            contractDateLabel = "3 de marzo de 2026"
        ),
        SpecialistProfile(
            id = "sp3",
            name = "Camila Ortiz",
            specialty = "Uñas acrílicas",
            phone = "+52 55 7788 1122",
            email = "camila@correo.com",
            since = "Especialista desde 2025",
            rating = 4.7,
            reviewCount = 18,
            imageUrl = AuraImages.CAMILA,
            planId = "turista",
            hoursUsed = 28,
            paymentStatus = PaymentStatus.PENDING,
            nextServiceLabel = "Disponible mañana a las 11:00 am",
            contractSigned = false
        )
    )

    val monthMetrics: List<ReportMetric> = listOf(
        ReportMetric("Ingresos del mes", "$186,400", "+12% vs julio", true),
        ReportMetric("Ocupación promedio", "78%", "+6 pts vs julio", true),
        ReportMetric("Especialistas activas", "14", "+2 nuevas rentistas", true),
        ReportMetric("Citas del mes", "312", "-4% vs julio", false)
    )

    val serviceDemand: List<ServiceDemand> = listOf(
        ServiceDemand("Manicure gel", 128, 1f),
        ServiceDemand("Uñas acrílicas", 86, 0.67f),
        ServiceDemand("Pedicure spa", 61, 0.48f),
        ServiceDemand("Pestañas / cejas", 37, 0.29f)
    )

    val revenueByMonth: List<MonthRevenue> = listOf(
        MonthRevenue("Mar", 98400, 0.53f),
        MonthRevenue("Abr", 112300, 0.60f),
        MonthRevenue("May", 128000, 0.69f),
        MonthRevenue("Jun", 142500, 0.76f),
        MonthRevenue("Jul", 166200, 0.89f),
        MonthRevenue("Ago", 186400, 1f)
    )

    /** Recurring monthly rent split by membership tier. 5 + 6 + 3 = 14 especialistas. */
    val revenueByPlan: List<PlanRevenue> = listOf(
        PlanRevenue("Residente", 6, 19200, 1f),
        PlanRevenue("Anchor", 3, 16500, 0.86f),
        PlanRevenue("Turista", 5, 10500, 0.55f)
    )

    val topSpecialists: List<TopSpecialist> = listOf(
        TopSpecialist(1, "Juanita Cruz", "Uñas", AuraImages.JUANITA, 38, 17100, 1f),
        TopSpecialist(2, "Renata Fuentes", "Pestañas / cejas", AuraImages.RENATA, 31, 21700, 0.82f),
        TopSpecialist(3, "Camila Ortiz", "Uñas acrílicas", AuraImages.CAMILA, 24, 20400, 0.63f)
    )

    val pendingCharges: List<PendingCharge> = listOf(
        PendingCharge("pc1", "Camila Ortiz", "Turista", 2100, "Venció hace 4 días", true),
        PendingCharge("pc2", "Mariana Ríos", "Residente", 3200, "Venció ayer", true),
        PendingCharge("pc3", "Valeria Núñez", "Turista", 2100, "Vence en 3 días", false)
    )

    val overdueTotal: Int = pendingCharges.filter { it.overdue }.sumOf { it.amount }

    val pendingTotal: Int = pendingCharges.sumOf { it.amount }

    /** Notices shown in the bell centre, per role. */
    val specialistNotifications: List<AppNotification> = listOf(
        AppNotification(
            "n1",
            NotificationKind.APPOINTMENT,
            "Lucía Gómez confirmó su cita",
            "Manicure gel hoy a las 10:00 am en Mesa de uñas 2.",
            "Hace 8 min",
            unread = true
        ),
        AppNotification(
            "n2",
            NotificationKind.PAYMENT,
            "Tu membresía de septiembre está por vencer",
            "Residente · $3,200. Puedes pagarla desde la pestaña de Pagos.",
            "Hace 2 h",
            unread = true
        ),
        AppNotification(
            "n3",
            NotificationKind.STUDIO,
            "Mesa de uñas 1 se liberó",
            "Quedó libre a partir de las 6:30 pm por si quieres reservarla.",
            "Hace 5 h"
        ),
        AppNotification(
            "n4",
            NotificationKind.CONTRACT,
            "Tu contrato quedó firmado",
            "Folio AD-2026-0087. Puedes consultarlo desde tu perfil.",
            "Ayer"
        )
    )

    val clientNotifications: List<AppNotification> = listOf(
        AppNotification(
            "cn1",
            NotificationKind.APPOINTMENT,
            "Tu cita está confirmada",
            "Manicure gel con Juanita Cruz, hoy a las 10:00 am.",
            "Hace 15 min",
            unread = true
        ),
        AppNotification(
            "cn2",
            NotificationKind.STUDIO,
            "Cómo llegar a The Aura Den",
            "${AuraCopy.ADDRESS_LINE_1}, ${AuraCopy.ADDRESS_LINE_2}. Toca el timbre del piso 2.",
            "Hace 1 h",
            unread = true
        ),
        AppNotification(
            "cn3",
            NotificationKind.PAYMENT,
            "Tu pago se aplicó correctamente",
            "Recibimos $450 por tu último servicio. Ya puedes descargar tu comprobante.",
            "Hace 3 días"
        )
    )

    val receptionNotifications: List<AppNotification> = listOf(
        AppNotification(
            "rn1",
            NotificationKind.APPOINTMENT,
            "Lucía Gómez llegó al estudio",
            "Está en espera para Manicure gel con Juanita Cruz.",
            "Hace 4 min",
            unread = true
        ),
        AppNotification(
            "rn2",
            NotificationKind.STUDIO,
            "Walk-in registrada",
            "Valeria Núñez fue asignada a Mesa de uñas 1 con Camila Ortiz.",
            "Hace 40 min"
        )
    )

    val adminNotifications: List<AppNotification> = listOf(
        AppNotification(
            "an1",
            NotificationKind.PAYMENT,
            "2 membresías vencidas",
            "Camila Ortiz y Mariana Ríos suman $5,300 por cobrar.",
            "Hace 30 min",
            unread = true
        ),
        AppNotification(
            "an2",
            NotificationKind.STUDIO,
            "Ocupación al 78%",
            "3 de 4 estaciones están en uso en este momento.",
            "Hace 1 h",
            unread = true
        ),
        AppNotification(
            "an3",
            NotificationKind.CONTRACT,
            "Nueva rentista dada de alta",
            "Camila Ortiz completó su registro con el plan Turista.",
            "Ayer"
        )
    )

    fun notificationsForRole(role: UserRole): List<AppNotification> = when (role) {
        UserRole.SPECIALIST -> specialistNotifications
        UserRole.CLIENT -> clientNotifications
        UserRole.RECEPTION -> receptionNotifications
        UserRole.ADMIN -> adminNotifications
    }

    val todayKpis: List<Pair<String, String>> = listOf(
        "78%" to "Ocupación",
        "$8,400" to "Ingresos del día",
        "12" to "Citas totales",
        "6" to "Especialistas activas"
    )

    fun planById(id: String): MembershipPlan =
        plans.firstOrNull { it.id == id } ?: plans.first { it.recommended }

    fun stationById(id: String): Station =
        stations.firstOrNull { it.id == id } ?: stations.first()

    fun specialistById(id: String): SpecialistProfile =
        specialists.firstOrNull { it.id == id } ?: currentSpecialist

    fun serviceByName(name: String): ClientService =
        services.firstOrNull { it.name == name } ?: services.first()
}
