package com.decaffeine.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class MusicService {

    private static final String WEB_DRIVER_PATH = "/Users/hyejin/projects/music-migration/src/main/resources/chromedriver";
    private static final String TARGET_PAGE_PATH = "https://music.bugs.co.kr";

    private WebDriver webDriver;

    public static void main(String[] args) {
        MusicService musicService = new MusicService();
        musicService.crawl();
    }

    private String base_url;

    public MusicService() {
        System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH);
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setCapability("ignoreProtectedModeSettings", true);
        chromeOptions.addArguments("--remote-debugging-port=1559");
//        webDriver = new ChromeDriver(chromeOptions);
        chromeOptions.setExperimentalOption("debuggerAddress","127.0.0.1:1559");
        webDriver = new ChromeDriver(chromeOptions);
    }

    public void crawl() {
        try {
            String orignalWindow = webDriver.getWindowHandle();

//            webDriver.get(TARGET_PAGE_PATH);
            WebElement element = webDriver.findElement(By.className("login"));
            element.click();
            WebElement paycoElement = webDriver.findElement(By.className("btnPaycoLogin"));
            paycoElement.click();

            for (String windowHandle : webDriver.getWindowHandles()) {
                if (!orignalWindow.contentEquals(windowHandle)) {
                    webDriver.switchTo().window(windowHandle);
                    break;
                }
            }

            Thread.sleep(2000);

            WebElement loginButton = webDriver.findElement(By.id("loginBtn"));
            loginButton.submit();

            Thread.sleep(2000);

            webDriver.switchTo().window(orignalWindow);

            webDriver.get("https://music.bugs.co.kr/user/library/like/track?wl_ref=M_left_03_02");
            //System.out.println(webDriver.getPageSource());
            WebElement trackList = webDriver.findElement(By.id("USER_LIKE_TRACK1234"));
            List<WebElement> titleElements = trackList.findElements(By.className("title"));
            List<WebElement> artistElements = trackList.findElements(By.className("artist"));

            List<String> titles = new ArrayList<>();
            List<String> artists = new ArrayList<>();
            for (int i = 1 ; i < titleElements.size() ; i++) {
                titles.add(titleElements.get(i).getText());
                artists.add(artistElements.get(i).getText().replaceAll("\\([^()]*\\)",""));
            }

            for (int i = 0 ; i < titles.size() ; i++) {
                System.out.println(i + " ## " + titles.get(i) + " " + artists.get(i));
                webDriver.get("https://music.apple.com/us/search?term=" +  titles.get(i) + " " + artists.get(i));
                Thread.sleep(10000); // APPLE MUSIC 반응속도가 생각보다 엄청 느림
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.close();
        }
    }

}
