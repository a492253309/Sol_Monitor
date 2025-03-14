package com.shaohao.mytask.utils;

import java.nio.charset.StandardCharsets;

/**
 * ASCII工具类
 *
 * @version
 * @author shaohao  2023年12月08日 下午2:28:00
 *
 */
public class ABIUtils {

    /**
     * 将字符串转换为十六进制表示 不带0x
     *
     * @return address
     */
    public static String stringToHexadecimal(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();

    }
}
