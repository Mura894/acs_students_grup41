package basenostates;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Schedule {
  private final LocalDateTime fromDate;
  private final LocalDateTime toDate;
  private final LocalTime fromHour;
  private final LocalTime toHour;
  private final DayOfWeek fromDayOfWeek;
  private final DayOfWeek toDayOfWeek;
  private static final Logger logger = LoggerFactory.getLogger(Schedule.class);

  // Constructor for Schedule with date, time, and day constraints
  public Schedule(LocalDateTime fromDate, LocalDateTime toDate,
                  LocalTime fromHour, LocalTime toHour, DayOfWeek fromDayOfWeek,
                  DayOfWeek toDayOfWeek) {
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.fromHour = fromHour;
    this.toHour = toHour;
    this.fromDayOfWeek = fromDayOfWeek;
    this.toDayOfWeek = toDayOfWeek;

    logger.debug("Schedule created: {} to {}, Hours: {}-{}, Days: {}-{}",
        fromDate, toDate, fromHour, toHour, fromDayOfWeek, toDayOfWeek);
  }

  // Check if access is allowed at the given date and time
  public int canAccess(LocalDateTime now) {
    LocalTime horaActual = now.toLocalTime();
    DayOfWeek dia = now.getDayOfWeek();

    logger.debug("=== SCHEDULE CHECK ===");
    logger.debug("Checking schedule for: {}", now);
    logger.debug("Current day: {} (value: {})", dia, dia.getValue());
    logger.debug("Allowed days: {} to {} (values: {}-{})",
        fromDayOfWeek, toDayOfWeek, fromDayOfWeek.getValue(), toDayOfWeek.getValue());
    logger.debug("Current time: {}", horaActual);
    logger.debug("Allowed time: {} to {}", fromHour, toHour);

    // 1. Check date range
    if (now.isBefore(fromDate)) {
      logger.warn("Date too early: {} is before {}", now, fromDate);
      return 3; // Invalid date (too early)
    }
    if (now.isAfter(toDate)) {
      logger.warn("Date too late: {} is after {}", now, toDate);
      return 3; // Invalid date (too late)
    }
    logger.debug("Date check passed");

    // 2. Check day of week - CORRECTED LOGIC
    int diaValue = dia.getValue();
    int fromDayValue = fromDayOfWeek.getValue();
    int toDayValue = toDayOfWeek.getValue();

    boolean diaValido;

    if (fromDayValue <= toDayValue) {
      // Normal range (e.g., Monday=1 to Friday=5)
      diaValido = (diaValue >= fromDayValue && diaValue <= toDayValue);
      logger.debug("Normal range check: {} between {} and {} = {}",
          diaValue, fromDayValue, toDayValue, diaValido);
    } else {
      // Range that crosses Sunday (e.g., Friday=5 to Monday=1)
      diaValido = (diaValue >= fromDayValue || diaValue <= toDayValue);
      logger.debug("Weekend cross check: {} >= {} OR {} <= {} = {}",
          diaValue, fromDayValue, diaValue, toDayValue, diaValido);
    }

    if (!diaValido) {
      logger.warn("Day not allowed: {} not between {} and {}",
          dia, fromDayOfWeek, toDayOfWeek);
      return 4; // Invalid day of the week
    }
    logger.debug("Day check passed");

    // 3. Check time of day
    if (horaActual.isBefore(fromHour)) {
      logger.warn("Time too early: {} is before {}", horaActual, fromHour);
      return 5; // Invalid time (too early)
    }
    if (horaActual.isAfter(toHour)) {
      logger.warn("Time too late: {} is after {}", horaActual, toHour);
      return 5; // Invalid time (too late)
    }
    logger.debug("Time check passed");

    logger.debug("Schedule check PASSED for: {}", now);
    logger.debug("=== END SCHEDULE CHECK ===");
    return 0; // Access granted
  }
}