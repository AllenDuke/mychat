package com.github.AllenDuke.dao;


import com.github.AllenDuke.pojo.do0.UserCollection;

import java.util.List;

public interface UserCollectionMapper {
    int deleteUserCollection(Integer id);

    int saveUserCollection(UserCollection record);

    UserCollection getUserCollection(Integer id);

    List<UserCollection> listUserCollections();

    int updateUserCollection(UserCollection record);
}