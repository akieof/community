package cf.vsing.community.service;

import cf.vsing.community.dao.LoginTicketMapper;
import cf.vsing.community.dao.UserMapper;
import cf.vsing.community.entity.LoginTicket;
import cf.vsing.community.entity.User;
import cf.vsing.community.util.CommunityUtil;
import cf.vsing.community.util.MailClientUtil;
import cf.vsing.community.util.StatusUtil;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements StatusUtil {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private MailClientUtil mailClientUtil;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${project.domain}")
    private String domain;


    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        //非法值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        //空值处理
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        //重复注册判断
        if (userMapper.selectByName(user.getUsername()) != null) {
            map.put("usernameMsg", "用户已存在！");
            return map;
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            map.put("emailMsg", "邮箱已被注册！");
            return map;
        }

        //用户信息初始化
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl("http://images.nowcoder.com/head/1t.png");
        user.setCreateTime(new Date());

        userMapper.insertUser(user);
        String activationUrl = domain + "/activation/" + user.getId() + "/" + user.getActivationCode();

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        context.setVariable("url", activationUrl);
        String emailText = templateEngine.process("/mail/activation", context);
        mailClientUtil.sendMail(user.getEmail(), "Vsing邮箱验证", emailText);

        return map;
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param expiredSeconds
     * @return
     */
    public Map<String, String> login(String username, String password, int expiredSeconds) {
        Map<String, String> msg = new HashMap<>();

        //空值处理
        if (StringUtils.isBlank(username)) {
            msg.put("usernameMsg", "账号不能为空");
            return msg;
        }
        if (StringUtils.isBlank(password)) {
            msg.put("passwordMsg", "密码不能为空");
            return msg;
        }

        //验证用户
        User user = userMapper.selectByName(username);
        if (user == null) {
            msg.put("usernameMsg", "用户不存在，请先注册！");
            return msg;
        }
        if (user.getStatus() == 0) {
            msg.put("usernameMsg", "用户未激活，请先激活！");
            return msg;
        }
        //比对密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            msg.put("passwordMsg", "密码错误，请重新输入！");
            return msg;
        }
        //保存凭证
        LoginTicket ticket = new LoginTicket(user.getId(), CommunityUtil.generateUUID(), 0, new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insetTicket(ticket);
        msg.put("ticket", ticket.getTicket());
        return msg;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }
    public LoginTicket getLoginTicket(String ticket){
        return loginTicketMapper.selectTicketByTicket(ticket);
    }

    public int activation(int userId, String activationCode) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ACTIVATION_NULL;
        } else if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(activationCode)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_OK;
        } else {
            return ACTIVATION_WRONG;
        }
    }
}
