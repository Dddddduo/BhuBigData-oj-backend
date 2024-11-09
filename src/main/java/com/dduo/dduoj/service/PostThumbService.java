package com.dduo.dduoj.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dduo.dduoj.model.entity.PostThumb;
import com.dduo.dduoj.model.entity.User;


public interface PostThumbService extends IService<PostThumb> {

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, long postId);
}
