package com.example.wyymusic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.common.ErrorCode;
import com.example.wyymusic.common.Results;
import com.example.wyymusic.common.exception.BusinessException;
import com.example.wyymusic.model.domain.Comment;
import com.example.wyymusic.model.domain.User;
import com.example.wyymusic.model.vo.CommentVo;
import com.example.wyymusic.service.CommentService;
import com.example.wyymusic.mapper.CommentMapper;
import com.example.wyymusic.service.UserService;
import com.example.wyymusic.utils.UserHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.wyymusic.constant.CommonConstant.PREFIX_MIN;

/**
 * @author xyc
 * @description 针对表【comment(评论表)】的数据库操作Service实现
 * @createDate 2023-04-26 09:12:49
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {


    @Resource
    private UserService userService;

    /**
     * otherId为歌单或者是歌曲或者是动态的is
     *
     * @param parentId
     * @param otherId
     * @param content
     * @return
     */
    @Override
    public BaseResponse<Boolean> saveComment(Long parentId, Long otherId, String content) {

        Long userId = UserHolder.getUser().getUserId();

        //判断参数是否有误
        isHaveError(parentId, otherId, content, userId);

        Comment comment = new Comment();
        comment.setOtherId(otherId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);

        boolean isSuccess = this.save(comment);
        return Results.success(isSuccess);
    }

    @Override
    public BaseResponse<Boolean> removeComment(Long commentId) {

        Long userId = UserHolder.getUser().getUserId();

        if (commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("commentId", commentId).eq("userId", userId);
        boolean isSuccess = this.remove(commentQueryWrapper);
        return Results.success(isSuccess);
    }

    @Override
    public BaseResponse<Boolean> updateComment(Long commentId, String content) {

        Long userId = UserHolder.getUser().getUserId();

        if (commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (content == null || content.length() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean isSuccess = this.update().eq("commentId", commentId).eq("userId", userId).set("content", content).update();
        return Results.success(isSuccess);
    }


    @Override
    public BaseResponse<List<CommentVo>> getListById(Long otherId) {
        if (otherId <= PREFIX_MIN) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        List<Comment> comments = this.query().eq("otherId", otherId).list();


        List<CommentVo> commentVos = BeanUtil.copyToList(comments, CommentVo.class);

        List<CommentVo> commentVoList = commentVos.stream().map(comment -> {
            User user = userService.getById(comment.getUserId());
            comment.setNickName(user.getNickName());
            comment.setImagePath(user.getAvatarUrl());
            List<Comment> children = query().eq("parentId", comment.getCommentId()).list();
            List<CommentVo> childCommonVos = BeanUtil.copyToList(children, CommentVo.class);
            childCommonVos.stream().map(commentVo -> {
                User userC = userService.getById(commentVo.getUserId());
                commentVo.setNickName(userC.getNickName());
                commentVo.setImagePath(userC.getAvatarUrl());
                return commentVo;
            }).collect(Collectors.toList());
            comment.setChildList(childCommonVos);
            return comment;
        }).collect(Collectors.toList());

        return Results.success(commentVoList);
    }


//    public List<CommentVo> setUser(List<CommentVo> commentVoList){
//
//    }

    private void isOne(Long userId, Long loginUserId, Long commentId) {
        if (userId <= 0 || loginUserId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (!userId.equals(loginUserId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (commentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    private void isHaveError(Long parentId, Long otherId, String content, Long userId) {
        if (otherId == null && parentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (parentId == null && otherId < PREFIX_MIN) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        if (content == null || content.length() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


    }
}




