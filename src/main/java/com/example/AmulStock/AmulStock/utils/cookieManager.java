package com.example.AmulStock.AmulStock.utils;

import org.springframework.stereotype.Component;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.net.CookieManager;

@Component
public class cookieManager {
    public final CookieManager cookieManager;
    private static final String AMUL_BASE_URL = "https://shop.amul.com";

    public cookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }
    public void storeCookies(List<String> cookieHeaders) {
        try{
            URI uri = new URI(AMUL_BASE_URL);
            for (String header: cookieHeaders){
                List<HttpCookie> cookies= HttpCookie.parse(header);
                for (HttpCookie cookie : cookies){
                    cookieManager.getCookieStore().add(uri,cookie);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to store cookies",e);
        }

    }
    public String getCookiesAsString(){
        try{
            URI uri =new URI(AMUL_BASE_URL);
            StringBuilder sb=new StringBuilder();
            for (HttpCookie cookie: cookieManager.getCookieStore().get(uri)){
                if (sb.length()>0){sb.append("; ");
                sb.append(cookie.getName()).append("=").append(cookie.getValue());}
            }
            return sb.toString();
    } catch (Exception e) {
            throw new RuntimeException("Failed to get Cookie as a String",e);
        }
    }
}
