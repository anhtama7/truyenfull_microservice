/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.crawler.config;

import com.hcmut.truyenfull.crawler.controller.CrawlerController;
import com.hcmut.truyenfull.lib.CrawlerService;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author TAM
 */
@Configuration
public class ThriftCrawlerServer {
    @Bean
    public TProtocolFactory tProtocolFactory(){
        return new TBinaryProtocol.Factory();
    }
    
    @Bean
    public ServletRegistrationBean thriftBookServlet(
            TProtocolFactory tProtocolFactory,CrawlerController crawlerController){
        TServlet tServlet = new TServlet(new CrawlerService.Processor <>(crawlerController), tProtocolFactory);
        return new ServletRegistrationBean(tServlet,"/crawler");
    }
}
