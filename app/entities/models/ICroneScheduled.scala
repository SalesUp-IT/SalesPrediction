package entities.models


sealed abstract class ICroneScheduled(val ord: Int)

sealed abstract class IWeekDay(override val ord: Int) extends ICroneScheduled(ord)

sealed abstract class IMonth(override val ord: Int) extends ICroneScheduled(ord)

/**
  * WEEK DAYS
  **/

case object Sun extends IWeekDay(0)

case object Mon extends IWeekDay(1)

case object Tue extends IWeekDay(2)

case object Wed extends IWeekDay(3)

case object Thu extends IWeekDay(4)

case object Fri extends IWeekDay(5)

case object Sat extends IWeekDay(6)

/**
  * MONTHS
  **/

case object Jan extends IMonth(1)

case object Feb extends IMonth(2)

case object Mar extends IMonth(3)

case object Apr extends IMonth(4)

case object May extends IMonth(5)

case object Jun extends IMonth(6)

case object Jul extends IMonth(7)

case object Aug extends IMonth(8)

case object Sep extends IMonth(9)

case object Oct extends IMonth(10)

case object Nov extends IMonth(11)

case object Dec extends IMonth(12)


