package com.github.AllenDuke.service.impl;


import com.github.AllenDuke.dao.PyqCommentReplyMapper;
import com.github.AllenDuke.pojo.do0.PyqCommentReply;
import com.github.AllenDuke.service.PyqCommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/5/8
 */
@Service
@Transactional
public class PyqComentReplyServiceImpl implements PyqCommentReplyService {

    @Autowired
    PyqCommentReplyMapper replyMapper;

    @Override
    public List<PyqCommentReply> getRepliesByPyqId(Integer pyqId) {
        return replyMapper.getPyqCommentRepliesByPyqId(pyqId);
    }

    @Override
    public void save(PyqCommentReply reply) {
        replyMapper.savePyqCommentReply(reply);
    }

    @Override
    public void delete(Integer replyId) {
        replyMapper.deletePyqCommentReply(replyId);
    }
}
