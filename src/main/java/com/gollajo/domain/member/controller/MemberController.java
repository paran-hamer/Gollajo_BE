package com.gollajo.domain.member.controller;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.member.dto.CreateMemberRequest;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final AccountService accountService;

    @PostMapping("/singUp")
    public ResponseEntity<String> singUp(CreateMemberRequest createMemberRequest){
        String message = memberService.signUp(createMemberRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/myAccount")
    public ResponseEntity<List<Account>> showMyAccountList(){
        List<Account> accounts = accountService.showMyAccount();

        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/point")
    public ResponseEntity<Integer> pointUp(Long targetMemberId,int point){
        //TODO : 실제 운영 서버에서는 jwt토큰으로 member불러와서 저장할 예정
        Long memberId = 1L;
        Member adminMember = memberService.findById(memberId);

        Member targetMember = memberService.findById(targetMemberId);

        int currentPoint = memberService.savePaymentMember(adminMember,targetMember, point);

        return new ResponseEntity<>(currentPoint, HttpStatus.OK);
    }
}
