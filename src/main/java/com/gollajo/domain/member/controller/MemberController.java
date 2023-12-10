package com.gollajo.domain.member.controller;

import com.gollajo.domain.account.dto.AccountResponse;
import com.gollajo.domain.account.entity.Account;
import com.gollajo.domain.account.service.AccountService;
import com.gollajo.domain.auth.dto.LoginResponse;
import com.gollajo.domain.member.dto.CreateMemberRequest;
import com.gollajo.domain.member.dto.RequestMypageDto;
import com.gollajo.domain.member.entity.Member;
import com.gollajo.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

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
    public ResponseEntity<List<AccountResponse>> showMyAccountList(@CookieValue(name="memberId", required = false)Long memberId){

        List<Account> accounts = accountService.showMyAccount(memberId);

        List<AccountResponse> accountResponses = accountService.transferAccountResponse(accounts);

        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name="memberId", required = false)Long memberId,
                                         HttpServletResponse response){
        ResponseCookie cookie = ResponseCookie.from("memberId", String.valueOf(memberId))
                .maxAge(0)
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
        response.addHeader(SET_COOKIE,cookie.toString());

        return new ResponseEntity<>("로그아웃되었습니다.",HttpStatus.OK);
    }


    @Operation(summary = "포인트 지급하기", description = "해당멤버에게 포인트를 지급한다")
    @ApiResponse(responseCode = "200", description = "포인트 지급 성공")
    @PostMapping("/point")
    public ResponseEntity<Integer> pointUp(@RequestParam("targetMemberId") Long targetMemberId,
                                           @RequestParam("point") int point,
                                           @CookieValue(name="memberId", required = false)Long memberId){
        Member adminMember = memberService.findById(memberId);

        Member targetMember = memberService.findById(targetMemberId);

        int currentPoint = memberService.savePaymentMember(adminMember,targetMember, point);


        return new ResponseEntity<>(currentPoint, HttpStatus.OK);
    }


    @Operation(summary = "내 정보 업데이트", description = "내 정보를 수정한다")
    @ApiResponse(responseCode = "200", description = "정보 수정 성공")
    @PostMapping("/update")
    public ResponseEntity<Member> update(@CookieValue(name="memberId", required = false)Long memberId,CreateMemberRequest createMemberRequest){

        Member member = memberService.findById(memberId);

        Member updatedMember = memberService.update(createMemberRequest);
        //TODO : 내 정보보기에서 Dto를 따로 만들지 고민중

        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }


    @Operation(summary = "내 정보보기", description = "내 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "정보조회 성공")
    @GetMapping("/my-page")
    public ResponseEntity<RequestMypageDto> showMyPage(@CookieValue(name="memberId", required = false)Long memberId, HttpServletRequest request){

        Member member = memberService.findById(memberId);

        RequestMypageDto mypageInfo = memberService.getMypageInfo(member);
        //TODO: 누적합계 계산해서 requestMypageDto 만들어서 response로 보내주기

        return new ResponseEntity<>(mypageInfo, HttpStatus.OK);
    }


}
