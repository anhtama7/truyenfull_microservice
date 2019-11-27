/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.crawl;


import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.core.io.JsonEOFException;
import com.hcmut.truyenfull.crawler.model.Category;
import com.hcmut.truyenfull.crawler.repository.CategoryRepository;
import static com.hcmut.truyenfull.crawler.util.ResponseUtil.returnListCategory;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
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
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;
    
    @GetMapping(value = "/category", produces = "application/json")
    public ArrayNode getAllCategory() throws JsonEOFException{
        return returnListCategory(categoryRepository.findAll());
    }
    
    @PostMapping(value = "/category", produces = "application/json")
    public Category createCategory(@Valid @RequestBody Category category){
        return categoryRepository.save(category);
    }
        @GetMapping("/crawler")
    public ArrayNode crarnlerTest() throws IOException{
        List<Category> list = categoryRepository.findAll();
        
        Document doc = Jsoup.connect("https://truyenfull.vn/").get();
        System.out.println("Title : "+doc.title());
        Elements categories = doc.select("div.row > div.col-md-4 > ul > li > a");
        for(Element category : categories){
//            System.out.println("Category name :" + category.text());
//            System.out.println("Link : " +category.attr("href"));
            Category c = new Category();
            c.setTitle(category.text());
            c.setUrlName(category.attr("href"));
            if(!isExist(c,list)){
                createCategory(c);
            }
            
        }
        return returnListCategory(categoryRepository.findAll());
    }
    public boolean isExist(Category catergory,List<Category> categorys){
        for(Category c:categorys){
            if(c.getUrlName().equals(catergory.getUrlName())){
                return true;
            }
        }
        return false;
    }
}

