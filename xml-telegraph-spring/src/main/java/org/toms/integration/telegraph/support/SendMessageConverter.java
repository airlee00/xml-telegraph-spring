package org.toms.integration.telegraph.support;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

import org.toms.integration.telegraph.MessageContext;
import org.toms.integration.telegraph.MessageConverter;
import org.toms.integration.telegraph.util.MessageConverterUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 *  송신 메시지를 조립한다.
 *  @author airlee
 */
//@Component("sendMessageConverter")
public class SendMessageConverter extends MessageConverter{

    /**
     *  송신메시지 조립
     * @param : xml 전문포멧 xml파일명
     *            in  입력값
     * @return : 
     * @version : 없음
    */
    @SuppressWarnings("rawtypes")
    public String convertMessage(String xmlId, Map map) {

        try {
            Element root = getElementXmlFileId(xmlId);

           // String serviceId = ((Element) root).getAttribute(MessageContext.ID_ATTR);
            //log(serviceId + "=======" + xmlId + "====start=============");
            // setCommonInfo(request, in);

            byte[] sndMsg = convertMessage(root, map);
            log("message : [" + new String(sndMsg) + "]");
            //log(serviceId + "===========end=============");

            return new String(sndMsg);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     *  입력값으로부터 송신전문 작성
     * @param : root XML ELEMENT , input INPUT MAP
     * @return : 
     * @version : 없음
    */
    @SuppressWarnings("rawtypes")
    private byte[] convertMessage(Element root, Map input) {

        ByteBuffer buff = ByteBuffer.allocate(MessageContext.MAX_SEND_BUFFER_SIZE);
        // 공통헤더부 조립
        NodeList nodes = root.getElementsByTagName(MessageContext.HEADER_ELEMENT).item(0)
                .getChildNodes();
        log("[Send common] =============================");
        for (int i = 0; i < nodes.getLength(); i++) {
            assembleInputProperty(nodes.item(i), input, buff);
        }

        // 데이타부 조립
        log("[Send sndmsg] =============================");
        nodes = root.getElementsByTagName(MessageContext.SEND_ELEMENT).item(0).getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getTagName().equals(MessageContext.ARRAY_ELEMENT)) { // multi-row data
                    assembleInputArray(element, input, buff);
                } else {
                    assembleInputProperty(nodes.item(i), input, buff);
                }
            }
        }

        // 전문길이(전문의 처음 위치에 대체)
        int tmsg_len = buff.position();

        // 전문완성
        byte[] sndmsg = new byte[tmsg_len];

        buff.position(0);
        buff = buff.get(sndmsg);

        return sndmsg;
    }

    /**
     *  단건 송신 전문 조립
     * @param : node Node, input Map, buff  ByteBuffer
     * @return : void
     * @version : 없음
    */
    @SuppressWarnings({ "rawtypes" })
    private  void assembleInputProperty(Node node, Map input, ByteBuffer buff) {

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String val = "" + input.get(element.getTagName());

            if ("null".equals(val) || val == null) {
                val = element.getAttribute(MessageContext.DEFAULT_ATTR);
            }

            byte[] message = MessageConverterUtil.pad(val.getBytes(),
                    element.getAttribute(MessageContext.TYPE_ATTR),
                    Integer.parseInt(element.getAttribute(MessageContext.LENGTH_ATTR)));

            buff.put(message);

            log(element.getTagName() + ":"
                            + element.getAttribute(MessageContext.COLUMN_ATTR)
                            + ":" + element.getAttribute(MessageContext.TYPE_ATTR)
                            + ":" + element.getAttribute(MessageContext.LENGTH_ATTR)
                            + ":[" + new String(message) + "]");

        }

    }

    /**
     *  다건 송신 전문 조립
     * @param : 
     * @return : 
     * @version : 없음
    */
    @SuppressWarnings("rawtypes")
    private void assembleInputArray(Element element, Map input,
            ByteBuffer buff) {
        log("[array start] =============================");
        String name = element.getAttribute(MessageContext.ARRAY_ELEMENT);
        List inputList = (List) input.get(name);
        int cnt = 0;
        if (inputList != null)
            cnt = inputList.size();

        // record
        NodeList recs = element.getElementsByTagName(MessageContext.RECORD_ELEMENT).item(0)
                .getChildNodes();

        log("[rec] =============================");
        for (int j = 0; j < cnt; j++) {
            Map map = (Map) inputList.get(j);

            for (int k = 0; k < recs.getLength(); k++) {
                Node rec = recs.item(k);
                assembleInputProperty(rec, map, buff);
            }
        }

        log("[array end] =============================");
    }

}
