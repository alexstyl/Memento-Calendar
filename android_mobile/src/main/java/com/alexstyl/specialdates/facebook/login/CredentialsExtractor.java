package com.alexstyl.specialdates.facebook.login;

import com.alexstyl.specialdates.facebook.UserCredentials;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CredentialsExtractor {

    private static final Pattern BIRTHDAY_PATTERN = Pattern.compile("(www.facebook.com/ical/b.php\\?uid=\\w+&amp;key=.+?(?=\"))");
    private static final String UID = "uid=";
    private static final int UID_LENGTH = UID.length();
    private static final String KEY = "key=";
    private static final int KEY_LENGTH = KEY.length();
    private static final int USER_DETAILS = 1;
    private static final String OPENNING_SPAN = "<span>";
    private static final String CLOSING_SPAN = "</span>";

    UserCredentials extractCalendarURL(String pageSource) {
        Matcher matcher = BIRTHDAY_PATTERN.matcher(pageSource);
        if (matcher.find()) {
            String url = matcher
                    .group(1)
                    .replace("&amp;", "&");
            String name = obtainName(pageSource);
            return createFrom(url, name);
        } else {
            return UserCredentials.ANNONYMOUS;
        }
    }

    static UserCredentials createFrom(String calendarURL, String name) {
        int indexOfKey = calendarURL.indexOf(KEY);
        int indexOfUserID = calendarURL.indexOf(UID);
        int indexOfEnd = calendarURL.indexOf("&", indexOfUserID);

        long userID = Long.parseLong(calendarURL.substring(indexOfUserID + UID_LENGTH, indexOfEnd));
        String key = calendarURL.substring(indexOfKey + KEY_LENGTH);

        return new UserCredentials(userID, key, name);

    }

    private String obtainName(String pageSource) {
        try {
            String userDetails = pageSource.split("data-testid=\"blue_bar_profile_link\">")[USER_DETAILS];
            int startOfName = userDetails.indexOf(OPENNING_SPAN) + OPENNING_SPAN.length();
            int endOfName = userDetails.indexOf(CLOSING_SPAN);
            return userDetails.substring(startOfName, endOfName);
        } catch (Exception e) {
            return "";
        }
    }

}
