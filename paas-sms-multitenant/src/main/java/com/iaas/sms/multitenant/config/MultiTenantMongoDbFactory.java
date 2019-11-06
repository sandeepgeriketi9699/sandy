package com.iaas.sms.multitenant.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MultiTenantMongoDbFactory extends SimpleMongoDbFactory {

	@Value("${multitenant.tenantKey}")
    private String tenantKey;

    public MultiTenantMongoDbFactory(MongoClientURI uri) throws UnknownHostException {
        super(uri);
    }

    @Override
    public MongoDatabase getDb() throws DataAccessException {
    	String  tenant = (String) RequestContextHolder.getRequestAttributes().getAttribute("name", RequestAttributes.SCOPE_REQUEST);
        return getDb(String.valueOf(tenant));
    }
}