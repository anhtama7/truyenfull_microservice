/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.data.controller;

import com.hcmut.truyenfull.data.model.Comic;
import com.hcmut.truyenfull.data.repository.ComicRepository;
import com.hcmut.truyenfull.data.util.ResponseUtil;
import com.hcmut.truyenfull.lib.Dataservice;
import org.springframework.stereotype.Controller;

/**
 *
 * @author TAM
 */
@Controller
public class DataController implements Dataservice.Iface{
    ComicRepository comicRepository;
    ResponseUtil responseUtil;
    
    @Override
    public String GetComic(String string){
         Comic comic = comicRepository.findByName(string);
         return responseUtil.returnComic(comic).toString();
    }
}
