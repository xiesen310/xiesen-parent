package com.github.xiesen.consanguinity.utils;

import lombok.Builder;
import lombok.Data;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

/**
 * @author xiesen
 */
public class Neo4jHelper {
    private Driver driver = null;
    private Session session = null;
    private String uri;
    private String username;
    private String password;

    public Neo4jHelper(String uri, String username, String password) {
        this.uri = uri;
        this.username = username;
        this.password = password;
    }

    public Session getNeo4jSession() {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        session = driver.session();
        return session;
    }

    public void closeAll() {
        if (null != session) {
            session.close();
        }
        if (null != driver) {
            driver.close();
        }
    }
}
