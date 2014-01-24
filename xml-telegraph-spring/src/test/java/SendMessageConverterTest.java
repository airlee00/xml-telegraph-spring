

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.toms.integration.telegraph.support.ReceiveMessageConverter;
import org.toms.integration.telegraph.support.SendMessageConverter;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:context/*-context.xml" })
public class SendMessageConverterTest {

    @Autowired
    ApplicationContext aplicationContext;

    java.util.Map      map;

    @Before
    public void createTestVo() {
        try {
            map = new java.util.HashMap();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

   @Test
    public void sendConverterTest() {
        map.put("tr_code", "3333322");
        SendMessageConverter c = (SendMessageConverter) aplicationContext
                .getBean("sendMessageConverter");
        System.out.println(c.convertMessage("SAMPLE", map));

    }

   @Test
    public void receiveConverterTest() {
        String rcvMsg = "0124         KID0100N01011102              0099                                한글임 Y5I1920100100600000                    ";
        ReceiveMessageConverter c = (ReceiveMessageConverter) aplicationContext
                .getBean("receiveMessageConverter");

        System.out.println(c.convertMessage("SAMPLE", rcvMsg));

    }
}
