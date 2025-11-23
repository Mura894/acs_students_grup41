package basenostates;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Schedule {
  private final LocalDateTime fromDate;
  private final LocalDateTime toDate;
  private final LocalTime fromHour;
  private final LocalTime toHour;
  private final DayOfWeek fromDayOfWeek;
  private final DayOfWeek toDayOfWeek;

  public Schedule(LocalDateTime fromDate, LocalDateTime toDate,
                  LocalTime fromHour, LocalTime toHour, DayOfWeek fromDayOfWeek,
                  DayOfWeek toDayOfWeek) {
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.fromHour = fromHour;
    this.toHour = toHour;
    this.fromDayOfWeek = fromDayOfWeek;
    this.toDayOfWeek = toDayOfWeek;

    System.out.println("Schedule created: " + fromDate + " to " + toDate
            + ", Hours: " + fromHour + "-" + toHour
            + ", Days: " + fromDayOfWeek + "-" + toDayOfWeek);
  }

  public int canAccess(LocalDateTime now) {
    LocalTime horaActual = now.toLocalTime();
    DayOfWeek dia = now.getDayOfWeek();

    System.out.println("=== SCHEDULE CHECK ===");
    System.out.println("Checking schedule for: " + now);
    System.out.println("Current day: " + dia + " (value: " + dia.getValue() + ")");
    System.out.println("Allowed days: " + fromDayOfWeek + " to " + toDayOfWeek
        + " (values: " + fromDayOfWeek.getValue() + "-" + toDayOfWeek.getValue() + ")");
    System.out.println("Current time: " + horaActual);
    System.out.println("Allowed time: " + fromHour + " to " + toHour);

    // 1. Verificar fecha (rango de fechas)
    if (now.isBefore(fromDate)) {
      System.out.println("❌ Date too early: " + now + " is before " + fromDate);
      return 3; // Invalid date (too early)
    }
    if (now.isAfter(toDate)) {
      System.out.println("❌ Date too late: " + now + " is after " + toDate);
      return 3; // Invalid date (too late)
    }
    System.out.println("✅ Date check passed");

    // 2. Verificar día de la semana - LÓGICA CORREGIDA
    int diaValue = dia.getValue();
    int fromDayValue = fromDayOfWeek.getValue();
    int toDayValue = toDayOfWeek.getValue();

    boolean diaValido;

    if (fromDayValue <= toDayValue) {
      // Rango normal (ej: Lunes=1 a Viernes=5)
      diaValido = (diaValue >= fromDayValue && diaValue <= toDayValue);
      System.out.println("Normal range check: " + diaValue + " between "
              + fromDayValue + " and " + toDayValue + " = " + diaValido);
    } else {
      // Rango que cruza domingo (ej: Viernes=5 a Lunes=1)
      diaValido = (diaValue >= fromDayValue || diaValue <= toDayValue);
      System.out.println("Weekend cross check: " + diaValue + " >= " + fromDayValue + " OR "
              + diaValue + " <= " + toDayValue + " = " + diaValido);
    }

    if (!diaValido) {
      System.out.println("❌ Day not allowed: " + dia + " not between "
              + fromDayOfWeek + " and " + toDayOfWeek);
      return 4; // Invalid day of the week
    }
    System.out.println("✅ Day check passed");

    // 3. Verificar hora del día
    if (horaActual.isBefore(fromHour)) {
      System.out.println("❌ Time too early: " + horaActual + " is before " + fromHour);
      return 5; // Invalid time (too early)
    }
    if (horaActual.isAfter(toHour)) {
      System.out.println("❌ Time too late: " + horaActual + " is after " + toHour);
      return 5; // Invalid time (too late)
    }
    System.out.println("✅ Time check passed");

    System.out.println("🎉 Schedule check PASSED for: " + now);
    System.out.println("=== END SCHEDULE CHECK ===");
    return 0; // Access granted
  }


}