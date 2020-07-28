package com.github.AllenDuke.service;


import com.github.AllenDuke.pojo.do0.PyqComment;

import java.util.List;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/5/8
 */
public interface PyqCommentService {

    List<PyqComment> getCommentsByPyqId(Integer pyqId);

    void delete(Integer commentId);

    void save(PyqComment comment);
}
