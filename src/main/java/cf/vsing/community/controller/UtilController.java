package cf.vsing.community.controller;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class UtilController {
    private static final Logger log= LoggerFactory.getLogger(UtilController.class);
    @Autowired
    private Producer kaptcha;

    @RequestMapping(path = "/VerificationCode",method = RequestMethod.GET)
    public void verifcationCode(HttpServletResponse response, HttpSession session){

        String code=kaptcha.createText();
        BufferedImage image=kaptcha.createImage(code);

        session.setAttribute("code",code);
        response.setContentType("image/png");
        try {
            OutputStream os=response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            log.error("验证码生产失败"+e.getMessage());
        }
    }
}
