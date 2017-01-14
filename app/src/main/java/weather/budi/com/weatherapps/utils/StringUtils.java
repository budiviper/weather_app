package weather.budi.com.weatherapps.utils;

import android.util.Log;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final String TAG = "StringUtils";

    private static Random r = new Random();

    public static String truncateString(int length, String name, String replaceStr) {
        if (name.length() > length)
            name = name.substring(0, length) + replaceStr;

        return name;
    }

    public static String getFileExtension(String fullFileName) {
        String result = "";
        int dot = fullFileName.lastIndexOf(".");
        result = fullFileName.substring(dot);

        return result;
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public final static boolean isValidEmail(String target) {
        if (isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String delimiterChanger(String oldDelimiter,
                                          String newDelimiter, String msgStr) {
        String result = msgStr.replace(oldDelimiter, newDelimiter);

        return result;
    }

    public static String urlEncode(String value) {
        String encodeResult = "";

        try {
            encodeResult = URLEncoder.encode(value, "utf-8");
        } catch (Exception e) {
            Log.v(TAG, "ERROR ENCODE URL UTILS: " + e.getMessage());
        }

        return encodeResult;
    }

    public static String urlDecode(String value) {
        String decodeResult = "";

        try {
            decodeResult = URLDecoder.decode(value, "utf-8");
        } catch (Exception e) {
        }

        return decodeResult;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "MMMM dd 'at' hh:mm a", Locale.US);
    private static final SimpleDateFormat sdfHour = new SimpleDateFormat(
            "hh 'hours ago'", Locale.US);
    private static final SimpleDateFormat sdfMinutes = new SimpleDateFormat(
            "mm 'minutes ago'", Locale.US);

    public static long getCurentTime() {
        return new java.util.Date().getTime();
    }

    private static final SimpleDateFormat sdfEcash = new SimpleDateFormat(
            "MMMM dd 'at' hh:mm a", Locale.US);

    public static String dateFormatEcash(long time) {
        long different = getCurentTime() - time;
        final int one_day = 24 * 60 * 60 * 1000;
        if (different > one_day * 2) {
            Date date = new Date(time);
            return sdfEcash.format(date);
        } else {
            if (different < 60 * 60 * 1000) {
                Date date = new Date(different);
                return sdfMinutes.format(date);
            } else if (different < 24 * 60 * 60 * 1000) {
                Date date = new Date(different);
                return sdfHour.format(date);
            } else {
                return "yesterday";
            }

        }
    }

    public static String adjustmentLine(String msg, int lengthAdjust,
                                        boolean isPadLeft, String delimiter) {
        String result = msg;

        int lengthWord = msg.length();
        int remainSpace = 0;

        if (lengthWord < lengthAdjust) {
            remainSpace = lengthAdjust - lengthWord;
            String space = "";

            for (int i = 0; i < remainSpace; i++) {
                space = space + delimiter;
            }

            if (isPadLeft == true) {
                result = space + result;
            } else {
                result = result + space;
            }
        }

        return result;

    }

    public static String justifyLine(String msg, int lengthAdjust,
                                     String delimiter) {
        String result = msg;

        int lengthWord = msg.length();
        int remainSpace = 0;

        if (lengthWord < lengthAdjust) {
            remainSpace = lengthAdjust - lengthWord;
            String space = "";

            int leftPad = remainSpace / 2;
            int rightPad = remainSpace - leftPad;

            for (int i = 0; i < leftPad; i++) {
                space = space + delimiter;
            }

            result = space + result;

            for (int i = 0; i < rightPad; i++) {
                space = space + delimiter;
            }

            result = result + space;

        }

        return result;

    }

    public static boolean regexPasswordChecker(String regex, String password) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static String formatCurrency(String value, String format) {
        double convert = Double.parseDouble(value);

        NumberFormat formatter = new DecimalFormat(format);
        String resultFormat = formatter.format(convert);

        return resultFormat;
    }

    public static String generateAlphaNumeric(int length) {
        String C = "QWERTYUIOPLKJHGFDAZXCVBNM0987654321";
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            int idx = r.nextInt(C.length());
            sb.append(C.substring(idx, idx + 1));
        }
        return sb.toString();
    }

    public static String md5(String input) {

        String md5 = null;

        if (null == input)
            return null;

        try {

            // Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            // Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
        return md5;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static String convertEpoch(long time, String formatDate){
        java.util.Date date = new java.util.Date(time * 1000L);
        DateFormat format = new SimpleDateFormat(formatDate);
        format.setTimeZone(TimeZone.getDefault());
//        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        return formatted;
    }
}
