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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TAM
 */
@RestController
public class DataController {
     @Autowired
     DataClient dataClient;
     
     @GetMapping(value = "/data",produces = "application/json")
     public String getComic(HttpServletRequest request) throws TException {
         String name = request.getParameter("name");
         return dataClient.GetComic(name);
     }
}
