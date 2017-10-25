package com.xebialabs.xldeploy.stresstests.runner.chain.reporting

import java.time.{LocalDate, LocalDateTime, ZoneId, ZonedDateTime}

object Dates {

  def now: ZonedDateTime =
    LocalDateTime.now().atZone(ZoneId.of("UTC"))

  def startOfMonth: ZonedDateTime =
    LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.of("UTC"))

  def startOfHalfYearAgo: ZonedDateTime =
    LocalDate.now().minusMonths(7).withDayOfMonth(1).atStartOfDay(ZoneId.of("UTC"))

  def endOfLastMonth: ZonedDateTime =
    LocalDate.now().withDayOfMonth(1).minusDays(1).atTime(23, 59, 59).atZone(ZoneId.of("UTC"))

  def thirtyDaysAgo: ZonedDateTime =
    LocalDate.now().minusDays(30).atStartOfDay(ZoneId.of("UTC"))

  def endOfYesterday: ZonedDateTime =
    LocalDate.now().minusDays(1).atTime(23, 59, 59).atZone(ZoneId.of("UTC"))

  def thisMonth: (ZonedDateTime, ZonedDateTime) = startOfMonth -> now

  def lastHalfYear: (ZonedDateTime, ZonedDateTime) = startOfHalfYearAgo -> endOfLastMonth

  def last30days: (ZonedDateTime, ZonedDateTime) = thirtyDaysAgo -> endOfYesterday

}
