-- MCN 회사 더미 데이터 (수정본)
-- 작성일: 2026-01-27
-- 변경사항: vacation_days, vacation_remainder 컬럼 추가

-- ==========================================
-- 1. Department (부서)
-- ==========================================
INSERT INTO department (department_id, department_name, department_call, department_detail, department_color) VALUES
(1, '경영지원본부', '02-1234-5001', '회사의 전반적인 경영 및 행정 업무를 담당합니다', '#FF6B6B'),
(2, '사업본부', '02-1234-5002', '신규 사업 기획 및 수익 창출을 담당합니다', '#4ECDC4'),
(3, '매니지먼트팀', '02-1234-5003', '크리에이터 매니지먼트 및 콘텐츠 기획을 담당합니다', '#95E1D3'),
(4, '마케팅팀', '02-1234-5004', '브랜드 마케팅 및 홍보를 담당합니다', '#F38181'),
(5, '기술본부', '02-1234-5005', '시스템 개발 및 기술 지원을 담당합니다', '#AA96DA'),
(6, '인사문화팀', '02-1234-5006', '인사 관리 및 조직 문화 조성을 담당합니다', '#FCBAD3'),
(7, '글로벌사업팀', '02-1234-5007', '해외 시장 진출 및 글로벌 파트너십을 담당합니다', '#A8D8EA');

-- ==========================================
-- 2. Member (회원 - 직원, 매니저, 관리자, 크리에이터)
-- ==========================================
-- 관리자 (ADMINISTRATOR)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
(1, 6, 'HR001', 'admin123!', '김인사', 'ADMINISTRATOR', 'WORKING'),
(2, 6, 'HR002', 'admin456!', '이채용', 'ADMINISTRATOR', 'WORKING');

-- 매니저 (MANAGER)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
(3, 3, 'MG001', 'manager123', '박매니저', 'MANAGER', 'WORKING'),
(4, 3, 'MG002', 'manager456', '최담당', 'MANAGER', 'WORKING'),
(5, 3, 'MG003', 'manager789', '정관리', 'MANAGER', 'WORKING'),
(6, 4, 'MK001', 'manager111', '강마케팅', 'MANAGER', 'WORKING');

-- 일반 직원 (EMPLOYEE)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
(7, 5, 'IT001', 'dev12345', '윤개발', 'EMPLOYEE', 'WORKING'),
(8, 5, 'IT002', 'dev67890', '한코딩', 'EMPLOYEE', 'WORKING'),
(9, 4, 'MK002', 'mark1234', '송홍보', 'EMPLOYEE', 'WORKING'),
(10, 2, 'BIZ001', 'biz12345', '조사업', 'EMPLOYEE', 'WORKING'),
(11, 1, 'ADM001', 'admin999', '신경영', 'EMPLOYEE', 'WORKING'),
(12, 7, 'GL001', 'global11', 'Emma Wilson', 'EMPLOYEE', 'WORKING'),
(13, 3, 'MG004', 'thumb123', '문썸네일', 'EMPLOYEE', 'WORKING');

-- 퇴사한 직원
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
(14, 4, 'MK003', 'old12345', '구직원', 'EMPLOYEE', 'SUSPENDED');

-- 크리에이터 (CREATOR)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
(15, NULL, 'gamst', 'gam12345', '감스트', 'CREATOR', 'WORKING'),
(16, NULL, 'ddoganjip', 'ddogan99', '또간집', 'CREATOR', 'WORKING'),
(17, NULL, 'beautyjini', 'beauty88', '뷰티지니', 'CREATOR', 'WORKING'),
(18, NULL, 'mukbopd', 'mukbang7', '먹보PD', 'CREATOR', 'WORKING'),
(19, NULL, 'gamekingminsu', 'game2win', '게임왕민수', 'CREATOR', 'WORKING'),
(20, NULL, 'dailyvlogger', 'vlog1234', '일상브이로거', 'CREATOR', 'WORKING'),
(21, NULL, 'techreviewer', 'tech9999', '테크리뷰어', 'CREATOR', 'WORKING'),
(22, NULL, 'restingcreator', 'rest0000', '휴식중크리에이터', 'CREATOR', 'WORKING'),
(23, NULL, 'retiredcreator', 'retired1', '은퇴한크리', 'CREATOR', 'WORKING');

-- ==========================================
-- 3. MemberEmployeeDetail (직원 상세 정보)
-- vacation_remainder: 15일 기본 지급 - 승인된 ANNUAL 휴가 일수
-- ==========================================
INSERT INTO employee_detail (employee_detail_id, employee_id, task, nickname, department_id, eng_name, personal_email, personal_call, hire_date, emp_date, address, employment_type, leaving_reason, vacation_remainder) VALUES
(1, 1, '인사 관리', '인사왕', 6, 'Kim Insa', 'kim.insa@gmail.com', '010-1111-1111', '2020-03-02', NULL, '서울시 강남구 테헤란로 123', 'EXPERIENCED', NULL, 15.0),
(2, 2, '채용 담당', '채용마스터', 6, 'Lee Chaeyong', 'lee.chaeyong@naver.com', '010-2222-2222', '2021-06-15', NULL, '서울시 서초구 서초대로 456', 'NEWBIE', NULL, 15.0),
(3, 3, '크리에이터 매니저', '매니저박', 3, 'Park Manager', 'park.mg@gmail.com', '010-3333-3333', '2019-01-10', NULL, '서울시 마포구 월드컵로 789', 'EXPERIENCED', NULL, 10.0),  -- 5일 사용 (워케이션 5일)
(4, 4, '크리에이터 매니저', '최담당님', 3, 'Choi Damdang', 'choi.dd@kakao.com', '010-4444-4444', '2022-09-01', NULL, '경기도 성남시 분당구 판교역로 111', 'NEWBIE', NULL, 15.0),
(5, 5, '크리에이터 매니저', '정매니저', 3, 'Jung Gwanli', 'jung.gl@gmail.com', '010-5555-5555', '2020-11-20', NULL, '인천시 연수구 송도과학로 222', 'EXPERIENCED', NULL, 15.0),
(6, 6, '마케팅 기획', '강마케터', 4, 'Kang Marketing', 'kang.mk@naver.com', '010-6666-6666', '2021-03-15', NULL, '서울시 영등포구 여의대로 333', 'NEWBIE', NULL, 15.0),
(7, 7, '백엔드 개발자', '윤개발자', 5, 'Yoon Dev', 'yoon.dev@gmail.com', '010-7777-7777', '2022-01-03', NULL, '서울시 강남구 역삼로 444', 'NEWBIE', NULL, 10.0),  -- 5일 사용 (연차 5일)
(8, 8, '프론트엔드 개발자', '한코더', 5, 'Han Coding', 'han.code@naver.com', '010-8888-8888', '2020-07-15', NULL, '경기도 고양시 일산동구 중앙로 555', 'EXPERIENCED', NULL, 13.0),  -- 2일 사용 (병가는 연차 차감 안됨)
(9, 9, 'SNS 마케터', '송마케터', 4, 'Song Hongbo', 'song.sns@gmail.com', '010-9999-9999', '2023-02-01', NULL, '서울시 송파구 올림픽로 666', 'NEWBIE', NULL, 14.5),  -- 0.5일 사용 (반차)
(10, 10, '사업 기획', '조사업가', 2, 'Jo Business', 'jo.biz@kakao.com', '010-1010-1010', '2019-08-20', NULL, '서울시 중구 세종대로 777', 'EXPERIENCED', NULL, 10.0),  -- 5일 사용 (연차 5일)
(11, 11, '경영 지원', '신경영인', 1, 'Shin Gyeong', 'shin.admin@naver.com', '010-1212-1212', '2021-12-01', NULL, '서울시 종로구 종로 888', 'NEWBIE', NULL, 15.0),  -- 2일 대기 중 (경조사는 연차 차감 안됨)
(12, 12, '글로벌 매니저', 'Emma', 7, 'Emma Wilson', 'emma.wilson@gmail.com', '010-1313-1313', '2022-05-10', NULL, '서울시 용산구 이태원로 999', 'EXPERIENCED', NULL, 15.0),  -- 8일 대기 중 (워케이션은 연차 차감 안됨)
(13, 13, '썸네일러', '문디자이너', 3, 'Moon Thumbnail', 'moon.thumb@gmail.com', '010-1414-1414', '2023-04-01', NULL, '경기도 수원시 영통구 광교로 1010', 'NEWBIE', NULL, 15.0),  -- 0.5일 반려됨
(14, 14, '마케터', '구직원님', 4, 'Gu Jikwon', 'gu.old@naver.com', '010-1515-1515', '2020-02-01', '2024-12-31', '서울시 동대문구 왕산로 1111', 'NEWBIE', '개인 사정으로 인한 퇴사', 15.0);

-- ==========================================
-- 4. MemberCreatorDetail (크리에이터 상세 정보)
-- ==========================================
INSERT INTO creator_detail (creator_detail_id, member_creator_id, member_manager_id, creator_subscribe, creator_category, creator_platform, creator_status, creator_main_contact) VALUES
(1, 15, 3, '1250000', '게임', 'YOUTUBE', 'ACTIVE', '010-1501-1501'),
(2, 16, 3, '850000', '먹방', 'YOUTUBE', 'ACTIVE', '010-1601-1601'),
(3, 17, 4, '620000', '뷰티', 'YOUTUBE', 'ACTIVE', 'beauty.jini@gmail.com'),
(4, 18, 4, '430000', '먹방', 'TWITCH', 'ACTIVE', '010-1801-1801'),
(5, 19, 5, '980000', '게임', 'CHZZK', 'ACTIVE', 'gamer.minsu@kakao.com'),
(6, 20, 5, '340000', '일상', 'INSTAGRAM', 'ACTIVE', '010-2001-2001'),
(7, 21, 3, '560000', '테크', 'YOUTUBE', 'ACTIVE', 'tech.reviewer@naver.com'),
(8, 22, 4, '280000', '여행', 'TIKTOK', 'RESTING', '010-2201-2201'),
(9, 23, 5, '150000', '게임', 'YOUTUBE', 'RETREAT', '010-2301-2301');

-- ==========================================
-- 5. MemberProfile (회원 프로필)
-- ==========================================
INSERT INTO member_profile (profile_id, member_id, profile_image, profile_banner) VALUES
(1, 1, 'https://cdn.mcn.com/profiles/kim_insa.jpg', 'https://cdn.mcn.com/banners/kim_insa_banner.jpg'),
(2, 3, 'https://cdn.mcn.com/profiles/park_manager.jpg', 'https://cdn.mcn.com/banners/park_manager_banner.jpg'),
(3, 7, 'https://cdn.mcn.com/profiles/yoon_dev.jpg', 'https://cdn.mcn.com/banners/yoon_dev_banner.jpg'),
(4, 15, 'https://cdn.mcn.com/profiles/gamst.jpg', 'https://cdn.mcn.com/banners/gamst_banner.jpg'),
(5, 16, 'https://cdn.mcn.com/profiles/ddoganjip.jpg', 'https://cdn.mcn.com/banners/ddoganjip_banner.jpg'),
(6, 17, 'https://cdn.mcn.com/profiles/beautyjini.jpg', 'https://cdn.mcn.com/banners/beautyjini_banner.jpg'),
(7, 18, 'https://cdn.mcn.com/profiles/mukbopd.jpg', 'https://cdn.mcn.com/banners/mukbopd_banner.jpg'),
(8, 19, 'https://cdn.mcn.com/profiles/gameking.jpg', 'https://cdn.mcn.com/banners/gameking_banner.jpg'),
(9, 20, 'https://cdn.mcn.com/profiles/dailyvlogger.jpg', 'https://cdn.mcn.com/banners/dailyvlogger_banner.jpg'),
(10, 21, 'https://cdn.mcn.com/profiles/techreviewer.jpg', 'https://cdn.mcn.com/banners/techreviewer_banner.jpg');

-- ==========================================
-- 6. CreatorContract (크리에이터 계약)
-- ==========================================
INSERT INTO creator_contract (creator_contract_id, member_creator_id, contract_name, contract_start, contract_end) VALUES
(1, 15, '감스트 전속 계약', '2023-01-01', '2026-12-31'),
(2, 16, '또간집 전속 계약', '2023-06-01', '2026-05-31'),
(3, 17, '뷰티지니 전속 계약', '2024-01-01', '2027-12-31'),
(4, 18, '먹보PD 전속 계약', '2022-09-01', '2025-08-31'),
(5, 19, '게임왕민수 전속 계약', '2023-03-01', '2026-02-28'),
(6, 20, '일상브이로거 전속 계약', '2024-07-01', '2027-06-30'),
(7, 21, '테크리뷰어 전속 계약', '2023-11-01', '2026-10-31');

-- ==========================================
-- 7. CreatorLegalTax (크리에이터 법률/세무)
-- ==========================================
INSERT INTO creator_legal_tax (legal_tax_id, member_creator_id, legal_tax_type, legal_tax_name, legal_tax_detail, letal_tax_status) VALUES
(1, 15, 'TAX', '2025년 1분기 부가세 신고', '1분기 유튜브 광고 수익 및 협찬 관련 부가세 신고 필요', 'IN_PROGRESS'),
(2, 16, 'TAX', '2024년 종합소득세 신고', '전년도 수익 관련 종합소득세 신고 완료', 'DONE'),
(3, 17, 'LEGAL', '상표권 등록 건', '뷰티지니 브랜드 상표권 등록 진행 중', 'CONTACTED'),
(4, 18, 'TAX', '2025년 1분기 원천세 납부', '1분기 직원 급여 관련 원천세 납부 대기', 'NOT_RECEIVED'),
(5, 19, 'LEGAL', '게임사 제휴 계약서 검토', '게임사와의 장기 제휴 계약서 법률 검토 요청', 'IN_PROGRESS'),
(6, 21, 'TAX', '2025년 부가세 환급 신청', '장비 구입 관련 부가세 환급 신청', 'CONTACTED');

-- ==========================================
-- 8. CreatorPromotion (크리에이터 광고/협찬)
-- ==========================================
INSERT INTO creator_promotion (promotion_id, member_creator_id, promotion_client, promotion_name, promotion_fee, promotion_detail, created_at, promotion_targer_date, promotion_status) VALUES
(1, 15, '삼성전자', '갤럭시 S24 울트라 기능 리뷰 및 시연', '8000000', '신제품 출시 기념 메인 기능 집중 리뷰 영상 제작 (15분 이상), AI 카메라 기능 실사용 시연', '2025-01-10', '2025-02-15', 'ACCEPTED'),
(2, 16, 'CJ제일제당', '비비고 왕교자 먹방 콘텐츠', '5000000', '비비고 왕교자 신제품 먹방 콘텐츠 제작, 맛 리뷰 및 조리 과정 포함', '2025-01-15', '2025-02-28', 'WAITING'),
(3, 17, '아모레퍼시픽', '설화수 신제품 체험 영상', '6500000', '설화수 신제품 라인 체험 및 사용 후기 영상, 메이크업 튜토리얼 포함', '2025-01-20', '2025-03-10', 'ACCEPTED'),
(4, 18, '배달의민족', '배민 신규 서비스 홍보', '4000000', '배민 신규 서비스 소개 및 실사용 라이브 방송 2시간 진행', '2025-01-12', '2025-02-20', 'WAITING'),
(5, 19, '넥슨', '던전앤파이터 모바일 신작 찍먹 플레이', '10000000', '캐주얼하게 게임을 즐기는 모습을 담은 라이브 방송 3시간 진행, 신규 직업 체험 콘텐츠', '2025-01-08', '2025-02-05', 'ACCEPTED'),
(6, 21, 'LG전자', 'LG 그램 2025 신제품 리뷰', '7000000', 'LG 그램 신모델 상세 리뷰 영상 제작, 성능 테스트 및 비교 분석 포함', '2025-01-18', '2025-03-01', 'WAITING'),
(7, 15, '나이키', '나이키 에어맥스 신제품 홍보', '6000000', '나이키 에어맥스 신제품 착화 리뷰 및 운동 콘텐츠', '2025-01-05', '2025-02-10', 'REJECTED'),
(8, 17, '에스티로더', '에스티로더 어드밴스드 나이트 리페어', '5500000', '에스티로더 대표 제품 사용 후기 및 스킨케어 루틴 영상', '2024-12-20', '2025-01-31', 'ACCEPTED');

-- ==========================================
-- 9. CreatorWork (크리에이터 업무 현황)
-- ==========================================
INSERT INTO creator_work (creator_work_id, member_creator_id, work_name, work_status, member_worker_id) VALUES
(1, 15, '다음주 콘텐츠 기획안 피드백', 'WORKING', 3),
(2, 15, '유튜브 채널 아트 리뉴얼 시안 확인', 'WORKING', 13),
(3, 16, '2월 먹방 콘텐츠 스케줄 조율', 'WORKING', 3),
(4, 17, '뷰티 트렌드 분석 보고서 검토', 'DONE', 4),
(5, 17, '신규 협찬 제품 수령 및 확인', 'WORKING', 4),
(6, 18, '라이브 방송 장비 점검', 'DONE', 5),
(7, 19, '게임 대회 출전 일정 협의', 'WORKING', 5),
(8, 19, '신규 게임 협찬 미팅 준비', 'WORKING', 5),
(9, 20, '브이로그 촬영 장소 섭외', 'WORKING', 5),
(10, 21, '테크 신제품 발표회 참석 일정 확인', 'DONE', 3),
(11, 15, '삼성 광고 콘텐츠 1차 편집본 확인', 'WORKING', 3),
(12, 17, '아모레 협찬 콘텐츠 기획안 작성', 'DONE', 4);

-- ==========================================
-- 10. Team (팀)
-- ==========================================
INSERT INTO team (team_id, team_name, team_detail) VALUES
(1, '감스트 크루', '감스트를 중심으로 한 게임 콘텐츠 제작팀'),
(2, '뷰티 컬렉티브', '뷰티 크리에이터들의 협업 팀'),
(3, '먹방 유니버스', '먹방 크리에이터 연합'),
(4, '테크 리뷰어즈', '테크 리뷰어들의 정보 공유 팀');

-- ==========================================
-- 11. TeamRelay (팀 중계 - 팀 멤버 관계)
-- ==========================================
INSERT INTO team_relay (team_relay_id, team_id, member_id) VALUES
(1, 1, 15),
(2, 1, 19),
(3, 2, 17),
(4, 2, 20),
(5, 3, 16),
(6, 3, 18),
(7, 4, 21),
(8, 4, 15);

-- ==========================================
-- 12. Schedule (일정)
-- ==========================================
INSERT INTO schedule (schedule_id, member_id, schedule_name, schedule_date, schedule_detail, schedule_type) VALUES
(1, 15, '삼성 갤럭시 광고 촬영', '2025-02-10', '삼성전자 본사 방문, 갤럭시 S24 울트라 리뷰 촬영', 'PROMOTION'),
(2, 16, '비비고 먹방 촬영', '2025-02-20', '비비고 왕교자 신제품 먹방 콘텐츠 촬영', 'CONTENT'),
(3, 17, '설화수 체험단 행사', '2025-03-05', '아모레퍼시픽 본사 방문, 설화수 신제품 체험', 'PROMOTION'),
(4, 19, '던파 모바일 라이브 방송', '2025-02-05', '넥슨 던전앤파이터 모바일 신작 라이브 방송 3시간', 'LIVE'),
(5, 3, '월간 크리에이터 미팅', '2025-02-03', '담당 크리에이터들과 2월 스케줄 조율 및 피드백', 'MEETING'),
(6, 15, '게임왕민수 합방', '2025-02-15', '게임왕민수와 함께하는 합방 콘텐츠', 'MERGE'),
(7, 19, '게임왕민수 합방', '2025-02-15', '감스트와 함께하는 합방 콘텐츠', 'MERGE'),
(8, 1, '전사 워크샵', '2025-03-15', '2025년 상반기 전사 워크샵 (제주도)', 'COMPANY'),
(9, 7, '시스템 점검', '2025-02-01', '서버 및 시스템 정기 점검', 'COMPANY'),
(10, 20, '일상 브이로그 촬영', '2025-01-30', '카페 투어 브이로그 촬영', 'CONTENT'),
(11, 21, 'LG 그램 제품 발표회', '2025-02-28', 'LG전자 신제품 발표회 참석 및 취재', 'MEETING'),
(12, 17, '뷰티 세미나 참석', '2025-02-12', '2025 K-뷰티 트렌드 세미나 참석', 'PERSONAL'),
(13, 18, '배민 라이브 방송', '2025-02-20', '배달의민족 신규 서비스 소개 라이브', 'LIVE'),
(14, 6, '마케팅 전략 회의', '2025-02-05', '1분기 마케팅 전략 수립 회의', 'MEETING'),
(15, 11, '법인카드 정산', '2025-01-31', '1월 법인카드 사용 내역 정산', 'COMPANY');

-- ==========================================
-- 13. ScheduleVisitor (일정 참가자)
-- ==========================================
INSERT INTO schedule_visitor (schedule_visitor_id, schedule_id, member_id) VALUES
(1, 6, 15),
(2, 6, 19),
(3, 7, 15),
(4, 7, 19),
(5, 8, 1),
(6, 8, 2),
(7, 8, 3),
(8, 8, 4),
(9, 8, 5),
(10, 5, 3),
(11, 5, 15),
(12, 5, 16),
(13, 5, 21),
(14, 14, 6),
(15, 14, 9);

-- ==========================================
-- 14. Vacation (휴가)
-- vacation_days 계산: (종료일 - 시작일) + 1, 반차는 0.5
-- ==========================================
INSERT INTO vacation (vacation_id, member_id, vacation_type, vacation_start, vacation_end, vacation_request, vacation_detail, vacation_approve, vacation_rejected, vacation_days) VALUES
(1, 7, 'ANNUAL', '2025-02-10', '2025-02-14', '2025-01-20', '개인 여행', 'APPROVED', NULL, 5.0),  -- 5일
(2, 9, 'HALF', '2025-02-05', '2025-02-05', '2025-01-28', '오후 병원 방문', 'APPROVED', NULL, 0.5),  -- 반차
(3, 11, 'FAMILY', '2025-02-20', '2025-02-21', '2025-02-10', '조모 칠순 잔치', 'APPROVE_NEED', NULL, 2.0),  -- 2일
(4, 8, 'SICK', '2025-01-29', '2025-01-30', '2025-01-28', '독감 증상으로 병가 신청', 'APPROVED', NULL, 2.0),  -- 2일
(5, 12, 'WORKATION', '2025-03-10', '2025-03-17', '2025-02-15', '홍콩 거래처 미팅 및 원격 근무', 'APPROVE_NEED', NULL, 8.0),  -- 8일
(6, 10, 'ANNUAL', '2025-04-01', '2025-04-05', '2025-03-01', '봄 휴가', 'APPROVED', NULL, 5.0),  -- 5일
(7, 13, 'HALF', '2025-02-07', '2025-02-07', '2025-02-01', '오전 개인 사정', 'REJECTED', '업무 일정상 해당 날짜 반차 불가', 0.5),  -- 반차 (반려됨)
(8, 15, 'ANNUAL', '2025-03-20', '2025-03-22', '2025-03-05', '휴식', 'APPROVE_NEED', NULL, 3.0),  -- 3일
(9, 3, 'WORKATION', '2025-04-15', '2025-04-19', '2025-03-20', '제주 오피스 근무 및 크리에이터 여행 브이로그 보조', 'APPROVED', NULL, 5.0);  -- 5일

-- ==========================================
-- 15. VacationFamily (경조사 상세)
-- ==========================================
INSERT INTO vacation_family (family_id, vacation_id, family_relation, family_detail) VALUES
(1, 3, '조모', '칠순 잔치');

-- ==========================================
-- 16. VacationSick (병가 상세)
-- ==========================================
INSERT INTO vacation_sick (sick_id, vacation_id, sick_detail, sick_hospital) VALUES
(1, 4, '고열(38.5도), 기침, 인후통 증상. 독감 의심', '강남 세브란스 병원');

-- ==========================================
-- 17. VacationWorkation (워케이션 상세)
-- ==========================================
INSERT INTO vacation_workation (workation_id, vacation_id, workation_where, workation_contact, workation_plan, workation_handover) VALUES
(1, 5, '홍콩 오피스', '010-1313-1313', '홍콩 거래처 미팅 (3/11, 3/13), 글로벌 파트너십 계약서 검토, 원격 근무를 통한 일반 업무 처리', '긴급 사항 발생 시 조사업 대리(010-1010-1010)에게 연락'),
(2, 9, '제주 오피스', '010-3333-3333', '크리에이터 일상브이로거 여행 브이로그 촬영 보조 (4/16-4/17), 제주 지역 협력사 미팅 (4/18), 원격 업무 수행', '담당 크리에이터 관련 긴급 사항은 최담당 매니저(010-4444-4444)에게 인계');

-- ==========================================
-- 18. Health (건강 검진)
-- ==========================================
INSERT INTO health (health_id, member_id, checkup_name, checkup_date, checkup_summanary, checkup_file_url) VALUES
(1, 1, '2024년 정기 건강검진', '2024-11-15', 'NORMAL_AB', 'https://cdn.mcn.com/health/kim_insa_2024.pdf'),
(2, 3, '2024년 정기 건강검진', '2024-10-20', 'NORMAL_B', 'https://cdn.mcn.com/health/park_manager_2024.pdf'),
(3, 7, '2024년 정기 건강검진', '2024-12-05', 'CAUTION', 'https://cdn.mcn.com/health/yoon_dev_2024.pdf'),
(4, 15, '2024년 종합 건강검진', '2024-09-10', 'NORMAL_AB', 'https://cdn.mcn.com/health/gamst_2024.pdf'),
(5, 16, '2024년 종합 건강검진', '2024-09-25', 'DANGER', 'https://cdn.mcn.com/health/ddoganjip_2024.pdf'),
(6, 17, '2024년 종합 건강검진', '2024-10-05', 'NORMAL_B', 'https://cdn.mcn.com/health/beautyjini_2024.pdf'),
(7, 8, '2024년 정기 건강검진', '2024-11-30', 'RETEST_NEED', 'https://cdn.mcn.com/health/han_coding_2024.pdf'),
(8, 19, '2024년 종합 건강검진', '2024-08-15', 'NORMAL_AB', 'https://cdn.mcn.com/health/gameking_2024.pdf');

-- ==========================================
-- 19. CreatorMentalHealth (크리에이터 정신건강 설문)
-- ==========================================
INSERT INTO creator_metal_health (creator_mental_id, member_id, creator_mental_date, creator_mental_score) VALUES
(1, 15, '2025-01-15', 3),
(2, 16, '2025-01-15', 8),
(3, 17, '2025-01-15', 5),
(4, 18, '2025-01-15', 12),
(5, 19, '2025-01-15', 2),
(6, 20, '2025-01-15', 15),
(7, 21, '2025-01-15', 6),
(8, 22, '2025-01-15', 18),
(9, 15, '2024-12-15', 4),
(10, 16, '2024-12-15', 7),
(11, 17, '2024-12-15', 6),
(12, 19, '2024-12-15', 3);

-- ==========================================
-- 20. Attendance (근태) - 최근 2주간 데이터
-- ==========================================
INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end) VALUES
-- 2025-01-13 (월)
(1, 1, '2025-01-13', TIMESTAMP '2025-01-13 09:00:00', TIMESTAMP '2025-01-13 18:00:00'),
(2, 3, '2025-01-13', TIMESTAMP '2025-01-13 09:15:00', TIMESTAMP '2025-01-13 18:30:00'),
(3, 7, '2025-01-13', TIMESTAMP '2025-01-13 10:00:00', TIMESTAMP '2025-01-13 19:00:00'),
(4, 9, '2025-01-13', TIMESTAMP '2025-01-13 09:00:00', TIMESTAMP '2025-01-13 18:00:00'),
-- 2025-01-14 (화)
(5, 1, '2025-01-14', TIMESTAMP '2025-01-14 09:05:00', TIMESTAMP '2025-01-14 18:00:00'),
(6, 3, '2025-01-14', TIMESTAMP '2025-01-14 09:00:00', TIMESTAMP '2025-01-14 20:00:00'),
(7, 7, '2025-01-14', TIMESTAMP '2025-01-14 10:00:00', TIMESTAMP '2025-01-14 19:00:00'),
(8, 9, '2025-01-14', TIMESTAMP '2025-01-14 09:10:00', TIMESTAMP '2025-01-14 18:00:00'),
-- 2025-01-15 (수)
(9, 1, '2025-01-15', TIMESTAMP '2025-01-15 09:00:00', TIMESTAMP '2025-01-15 18:00:00'),
(10, 3, '2025-01-15', TIMESTAMP '2025-01-15 09:00:00', TIMESTAMP '2025-01-15 18:00:00'),
(11, 7, '2025-01-15', TIMESTAMP '2025-01-15 10:00:00', TIMESTAMP '2025-01-15 19:00:00'),
(12, 9, '2025-01-15', TIMESTAMP '2025-01-15 09:00:00', TIMESTAMP '2025-01-15 18:00:00'),
-- 2025-01-16 (목)
(13, 1, '2025-01-16', TIMESTAMP '2025-01-16 09:00:00', TIMESTAMP '2025-01-16 18:00:00'),
(14, 3, '2025-01-16', TIMESTAMP '2025-01-16 09:30:00', TIMESTAMP '2025-01-16 18:30:00'),
(15, 7, '2025-01-16', TIMESTAMP '2025-01-16 10:00:00', TIMESTAMP '2025-01-16 19:00:00'),
(16, 9, '2025-01-16', TIMESTAMP '2025-01-16 09:00:00', TIMESTAMP '2025-01-16 18:00:00'),
-- 2025-01-17 (금)
(17, 1, '2025-01-17', TIMESTAMP '2025-01-17 09:00:00', TIMESTAMP '2025-01-17 18:00:00'),
(18, 3, '2025-01-17', TIMESTAMP '2025-01-17 09:00:00', TIMESTAMP '2025-01-17 18:00:00'),
(19, 7, '2025-01-17', TIMESTAMP '2025-01-17 10:00:00', TIMESTAMP '2025-01-17 19:00:00'),
(20, 9, '2025-01-17', TIMESTAMP '2025-01-17 09:00:00', TIMESTAMP '2025-01-17 17:30:00'),
-- 2025-01-20 (월)
(21, 1, '2025-01-20', TIMESTAMP '2025-01-20 09:00:00', TIMESTAMP '2025-01-20 18:00:00'),
(22, 3, '2025-01-20', TIMESTAMP '2025-01-20 09:00:00', TIMESTAMP '2025-01-20 19:00:00'),
(23, 7, '2025-01-20', TIMESTAMP '2025-01-20 10:00:00', TIMESTAMP '2025-01-20 19:00:00'),
(24, 9, '2025-01-20', TIMESTAMP '2025-01-20 09:00:00', TIMESTAMP '2025-01-20 18:00:00'),
-- 2025-01-21 (화)
(25, 1, '2025-01-21', TIMESTAMP '2025-01-21 09:00:00', TIMESTAMP '2025-01-21 18:00:00'),
(26, 3, '2025-01-21', TIMESTAMP '2025-01-21 09:00:00', TIMESTAMP '2025-01-21 18:00:00'),
(27, 7, '2025-01-21', TIMESTAMP '2025-01-21 10:00:00', TIMESTAMP '2025-01-21 19:00:00'),
(28, 9, '2025-01-21', TIMESTAMP '2025-01-21 09:00:00', TIMESTAMP '2025-01-21 18:00:00'),
-- 2025-01-22 (수)
(29, 1, '2025-01-22', TIMESTAMP '2025-01-22 09:00:00', TIMESTAMP '2025-01-22 18:00:00'),
(30, 3, '2025-01-22', TIMESTAMP '2025-01-22 09:00:00', TIMESTAMP '2025-01-22 18:30:00'),
(31, 7, '2025-01-22', TIMESTAMP '2025-01-22 10:00:00', TIMESTAMP '2025-01-22 19:00:00'),
(32, 9, '2025-01-22', TIMESTAMP '2025-01-22 09:00:00', TIMESTAMP '2025-01-22 18:00:00'),
-- 2025-01-23 (목)
(33, 1, '2025-01-23', TIMESTAMP '2025-01-23 09:00:00', TIMESTAMP '2025-01-23 18:00:00'),
(34, 3, '2025-01-23', TIMESTAMP '2025-01-23 09:00:00', TIMESTAMP '2025-01-23 18:00:00'),
(35, 7, '2025-01-23', TIMESTAMP '2025-01-23 10:00:00', TIMESTAMP '2025-01-23 19:00:00'),
(36, 9, '2025-01-23', TIMESTAMP '2025-01-23 09:00:00', TIMESTAMP '2025-01-23 18:00:00'),
-- 2025-01-24 (금)
(37, 1, '2025-01-24', TIMESTAMP '2025-01-24 09:00:00', TIMESTAMP '2025-01-24 18:00:00'),
(38, 3, '2025-01-24', TIMESTAMP '2025-01-24 09:00:00', TIMESTAMP '2025-01-24 18:00:00'),
(39, 7, '2025-01-24', TIMESTAMP '2025-01-24 10:00:00', TIMESTAMP '2025-01-24 19:00:00'),
(40, 9, '2025-01-24', TIMESTAMP '2025-01-24 09:00:00', TIMESTAMP '2025-01-24 18:00:00'),
-- 2025-01-27 (월) - 오늘
(41, 1, '2025-01-27', TIMESTAMP '2025-01-27 09:00:00', NULL),
(42, 3, '2025-01-27', TIMESTAMP '2025-01-27 09:00:00', NULL),
(43, 7, '2025-01-27', TIMESTAMP '2025-01-27 10:00:00', NULL),
(44, 9, '2025-01-27', TIMESTAMP '2025-01-27 09:00:00', NULL);

-- ==========================================
-- 데이터 입력 완료
-- ==========================================