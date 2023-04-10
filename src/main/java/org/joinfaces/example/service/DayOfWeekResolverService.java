package org.joinfaces.example.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
public class DayOfWeekResolverService {

    public String determineDayOfWeek(final int year, final int month, final int dayOfMonth) {
        LocalDate resolvedLocalDate = LocalDate.of(year, month, dayOfMonth);
        return convertToDayOfWeekName(resolvedLocalDate.getDayOfWeek());
    }

    private String convertToDayOfWeekName(final DayOfWeek dayOfWeek) {
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }
}
