#slack_backup
슬랙 백업 파일 텍스트로그 만들기

유료 슬랙이 부담되는 돈 없는 사람이 작성하는 슬랙 커스텀 백업

Requirements)...

  1. INPUT: 최상위 경로 => 경로 정보 확인
  2. 대화 파일 목록 확인(yyyy-mm-dd.json)
  3. 추출 내용 확인
    * ts: 유닉스 시간 - 변환 필요
    * user: id(대화 내용) - real_name(users.json)
    * text: 대화 내용
  4. 대화 내용 추출 Format [ 시간 ][ 작성자 ] : "대화 내용"


Devlog)...

2024-06-27 : users.json에서 id와 real_name을 파싱해서 출력해봄

2024-06-28 : 최상위 경로를 입력받아서 하위 폴더 경로를 추출 후 모든 폴더의 파일을 확인

2024-07-01 : 대화 내용 포맷 설정에 따라 File Out
