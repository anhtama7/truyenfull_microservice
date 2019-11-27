/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.crawl;



import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hcmut.truyenfull.crawler.model.Chapter;
import com.hcmut.truyenfull.crawler.model.Comic;
import com.hcmut.truyenfull.crawler.repository.ChapterRepository;
import com.hcmut.truyenfull.crawler.repository.ComicRepository;
import com.hcmut.truyenfull.crawler.util.ResponseUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TAM
 */
@RestController
@RequestMapping("/api")
public class ComicController {

    @Autowired
    ComicRepository comicRepository;

    ChapterRepository chapterRepository;
    ChapterController chapterController;

    @GetMapping(value = "/comic", produces = "application/json")
    public ArrayNode getAllComics() {
        return ResponseUtil.returnListComic(comicRepository.findAll());
    }

    @PostMapping(value = "/comic", produces = "application/json")
    public Comic createComic(@Valid @RequestBody Comic comic) {
        return comicRepository.save(comic);
    }

    @GetMapping("/crawler2")
    public Boolean crawlerAllComic() throws IOException {
        Boolean cont = true;
        String url = "https://truyenfull.vn/danh-sach/truyen-teen-hay/";
        String nextUrl = null;
        int i = 1;
        do {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title : " + doc.title());
            Elements comics = doc.select(".list>.row>div>div>.truyen-title>a");

            for (Element comic : comics) {
                System.out.println(comic.text());
                if (isExistComic(comic.attr("href"))) {
                    System.out.println("comic da ton tai");
                    continue;

                } else {
                    System.out.println("cao truyen");
                }

            }
            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if(nextEl != null ){
                
                if (!nextEl.attr("href").equals("javascript:void(0)")) {

                    url = nextEl.attr("href");
                    System.out.println(nextEl.attr("href"));

                }
                else {
                    break;
                }
                
                    
            }
            
            i++;

        } while (i < 5);

        return true;
    }
    public boolean isExistComic(String Title) {

        if (comicRepository.findByTitle(Title) != null) {
            return true;
        }
        return false;
    }
    
    @GetMapping("/crawler3")
    public boolean crawlAllChapter() throws IOException {

        String  url = "https://truyenfull.vn/phi-thien/";
        
        String nextUrl = null;
        Boolean cont = true;
        int indexChapter = 1;
        int i = 0;
        do {
            Document doc = Jsoup.connect(url).get();
            Elements chapters = doc.select(".list-chapter>li>a");
            for (Element chapter : chapters) {
                System.out.println(chapter.text());
                System.out.println(chapter.attr("href"));
                crawlChapter(chapter.attr("href"));
                indexChapter++;

            }

            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if(nextEl != null ){
                
                

                    url = nextEl.attr("href");
                    System.out.println(nextEl.attr("href"));            
            }else{
                break;
            }

            
            i++;

        } while (i<3);
        return true;

    }
    
    public void crawlChapter(String urlChapter) throws IOException{
//        urlChapter = "https://truyenfull.vn/hao-mon-ke-nu/chuong-1/";
        Document doc = Jsoup.connect(urlChapter).get();
        Chapter chapter = new Chapter();
        Elements chapters = doc.select("div > div > h2 > a");
        for(Element c : chapters){
            System.out.println(c.attr("title"));
            
        }
        
//        chapter.setTitle(title);
//        chapter.setIndex(indexChapter);
//        comic.addChapter(chapter);
//        chapterRepository.save(chapter);
        
    }
    

}
