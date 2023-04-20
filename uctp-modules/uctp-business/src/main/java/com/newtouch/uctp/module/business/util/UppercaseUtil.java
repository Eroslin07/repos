package com.newtouch.uctp.module.business.util;


import java.math.BigDecimal;
import java.text.NumberFormat;

public class UppercaseUtil {
    private static final String[] UNIT = {"", "拾", "佰", "仟", "万", "拾万", "佰万", "仟万", "亿", "拾亿", "佰亿", "仟亿"};
    private static final String[] NUM = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    private static final String[] DECIMAL = {"角", "分"};

    public static String convert(BigDecimal amount) {
        StringBuilder sb = new StringBuilder();
        int scale = amount.scale();
        if (scale > 2) {
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        String str = amount.toString();
        String[] parts = str.split("\\.");
        String integerPart = parts[0];
        String decimalPart = "00";
        if (parts.length > 1) {
            decimalPart = parts[1];
        }
        int integerLen = integerPart.length();
        int decimalLen = decimalPart.length();
        if (integerLen == 1 && integerPart.charAt(0) == '0') {
            sb.append(NUM[0]);
        } else {
            for (int i = 0; i < integerLen; i++) {
                int digit = integerPart.charAt(i) - '0';
                int unitIndex = integerLen - i - 1;
                int unit = unitIndex % 4;
                if (digit == 0) {
                    if (unit != 0 && sb.length() > 0 && sb.charAt(sb.length() - 1) != '零') {
                        sb.append(NUM[0]);
                    }
                } else {
                    sb.append(NUM[digit]);
                    sb.append(UNIT[unit]);
                }
                if (unit == 0 && unitIndex > 0 && sb.charAt(sb.length() - 1) != '亿') {
                    sb.append(UNIT[unitIndex]);
                }
            }
        }


        sb.append("元");
        if (decimalLen == 1) {
            decimalPart += "0";
        }

        if (!decimalPart.equals("00")) { //如果小数部分不为0才进行转换
            for (int i = 0; i < decimalLen; i++) {
                int digit = decimalPart.charAt(i) - '0';

                if (digit != 0) {
                    // 如果小数位不为0才添加单位
                    sb.append(NUM[digit]);
                    sb.append(DECIMAL[i]);
                }
            }
        }

        if (decimalPart.equals("00")) {
            sb.append("整");
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        NumberFormat instance = NumberFormat.getInstance();
        instance.setGroupingUsed(false);
        BigDecimal num = BigDecimal.valueOf(2010046.01);
        String salary = instance.format(num);
        String s = convert(num);
        System.out.println("你的工资为：" + num + "元(取消科学记数法：" + salary + "元)\n大写金额:" + s);
    }
}
