package com.example.wyymusic.controller;

import com.example.wyymusic.common.BaseResponse;
import com.example.wyymusic.model.domain.Comment;
import com.example.wyymusic.model.request.RemoveCommentRequest;
import com.example.wyymusic.model.request.SaveCommentRequest;
import com.example.wyymusic.model.request.UpdateCommentRequest;
import com.example.wyymusic.model.vo.CommentVo;
import com.example.wyymusic.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyc
 * @CreteDate 2023/4/30 18:10
 **/
@Controller
@RequestMapping("/comment")
@ResponseBody
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/save")
    public BaseResponse<Boolean> saveComment(@RequestBody SaveCommentRequest request) {
        return commentService.saveComment(request.getParentId(), request.getOtherId(), request.getContent());
    }

    @GetMapping("/update")
    public BaseResponse<Boolean> updateComment(@RequestBody UpdateCommentRequest request) {
        return commentService.updateComment(request.getCommentId(), request.getContent());
    }

    @GetMapping("/remove")
    public BaseResponse<Boolean> removeComment(Long commentId) {
        return commentService.removeComment(commentId);
    }

    /**+
     * 获取某一个的评论，歌曲100，歌单100，歌曲101
     * @param otherId
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<List<CommentVo>> getComment(Long otherId){
        return commentService.getListById(otherId);
    }
}
