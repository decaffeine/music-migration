package com.decaffeine.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

//import org.springframework.stereotype.Service;
//
//@Service
public class MusicService {

    private WebDriver webDriver;

    public static void main(String[] args) {
        MusicService musicService = new MusicService();
        musicService.crawl();
    }

    private String base_url;

    public MusicService() {
        System.setProperty("webdriver.chrome.driver","/Users/hyejin/projects/music-migration/src/resources/chromedriver");
        webDriver = new ChromeDriver();
        base_url = "https://music.bugs.co.kr";

    }

    public void crawl() {
        try {
            webDriver.get(base_url);
            System.out.println(webDriver.getPageSource());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.close();
        }
    }

}
