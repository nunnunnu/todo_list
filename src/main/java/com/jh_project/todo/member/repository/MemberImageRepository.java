package com.jh_project.todo.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jh_project.todo.member.entity.MemberImageEntity;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImageEntity, Long> {
    public List<MemberImageEntity> findByMiSeq(Long miSeq);
    //select * from todo_image_info where tii_uri = uri order by tii_seq desc limit 1;
    //가장 나중에 입력된 이미지를 가져옴.
    public List<MemberImageEntity> findTopByUriOrderBySeqDesc(String uri);
    
}
