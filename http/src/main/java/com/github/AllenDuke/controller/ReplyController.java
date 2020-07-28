package com.github.AllenDuke.controller;


import com.github.AllenDuke.pojo.ao.PyqCommentReplyAO;
import com.github.AllenDuke.pojo.do0.PyqCommentReply;
import com.github.AllenDuke.result.ErrorCode;
import com.github.AllenDuke.result.Result;
import com.github.AllenDuke.service.PyqCommentReplyService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/5/8
 */
@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    PyqCommentReplyService replyService;

    @Autowired
    Mapper dozerMapper;

    @RequestMapping("/getRepliesByPyqId")
    public Result<List<PyqCommentReply>> getRepliesByPyqId(Integer pyqId){
        List<PyqCommentReply> replies=null;
        try{
            replies = replyService.getRepliesByPyqId(pyqId);
        }catch (Exception e){
            return Result.fail(ErrorCode.INTERNAL_ERROR,e.getMessage());
        }
        return Result.success(replies);
    }

    @RequestMapping("/save")
    public Result save(@RequestBody PyqCommentReplyAO replyAO){
        try {
            PyqCommentReply reply=dozerMapper.map(replyAO,PyqCommentReply.class);
            replyService.save(reply);
        }catch (Exception e){
            return Result.fail(ErrorCode.INTERNAL_ERROR,e.getMessage());
        }
        return Result.success();
    }

    @RequestMapping("/delete")
    public Result delete(Integer replyId){
        try {
            replyService.delete(replyId);
        }catch (Exception e){
            return Result.fail(ErrorCode.INTERNAL_ERROR,e.getMessage());
        }
        return Result.success();
    }
}
