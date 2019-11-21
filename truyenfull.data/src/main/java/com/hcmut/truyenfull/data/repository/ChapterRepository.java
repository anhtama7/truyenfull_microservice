/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hcmut.truyenfull.data.repository;



import com.hcmut.truyenfull.data.model.Chapter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TAM
 */
@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long>{
    List<Chapter> findByLink(String link);
    
}
