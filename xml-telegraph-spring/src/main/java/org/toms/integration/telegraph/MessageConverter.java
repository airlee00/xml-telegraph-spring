package org.toms.integration.telegraph;

import org.toms.integration.telegraph.util.PropertiesRegistry;
import org.toms.integration.telegraph.util.StringUtil;
import org.toms.integration.telegraph.util.XMLUtil;
import org.w3c.dom.Element;



/**
 *  메시지를 조립한다.
 *  @author airlee
 *  @version 1.2
 */
public abstract class MessageConverter {

	public static final String DEFAULT_ENCODING ="UTF-8";
	protected String url ;
	protected String extension = ".xml";
    protected String encoding = DEFAULT_ENCODING;
    
    
     public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setExtension(String extension) {
        this.extension = extension;
    }
     
    /**
     *  메시지 조립
     * @param : xml 전문포멧 xml파일명
     *            in  입력값
     * @return : 
     * @version : 없음
    */
    //public abstract Object convertMessage(String xmlId, MessageParameterMap in);


    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * xml 아이디로 부터(message id) 해당 xml을 불러온다. 
     * @param  xmlId는 메시지 아이디로 bss_fwk.properties에 정의 되어 있음 
     * @return : Element
     * @throws Exception 
    */
    protected Element getElementXmlFileId(String xmlId) throws Exception {
        String xmlpath ;
        if(StringUtil.isEmpty(url)) {
            xmlpath = PropertiesRegistry.getInstance().getValue(MessageContext.XML_LOCATION) + "/" + xmlId + ".xml";
            //xmlpath = "/telegraph/"+ xmlId + ".xml";
        }else {
            xmlpath = url + "/" + xmlId;
        }
        System.out.println(xmlpath);
        return XMLUtil.getDocumentFromFile(xmlpath);
    }

    /**
     *  로깅
     * @param :  msg
     * @return : void
     * @version : 없음
    */
    protected void log(String msg) {
        System.out.println(msg);//logger로 변경할것
    }
}
