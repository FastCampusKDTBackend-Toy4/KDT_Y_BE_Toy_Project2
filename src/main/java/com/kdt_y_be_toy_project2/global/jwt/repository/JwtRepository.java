package com.kdt_y_be_toy_project2.global.jwt.repository;

public interface JwtRepository {

    void save(JwtEntity entity);

    String getByKey(String key);

}
