package fr.melaine_gerard.melenbot.utils;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateHelper {

    public static String formatDateTime(OffsetDateTime dateTime) {

        return String.format("Le %s %d %s %d à %d:%d", dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getDayOfMonth(), dateTime.getMonth().getDisplayName(TextStyle.FULL, new Locale("FR", "fr")), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute());
    }
}
