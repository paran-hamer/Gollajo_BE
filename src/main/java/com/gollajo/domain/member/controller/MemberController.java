package com.gollajo.domain.member.controller;

import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.member.dto.CreateMemberRequest;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final AccountService accountService;

    @Operation(summary = "회원가입", description = "회원가입한다.")
    @ApiResponse(responseCode = "201", description = "회원가입")
    @PostMapping("/singUp")
    public ResponseEntity<String> singUp(CreateMemberRequest createMemberRequest){
        String message = memberService.signUp(createMemberRequest);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @Operation(summary = "거래내역 조회하기", description = "모든 거래내역을 조회한다.")
    @ApiResponse(responseCode = "200", description = "거래내역 조회 성공")
    @GetMapping("/myAccount")
    public ResponseEntity<List<Account>> showMyAccountList(){
        List<Account> accounts = accountService.showMyAccount();

        return ResponseEntity.ok(accounts);
    }


    @Operation(summary = "포인트 지급하기", description = "해당멤버에게 포인트를 지급한다")
    @ApiResponse(responseCode = "200", description = "포인트 지급 성공")
    @PostMapping("/point")
    public ResponseEntity<Integer> pointUp(@RequestParam("targetMemberId") Long targetMemberId,@RequestParam("point") int point){
        //TODO : 실제 운영 서버에서는 jwt토큰으로 member불러와서 저장할 예정
        Long memberId = 1L;
        Member adminMember = memberService.findById(memberId);

        Member targetMember = memberService.findById(targetMemberId);

        int currentPoint = memberService.savePaymentMember(adminMember,targetMember, point);

        return new ResponseEntity<>(currentPoint, HttpStatus.OK);
    }
}
