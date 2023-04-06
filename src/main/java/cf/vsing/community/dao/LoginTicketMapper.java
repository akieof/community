package cf.vsing.community.dao;

import cf.vsing.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketMapper {
    @Insert("INSERT INTO login_ticket (user_id,ticket,status,expired) VALUES(#{userId},#{ticket},#{status},#{expired});")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insetTicket(LoginTicket loginTicket);
    @Select("SELECT user_id,ticket,status,expired FROM login_ticket WHERE ticket=#{ticket}; ")
    LoginTicket selectTicketByTicket(String ticket);
    @Update("UPDATE login_ticket SET status=#{status} WHERE ticket=#{ticket};")
    int updateStatus(String ticket,int status);
}
