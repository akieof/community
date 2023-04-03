package cf.vsing.community.controller;

import cf.vsing.community.entity.DiscussPost;
import cf.vsing.community.entity.Page;
import cf.vsing.community.entity.User;
import cf.vsing.community.service.DiscussPostService;
import cf.vsing.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;

    @RequestMapping(path="/",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){

        //设置页码相关参数
        //page 可在模板中直接访问
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        //获取帖子信息列表
        List<DiscussPost> list = discussPostService.findDiscussPost(0, page.getOffset(), page.getLimit());

        //轮询查询帖子作者信息
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        if(list!=null){
            for(DiscussPost post:list){
                Map<String,Object> map=new HashMap<>();
                User user=userService.findUserById(post.getUserId());
                map.put("post",post);
                map.put("user",user);
                discussPosts.add(map);
            }
        }

        //传入model
        model.addAttribute("discussPosts",discussPosts);

        //指定视图
        return "/index";
    }
}
