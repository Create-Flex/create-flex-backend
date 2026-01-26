## 관련 이슈 번호
- Closes #


## 변경 내용
- 예)Trade 서비스 분리 대비: 직접 참조 제거 및 Client 패턴 적용
- 예)SettlementService에서 Trade(SellingBid) 의존성 제거
- 예)Settlement에 sellerId 추가(조회 최적화), Order의 seller 정보 활용 리팩토링

## 리뷰 포인트
- Trade 도메인 의존성 제거 방식 적절한지
- sellerId 중복 저장(성능 목적) 설계 타당성

## 테스트
- 로컬 테스트:
- 로그/스크린샷:


## 체크리스트
- [ ] 빌드/컴파일 정상
- [ ] 로컬 테스트 완료
- [ ] 불필요 코드 제거
- [ ] 브랜치 전략 준수