package cf.vsing.community;

import cf.vsing.community.dao.DiscussPostMapper;
import cf.vsing.community.dao.LoginTicketMapper;
import cf.vsing.community.dao.UserMapper;
import cf.vsing.community.entity.DiscussPost;
import cf.vsing.community.entity.LoginTicket;
import cf.vsing.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public final class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void  userInsert(){

    }

    @Test
    public void  userSelect(){

    }

    @Test
    public void  discuss_post(){
        int userId=1,
            offset=1,
            limit=1;
        try{
            int result=discussPostMapper.selectDiscussPostPostRows(0);
            System.out.println("selectDiscussPostPostRows 方法成功执行!\n参数:userId=0"+"\n结果:" + result);
        }catch (Exception e){
            System.out.println("selectDiscussPostPostRows 方法执行失败,错误信息如下为:\n" + e.getMessage());
        }

        try{
            int result=discussPostMapper.selectDiscussPostPostRows(userId);
            System.out.println("selectDiscussPostPostRows 方法成功执行!\n参数:userId="+userId+"\n结果:" + result);
        }catch (Exception e){
            System.out.println("selectDiscussPostPostRows 方法执行失败,错误信息如下为:\n" + e.getMessage());
        }

        try{
            List<DiscussPost> result=discussPostMapper.selectDiscussPost(0,offset,limit);
            System.out.println("selectDiscussPost 方法成功执行!\n参数:userId=0"+"\t+offset="+offset+"\tlimit="+offset+"\n结果:" + result);
        }catch (Exception e){
            System.out.println("selectDiscussPost 方法执行失败,错误信息如下为:\n" + e.getMessage());
        }

        try{
            List<DiscussPost> result=discussPostMapper.selectDiscussPost(userId,offset,limit);
            System.out.println("selectDiscussPost 方法成功执行!\n参数:userId="+userId+"\t+offset="+offset+"\tlimit="+offset+"\n结果:" + result);
        }catch (Exception e){
            System.out.println("selectDiscussPost 方法执行失败,错误信息如下为:\n" + e.getMessage());
        }

    }


    @Test
    public void  login_ticket(){

        //Test Data
        LoginTicket loginTicket=new LoginTicket(2,"abc",0,new Date());

        //insetTicket(LoginTicket ticket);
        if(loginTicketMapper.insetTicket(loginTicket)==1)
            System.out.println("insetTicket 方法成功执行,插入信息为:" + loginTicket.toString());
        else
            System.out.println("insetTicket 方法执行失败");

        //selectTicketByTicket(Sting ticket)
        if(loginTicketMapper.selectTicketByTicket(loginTicket.getTicket())!=null)
            System.out.println("selectTicketByTicket 方法成功执行,结果为："+loginTicketMapper.selectTicketByTicket(loginTicket.getTicket()).toString());
        else
            System.out.println("selectTicketByTicket 方法执行失败");

        //updateStatus(Sting ticket,int status)
        if(loginTicketMapper.updateStatus("abc",1)==1)
            System.out.println("updateStatus 方法成功执行，结果为："+loginTicketMapper.selectTicketByTicket(loginTicket.getTicket()).toString());
        else
            System.out.println("updateStatus 方法成功执行失败");
    }
}
