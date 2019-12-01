/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.api.controller;

import com.hcmut.truyenfull.api.config.CrawlerClient;
import javax.servlet.http.HttpServletRequest;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TAM
 */
@RestController
@RequestMapping("/api")
public class CrawlerController {
    @Autowired
    CrawlerClient crawlerClient;
    
    @GetMapping(value = "/crawler",produces = "application/json")
    public boolean Crawler(HttpServletRequest request) throws TException{
        int sl = Integer.parseInt(request.getParameter("sl"));
        return crawlerClient.Crawler(sl);
    }
    @GetMapping(value = "/update",produces = "application/json")
    public boolean UpdateComics(HttpServletRequest request) throws TException{
        int sl = Integer.parseInt(request.getParameter("sl"));
        return crawlerClient.UpdateComic(sl);
    }
}
