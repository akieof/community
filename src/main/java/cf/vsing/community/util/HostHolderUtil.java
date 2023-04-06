package cf.vsing.community.util;

import cf.vsing.community.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolderUtil {
    public ThreadLocal<User> users=new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
