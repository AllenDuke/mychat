package com.github.AllenDuke.service.impl;


import com.github.AllenDuke.dao.PyqMapper;
import com.github.AllenDuke.pojo.do0.Pyq;
import com.github.AllenDuke.service.PyqService;
import com.github.AllenDuke.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/4/25
 */
@Service
@Transactional
public class PyqServiceImpl implements PyqService {

    @Autowired
    PyqMapper pyqMapper;

    @Autowired
    FastDFSClient fastDFSClient;

    @Override
    public void save(Pyq pyq) {
        pyqMapper.savePyq(pyq);
    }

    @Override
    public List<Pyq> getPyqsByUserId(Integer userId) {
        List<Pyq> pyqs = pyqMapper.getPyqsByUserId(userId);
        return pyqs;
    }

    @Override
    public void delete(Integer pyqId) {
        String path = pyqMapper.getPyq(pyqId).getPictureLocation();
        pyqMapper.deletePyq(pyqId);//todo 返回图片路径
        fastDFSClient.deleteFile(path);
    }

    @Override
    public List<Pyq> getUserAndFriendPyqsByUserId(Integer userId) {
        List<Pyq> pyqs = pyqMapper.getUserAndFriendPyqsByUserId(userId);
        return pyqs;
    }

    @Override
    public Pyq getPyq(Integer pyqId) {
        return pyqMapper.getPyq(pyqId);
    }

    @Override
    public void updatePyq(Pyq pyq) {
        pyqMapper.updatePyq(pyq);
    }
}
