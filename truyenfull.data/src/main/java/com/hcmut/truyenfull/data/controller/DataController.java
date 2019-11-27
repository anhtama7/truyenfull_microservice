/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.data.controller;

import com.hcmut.truyenfull.data.model.Category;
import com.hcmut.truyenfull.data.model.Comic;
import com.hcmut.truyenfull.data.repository.ComicRepository;
import com.hcmut.truyenfull.data.repository.CategoryRepository;
import static com.hcmut.truyenfull.data.util.ResponseUtil.returnComic;
import static com.hcmut.truyenfull.data.util.ResponseUtil.returnCategory;
import static com.hcmut.truyenfull.data.util.ResponseUtil.returnListComic;
import com.hcmut.truyenfull.lib.Dataservice;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author TAM
 */
@Controller
public class DataController implements Dataservice.Iface{
    @Autowired
    ComicRepository comicRepository;
    
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public String GetComic(String string) {
        Comic comic = comicRepository.findByUrlName(string);
        return returnComic(comic).toString();
        
    }
    

    @Override
    public String GetCategory(String name) throws TException {
        Category category = categoryRepository.findByUrlName(name);
        return returnCategory(category).toString();
    }

    @Override
    public String GetAllComicInCategory(String name) throws TException {
        Category category = categoryRepository.findByUrlName(name);
        return returnListComic(category.getComics()).toString() ;
    }
    
    
    
}
