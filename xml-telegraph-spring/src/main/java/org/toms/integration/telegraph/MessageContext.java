package org.toms.integration.telegraph;


/**--
 * xml전문 송신및 수신 조립을 위한 context
 * @author airlee
 * @version 1.0, 1.2  version test
 */

public class MessageContext {
    public static final int    MAX_SEND_BUFFER_SIZE    = 1024 * 128;
    public static final int    MAX_RECEIVE_BUFFER_SIZE = 1024 * 128;

    public static final String XML_LOCATION            = "telegraph.xml.location";

    public static final String SUCC_ERR_CODE           = "IZZ000";
    public static final String SYSTEM_ERR_CODE         = "EHB001";
    public static final String ERR_CODE_ELEMENT        = "err_code";
    // xml element
    public static final String HEADER_ELEMENT          = "header";    // 헤더
    public static final String SEND_ELEMENT            = "send";      // 송신
    public static final String RECEIVE_ELEMENT         = "receive";   // 수신
    public static final String RECORD_ELEMENT          = "record";    // 레코드
    public static final String ARRAY_ELEMENT           = "array";     // array의 name속성

    // xml attribute
    public static final String COLUMN_ATTR             = "c";        // 칼럼명
    public static final String TYPE_ATTR               = "t";        // 타입
    public static final String LENGTH_ATTR             = "l";        // 길이
    public static final String DEFAULT_ATTR            = "d";        // 기본값
    public static final String ID_ATTR                 = "id";       // 아이디
    // xml value
    public static final String CHAR_VALUE              = "c";        // 송신,수신 문자타입
    public static final String NUMBER_VALUE            = "n";        // 송신 숫자타입
    public static final String RTRIM_VALUE             = "r";        // 수신 뒤쪽공백제거 문자
    public static final String INT_VALUE               = "i";        // 수신 int
    public static final String BIGDECIMAL_VALUE        = "b";        // 수신 bigDecimal
    public static final String DOUBLE_VLAUE            = "d";        // 수신 double
    public static final String LONG_VALUE              = "l";        // 수신 long
    public static final String DATE_VALUE              = "t";        // 수신 date
    public static final String SHORT_VALUE             = "s";        // 수신 short
    //
    public static final String RECORD_CNT_ELEMENT      = "recordCount" ;//레코드 수

}
