package cf.vsing.community;

import cf.vsing.community.dao.DiscussPostMapper;
import cf.vsing.community.dao.UserMapper;
import cf.vsing.community.entity.DiscussPost;
import cf.vsing.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public final class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void  testSelect(){
        List<DiscussPost> list=discussPostMapper.selectDiscussPost(0,0,10);
        int row=discussPostMapper.selectDiscussPostPostRows(0);
        List<DiscussPost> post=discussPostMapper.selectDiscussPost(102,0,10);


        System.out.println("Row="+row);
        System.out.println("post="+post);
        System.out.println("list"+list);
    }
}
