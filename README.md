
# 골라줘

### ✨골라줘, NL✨
<img width="1199" alt="스크린샷 2024-09-23 오후 7 17 58" src="https://github.com/user-attachments/assets/2cf4918e-ccec-4e2f-9d5c-d0c64530ce6c">



## 🗨️ About 골라줘 서비스
"고민거리가 있는 투표를 올리고 다른이용자들이 투표를 함으로써 고민을 해결해주고 리워드를 지급하는 서비스"

### <기존의 문제점 해결>
- 의사결정에 도움
<table>
<tr>
<td align="center">
   <img width="1190" alt="메인페이지">
</td>
<td align="center">
    <img width="1186" alt="스크린샷 2024-09-23 오후 7 22 18">
</td>
</tr>
<tr>
<td align="center">메인 페이지</td>
<td align="center">관리자 페이지</td>
</tr>
<tr>
<td align="center">
   <img width="1173" alt="예약 프롬프트">
</td>
<td align="center">
    <img width="1193" alt="연장 프롬프트">
</td>
</tr>
<tr>
<td align="center">예약 페이지</td>
<td align="center">연장 페이지</td>
</tr>
<tr>
<td align="center">
   <img width="1193" alt="관리자모드 전환">
</td>
<td align="center">
    <img width="1191" alt="앱 종료 불가">
</td>
</tr>
<tr>
<td align="center">관리자모드 전환 프롬프트</td>
<td align="center">앱 종료 불가</td>
</tr>
</table>
## 👨🏻‍💻 Member
<table>
<tr>
<td align="center">프론트엔드</td>
<td align="center">백엔드</td>
<td align="center">백엔드</td>
</tr>
  <tr>
    <td align="center" width="120px">
      <a href="https://github.com/kimsunin" target="_blank">
        <img src="https://avatars.githubusercontent.com/kimsunin" alt="임수진 프로필" />
      </a>
    </td>
    <td align="center" width="120px">
      <a href="https://github.com/hcmhcs" target="_blank">
        <img src="https://avatars.githubusercontent.com/hcmhcs" alt="한창민 프로필" />
      </a>
      <td align="center" width="120px">
      <a href="https://github.com/hcmhcs" target="_blank">
        <img src="https://avatars.githubusercontent.com/hcmhcs" alt="최재민 프로필" />
      </a>
  </tr>
 <tr>
    <td align="center">
      <a href="https://github.com/kimsunin" target="_blank">
        임수진
      </a>
    </td>
     <td align="center">
      <a href="https://github.com/hcmhcs" target="_blank">
       한창민
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/hcmhcs" target="_blank">
       최재민
      </a>
    </td>
  </tr>
<tr>
<td align="center"> <a href="https://github.com/HK-DUO/seating-system">Repository</a></td>
<td align="center"> <a href="https://github.com/HK-DUO/seating-system">Repository</a></td>
<td align="center"> <a href="https://github.com/HK-DUO/seating-system">Repository</a></td>
</tr>
</table>

## 🛠️ Skills
<img width="410" alt="스크린샷 2024-09-23 오후 7 51 28" src="https://github.com/user-attachments/assets/8ea819e5-e42e-4189-bb2c-e17091487089">



# Gollajo Service
- 고민거리가 있는 투표를 올리고 다른이용자들이 투표를 함으로써 고민을 해결해주고 리워드를 지급하는 서비스
- Spring RestAPI


## TODO
### 1. 투표글 생성취소시에 참조가 겹쳐서 취소안되는 오류해결해야됨
- 해결방법
1. Account와 Post의 연관관계 없애기 - 이러면 다른 연관관계에서도 다 id값으로 찾도록 변경해야됨 ㅠㅠ
2. 취소시에 Account의 상태를 취소로 만드는게 아니라 입금내역 삭제하기(이건 비추)

우선 취소하기전에 입금내역에 연관관계 있는 post 를 삭제

### 2. 회원정보 업데이트 되는지 확인
- 영속성으로 Member.build()해도 기존 member객체가 불러와지는지 새 객체가 생성되는지 확인

### 3. OAuth, JWT, 인가 구현하기..

### 4. 피드백
- service 인터페이스 상속받아서 구현하기(확정성 측면)
- 서비스의 리턴값도 dto로 관리하기
- scheduling 관련 투표글 상태 업데이트를 DB에서 처리하는 법 알아보기 + for문보다 stream 이용해보기, save() 안해도 저장됨(@Transitional)
