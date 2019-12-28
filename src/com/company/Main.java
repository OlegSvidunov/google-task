package com.company;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String[][] firstPersonSchedule = {{"9:00", "10:30"}, {"12:00", "13:00"}, {"16:00", "18:00"}};
    public static final String[][] firstPersonTotalWorkHours = {{"9:00", "20:00"}};
    public static final String[][] secondPersonSchedule = {{"10:00", "11:30"}, {"12:30", "14:30"}, {"14:30", "15:00"}, {"16:00", "17:00"}};
    public static final String[][] secondPersonTotalWorkHours = {{"10:00", "18:30"}};
    public static final Integer TIME_DELIMITER = 30;

    public static void main(String[] args) {
        List<Period> firstPersonBusyPeriods = getTimePeriodsListFromStringsArray(firstPersonSchedule);
        Period totalFirstPersonWorkingPeriod = getPeriod(firstPersonTotalWorkHours[0][0], firstPersonTotalWorkHours[0][1]);
        List<Period> firstPersonFreePeriods = getFreePeriodsDuringTheDay(firstPersonBusyPeriods, totalFirstPersonWorkingPeriod);


        List<Period> secondPersonBusyPeriods = getTimePeriodsListFromStringsArray(secondPersonSchedule);
        Period totalSecondPersonWorkingPeriod = getPeriod(secondPersonTotalWorkHours[0][0], secondPersonTotalWorkHours[0][1]);
        List<Period> secondPersonFreePeriods = getFreePeriodsDuringTheDay(secondPersonBusyPeriods, totalSecondPersonWorkingPeriod);

        firstPersonFreePeriods.retainAll(secondPersonFreePeriods);
        groupPeriods(firstPersonFreePeriods);

        firstPersonFreePeriods.forEach(System.out::println);
    }

    public static void groupPeriods(List<Period> periods) {
        while (isListNeedToBeGrouped(periods)) {
            for (int i = 0; i < periods.size() - 1; i++) {
                if (periods.get(i).to.equals(periods.get(i + 1).from)) {
                    periods.get(i).to = periods.get(i + 1).to;
                    periods.remove(i + 1);
                }
            }
        }
    }

    private static boolean isListNeedToBeGrouped(List<Period> periods) {
        for (int i = 0; i < periods.size() - 1; i++) {
            if (periods.get(i).to.equals(periods.get(i + 1).from)) {
                return true;
            }
        }
        return false;
    }

    public static List<Period> getFreePeriodsDuringTheDay(List<Period> busyPeriods, Period totalWorkingPeriod) {
        List<Period> freePeriods = new ArrayList<>();
        LocalTime i = totalWorkingPeriod.from;
        while (i.isBefore(totalWorkingPeriod.to)) {
            Period p = new Period(i, i.plus(TIME_DELIMITER, ChronoUnit.MINUTES));
            if (!isTimePeriodBusy(busyPeriods, p)) {
                freePeriods.add(p);
            }
            i = i.plus(TIME_DELIMITER, ChronoUnit.MINUTES);
        }
        return freePeriods;
    }

    public static boolean isTimePeriodBusy(List<Period> list, Period current) {
        for (Period p : list) {
            if (p.doesIncludeAnotherPeriod(current)) {
                return true;
            }
        }
        return false;
    }

    public static List<Period> getTimePeriodsListFromStringsArray(String[][] schedule) {
        List<Period> list = new ArrayList<>();
        for (String[] arr : schedule) {
            list.add(getPeriod(arr[0], arr[1]));
        }
        return list;
    }

    public static Period getPeriod(String timeFrom, String timeTo) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm");
        return new Period(
                LocalTime.parse(timeFrom, dtf),
                LocalTime.parse(timeTo, dtf)
        );
    }
}


