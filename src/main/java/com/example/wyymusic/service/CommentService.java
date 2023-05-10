package com.example.wyymusic.service;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wyymusic.model.vo.CommentVo;

import java.util.List;

/**
* @author xyc
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2023-04-26 09:12:49
*/
public interface CommentService extends IService<Comment> {

    /**
     * 评论
     * @param parentId
     * @param otherId
     * @param content
     * @return
     */
    BaseResponse<Boolean> saveComment(Long parentId, Long otherId, String content);

    /**
     * 删除评论
     * @param commentId
     * @return
     */
    BaseResponse<Boolean> removeComment(Long commentId);

    /**
     * 修改评论
     * @param commentId
     * @param content
     * @return
     */
    BaseResponse<Boolean> updateComment(Long commentId, String content);

    /**
     * 获取某一个的评论，歌曲100，歌单100，歌曲101
     * @param otherId
     * @return
     */
    BaseResponse<List<CommentVo>> getListById(Long otherId);
}
