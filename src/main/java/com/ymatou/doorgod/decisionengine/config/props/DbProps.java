/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.doorgod.decisionengine.config.props;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

@Component
@DisconfFile(fileName = "db.properties")
public class DbProps {

    private String driver;
    private String url;
    private String username;
    private String password;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;


    private String mongoAddress;
    private String mongoDatabaseName;

    private String deviceIdMongoAddress;
    private String deviceIdMongoDatabaseName;

    @DisconfFileItem(name = "jdbc.driver")
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @DisconfFileItem(name = "jdbc.url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @DisconfFileItem(name = "jdbc.username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DisconfFileItem(name = "jdbc.password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DisconfFileItem(name = "jdbc.initialSize")
    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    @DisconfFileItem(name = "jdbc.minIdle")
    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    @DisconfFileItem(name = "jdbc.maxActive")
    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }


    @DisconfFileItem(name = "mongo.mongoAddress")
    public String getMongoAddress() {
        return mongoAddress;
    }

    public void setMongoAddress(String mongoAddress) {
        this.mongoAddress = mongoAddress;
    }

    @DisconfFileItem(name = "mongo.mongoDatabaseName")
    public String getMongoDatabaseName() {
        return mongoDatabaseName;
    }

    public void setMongoDatabaseName(String mongoDatabaseName) {
        this.mongoDatabaseName = mongoDatabaseName;
    }

    @DisconfFileItem(name = "mongo.deviceIdMongoAddress")
    public String getDeviceIdMongoAddress() {
        return deviceIdMongoAddress;
    }

    public void setDeviceIdMongoAddress(String deviceIdMongoAddress) {
        this.deviceIdMongoAddress = deviceIdMongoAddress;
    }

    @DisconfFileItem(name = "mongo.deviceIdMongoDatabaseName")
    public String getDeviceIdMongoDatabaseName() {
        return deviceIdMongoDatabaseName;
    }

    public void setDeviceIdMongoDatabaseName(String deviceIdMongoDatabaseName) {
        this.deviceIdMongoDatabaseName = deviceIdMongoDatabaseName;
    }
}
