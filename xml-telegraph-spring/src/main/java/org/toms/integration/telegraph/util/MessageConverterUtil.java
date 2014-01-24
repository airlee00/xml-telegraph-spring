package org.toms.integration.telegraph.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.toms.integration.telegraph.MessageContext;


/**
 * 전문처리시 string처리 유틸
 * @author airlee
 */

public class MessageConverterUtil {
    /**
     * null값을 체크해서 지정된 문자열로 대체.
     * 
     * @param param
     *            변환할 문자열
     * @param newParam
     *            변환된 문자열
     * @return 널 대체 문자열
     */
    public static String nvl(String param, String newParam) {

        if (param == null || param.equals("") || param.equals("null")) {
            return newParam;
        } else {
            return param.trim();
        }
    }

    /**
     * 송신-타입변환 
     * 공백 또는 '0'을  length만큼 채워넣음
     * 
     * @param s
     * @param type
     * @param len
     * @return
     */
    public static byte[] pad(byte[] s, String type, int len) {

        byte[] ret = new byte[len];

        if (s.length > len) {
            for (int i = 0; i < len; i++) {
                System.arraycopy(s, 0, ret, 0, s.length);
                //ret[i] = s[i];
            }
        } else {
            if (MessageContext.CHAR_VALUE.equals(type)) {//char타입인경우 뒤쪽에 공백처리
                for (int i = 0; i < s.length; i++) {
                    System.arraycopy(s, 0, ret, 0, s.length);
                    //ret[i] = s[i];
                }
                for (int i = 0; i < len - s.length; i++) {
                    ret[s.length + i] = ' ';
                }
            } else if (MessageContext.NUMBER_VALUE.equals(type)) {//number타입일경우 앞쪽에 0을 채움
                for (int i = 0; i < len - s.length; i++) {
                    ret[i] = '0';
                }
                for (int i = 0; i < s.length; i++) {
                    ret[len - s.length + i] = s[i];
                }
            } else {
                for (int i = 0; i < len - s.length; i++) {
                    ret[i] = ' ';
                }
                for (int i = 0; i < s.length; i++) {
                    ret[len - s.length + i] = s[i];
                }
            }
        }

        return ret;
    }

    /**
     * 수신시 - 해당 타입으로 변환
     * 
     * @param val
     * @param type
     * @return
     */
    public static Object convType(byte[] val, String type,String encoding) {

        String sVal = null;
        Object obj = null;

        try {
            if (type.equals(MessageContext.RTRIM_VALUE)) {// 뒤쪽 공백 제거
               obj = new String(rtrim(val));
            }
            else if (type.equals(MessageContext.CHAR_VALUE)) { // String
                obj = new String(val, encoding);
            } else if (type.equals(MessageContext.SHORT_VALUE)) { // Short
                obj = Short.valueOf(sVal);
            } else if (type.equals(MessageContext.INT_VALUE)) { // int
                obj = Integer.valueOf(sVal);
            } else if (type.equals(MessageContext.BIGDECIMAL_VALUE)) { // bigDecimal
                obj = new BigDecimal(sVal);
            } else if (type.equals(MessageContext.LONG_VALUE)) { // long
                obj = Long.valueOf(sVal);
            } else if (type.equals(MessageContext.DOUBLE_VLAUE)) { // double
                obj = Double.valueOf(sVal);
            } else if (type.equals(MessageContext.DATE_VALUE)) { // date
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
                obj = df.parse(sVal);
            }
        } catch (Exception e) {
            obj = sVal;
        }

        return obj;
    }

    /**
     * 오른쪽 공백제거
     * 
     * @param s
     * @return
     */
    public static byte[] rtrim(byte[] s) {

        String ret = ("*" + new String(s)).trim();

        ret = ret.substring(1);

        return ret.getBytes();
    }

    /**
     * 공백제거
     * 
     * @param s
     * @return
     */
    public static byte[] trim(byte[] s) {

        String ret = (new String(s)).trim();

        return ret.getBytes();
    }
}
