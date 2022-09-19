package com.github.xiesen.mock.util;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiese
 * @Description Kafka ScramSaslConfig
 * @Email xiesen310@163.com
 * @Date 2022/8/5 18:44
 */
public class ScramSaslConfig extends Configuration {

    private String username;
    private String password;

    public ScramSaslConfig(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("username", username);
        options.put("password", password);
        AppConfigurationEntry entry = new AppConfigurationEntry(
                "org.apache.kafka.common.security.scram.ScramLoginModule",
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);
        AppConfigurationEntry[] configurationEntries = new AppConfigurationEntry[1];
        configurationEntries[0] = entry;
        return configurationEntries;
    }

}
