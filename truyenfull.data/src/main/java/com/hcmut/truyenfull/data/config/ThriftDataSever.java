/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.data.config;

import com.hcmut.truyenfull.data.controller.DataController;
import com.hcmut.truyenfull.lib.Dataservice;
import org.apache.thrift.TProcessorFactory;
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

public class ThriftDataSever {
    @Bean
    public TProtocolFactory tProtocolFactory(){
        return new TBinaryProtocol.Factory();
    }
    
    @Bean
    public ServletRegistrationBean thriftBookServlet(TProtocolFactory tProtocolFactory,
            DataController dataController){
        TServlet tServlet = new TServlet(new Dataservice.Processor<>(dataController),tProtocolFactory);
        
        return new ServletRegistrationBean(tServlet,"/data");
    }
}
