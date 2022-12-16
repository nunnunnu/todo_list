package com.jh_project.todo.member.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.jh_project.todo.member.data.LoginVO;
import com.jh_project.todo.member.entity.MemberImageEntity;
import com.jh_project.todo.member.entity.MemberInfoEntity;
import com.jh_project.todo.member.repository.MemberImageRepository;
import com.jh_project.todo.member.repository.MemberRepository;
import com.jh_project.todo.utils.AESAlgorith;

@Service
public class MemberService {
    @Autowired MemberRepository m_repo;
    @Autowired MemberImageRepository mi_repo;
    
    public Map<String, Object>  addMemberImage(MemberImageEntity data, Long miSeq){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        data.setMiSeq(miSeq);
        mi_repo.save(data);
        map.put("status", true);
        map.put("message", "이미지가 저장되었습니다.");
        map.put("code", HttpStatus.OK);
        return map;
    }
    public String getFileNameByUri(String uri){
        List<MemberImageEntity> data = mi_repo.findTopByUriOrderBySeqDesc(uri);
        return data.get(0).getFileName();
    }
    
    public Map<String, Object> addMember(MemberInfoEntity data){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        if(m_repo.countByEmail(data.getEmail())==1){
            resultMap.put("status", false);
            resultMap.put("message", data.getEmail()+"은/는 이미 가입된 이메일입니다.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }else{
            try{
                String encPwd = AESAlgorith.Encrypt(data.getPwd());
                data.setPwd(encPwd);
            }catch(Exception e){e.printStackTrace();}

            resultMap.put("status", true);
            resultMap.put("message", "회원이 등록되었습니다.");
            resultMap.put("code", HttpStatus.CREATED);
            m_repo.save(data);
        }
        return resultMap;
    }
    public Map<String, Object> loginMember(LoginVO data){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        MemberInfoEntity loginUser = null;
        try{
            loginUser = m_repo.findByEmailAndPwd(data.getEmail(), AESAlgorith.Encrypt(data.getPwd()));
        }catch(Exception e){e.printStackTrace();}
        if(loginUser==null){
            resultMap.put("status", false);
            resultMap.put("message", "로그인 실패. 이메일 또는 비밀번호를 확인해주세요.");
            resultMap.put("code", HttpStatus.BAD_REQUEST);
        }else{
            resultMap.put("status", true);
            resultMap.put("message", "로그인 성공");
            resultMap.put("code", HttpStatus.ACCEPTED);
            resultMap.put("loginUser", loginUser);
        }
        return resultMap;
    }
}
