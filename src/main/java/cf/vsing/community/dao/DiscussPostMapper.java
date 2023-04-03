package cf.vsing.community.dao;

import cf.vsing.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    List<DiscussPost> selectDiscussPost(int userId, int offset, int limit);

    int selectDiscussPostPostRows(@Param("userId") int userId);
}
