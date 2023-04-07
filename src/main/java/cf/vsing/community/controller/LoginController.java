package cf.vsing.community.controller;

import cf.vsing.community.entity.User;
import cf.vsing.community.service.UserService;
import cf.vsing.community.util.StatusUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements StatusUtil {
    @Autowired
    private UserService userService;


    @RequestMapping(path = "/login/register", method = RequestMethod.GET)
    public String goRegister() {
        return "/site/register";
    }
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String goLogin() {
        return "/site/login";
    }



    @RequestMapping(path = "/login/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> msg = userService.register(user);
        if (msg == null || msg.isEmpty()) {
            model.addAttribute("msg", "注册成功，请查看邮箱激活账号");
            model.addAttribute("target", "/login");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", msg.get("usernameMsg"));
            model.addAttribute("passwordMsg", msg.get("passwordMsg"));
            model.addAttribute("emailMsg", msg.get("emailMsg"));
            return "/site/register";
        }

    }


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String verifycode, boolean remember, Model model, HttpServletResponse response, HttpSession session) {

        //判断验证码
        String code = (String) session.getAttribute("code");
        if (StringUtils.isBlank(verifycode)) {
            model.addAttribute("verifyMsg", "验证码不能为空！");
            return "/site/login";
        }
        if (!code.equalsIgnoreCase(verifycode) || StringUtils.isBlank(code)) {
            model.addAttribute("verifyMsg", "验证码错误！");
            return "/site/login";
        }


        //登录
        int expiredSeconds = remember ? LONG_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, String> msg = userService.login(username, password, expiredSeconds);
        if (msg.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", msg.get("ticket"));
            cookie.setPath("/");
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/";
        } else {

            model.addAttribute("usernameMsg", msg.get("usernameMsg"));
            model.addAttribute("passwordMsg", msg.get("passwordMsg"));
            return "/site/login";
        }

    }


    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }


    @RequestMapping(path = "/activation/{userId}/{activationCode}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("activationCode") String activationCode) {
        int result = userService.activation(userId, activationCode);
        if (result == ACTIVATION_NULL) {
            model.addAttribute("msg", "操作无效，用户未注册！");
            model.addAttribute("target", "/");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "操作无效，用户已激活！");
            model.addAttribute("target", "/");
        } else if (result == ACTIVATION_OK) {
            model.addAttribute("msg", "激活成功，您的账号以经可以正常使用");
            model.addAttribute("target", "/login");
        } else {
            model.addAttribute("msg", "激活失败，激活链接有误");
            model.addAttribute("target", "/");
        }
        return "/site/operate-result";
    }

}
