### Easywaldo 전자결재 API

#### API 사용법
0. Active Profile 이 Local 로 세팅이 되어 있으므로 인텔리제이 또는 이클립스 등에서 Active.profile 을 local 로 설정하여 구동하여야 합니다.
1. Springboot 프로젝트를 빌드 후 인텔리제이 등을 이용하여 jar 를 실행합니다.
2. 8088 포트로 접속이 가능 합니다. (http://localhost:8088/swagger-ui.html)
3. Swagger 문서에 각 URI 에 대한 설명을 참조하여 전자결재 테스트를 진행이 가능 합니다.

#### 컨트롤러 구성
- MemberController
  - 회원 가입 및 로그인과 로그아웃 진행이 가능 합니다.
- DocumentController
  - 전자결재문서 작성 / 승인 / 조회등이 가능 합니다.
    
#### 테스트 시나리오
- /member/userJoin 으로 회원가입
- /member/userLogin 으로 로그인 >> JWT 발행이 완료 
  - (등록계정 : easywaldo 아이디  / 1234 패스워드)
  - (등록계정 : acetious 아이디  / 1234 패스워드)
- /document/create 으로 전자결재문서작성
  >{
  "approvalLine": [
  {
  "order": 1,
  "userId": "easywaldo"
  },
  {
  "order": 2,
  "userId": "acetious"
  }
  ],
  "documentContent": "테스트",
  "documentTitle": "테스트",
  "documentType": "PRODUCT_PURCHASE"
  }
- /document/selectInBox 으로 내가 결재해야할 문서를 조회
- /document/confirm 으로 승인 또는 반려
  > {
  "approvalId": 1,
  "approved": true,
  "comment": "결재 완료",
  "documentId": 1,
  "userId": "easywaldo"
  }
- /document/selectOutBox 으로 내가 생성한 문서 중 진행중인 문서를 조회
- /document/selectArchive 으로 내가 관련된 문서를 조회


#### 결재라인 2개이상인 경우
> 로그아웃 URI 를 이용하여 결재라인이 2개 이상인 문서들에 대해서 다음 결재자로 로그인 한 후
> 결재 승인 작업을 진행할 수 있습니다.


#### 참고사항 및 명령에 대한 불변식
- 전자결재문서 작성 시 등록된 계정여부를 확인하는 기능은 없습니다.
- 잔자결재문서의 유형은 "PRODUCT_PURCHASE" 와 "EDUCATION_JOIN" 2개의 타입으로 이뤄져있습니다.
- EDUCATION_JOIN 유형의 경우에는 전자결재라인을 2개 이상으로 지정하도록 불변식을 설정하였습니다.
- 전자결재라인의 order 의 가장낮은 순서의 userId 값은 결재문서생성자의 userId, 즉 로그인한 사용자와 동일해야 합니다.



#### 인메모리 H2 DB 정보
http://localhost:8088/h2-console/
- 계정 : sa / password


