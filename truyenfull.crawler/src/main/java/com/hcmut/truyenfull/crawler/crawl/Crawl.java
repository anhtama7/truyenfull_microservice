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
@Controller
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
            String[] strings = element.absUrl("href").split("/");
            String title = element.text();
            newCategory.setTitle(title);
            String urlName = strings[strings.length - 1];
            newCategory.setUrlName(urlName);
            categories.add(newCategory);
        }

        categoryRepository.saveAll(categories);
        return categories;
    }

    public boolean isExistComic(String urlComic) {
        String[] strings = urlComic.split("/");
        String urlName = strings[strings.length - 1];
        if (comicRepository.findByUrlName(urlName) != null) {
            return true;
        }
        return false;
    }

    
    public Boolean crawlerAllComic(int sl) throws IOException {
        Set<Category> categories = categoryRepository.findAll().stream().collect(Collectors.toSet());
        if (categories.isEmpty()) {
            categories = getListCategory();
        }
        Boolean cont = true;
        String url = "https://truyenfull.vn/danh-sach/ngon-tinh-sac/";
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
            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if (nextEl != null) {

                if (!nextEl.attr("href").equals("javascript:void(0)")) {

                    url = nextEl.attr("href");
                    System.out.println(nextEl.attr("href"));

                } else {
                    break;
                }

            }
            i++;

        } while (i <= sl);

        return true;
    }

    public void crawlComic(String urlComic) throws IOException {
        Document doc = Jsoup.connect(urlComic).get();

        Comic comic = new Comic();
        comic.setAuthor(doc.selectFirst("div.info > div:nth-child(1) > a").attr("title"));
        String[] strings = urlComic.split("/");
        String urlName = strings[strings.length - 1];
        comic.setUrlName(urlName);
        comic.setTitle(doc.selectFirst(".col-info-desc h3.title").text());
        if (doc.selectFirst("div:nth-child(4) > span") == null) {
            comic.setStatus("Đang ra");
        } else {
            comic.setStatus(doc.selectFirst("div:nth-child(4) > span").text());
        }
        Elements geners = doc.select("div:nth-child(2) > a");
        for (Element gener : geners) {
            Category category = categoryRepository.findByTitle(gener.attr("title"));
            System.out.println(category.getUrlName());
            if (category != null) {
                comic.getCategorys().add(category);
            }
        }
        comicRepository.save(comic);
        crawlAllChapter(urlComic);
    }

    public void crawlAllChapter(String url) throws IOException {

//        String url = "https://truyenfull.vn/doc-ton-tam-gioi/";
        String[] strings = url.split("/");
        String urlName = strings[strings.length - 1];
        Comic comic = comicRepository.findByUrlName(urlName);
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
                crawlChapter(chapter.attr("href"), comic, indexChapter);
                indexChapter++;
            }

            Element nextEl = doc.select(" div > ul > li.active + li >a").first();
            if (nextEl != null) {
                url = nextEl.attr("href");
                System.out.println(nextEl.attr("href"));
            } else {
                break;
            }
            i++;

        } while (cont);

    }

    public void crawlChapter(String urlChapter, Comic comic, int indexChapter) throws IOException {
        Document doc = Jsoup.connect(urlChapter).get();
        Chapter chapter = new Chapter();
        String title = doc.select(".chapter .chapter-title").attr("title");
        String[] strings = urlChapter.split("/");
        String urlName = strings[strings.length - 1];
        chapter.setUrlName(urlName);
        chapter.setTitle(title);
        chapter.setIndex(indexChapter);
        comic.addChapter(chapter);
        chapterRepository.save(chapter);
    }
    public void crawlComicTest() throws IOException {
        String urlComic = "https://truyenfull.vn/vo-yeu-den-roi/";
        Document doc = Jsoup.connect(urlComic).get();

        
        Elements geners = doc.select("div:nth-child(2) > a");
        for (Element gener : geners) {
            Category category = categoryRepository.findByTitle(gener.attr("title"));
            System.out.println(category.getUrlName());
            
        }
        
    }
//    public Boolean crawlerAllComic2(int sl){
//        System.out.println("da thuc hien");
//        return true;
//    }
    
    
}
