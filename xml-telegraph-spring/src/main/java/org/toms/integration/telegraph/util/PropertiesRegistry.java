
package org.toms.integration.telegraph.util;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/** 
* 프로퍼티를 가지고 있는 레지스트리 ---
*@author airlee
**/
public class PropertiesRegistry {
    
    //싱글톤구현을 위한 변수 
    private static PropertiesRegistry mConfigRegistry ;
   // private static Logger logger = Logger.getLogger(PropertiesRegistry.class);
    
    private java.util.Properties properties = new Properties();
    //생성자        
    protected PropertiesRegistry() {
        try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error("config registry init error", e);
		}
    }

    public static PropertiesRegistry getInstance()
    {
        if(mConfigRegistry == null)
        {
                mConfigRegistry = new PropertiesRegistry();
               // logger.info(" config registry is created");
        }
        return mConfigRegistry;
    }
    
	@SuppressWarnings("rawtypes")
	private void init() throws Exception{

           properties.load(PropertiesRegistry.class.getResourceAsStream("/properties/telegraph.properties"));

            Set propertySet = properties.entrySet();
            for (Object o : propertySet) {
                Map.Entry entry = (Map.Entry) o;
                System.out.printf("%s = %s%n", entry.getKey(), entry.getValue());
            }
            
    }
    
    public String getValue(String key)
    {
        return properties.getProperty(key);
    }

    public int getInt(String key)
    {
        return Integer.parseInt(properties.getProperty(key));
    }
    
	public java.util.Properties getProperties() {
		return properties;
	}


}
