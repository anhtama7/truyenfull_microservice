/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.controller;
import com.hcmut.truyenfull.crawler.crawl.Crawl;
import com.hcmut.truyenfull.lib.CrawlerService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
/**
 *
 * @author TAM
 */
@Controller
public class CrawlerController implements CrawlerService.Iface{
    Crawl crawlService;
    @Override
    public boolean Crawler(int sl) {
        try {
            return crawlService.crawlerAllComic(sl);
        } catch (IOException ex) {
            Logger.getLogger(CrawlerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
