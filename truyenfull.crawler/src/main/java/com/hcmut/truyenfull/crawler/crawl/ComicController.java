/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.crawl;

import com.hcmut.truyenfull.crawler.model.*;
import com.hcmut.truyenfull.crawler.repository.*;
import static com.hcmut.truyenfull.crawler.util.ResponseUtil.returnListComic;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
        return returnListComic(comicRepository.findAll());
    }

    @PostMapping(value = "/comic", produces = "application/json")
    public Comic createComic(@Valid @RequestBody Comic comic) {
        return comicRepository.save(comic);
    }

    @GetMapping("/crawler2")
    public ArrayNode crarnlerTest() throws IOException, NullPointerException {
        Boolean a = true;
        
        do {
            
            Document doc = Jsoup.connect("https://truyenfull.vn/danh-sach/truyen-hot/").get();
            System.out.println("Title : " + doc.title());
            Elements comics = doc.select(".list>.row>div>div>.truyen-title>a");

            for (Element comic : comics) {
//            System.out.println("Book name :" + book.text());
//            System.out.println("Link : " + book.attr("href"));

                Comic cm = new Comic();
                cm.setTitle(comic.text());
                cm.setLink(comic.attr("href"));

                if (!isExist(cm, comicRepository.findAll())) {
                    createComic(cm);
                }

            }
            Elements test = doc.select(".pagination>li>a>span.glyphicon");
            if(test.isEmpty()){
                a =false;
            }
            
        } while (a);
        
        return returnListComic(comicRepository.findAll());
//        return true;
    }

    public boolean isExist(Comic comic, List<Comic> comics) {
        Boolean a = true;
        for (Comic c : comics) {
            if (c.getLink().equals(comic.getLink())) {
                return true;
            }
        }
        return false;
    }

    public boolean isExist(Chapter chapter, List<Chapter> chapters) {

        for (Chapter c : chapters) {
            if (c.getLink().equals(chapter.getLink())) {
                return true;
            }
        }

        return false;
    }

}
