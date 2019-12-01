/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.data.controller;

import com.hcmut.truyenfull.data.model.Category;
import com.hcmut.truyenfull.data.model.Chapter;
import com.hcmut.truyenfull.data.model.Comic;
import com.hcmut.truyenfull.data.repository.ComicRepository;
import com.hcmut.truyenfull.data.repository.CategoryRepository;
import com.hcmut.truyenfull.data.repository.ChapterRepository;
import static com.hcmut.truyenfull.data.util.ResponseUtil.returnComic;
import static com.hcmut.truyenfull.data.util.ResponseUtil.returnCategory;
import static com.hcmut.truyenfull.data.util.ResponseUtil.returnListComic;
import static com.hcmut.truyenfull.data.util.ResponseUtil.returnListChapter;
import com.hcmut.truyenfull.lib.Dataservice;
import com.hcmut.truyenfull.lib.PageInfor;

import java.util.List;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    
    @Autowired
    ChapterRepository chapterRepository;
    
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

    @Override
    public String SortComicByHot() throws TException {
        List<Comic> comics = comicRepository.findAll(Sort.by("views"));
        return returnListComic(comics).toString();
    }

    @Override
    public String SortComicByRating() throws TException {
        List<Comic> comics = comicRepository.findAll(Sort.by("rating"));
        return returnListComic(comics).toString();
    }

    @Override
    public String GetComicsPerPage(PageInfor pageInfor) throws TException {
        Pageable pageable = PageRequest.of(pageInfor.page-1, pageInfor.maxItemInPage);
        List<Comic> comics =  comicRepository.findAllComic(pageable);
        return returnListComic(comics).toString();
    }

    @Override
    public String GetChaptersPerPage(String urlComic, PageInfor pageInfor) throws TException {
        Pageable pageable =  PageRequest.of(pageInfor.page-1, pageInfor.maxItemInPage);
        Comic comic = comicRepository.findByUrlName(urlComic);
        List<Chapter> chapters = chapterRepository.findByComic(comic,pageable);
        return returnListChapter(chapters).toString();
    }

    @Override
    public String GetComicFull(String status) throws TException {
        List<Comic> comics = comicRepository.findByStatus(status);
        return returnListComic(comics).toString();
    }

    

    

    
    
    
    
    
}


