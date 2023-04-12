package edu.sumdu.blogwebapp.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "server")
public  class ServerInfo {
    public ServerInfo() {
    }

    private int port;
    private String hostname;


    /*@Value("${hostname}")
    private String hostname;

    @Value("${server.port}")
    private  Integer  port;
*/
    public  String getHost(){

        if (port!=0){
            return  hostname+":"+port;
        }else{
            return  hostname;
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
