package cf.vsing.community;

import cf.vsing.community.util.CommunityUtil;
import cf.vsing.community.util.MailClientUtil;
import cf.vsing.community.util.SensitiveWorldUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClientUtil mailClientUtil;
    @Autowired
    private SensitiveWorldUtil filter;

    @Test
    public void mailTeat(){
        mailClientUtil.sendMail("REGIST_NO_020366@outlook.com","MailServiceTest","<h1>Test</h1>");
    }
    @Test
    public void tas(){
        String text="dvsv胡进涛affsdfzd胡●进涛";
        System.out.println(filter.filter(text));
    }

}
