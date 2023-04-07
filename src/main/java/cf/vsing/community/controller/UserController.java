package cf.vsing.community.controller;

import cf.vsing.community.annotation.LoginRequired;
import cf.vsing.community.entity.User;
import cf.vsing.community.service.UserService;
import cf.vsing.community.util.CommunityUtil;
import cf.vsing.community.util.CookieUtil;
import cf.vsing.community.util.HostHolderUtil;
import jakarta.mail.Multipart;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.Buffer;

@Controller
public class UserController {
    private static final Logger log= LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    HostHolderUtil hostHolder;
    @Value("${project.path.upload}")
    private String uploadPath;
    @Value("${project.path.domain}")
    private String domain;


    @LoginRequired
    @RequestMapping(value = "/user/setting",method = RequestMethod.GET)
    public String goSetting(){
        return "/site/setting";
    }


    @LoginRequired
    @RequestMapping(value = "/user/profile",method = RequestMethod.GET)
    public String goProfile(){
        return "/site/profile";
    }


    @LoginRequired
    @RequestMapping(value = "/setting/header",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){

        //判空
        if(headerImage==null){
            model.addAttribute("error","请先选择头像文件！");
            return "redirect:/user/setting";
        }
        String originalFilename=headerImage.getOriginalFilename();
        String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));

        //判非法格式
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","请选择正确格式的图片！");
            return "redirect:/user/setting";
        }
        String localFileName=CommunityUtil.generateUUID()+suffix;
        File dest=new File(uploadPath+"/"+localFileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            log.error("用户上传头像文件保存出错: "+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        String headerUrl=domain+"/api/file/"+localFileName;
        userService.updateHeaderUrl(hostHolder.getUser().getId(),headerUrl);
        hostHolder.getUser().setHeaderUrl(headerUrl);
        return "redirect:/";
    }


    @RequestMapping(value = "/api/file/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        String filePath=uploadPath+"/"+fileName;
        String suffix=fileName.substring(fileName.lastIndexOf("."));
        if(!StringUtils.isBlank(suffix)){
            response.setContentType("image/"+suffix);
            try(
                    FileInputStream fis=new FileInputStream(filePath);
                    OutputStream os=response.getOutputStream();
                    ){

                byte[] buffer=new byte[1024];
                int b=0;
                while((b=fis.read(buffer))!=-1){
                    os.write(buffer,0,b);
                }
            } catch (IOException e) {
                log.error("文件读取错误："+e.getMessage());
                throw new RuntimeException(e);
            }
        }

    }


    @LoginRequired
    @RequestMapping(path = "/setting/password",method = RequestMethod.POST)
    public String resetPassword(@CookieValue("ticket")String ticket,HttpServletResponse response, String oldPassword,String newPassword, Model model){
        User user=hostHolder.getUser();
        if(user==null||StringUtils.isBlank(oldPassword)){
            model.addAttribute("oldPasswordMsg","请输入原密码！");
            model.addAttribute("newPassword",newPassword);
            return "/site/setting";
        }
        if(StringUtils.isBlank(newPassword)){
            model.addAttribute("newPasswordMsg","请输入新密码！");
            model.addAttribute("oldPassword",oldPassword);
            return "/site/setting";
        }
        if(CommunityUtil.md5(oldPassword+user.getSalt()).equals(user.getPassword())){
            userService.updatePassword(user.getId(),CommunityUtil.md5(newPassword+user.getSalt()));
            Cookie cookie= new Cookie("ticket",null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            userService.logout(ticket);

            return "redirect:/";
        }
        model.addAttribute("oldPasswordMsg", "密码错误！");
        model.addAttribute("oldPassword",oldPassword);
        model.addAttribute("newPassword",newPassword);
        return "/site/setting";
    }
}
