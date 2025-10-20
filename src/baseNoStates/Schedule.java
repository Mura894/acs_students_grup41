package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

//User schedule class
public class Schedule {
  private final LocalDateTime fromDate;
  private final LocalDateTime toDate;
  private final LocalTime fromHour;
  private final LocalTime toHour;
  private final DayOfWeek toDayOfWeek;
  private final DayOfWeek fromDayOfWeek;

  public Schedule(LocalDateTime fromdate, LocalDateTime todate, LocalTime fromhour, LocalTime tohour, DayOfWeek toDayOfWeek, DayOfWeek fromDayOfWeek) {
    this.fromDate = fromdate;
    this.toDate = todate;
    this.fromHour = fromhour;
    this.toHour = tohour;
    this.toDayOfWeek = toDayOfWeek;
    this.fromDayOfWeek = fromDayOfWeek;

    System.out.println("Schedule created: " + fromdate + " to " + todate +
            ", Hours: " + fromhour + "-" + tohour +
            ", Days: " + fromDayOfWeek + "-" + toDayOfWeek);
  }

  public int canAccess(LocalDateTime now) {
    LocalTime horaActual = now.toLocalTime();
    DayOfWeek dia = now.getDayOfWeek();

    System.out.println("Checking schedule for: " + now + " (Day: " + dia + ", Time: " + horaActual + ")");

    // Check date
    if (now.isBefore(fromDate) || now.isAfter(toDate)) {
      System.out.println("Date out of range: " + now + " not between " + fromDate + " and " + toDate);
      return 3; // Invalid date
    }

    // Check day of the week
    int diaValue = dia.getValue();
    int fromDayValue = fromDayOfWeek.getValue();
    int toDayValue = toDayOfWeek.getValue();

    // Handle ranges that cross Sunday (e.g., Friday to Monday)
    boolean validDay;
    if (fromDayValue <= toDayValue) {
      // Normal range (e.g., Monday to Friday)
      validDay = (diaValue >= fromDayValue && diaValue <= toDayValue);
    } else {
      // Range that crosses Sunday (e.g., Friday to Monday)
      validDay = (diaValue >= fromDayValue || diaValue <= toDayValue);
    }

    if (!validDay) {
      System.out.println("Day not allowed: " + dia + " not between " + fromDayOfWeek + " and " + toDayOfWeek);
      return 4; // Invalid day of the week
    }

    // Check time
    if (horaActual.isBefore(fromHour) || horaActual.isAfter(toHour)) {
      System.out.println("Time not allowed: " + horaActual + " not between " + fromHour + " and " + toHour);
      return 5; // Invalid time
    }

    System.out.println("Schedule check passed for: " + now);
    return 0; // Access granted
  }

}