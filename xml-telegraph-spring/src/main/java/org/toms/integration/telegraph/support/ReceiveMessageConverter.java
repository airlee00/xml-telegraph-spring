package org.toms.integration.telegraph.support;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.toms.integration.telegraph.MessageContext;
import org.toms.integration.telegraph.MessageConverter;
import org.toms.integration.telegraph.util.MessageConverterUtil;
import org.toms.integration.telegraph.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 수신메시지를 조립
 * @author airlee
 */
//@Component("receiveMessageConverter")
public class ReceiveMessageConverter extends MessageConverter{

    /**
     *  수신메시지 조립
     * @param : xmlId 메시지 아이디,
     *            receivedMessage 수신된 메시지  
     * @return : 
     * @version : 없음
    */
    @SuppressWarnings("rawtypes")
    public Map convertMessage(String xmlId,  String receivedMessage) {

        try {
            Element root = super.getElementXmlFileId(xmlId);
            
            Map map = convertMessage(root, receivedMessage.getBytes());
            log("message received : [" + map + "]");

            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     *  수신전문으로부터 출력값을 추출
     * @param : root xml element, msg-received message 
     * @return : 
     * @version : 없음
    */
    @SuppressWarnings("rawtypes")
    private  Map convertMessage(Element root, byte[] msg) {

        Map out = new LinkedHashMap();

        ByteBuffer buff = null;
        if (msg == null)
            buff = ByteBuffer.allocate(MessageContext.MAX_RECEIVE_BUFFER_SIZE);
        else
            buff = ByteBuffer.allocate(msg.length);

        buff.put(msg);
        buff.position(0);

        // 공통정보
        NodeList nodes = root.getElementsByTagName(MessageContext.HEADER_ELEMENT).item(0)
                .getChildNodes();
        log("[recv common] =============================");

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            assembleOutputProperty(node, out, buff);
        }
        log("[rcvmsg start] =============================" + buff.position() +"," + msg.length + "," + buff.remaining());

        String recordCnt = (String) out.get(MessageContext.RECORD_CNT_ELEMENT); //"record_cnt"
        log("[rcvmsg record_cnt] =============================[" + recordCnt+"]");
        if (!StringUtil.isEmpty(recordCnt)) {
            int rec_cnt = Integer.parseInt(recordCnt);
            if (rec_cnt > 0) { // 정상===========>
                // 내용정보
                Node rcvnode = root.getElementsByTagName(MessageContext.RECEIVE_ELEMENT).item(0);// rcvmsg가 없을때
                if (rcvnode == null)
                    return out;
                else
                    nodes = rcvnode.getChildNodes();

                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        if (element.getTagName().equals(MessageContext.ARRAY_ELEMENT)) { // multi-row data
                            assembleOutputArray(element, out, buff, rec_cnt);
                        } else {
                            assembleOutputProperty(node, out, buff);
                        }
                    }
                }
            }
        }
        return out;
    }

    /**
     *  단건 수신 전문 조립
     * @param : node - xml Node, out - Map ,buff -ByteBuffer
     * @return : 
     * @version : 없음
    */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private  void assembleOutputProperty(Node node, Map out, ByteBuffer buff) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            try {
                Element element = (Element) node;
                byte[] dst = new byte[Integer.parseInt(element
                        .getAttribute(MessageContext.LENGTH_ATTR))];
                buff = buff.get(dst);
                out.put(element.getTagName(),
                        MessageConverterUtil.convType(dst, element.getAttribute(MessageContext.TYPE_ATTR),encoding));

                log( element.getTagName()   + ":"
                                + element.getAttribute(MessageContext.COLUMN_ATTR)   + ":"
                                + element.getAttribute(MessageContext.TYPE_ATTR)     + ":"
                                + element.getAttribute(MessageContext.LENGTH_ATTR)   
                                + ":["
                                + MessageConverterUtil.convType(dst,
                                        element.getAttribute(MessageContext.TYPE_ATTR),encoding)
                                + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  다건 수신 전문 조립
     * @param : element - xml element, out - MessageParameterMap, buff
     * @return : 
     * @version : 없음
    */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private  void assembleOutputArray(Element element, Map out,
            ByteBuffer buff, int cnt) {
        try {
            // name은 이름만 사용하여 map의 key값이 된다.
            String name = element.getAttribute(MessageContext.ARRAY_ELEMENT);

            List array = new java.util.ArrayList();

            log("[array cnt] ======================================>>" + cnt);
            // record
            NodeList recs = element.getElementsByTagName(MessageContext.RECORD_ELEMENT).item(0)
                    .getChildNodes();

            for (int j = 0; j < cnt; j++) {
                Map map = new java.util.HashMap();
                log("array index=====================>>" + j);
                for (int k = 0; k < recs.getLength(); k++) {

                    Node rec = recs.item(k);
                    assembleOutputProperty(rec, map, buff);
                }
                array.add(map);
            }
            out.put(name, array);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
