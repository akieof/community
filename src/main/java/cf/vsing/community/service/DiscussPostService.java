package cf.vsing.community.service;

import cf.vsing.community.dao.DiscussPostMapper;
import cf.vsing.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    /**
     *
     * @param id 为0查询所有帖子，否则查询id对应用户的帖子
     * @param offset    开始数据偏置
     * @param limit     查询条数
     * @return  帖子信息列表
     */
    public List<DiscussPost> findDiscussPost(int id, int offset, int limit){
        return discussPostMapper.selectDiscussPost(id,offset,limit);
    }

    /**
     *
     * @param userId 为0查询总贴数，否则返回用户帖子数量
     * @return
     */
    public int findDiscussPostRows(int userId){
        return discussPostMapper.selectDiscussPostPostRows(userId);
    }
}
