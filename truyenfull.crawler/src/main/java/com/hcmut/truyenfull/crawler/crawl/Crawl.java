/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.crawl;

import com.hcmut.truyenfull.crawler.model.*;
import com.hcmut.truyenfull.crawler.repository.*;
import static com.hcmut.truyenfull.crawler.util.ResponseUtil.returnListChapter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TAM
 */



public class Crawl {

    @Autowired
    ComicRepository comicRepository;

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    ChapterController chapterController;

    @Autowired
    ComicController comicController;

    @Autowired
    CategoryRepository categoryRepository;

    
    public Set<Category> getListCategory() throws IOException {
//        Chuyển List sang Set.
        Set<Category> categories = categoryRepository.findAll().stream().collect(Collectors.toSet());
        Document document = Jsoup.connect("https://truyenfull.vn/").get();
        Elements elements = document.select("div.row > div.col-md-4 > ul > li > a");
        for (Element element : elements) {
            Category newCategory = new Category();
            String link = element.absUrl("href");
            String title = element.text();
            newCategory.setTitle(title);
            newCategory.setLink(link);
            categories.add(newCategory);
        }

        categoryRepository.saveAll(categories);
        return categories;
    }

    public boolean isExistComic(String urlComic) {

        if (comicRepository.findByLink(urlComic) != null) {
            return true;
        }
        return false;
    }

    
    public Boolean crawlerAllComic(int sl) throws IOException {
        Boolean cont = true;
        String url = "https://truyenfull.vn/danh-sach/truyen-teen-hay/";
        String nextUrl = null;
        int i = 1;
        do {

            Document doc = Jsoup.connect(url).get();
            System.out.println("Title : " + doc.title());
            Elements comics = doc.select(".list>.row>div>div>.truyen-title>a");

            for (Element comic : comics) {
                if (isExistComic(comic.attr("href"))) {
                    crawlAllChapter(comic.attr("href"));
                    continue;

                } else {
                    crawlComic(comic.attr("href"));
                }

            }
            Elements nextEls = doc.select(".pagination>li>a>span");
            for (Element nextEl : nextEls) {
                if (nextEl.text().equals("Trang tiếp")) {

                    nextUrl = nextEl.parent().attr("href");
                    break;

                }
            }
            if (nextUrl == null) {
                cont = false;

            } else {
                url = nextUrl;
                nextUrl = null;
            }
            i++;

        } while (i <= sl);

        return true;
    }

    public void crawlComic(String urlComic) throws IOException {
        Document doc = Jsoup.connect(urlComic).get();

        Comic comic = new Comic();
        comic.setAuthor(doc.selectFirst("div.info > div:nth-child(1) > a").attr("title"));
        comic.setLink(urlComic);
        comic.setTitle(doc.selectFirst("div.col-xs-12.col-info-desc > h3").attr("title"));
        if (doc.selectFirst("div:nth-child(4) > span") == null) {
            comic.setStatus("Đang ra");
        } else {
            comic.setStatus(doc.selectFirst("div:nth-child(4) > span").text());
        }
        Elements geners = doc.select(".info a[itemprop=genre]");
        for (Element gener : geners) {
            Category category = categoryRepository.findByTitle(gener.absUrl("title"));
            category.addComic(comic);
        }
        comicRepository.save(comic);
        crawlAllChapter(urlComic);
    }

    public void crawlAllChapter(String url) throws IOException {

//        String url = "https://truyenfull.vn/doc-ton-tam-gioi/";
        Comic comic = comicRepository.findByLink(url).get(0);
        String nextUrl = null;
        Boolean cont = true;
        int i = 0;
        do {
            Document doc = Jsoup.connect(url).get();
            Elements chapters = doc.select(".list-chapter>li>a");
            for (Element chapter : chapters) {
                System.out.println(chapter.text());
                System.out.println(chapter.attr("href"));
                crawlAllChapter(url);

            }

            Elements nextEls = doc.select(".pagination>li>a>span");
            for (Element nextEl : nextEls) {
                if (nextEl.text().equals("Trang tiếp")) {

                    nextUrl = nextEl.parent().attr("href");
                    break;
                }
            }

            if (nextUrl == null) {
                cont = false;
            } else {
                url = nextUrl;
                nextUrl = null;
            }
            i++;

        } while (cont);

    }public void crawlChapter(String urlChapter,Comic comic) throws IOException{
        Document doc = Jsoup.connect(urlChapter).get();
        Chapter chapter = new Chapter();
        String[] strings = doc.select("div > div > a").attr("title").split(" - ",2);
        String[] string2 = strings[1].split(": ");
        chapter.setTitle(string2[1]);
        chapter.setIndex(string2[0]);
        comic.addChapter(chapter);
        chapterRepository.save(chapter);
    }
}
