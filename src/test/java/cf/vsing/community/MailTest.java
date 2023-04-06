package cf.vsing.community;

import cf.vsing.community.util.MailClientUtil;
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

    @Test
    public void mailTeat(){
        mailClientUtil.sendMail("REGIST_NO_020366@outlook.com","MailServiceTest","<h1>Test</h1>");
    }
}
