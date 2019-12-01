/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.api.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcmut.truyenfull.api.config.DataClient;
import javax.servlet.http.HttpServletRequest;
import org.apache.thrift.TException;
import com.hcmut.truyenfull.lib.PageInfor;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TAM
 */
@RestController
@RequestMapping("/api")
public class DataController {
     @Autowired
     DataClient dataClient;
     
//     @Autowired
//     PageInfor pageInfor;
     
     
     @GetMapping(value = "/getComic",produces = "application/json")
     public String getComic(@PathVariable String comicUrlName) throws TException {
         
         return dataClient.GetComic(comicUrlName);
     }
     @GetMapping(value = "/getCategory",produces = "application/json")
     public String getCategory(HttpServletRequest request) throws TException {
         String name = request.getParameter("name");
         return dataClient.GetCategory(name);
     }
     @GetMapping(value = "/getAllComicInCategory",produces = "application/json")
     public String getAllComicInCategory(HttpServletRequest request) throws TException {
         String categoryUrlName = request.getParameter("categoryUrlName");
         return dataClient.GetAllComicInCategory(categoryUrlName);
     }
     @GetMapping(value = "/getComicsHot",produces = "application/json")
     public String getAllComicsHot() throws TException{
         return dataClient.SortComicByHot();
     }
     @GetMapping(value = "/getComicsHighRating",produces = "application/json")
     public String getAllComicsHighRating() throws TException{
         return dataClient.SortComicByRating();
     }
     @GetMapping(value = "/getComicsPerPage",produces = "application/json")
     public String getComicsPerPage(@RequestBody PageInfor pageInfor) throws TException{
         pageInfor.setPage(1);
         return dataClient.GetComicsPerPage(pageInfor);
     }
     @GetMapping(value = "/{comicUrlName}/trang-{page}",produces = "application/json")
     public String getChaptersPerPage(@PathVariable String comicUrlName, @PathVariable int page,@RequestBody PageInfor pageInfor) throws TException{
         pageInfor.setPage(page);
         return dataClient.GetChaptersPerPage(comicUrlName, pageInfor);
     
     }
          @GetMapping(value = "/getAllComicFull",produces = "application/json")
     public String getAllComicFull(HttpServletRequest request) throws TException {
         String status = request.getParameter("status");
         return dataClient.GetComicFull(status);
     }
     
}
