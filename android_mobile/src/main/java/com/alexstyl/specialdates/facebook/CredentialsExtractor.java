package com.alexstyl.specialdates.facebook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CredentialsExtractor {

    private static final Pattern BIRTHDAY_PATTERN = Pattern.compile("(www.facebook.com/ical/b.php\\?uid=\\w+&amp;key=.+?(?=\"))");
    private static final String UID = "uid=";
    private static final int UID_LENGTH = UID.length();
    private static final String KEY = "key=";
    private static final int KEY_LENGTH = KEY.length();

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

        long userID = Long.valueOf(calendarURL.substring(indexOfUserID + UID_LENGTH, indexOfEnd));
        String key = calendarURL.substring(indexOfKey + KEY_LENGTH);

        return new UserCredentials(userID, key, name);

    }

    String obtainName(String pageSource) {
        try {
            String[] splits = pageSource.split("data-testid=\"blue_bar_profile_link\">");
            return splits[1].substring(splits[1].indexOf("span") + 5, splits[1].indexOf("/span") - 1);
        } catch (Exception e) {
            return "Facebook-User";
        }
    }

}
