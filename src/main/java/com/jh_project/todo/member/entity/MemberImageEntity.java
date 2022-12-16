package com.jh_project.todo.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "member_images_info")
public class MemberImageEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mii_seq")       private Long seq;
    @Column(name = "mii_mi_seq")    private Long miSeq;
    @Column(name = "mii_file_name") private String fileName;
    @Column(name = "mii_uri")       private String uri;
}
