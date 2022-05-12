package com.cliplayer.utils;

import com.saavn.player.constants.UIConstants;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class SongLyrics {

    public static void printLyrics(WebDriver browser, String query, String service) {
        String lyrics = null;
        JavascriptExecutor exe = (JavascriptExecutor) browser;

        try {
            if(service.equals("SaavnCLI")) {
                ApplicationUtils.clickWebElement(exe,
                        browser.findElement(By.cssSelector(UIConstants.SAAVN.PLAYER.CURRENT_SONG_MENU)), 1D);
                ApplicationUtils.clickWebElement(exe,
                        browser.findElement(By.cssSelector(UIConstants.SAAVN.LYRICS.SONG_DETAILS)), 1D);

                if (browser.findElements(By.cssSelector(UIConstants.SAAVN.LYRICS.LANGUAGE)).get(1).getText().contains("English")) {
                    lyrics = thirdPartyLyrics(query);
                } else {
                    ApplicationUtils.clickWebElement(exe,
                            browser.findElement(By.cssSelector(UIConstants.SAAVN.LYRICS.LYRICS_LINK)), 1D);

                    lyrics = browser.findElement(By.cssSelector(UIConstants.SAAVN.LYRICS.LYRICS_TEXT)).getText();
                }
            } else {
                lyrics = thirdPartyLyrics(query);
            }

            System.out.println("\n" + lyrics);
        } catch (Exception e) {
            System.out.println("\nLyrics not found... sorry 😓\n");
        }
    }

    public static String thirdPartyLyrics(String query) throws Exception {
        String first = Jsoup.connect("https://search.azlyrics.com/search.php?q=" + query)
                .get()
                .body()
                .selectFirst(UIConstants.SAAVN.LYRICS.SEARCH_RESULTS_LINKS)
                .getElementsByTag("a")
                .first()
                .attr("href");

        return Jsoup.connect(first)
                .get()
                .select(UIConstants.SAAVN.LYRICS.LYRICS_DIV).get(4)
                .wholeText();
    }
}
