package com.company;

import java.time.LocalTime;
import java.util.Objects;

public class Period {
    public LocalTime from;
    public LocalTime to;

    public Period(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public boolean doesIncludeAnotherPeriod(Period another) {
        return doesIncludeTime(this, another.from) && doesIncludeTime(this, another.to);
    }

    private boolean doesIncludeTime(Period period, LocalTime time) {
        return (time.equals(period.from) || time.isAfter(period.from))
                && (time.equals(period.to) || time.isBefore(period.to));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return from.equals(period.from) &&
                to.equals(period.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "TimePeriod{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
