/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.crawl;

import com.hcmut.truyenfull.crawler.model.*;
import com.hcmut.truyenfull.crawler.repository.*;
import static com.hcmut.truyenfull.crawler.util.ResponseUtil.returnListChapter;

import com.fasterxml.jackson.core.io.JsonEOFException;
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
public class ChapterController {

    @Autowired
    ChapterRepository chapterRepository;
    ComicRepository comicRepository;

    @GetMapping(value = "/chapter", produces = "application/json")
    public ArrayNode getAllChapters() throws JsonEOFException {
        return returnListChapter(chapterRepository.findAll());
    }

    @PostMapping(value = "/chapter", produces = "application/json")
    public Chapter createChapter(@Valid @RequestBody Chapter chapter) {
        return chapterRepository.save(chapter);
    }

    @GetMapping("/chapter_crawler")
    public ArrayNode crarnlerTest() throws IOException {

        Document doc2 = Jsoup.connect("https://truyenfull.vn/choc-tuc-vo-yeu-mua-mot-tang-mot-241019/").get();
        Elements chapters = doc2.select(".list-chapter>li>a");
       
        
        for (Element chapter : chapters) {
            
            Chapter c = new Chapter();
            c.setTitle(chapter.text());
            c.setLink(chapter.attr("href"));
            if (!isExist(c, chapterRepository.findAll())) {
                createChapter(c);

            }

        }

        return returnListChapter(chapterRepository.findAll());
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
