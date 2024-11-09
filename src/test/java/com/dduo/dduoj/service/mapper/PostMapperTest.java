package com.dduo.dduoj.service.mapper;

import com.dduo.dduoj.mapper.PostMapper;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.dduo.dduoj.model.entity.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 帖子数据库操作测试
 *
 * @author <a href="https://github.com/lidduo">程序员鱼皮</a>
 * @from <a href="https://dduo.icu">编程导航知识星球</a>
 */
@SpringBootTest
class PostMapperTest {

    @Resource
    private PostMapper postMapper;

    @Test
    void listPostWithDelete() {
        List<Post> postList = postMapper.listPostWithDelete(new Date());
        Assertions.assertNotNull(postList);
    }
}