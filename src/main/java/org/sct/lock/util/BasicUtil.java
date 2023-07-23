package org.sct.lock.util;

import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class BasicUtil {

    public static String convert(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> convert(List<String> message) {
        List<String> msgList = new ArrayList<>();
        for (String key : message) {
            msgList.add(convert(key));
        }
        return msgList;
    }

    public static String remove(String message) {
        return ChatColor.stripColor(message);
    }

    public static List<String> remove(List<String> message) {
        List<String> msgList = new ArrayList<>();
        for (String key : message) {
            msgList.add(remove(key));
        }
        return msgList;
    }

    public static int ExtraceInt(String string) {
        Pattern pattern = compile("\\d+");
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            buffer.append(matcher.group());
        }
        return buffer.toString().isEmpty() ? 0 : Integer.parseInt(buffer.toString());
    }

    public static <T> String replace(String message, String var, T replace) {
        return message.replace(var, String.valueOf(replace));
    }

    public static String getFileMD5(File file) {
        try {
            byte[] bytes = new byte[8192];
            int len = 0;
            FileInputStream inputStream = new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            while ((len = inputStream.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, len);
            }
            inputStream.close();
            byte[] md5bytes = messageDigest.digest();
            BigInteger bigInteger = new BigInteger(1, md5bytes);
            return bigInteger.toString(16);
        } catch (IOException | NoSuchAlgorithmException e) {
            return null;
        }
    }

}