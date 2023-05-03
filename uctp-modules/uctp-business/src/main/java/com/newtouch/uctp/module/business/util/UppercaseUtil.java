package com.newtouch.uctp.module.business.util;


import java.math.BigDecimal;
import java.text.NumberFormat;

public class UppercaseUtil {
    private static final String[] UNIT = {"", "拾", "佰", "仟", "万", "拾万", "佰万", "仟万", "亿", "拾亿", "佰亿", "仟亿"};
    private static final String[] NUM = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    private static final String[] DECIMAL = {"角", "分"};

    public static String convert_FQ(BigDecimal amount) {
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

    public static String convert(BigDecimal money) {
        String[] upNum = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String[] danwei = {"圆", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟"};
        //取消科学记数法
        NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(2);//设置小数位个数
        numFormat.setGroupingUsed(false);//取消科学技术发
        String formatNum = numFormat.format(money);
        String strmoney = formatNum + "";//浮点型转为字符型
        String lastUpNum = "null"; //用于存放上个参数的值
        String result = "";//返回的结果
        String[] split = strmoney.split("\\.");
        String strMoney = split[0];
        String point = "";
        //小数部分取值处理。
        if (split.length > 1) {
            point = split[1];
            if (point.length() == 1) {
                point = point.concat("0");
            }
        } else {
            point = "0";
        }
        //大于12位就直接返回。
        int moneyLen = strMoney.length();
        if (money == BigDecimal.valueOf(0)) {
            return "零圆整";
        }
        if (moneyLen > 12) {
            return "金额：" + money + "元，超出大写转换范围。最大金额：999999999999.99元";
        }
        //整数(integer)部分处理。
        if (!"0".equals(strMoney)) {
            for (int i = 0; i < moneyLen; i++) {
                String strNum = strMoney.charAt(i) + "";
                int singleNum = Integer.parseInt(strNum);
                String upSingleNum = upNum[singleNum];
                //上一为不等于0的情况
                if (!"零".equals(lastUpNum)) {
                    if (!"零".equals(upSingleNum)) {
                        result = result.concat(upSingleNum).concat(danwei[moneyLen - i - 1]);
                    } else
                        //为零但是在万、亿位上要加单位 (moneyLen-i)==9 指的是单位：亿。  (moneyLen-i)==5指的是单位：万
                        if ((moneyLen - i) == 5 || (moneyLen - i) == 9) {
                            lastUpNum = "";
                        } else {
                            result = result.concat(upSingleNum);
                        }
                }
                //上一位为0的情况
                if ("零".equals(lastUpNum) && !"零".equals(upSingleNum)) {
                    result = result.concat(upSingleNum).concat(danwei[moneyLen - i - 1]);
                }
                //捕捉上一位数（lastUpNum）为零的情况做优化。
                if ((moneyLen - i) == 5 || (moneyLen - i) == 9) {
                    //排除加单位时前面为"零"的情况。如：两百零万
                    if ("零".equals(lastUpNum) || "null".equals(lastUpNum)) {
                        result = result.substring(0, result.length() - 1);
                    }
                    if (!result.endsWith("亿")) {
                        result = result.concat(danwei[moneyLen - i - 1]);
                    }
                    lastUpNum = "";
                } else {
                    //把当前大写数字复制给 lastUpNum 用于下次判断
                    lastUpNum = upSingleNum;
                }
            }
            //对几万元整和几亿元整(result:五万零或者五亿零零)做优化。
            result = result.replaceAll("零零", "零");
            if (result.endsWith("零")) {
                String substring = result.substring(0, result.length() - 1);
                result = substring;
            }
            result = result.concat("圆");
            result = result.replaceAll("圆圆", "圆");
            result = result.replaceAll("万万", "万");

        }

        //小数(point)部分处理
        if ("0".equals(point)) {
            result = result + "整";
        } else {
            //去 整
//            if(result.endsWith("整")){
//                result = result.substring(0,result.length()-1);
//            }
            if ((point.charAt(0) + "").equals("0")) {
                result = result.concat(upNum[Integer.parseInt(point.charAt(1) + "")] + "分");
            } else if ((point.charAt(1) + "").equals("0")) {
                result = result.concat(upNum[Integer.parseInt(point.charAt(0) + "")] + "角");
            } else {
                result = result.concat(upNum[Integer.parseInt(point.charAt(0) + "")] + "角").concat(upNum[Integer.parseInt(point.charAt(1) + "")] + "分");
            }
        }
        return result;
    }

    public static void main(String[] args) {
        NumberFormat instance = NumberFormat.getInstance();
        instance.setGroupingUsed(false);
        BigDecimal num = BigDecimal.valueOf(150000);
        String salary = instance.format(num);
        String s = convert(num);
        System.out.println("你的工资为：" + num + "元(取消科学记数法：" + salary + "元)\n大写金额:" + s);
    }
}
