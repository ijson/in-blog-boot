package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.CountEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/9 4:38 PM
 */
public interface CountDao {

    CountEntity create(CountEntity entity);

    CountEntity findCountById(String id);

    CountEntity findCountByWebType(String type);

    Map<String, Long> findCountByIds(Set<String> ids);

    CountEntity createOrUpdate (CountEntity entity);

    CountEntity inc(CountEntity entity);

    List<CountEntity> findHot();

}
