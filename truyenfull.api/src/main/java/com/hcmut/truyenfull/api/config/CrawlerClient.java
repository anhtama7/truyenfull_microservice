/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.api.config;

import com.hcmut.truyenfull.lib.CrawlerService;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

import org.springframework.stereotype.Component;

/**
 *
 * @author TAM
 */

@Component
public class CrawlerClient extends CrawlerService.Client{
    
    public CrawlerClient(TProtocolFactory tProtocolFactory) throws TTransportException {
        super(tProtocolFactory.getProtocol(new THttpClient("http://localhost:8082/crawler")));
    }
    
}
