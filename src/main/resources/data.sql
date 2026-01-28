-- MCN 회사 더미 데이터 (ID 1000번대 시작)
-- 작성일: 2026-01-27
-- 변경사항: 포스트맨 테스트와 충돌 방지를 위해 ID를 1000번대로 변경

-- ==========================================
-- DUMMY DATA (더미데이터)
-- ID 범위: 1000~2999
-- 포스트맨 테스트 데이터는 1~999 사용
-- ==========================================

-- ==========================================
-- 1. Department (부서) - 1000번대
-- ==========================================
INSERT INTO department (department_id, department_name, department_call, department_detail, department_color) VALUES
                                                                                                                  (1001, '경영지원본부', '02-1234-5001', '회사의 전반적인 경영 및 행정 업무를 담당합니다', '#FF6B6B'),
                                                                                                                  (1002, '사업본부', '02-1234-5002', '신규 사업 기획 및 수익 창출을 담당합니다', '#4ECDC4'),
                                                                                                                  (1003, '매니지먼트팀', '02-1234-5003', '크리에이터 매니지먼트 및 콘텐츠 기획을 담당합니다', '#95E1D3'),
                                                                                                                  (1004, '마케팅팀', '02-1234-5004', '브랜드 마케팅 및 홍보를 담당합니다', '#F38181'),
                                                                                                                  (1005, '기술본부', '02-1234-5005', '시스템 개발 및 기술 지원을 담당합니다', '#AA96DA'),
                                                                                                                  (1006, '인사문화팀', '02-1234-5006', '인사 관리 및 조직 문화 조성을 담당합니다', '#FCBAD3'),
                                                                                                                  (1007, '글로벌사업팀', '02-1234-5007', '해외 시장 진출 및 글로벌 파트너십을 담당합니다', '#A8D8EA');

-- ==========================================
-- 2. Member (회원 - 직원, 매니저, 관리자, 크리에이터) - 1000번대(직원), 2000번대(크리에이터)
-- ==========================================
-- 관리자 (ADMINISTRATOR)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
                                                                                                                             (1001, 1006, 'HR001', 'admin123!', '김인사', 'ADMINISTRATOR', 'WORKING'),
                                                                                                                             (1002, 1006, 'HR002', 'admin456!', '이채용', 'ADMINISTRATOR', 'WORKING');

-- 매니저 (MANAGER)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
                                                                                                                             (1003, 1003, 'MG001', 'manager123', '박매니저', 'MANAGER', 'WORKING'),
                                                                                                                             (1004, 1003, 'MG002', 'manager456', '최담당', 'MANAGER', 'WORKING'),
                                                                                                                             (1005, 1003, 'MG003', 'manager789', '정관리', 'MANAGER', 'WORKING'),
                                                                                                                             (1006, 1004, 'MK001', 'manager111', '강마케팅', 'MANAGER', 'WORKING');

-- 일반 직원 (EMPLOYEE)
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
                                                                                                                             (1007, 1005, 'IT001', 'dev12345', '윤개발', 'EMPLOYEE', 'WORKING'),
                                                                                                                             (1008, 1005, 'IT002', 'dev67890', '한코딩', 'EMPLOYEE', 'WORKING'),
                                                                                                                             (1009, 1004, 'MK002', 'mark1234', '송홍보', 'EMPLOYEE', 'WORKING'),
                                                                                                                             (1010, 1002, 'BIZ001', 'biz12345', '조사업', 'EMPLOYEE', 'WORKING'),
                                                                                                                             (1011, 1001, 'ADM001', 'admin999', '신경영', 'EMPLOYEE', 'WORKING'),
                                                                                                                             (1012, 1007, 'GL001', 'global11', 'Emma Wilson', 'EMPLOYEE', 'WORKING'),
                                                                                                                             (1013, 1003, 'MG004', 'thumb123', '문썸네일', 'EMPLOYEE', 'WORKING');

-- 퇴사한 직원
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
    (1014, 1004, 'MK003', 'old12345', '구직원', 'EMPLOYEE', 'SUSPENDED');

-- 크리에이터 (CREATOR) - 2000번대
INSERT INTO members (member_id, department_id, member_account, member_password, member_name, member_role, member_status) VALUES
                                                                                                                             (2001, NULL, 'gamst', 'gam12345', '감스트', 'CREATOR', 'WORKING'),
                                                                                                                             (2002, NULL, 'ddoganjip', 'ddogan99', '또간집', 'CREATOR', 'WORKING'),
                                                                                                                             (2003, NULL, 'beautyjini', 'beauty88', '뷰티지니', 'CREATOR', 'WORKING'),
                                                                                                                             (2004, NULL, 'mukbopd', 'mukbang7', '먹보PD', 'CREATOR', 'WORKING'),
                                                                                                                             (2005, NULL, 'gamekingminsu', 'game2win', '게임왕민수', 'CREATOR', 'WORKING'),
                                                                                                                             (2006, NULL, 'dailyvlogger', 'vlog1234', '일상브이로거', 'CREATOR', 'WORKING'),
                                                                                                                             (2007, NULL, 'techreviewer', 'tech9999', '테크리뷰어', 'CREATOR', 'WORKING'),
                                                                                                                             (2008, NULL, 'restingcreator', 'rest0000', '휴식중크리에이터', 'CREATOR', 'WORKING'),
                                                                                                                             (2009, NULL, 'retiredcreator', 'retired1', '은퇴한크리', 'CREATOR', 'WORKING');

-- ==========================================
-- 3. MemberEmployeeDetail (직원 상세 정보) - 1000번대
-- ==========================================
INSERT INTO employee_detail (employee_detail_id, employee_id, task, nickname, department_id, eng_name, personal_email, personal_call, hire_date, emp_date, address, employment_type, leaving_reason, vacation_remainder) VALUES
                                                                                                                                                                                                                             (1001, 1001, '인사 관리', '인사왕', 1006, 'Kim Insa', 'kim.insa@gmail.com', '010-1111-1111', '2020-03-02', NULL, '서울시 강남구 테헤란로 123', 'EXPERIENCED', NULL, 15.0),
                                                                                                                                                                                                                             (1002, 1002, '채용 담당', '채용마스터', 1006, 'Lee Chaeyong', 'lee.chaeyong@naver.com', '010-2222-2222', '2021-06-15', NULL, '서울시 서초구 서초대로 456', 'NEWBIE', NULL, 15.0),
                                                                                                                                                                                                                             (1003, 1003, '크리에이터 매니저', '매니저박', 1003, 'Park Manager', 'park.mg@gmail.com', '010-3333-3333', '2019-01-10', NULL, '서울시 마포구 월드컵로 789', 'EXPERIENCED', NULL, 10.0),
                                                                                                                                                                                                                             (1004, 1004, '크리에이터 매니저', '최담당님', 1003, 'Choi Damdang', 'choi.dd@kakao.com', '010-4444-4444', '2022-09-01', NULL, '경기도 성남시 분당구 판교역로 111', 'NEWBIE', NULL, 15.0),
                                                                                                                                                                                                                             (1005, 1005, '크리에이터 매니저', '정매니저', 1003, 'Jung Gwanli', 'jung.gl@gmail.com', '010-5555-5555', '2020-11-20', NULL, '인천시 연수구 송도과학로 222', 'EXPERIENCED', NULL, 15.0),
                                                                                                                                                                                                                             (1006, 1006, '마케팅 기획', '강마케터', 1004, 'Kang Marketing', 'kang.mk@naver.com', '010-6666-6666', '2021-03-15', NULL, '서울시 영등포구 여의대로 333', 'NEWBIE', NULL, 15.0),
                                                                                                                                                                                                                             (1007, 1007, '백엔드 개발자', '윤개발자', 1005, 'Yoon Dev', 'yoon.dev@gmail.com', '010-7777-7777', '2022-01-03', NULL, '서울시 강남구 역삼로 444', 'NEWBIE', NULL, 10.0),
                                                                                                                                                                                                                             (1008, 1008, '프론트엔드 개발자', '한코더', 1005, 'Han Coding', 'han.code@naver.com', '010-8888-8888', '2020-07-15', NULL, '경기도 고양시 일산동구 중앙로 555', 'EXPERIENCED', NULL, 13.0),
                                                                                                                                                                                                                             (1009, 1009, 'SNS 마케터', '송마케터', 1004, 'Song Hongbo', 'song.sns@gmail.com', '010-9999-9999', '2023-02-01', NULL, '서울시 송파구 올림픽로 666', 'NEWBIE', NULL, 14.5),
                                                                                                                                                                                                                             (1010, 1010, '사업 기획', '조사업가', 1002, 'Jo Business', 'jo.biz@kakao.com', '010-1010-1010', '2019-08-20', NULL, '서울시 중구 세종대로 777', 'EXPERIENCED', NULL, 10.0),
                                                                                                                                                                                                                             (1011, 1011, '경영 지원', '신경영인', 1001, 'Shin Gyeong', 'shin.admin@naver.com', '010-1212-1212', '2021-12-01', NULL, '서울시 종로구 종로 888', 'NEWBIE', NULL, 15.0),
                                                                                                                                                                                                                             (1012, 1012, '글로벌 매니저', 'Emma', 1007, 'Emma Wilson', 'emma.wilson@gmail.com', '010-1313-1313', '2022-05-10', NULL, '서울시 용산구 이태원로 999', 'EXPERIENCED', NULL, 15.0),
                                                                                                                                                                                                                             (1013, 1013, '썸네일러', '문디자이너', 1003, 'Moon Thumbnail', 'moon.thumb@gmail.com', '010-1414-1414', '2023-04-01', NULL, '경기도 수원시 영통구 광교로 1010', 'NEWBIE', NULL, 15.0),
                                                                                                                                                                                                                             (1014, 1014, '마케터', '구직원님', 1004, 'Gu Jikwon', 'gu.old@naver.com', '010-1515-1515', '2020-02-01', '2024-12-31', '서울시 동대문구 왕산로 1111', 'NEWBIE', '개인 사정으로 인한 퇴사', 15.0);

-- ==========================================
-- 4. MemberCreatorDetail (크리에이터 상세 정보) - 2000번대
-- ==========================================
INSERT INTO creator_detail (creator_detail_id, member_creator_id, member_manager_id, creator_subscribe, creator_category, creator_platform, creator_status, creator_main_contact) VALUES
                                                                                                                                                                                      (2001, 2001, 1003, '1250000', '게임', 'YOUTUBE', 'ACTIVE', '010-1501-1501'),
                                                                                                                                                                                      (2002, 2002, 1003, '850000', '먹방', 'YOUTUBE', 'ACTIVE', '010-1601-1601'),
                                                                                                                                                                                      (2003, 2003, 1004, '620000', '뷰티', 'YOUTUBE', 'ACTIVE', 'beauty.jini@gmail.com'),
                                                                                                                                                                                      (2004, 2004, 1004, '430000', '먹방', 'TWITCH', 'ACTIVE', '010-1801-1801'),
                                                                                                                                                                                      (2005, 2005, 1005, '980000', '게임', 'CHZZK', 'ACTIVE', 'gamer.minsu@kakao.com'),
                                                                                                                                                                                      (2006, 2006, 1005, '340000', '일상', 'INSTAGRAM', 'ACTIVE', '010-2001-2001'),
                                                                                                                                                                                      (2007, 2007, 1003, '560000', '테크', 'YOUTUBE', 'ACTIVE', 'tech.reviewer@naver.com'),
                                                                                                                                                                                      (2008, 2008, 1004, '280000', '여행', 'TIKTOK', 'RESTING', '010-2201-2201'),
                                                                                                                                                                                      (2009, 2009, 1005, '150000', '게임', 'YOUTUBE', 'RETREAT', '010-2301-2301');

-- ==========================================
-- 5. MemberProfile (회원 프로필) - 1000번대, 2000번대
-- ==========================================
INSERT INTO member_profile (profile_id, member_id, profile_image, profile_banner) VALUES
                                                                                      (1001, 1001, 'https://cdn.mcn.com/profiles/kim_insa.jpg', 'https://cdn.mcn.com/banners/kim_insa_banner.jpg'),
                                                                                      (1003, 1003, 'https://cdn.mcn.com/profiles/park_manager.jpg', 'https://cdn.mcn.com/banners/park_manager_banner.jpg'),
                                                                                      (1007, 1007, 'https://cdn.mcn.com/profiles/yoon_dev.jpg', 'https://cdn.mcn.com/banners/yoon_dev_banner.jpg'),
                                                                                      (2001, 2001, 'https://cdn.mcn.com/profiles/gamst.jpg', 'https://cdn.mcn.com/banners/gamst_banner.jpg'),
                                                                                      (2002, 2002, 'https://cdn.mcn.com/profiles/ddoganjip.jpg', 'https://cdn.mcn.com/banners/ddoganjip_banner.jpg'),
                                                                                      (2003, 2003, 'https://cdn.mcn.com/profiles/beautyjini.jpg', 'https://cdn.mcn.com/banners/beautyjini_banner.jpg'),
                                                                                      (2004, 2004, 'https://cdn.mcn.com/profiles/mukbopd.jpg', 'https://cdn.mcn.com/banners/mukbopd_banner.jpg'),
                                                                                      (2005, 2005, 'https://cdn.mcn.com/profiles/gameking.jpg', 'https://cdn.mcn.com/banners/gameking_banner.jpg'),
                                                                                      (2006, 2006, 'https://cdn.mcn.com/profiles/dailyvlogger.jpg', 'https://cdn.mcn.com/banners/dailyvlogger_banner.jpg'),
                                                                                      (2007, 2007, 'https://cdn.mcn.com/profiles/techreviewer.jpg', 'https://cdn.mcn.com/banners/techreviewer_banner.jpg');

-- ==========================================
-- 6. CreatorContract (크리에이터 계약) - 2000번대
-- ==========================================
INSERT INTO creator_contract (creator_contract_id, member_creator_id, contract_name, contract_start, contract_end) VALUES
                                                                                                                       (2001, 2001, '감스트 전속 계약', '2023-01-01', '2026-12-31'),
                                                                                                                       (2002, 2002, '또간집 전속 계약', '2023-06-01', '2026-05-31'),
                                                                                                                       (2003, 2003, '뷰티지니 전속 계약', '2024-01-01', '2027-12-31'),
                                                                                                                       (2004, 2004, '먹보PD 전속 계약', '2022-09-01', '2025-08-31'),
                                                                                                                       (2005, 2005, '게임왕민수 전속 계약', '2023-03-01', '2026-02-28'),
                                                                                                                       (2006, 2006, '일상브이로거 전속 계약', '2024-07-01', '2027-06-30'),
                                                                                                                       (2007, 2007, '테크리뷰어 전속 계약', '2023-11-01', '2026-10-31');

-- ==========================================
-- 7. CreatorLegalTax (크리에이터 법률/세무) - 2000번대
-- ==========================================
INSERT INTO creator_legal_tax (legal_tax_id, member_creator_id, legal_tax_type, legal_tax_name, legal_tax_detail, letal_tax_status) VALUES
                                                                                                                                        (2001, 2001, 'TAX', '2025년 1분기 부가세 신고', '1분기 유튜브 광고 수익 및 협찬 관련 부가세 신고 필요', 'IN_PROGRESS'),
                                                                                                                                        (2002, 2002, 'TAX', '2024년 종합소득세 신고', '전년도 수익 관련 종합소득세 신고 완료', 'DONE'),
                                                                                                                                        (2003, 2003, 'LEGAL', '상표권 등록 건', '뷰티지니 브랜드 상표권 등록 진행 중', 'CONTACTED'),
                                                                                                                                        (2004, 2004, 'TAX', '2025년 1분기 원천세 납부', '1분기 직원 급여 관련 원천세 납부 대기', 'NOT_RECEIVED'),
                                                                                                                                        (2005, 2005, 'LEGAL', '게임사 제휴 계약서 검토', '게임사와의 장기 제휴 계약서 법률 검토 요청', 'IN_PROGRESS'),
                                                                                                                                        (2006, 2007, 'TAX', '2025년 부가세 환급 신청', '장비 구입 관련 부가세 환급 신청', 'CONTACTED');

-- ==========================================
-- 8. CreatorPromotion (크리에이터 광고/협찬) - 2000번대
-- ==========================================
INSERT INTO creator_promotion (promotion_id, member_creator_id, promotion_client, promotion_name, promotion_fee, promotion_detail, created_at, promotion_targer_date, promotion_status) VALUES
                                                                                                                                                                                            (2001, 2001, '삼성전자', '갤럭시 S24 울트라 기능 리뷰 및 시연', '8000000', '신제품 출시 기념 메인 기능 집중 리뷰 영상 제작 (15분 이상), AI 카메라 기능 실사용 시연', '2025-01-10', '2025-02-15', 'ACCEPTED'),
                                                                                                                                                                                            (2002, 2002, 'CJ제일제당', '비비고 왕교자 먹방 콘텐츠', '5000000', '비비고 왕교자 신제품 먹방 콘텐츠 제작, 맛 리뷰 및 조리 과정 포함', '2025-01-15', '2025-02-28', 'WAITING'),
                                                                                                                                                                                            (2003, 2003, '아모레퍼시픽', '설화수 신제품 체험 영상', '6500000', '설화수 신제품 라인 체험 및 사용 후기 영상, 메이크업 튜토리얼 포함', '2025-01-20', '2025-03-10', 'ACCEPTED'),
                                                                                                                                                                                            (2004, 2004, '배달의민족', '배민 신규 서비스 홍보', '4000000', '배민 신규 서비스 소개 및 실사용 라이브 방송 2시간 진행', '2025-01-12', '2025-02-20', 'WAITING'),
                                                                                                                                                                                            (2005, 2005, '넥슨', '던전앤파이터 모바일 신작 찍먹 플레이', '10000000', '캐주얼하게 게임을 즐기는 모습을 담은 라이브 방송 3시간 진행, 신규 직업 체험 콘텐츠', '2025-01-08', '2025-02-05', 'ACCEPTED'),
                                                                                                                                                                                            (2006, 2007, 'LG전자', 'LG 그램 2025 신제품 리뷰', '7000000', 'LG 그램 신모델 상세 리뷰 영상 제작, 성능 테스트 및 비교 분석 포함', '2025-01-18', '2025-03-01', 'WAITING'),
                                                                                                                                                                                            (2007, 2001, '나이키', '나이키 에어맥스 신제품 홍보', '6000000', '나이키 에어맥스 신제품 착화 리뷰 및 운동 콘텐츠', '2025-01-05', '2025-02-10', 'REJECTED'),
                                                                                                                                                                                            (2008, 2003, '에스티로더', '에스티로더 어드밴스드 나이트 리페어', '5500000', '에스티로더 대표 제품 사용 후기 및 스킨케어 루틴 영상', '2024-12-20', '2025-01-31', 'ACCEPTED');

-- ==========================================
-- 9. CreatorWork (크리에이터 업무 현황) - 2000번대
-- ==========================================
INSERT INTO creator_work (creator_work_id, member_creator_id, work_name, work_status, member_worker_id) VALUES
                                                                                                            (2001, 2001, '다음주 콘텐츠 기획안 피드백', 'WORKING', 1003),
                                                                                                            (2002, 2001, '유튜브 채널 아트 리뉴얼 시안 확인', 'WORKING', 1013),
                                                                                                            (2003, 2002, '2월 먹방 콘텐츠 스케줄 조율', 'WORKING', 1003),
                                                                                                            (2004, 2003, '뷰티 트렌드 분석 보고서 검토', 'DONE', 1004),
                                                                                                            (2005, 2003, '신규 협찬 제품 수령 및 확인', 'WORKING', 1004),
                                                                                                            (2006, 2004, '라이브 방송 장비 점검', 'DONE', 1005),
                                                                                                            (2007, 2005, '게임 대회 출전 일정 협의', 'WORKING', 1005),
                                                                                                            (2008, 2005, '신규 게임 협찬 미팅 준비', 'WORKING', 1005),
                                                                                                            (2009, 2006, '브이로그 촬영 장소 섭외', 'WORKING', 1005),
                                                                                                            (2010, 2007, '테크 신제품 발표회 참석 일정 확인', 'DONE', 1003),
                                                                                                            (2011, 2001, '삼성 광고 콘텐츠 1차 편집본 확인', 'WORKING', 1003),
                                                                                                            (2012, 2003, '아모레 협찬 콘텐츠 기획안 작성', 'DONE', 1004);

-- ==========================================
-- 10. Team (팀) - 1000번대
-- ==========================================
INSERT INTO team (team_id, team_name, team_detail) VALUES
                                                       (1001, '감스트 크루', '감스트를 중심으로 한 게임 콘텐츠 제작팀'),
                                                       (1002, '뷰티 컬렉티브', '뷰티 크리에이터들의 협업 팀'),
                                                       (1003, '먹방 유니버스', '먹방 크리에이터 연합'),
                                                       (1004, '테크 리뷰어즈', '테크 리뷰어들의 정보 공유 팀');

-- ==========================================
-- 11. TeamRelay (팀 중계 - 팀 멤버 관계) - 1000번대
-- ==========================================
INSERT INTO team_relay (team_relay_id, team_id, member_id) VALUES
                                                               (1001, 1001, 2001),
                                                               (1002, 1001, 2005),
                                                               (1003, 1002, 2003),
                                                               (1004, 1002, 2006),
                                                               (1005, 1003, 2002),
                                                               (1006, 1003, 2004),
                                                               (1007, 1004, 2007),
                                                               (1008, 1004, 2001);

-- ==========================================
-- 12. Schedule (일정) - 1000번대
-- ==========================================
INSERT INTO schedule (schedule_id, member_id, schedule_name, schedule_date, schedule_detail, schedule_type) VALUES
                                                                                                                (1001, 2001, '삼성 갤럭시 광고 촬영', '2025-02-10', '삼성전자 본사 방문, 갤럭시 S24 울트라 리뷰 촬영', 'PROMOTION'),
                                                                                                                (1002, 2002, '비비고 먹방 촬영', '2025-02-20', '비비고 왕교자 신제품 먹방 콘텐츠 촬영', 'CONTENT'),
                                                                                                                (1003, 2003, '설화수 체험단 행사', '2025-03-05', '아모레퍼시픽 본사 방문, 설화수 신제품 체험', 'PROMOTION'),
                                                                                                                (1004, 2005, '던파 모바일 라이브 방송', '2025-02-05', '넥슨 던전앤파이터 모바일 신작 라이브 방송 3시간', 'LIVE'),
                                                                                                                (1005, 1003, '월간 크리에이터 미팅', '2025-02-03', '담당 크리에이터들과 2월 스케줄 조율 및 피드백', 'MEETING'),
                                                                                                                (1006, 2001, '게임왕민수 합방', '2025-02-15', '게임왕민수와 함께하는 합방 콘텐츠', 'MERGE'),
                                                                                                                (1007, 2005, '게임왕민수 합방', '2025-02-15', '감스트와 함께하는 합방 콘텐츠', 'MERGE'),
                                                                                                                (1008, 1001, '전사 워크샵', '2025-03-15', '2025년 상반기 전사 워크샵 (제주도)', 'COMPANY'),
                                                                                                                (1009, 1007, '시스템 점검', '2025-02-01', '서버 및 시스템 정기 점검', 'COMPANY'),
                                                                                                                (1010, 2006, '일상 브이로그 촬영', '2025-01-30', '카페 투어 브이로그 촬영', 'CONTENT'),
                                                                                                                (1011, 2007, 'LG 그램 제품 발표회', '2025-02-28', 'LG전자 신제품 발표회 참석 및 취재', 'MEETING'),
                                                                                                                (1012, 2003, '뷰티 세미나 참석', '2025-02-12', '2025 K-뷰티 트렌드 세미나 참석', 'PERSONAL'),
                                                                                                                (1013, 2004, '배민 라이브 방송', '2025-02-20', '배달의민족 신규 서비스 소개 라이브', 'LIVE'),
                                                                                                                (1014, 1006, '마케팅 전략 회의', '2025-02-05', '1분기 마케팅 전략 수립 회의', 'MEETING'),
                                                                                                                (1015, 1011, '법인카드 정산', '2025-01-31', '1월 법인카드 사용 내역 정산', 'COMPANY');

-- ==========================================
-- 13. ScheduleVisitor (일정 참가자) - 1000번대
-- ==========================================
INSERT INTO schedule_visitor (schedule_visitor_id, schedule_id, member_id) VALUES
                                                                               (1001, 1006, 2001),
                                                                               (1002, 1006, 2005),
                                                                               (1003, 1007, 2001),
                                                                               (1004, 1007, 2005),
                                                                               (1005, 1008, 1001),
                                                                               (1006, 1008, 1002),
                                                                               (1007, 1008, 1003),
                                                                               (1008, 1008, 1004),
                                                                               (1009, 1008, 1005),
                                                                               (1010, 1005, 1003),
                                                                               (1011, 1005, 2001),
                                                                               (1012, 1005, 2002),
                                                                               (1013, 1005, 2007),
                                                                               (1014, 1014, 1006),
                                                                               (1015, 1014, 1009);

-- ==========================================
-- 14. Vacation (휴가) - 1000번대
-- ==========================================
INSERT INTO vacation (vacation_id, member_id, vacation_type, vacation_start, vacation_end, vacation_request, vacation_detail, vacation_approve, vacation_rejected, vacation_days) VALUES
                                                                                                                                                                                      (1001, 1007, 'ANNUAL', '2025-02-10', '2025-02-14', '2025-01-20', '개인 여행', 'APPROVED', NULL, 5.0),
                                                                                                                                                                                      (1002, 1009, 'HALF', '2025-02-05', '2025-02-05', '2025-01-28', '오후 병원 방문', 'APPROVED', NULL, 0.5),
                                                                                                                                                                                      (1003, 1011, 'FAMILY', '2025-02-20', '2025-02-21', '2025-02-10', '조모 칠순 잔치', 'APPROVE_NEED', NULL, 2.0),
                                                                                                                                                                                      (1004, 1008, 'SICK', '2025-01-29', '2025-01-30', '2025-01-28', '독감 증상으로 병가 신청', 'APPROVED', NULL, 2.0),
                                                                                                                                                                                      (1005, 1012, 'WORKATION', '2025-03-10', '2025-03-17', '2025-02-15', '홍콩 거래처 미팅 및 원격 근무', 'APPROVE_NEED', NULL, 8.0),
                                                                                                                                                                                      (1006, 1010, 'ANNUAL', '2025-04-01', '2025-04-05', '2025-03-01', '봄 휴가', 'APPROVED', NULL, 5.0),
                                                                                                                                                                                      (1007, 1013, 'HALF', '2025-02-07', '2025-02-07', '2025-02-01', '오전 개인 사정', 'REJECTED', '업무 일정상 해당 날짜 반차 불가', 0.5),
                                                                                                                                                                                      (1008, 2001, 'ANNUAL', '2025-03-20', '2025-03-22', '2025-03-05', '휴식', 'APPROVE_NEED', NULL, 3.0),
                                                                                                                                                                                      (1009, 1003, 'WORKATION', '2025-04-15', '2025-04-19', '2025-03-20', '제주 오피스 근무 및 크리에이터 여행 브이로그 보조', 'APPROVED', NULL, 5.0);

-- ==========================================
-- 15. VacationFamily (경조사 상세) - 1000번대
-- ==========================================
INSERT INTO vacation_family (family_id, vacation_id, family_relation, family_detail) VALUES
    (1001, 1003, '조모', '칠순 잔치');

-- ==========================================
-- 16. VacationSick (병가 상세) - 1000번대
-- ==========================================
INSERT INTO vacation_sick (sick_id, vacation_id, sick_detail, sick_hospital) VALUES
    (1001, 1004, '고열(38.5도), 기침, 인후통 증상. 독감 의심', '강남 세브란스 병원');

-- ==========================================
-- 17. VacationWorkation (워케이션 상세) - 1000번대
-- ==========================================
INSERT INTO vacation_workation (workation_id, vacation_id, workation_where, workation_contact, workation_plan, workation_handover) VALUES
                                                                                                                                       (1001, 1005, '홍콩 오피스', '010-1313-1313', '홍콩 거래처 미팅 (3/11, 3/13), 글로벌 파트너십 계약서 검토, 원격 근무를 통한 일반 업무 처리', '긴급 사항 발생 시 조사업 대리(010-1010-1010)에게 연락'),
                                                                                                                                       (1002, 1009, '제주 오피스', '010-3333-3333', '크리에이터 일상브이로거 여행 브이로그 촬영 보조 (4/16-4/17), 제주 지역 협력사 미팅 (4/18), 원격 업무 수행', '담당 크리에이터 관련 긴급 사항은 최담당 매니저(010-4444-4444)에게 인계');

-- ==========================================
-- 18. Health (건강 검진) - 1000번대, 2000번대
-- ==========================================
INSERT INTO health (health_id, member_id, checkup_name, checkup_date, checkup_summanary, checkup_file_url) VALUES
                                                                                                               (1001, 1001, '2024년 정기 건강검진', '2024-11-15', 'NORMAL_AB', 'https://cdn.mcn.com/health/kim_insa_2024.pdf'),
                                                                                                               (1003, 1003, '2024년 정기 건강검진', '2024-10-20', 'NORMAL_B', 'https://cdn.mcn.com/health/park_manager_2024.pdf'),
                                                                                                               (1007, 1007, '2024년 정기 건강검진', '2024-12-05', 'CAUTION', 'https://cdn.mcn.com/health/yoon_dev_2024.pdf'),
                                                                                                               (2001, 2001, '2024년 종합 건강검진', '2024-09-10', 'NORMAL_AB', 'https://cdn.mcn.com/health/gamst_2024.pdf'),
                                                                                                               (2002, 2002, '2024년 종합 건강검진', '2024-09-25', 'DANGER', 'https://cdn.mcn.com/health/ddoganjip_2024.pdf'),
                                                                                                               (2003, 2003, '2024년 종합 건강검진', '2024-10-05', 'NORMAL_B', 'https://cdn.mcn.com/health/beautyjini_2024.pdf'),
                                                                                                               (1008, 1008, '2024년 정기 건강검진', '2024-11-30', 'RETEST_NEED', 'https://cdn.mcn.com/health/han_coding_2024.pdf'),
                                                                                                               (2005, 2005, '2024년 종합 건강검진', '2024-08-15', 'NORMAL_AB', 'https://cdn.mcn.com/health/gameking_2024.pdf');

-- ==========================================
-- 19. CreatorMentalHealth (크리에이터 정신건강 설문) - 2000번대
-- ==========================================
INSERT INTO creator_metal_health (creator_mental_id, member_id, creator_mental_date, creator_mental_score) VALUES
                                                                                                               (2001, 2001, '2025-01-15', 3),
                                                                                                               (2002, 2002, '2025-01-15', 8),
                                                                                                               (2003, 2003, '2025-01-15', 5),
                                                                                                               (2004, 2004, '2025-01-15', 12),
                                                                                                               (2005, 2005, '2025-01-15', 2),
                                                                                                               (2006, 2006, '2025-01-15', 15),
                                                                                                               (2007, 2007, '2025-01-15', 6),
                                                                                                               (2008, 2008, '2025-01-15', 18),
                                                                                                               (2009, 2001, '2024-12-15', 4),
                                                                                                               (2010, 2002, '2024-12-15', 7),
                                                                                                               (2011, 2003, '2024-12-15', 6),
                                                                                                               (2012, 2005, '2024-12-15', 3);

-- ==========================================
-- 20. Attendance (근태) - 최근 2주간 데이터 - 1000번대
-- ==========================================
INSERT INTO attendance (attendance_id, member_id, attendance_date, attendance_start, attendance_end) VALUES
-- 2025-01-13 (월)
(1001, 1001, '2025-01-13', TIMESTAMP '2025-01-13 09:00:00', TIMESTAMP '2025-01-13 18:00:00'),
(1002, 1003, '2025-01-13', TIMESTAMP '2025-01-13 09:15:00', TIMESTAMP '2025-01-13 18:30:00'),
(1003, 1007, '2025-01-13', TIMESTAMP '2025-01-13 10:00:00', TIMESTAMP '2025-01-13 19:00:00'),
(1004, 1009, '2025-01-13', TIMESTAMP '2025-01-13 09:00:00', TIMESTAMP '2025-01-13 18:00:00'),
-- 2025-01-14 (화)
(1005, 1001, '2025-01-14', TIMESTAMP '2025-01-14 09:05:00', TIMESTAMP '2025-01-14 18:00:00'),
(1006, 1003, '2025-01-14', TIMESTAMP '2025-01-14 09:00:00', TIMESTAMP '2025-01-14 20:00:00'),
(1007, 1007, '2025-01-14', TIMESTAMP '2025-01-14 10:00:00', TIMESTAMP '2025-01-14 19:00:00'),
(1008, 1009, '2025-01-14', TIMESTAMP '2025-01-14 09:10:00', TIMESTAMP '2025-01-14 18:00:00'),
-- 2025-01-15 (수)
(1009, 1001, '2025-01-15', TIMESTAMP '2025-01-15 09:00:00', TIMESTAMP '2025-01-15 18:00:00'),
(1010, 1003, '2025-01-15', TIMESTAMP '2025-01-15 09:00:00', TIMESTAMP '2025-01-15 18:00:00'),
(1011, 1007, '2025-01-15', TIMESTAMP '2025-01-15 10:00:00', TIMESTAMP '2025-01-15 19:00:00'),
(1012, 1009, '2025-01-15', TIMESTAMP '2025-01-15 09:00:00', TIMESTAMP '2025-01-15 18:00:00'),
-- 2025-01-16 (목)
(1013, 1001, '2025-01-16', TIMESTAMP '2025-01-16 09:00:00', TIMESTAMP '2025-01-16 18:00:00'),
(1014, 1003, '2025-01-16', TIMESTAMP '2025-01-16 09:30:00', TIMESTAMP '2025-01-16 18:30:00'),
(1015, 1007, '2025-01-16', TIMESTAMP '2025-01-16 10:00:00', TIMESTAMP '2025-01-16 19:00:00'),
(1016, 1009, '2025-01-16', TIMESTAMP '2025-01-16 09:00:00', TIMESTAMP '2025-01-16 18:00:00'),
-- 2025-01-17 (금)
(1017, 1001, '2025-01-17', TIMESTAMP '2025-01-17 09:00:00', TIMESTAMP '2025-01-17 18:00:00'),
(1018, 1003, '2025-01-17', TIMESTAMP '2025-01-17 09:00:00', TIMESTAMP '2025-01-17 18:00:00'),
(1019, 1007, '2025-01-17', TIMESTAMP '2025-01-17 10:00:00', TIMESTAMP '2025-01-17 19:00:00'),
(1020, 1009, '2025-01-17', TIMESTAMP '2025-01-17 09:00:00', TIMESTAMP '2025-01-17 17:30:00'),
-- 2025-01-20 (월)
(1021, 1001, '2025-01-20', TIMESTAMP '2025-01-20 09:00:00', TIMESTAMP '2025-01-20 18:00:00'),
(1022, 1003, '2025-01-20', TIMESTAMP '2025-01-20 09:00:00', TIMESTAMP '2025-01-20 19:00:00'),
(1023, 1007, '2025-01-20', TIMESTAMP '2025-01-20 10:00:00', TIMESTAMP '2025-01-20 19:00:00'),
(1024, 1009, '2025-01-20', TIMESTAMP '2025-01-20 09:00:00', TIMESTAMP '2025-01-20 18:00:00'),
-- 2025-01-21 (화)
(1025, 1001, '2025-01-21', TIMESTAMP '2025-01-21 09:00:00', TIMESTAMP '2025-01-21 18:00:00'),
(1026, 1003, '2025-01-21', TIMESTAMP '2025-01-21 09:00:00', TIMESTAMP '2025-01-21 18:00:00'),
(1027, 1007, '2025-01-21', TIMESTAMP '2025-01-21 10:00:00', TIMESTAMP '2025-01-21 19:00:00'),
(1028, 1009, '2025-01-21', TIMESTAMP '2025-01-21 09:00:00', TIMESTAMP '2025-01-21 18:00:00'),
-- 2025-01-22 (수)
(1029, 1001, '2025-01-22', TIMESTAMP '2025-01-22 09:00:00', TIMESTAMP '2025-01-22 18:00:00'),
(1030, 1003, '2025-01-22', TIMESTAMP '2025-01-22 09:00:00', TIMESTAMP '2025-01-22 18:30:00'),
(1031, 1007, '2025-01-22', TIMESTAMP '2025-01-22 10:00:00', TIMESTAMP '2025-01-22 19:00:00'),
(1032, 1009, '2025-01-22', TIMESTAMP '2025-01-22 09:00:00', TIMESTAMP '2025-01-22 18:00:00'),
-- 2025-01-23 (목)
(1033, 1001, '2025-01-23', TIMESTAMP '2025-01-23 09:00:00', TIMESTAMP '2025-01-23 18:00:00'),
(1034, 1003, '2025-01-23', TIMESTAMP '2025-01-23 09:00:00', TIMESTAMP '2025-01-23 18:00:00'),
(1035, 1007, '2025-01-23', TIMESTAMP '2025-01-23 10:00:00', TIMESTAMP '2025-01-23 19:00:00'),
(1036, 1009, '2025-01-23', TIMESTAMP '2025-01-23 09:00:00', TIMESTAMP '2025-01-23 18:00:00'),
-- 2025-01-24 (금)
(1037, 1001, '2025-01-24', TIMESTAMP '2025-01-24 09:00:00', TIMESTAMP '2025-01-24 18:00:00'),
(1038, 1003, '2025-01-24', TIMESTAMP '2025-01-24 09:00:00', TIMESTAMP '2025-01-24 18:00:00'),
(1039, 1007, '2025-01-24', TIMESTAMP '2025-01-24 10:00:00', TIMESTAMP '2025-01-24 19:00:00'),
(1040, 1009, '2025-01-24', TIMESTAMP '2025-01-24 09:00:00', TIMESTAMP '2025-01-24 18:00:00'),
-- 2025-01-27 (월) - 오늘
(1041, 1001, '2025-01-27', TIMESTAMP '2025-01-27 09:00:00', NULL),
(1042, 1003, '2025-01-27', TIMESTAMP '2025-01-27 09:00:00', NULL),
(1043, 1007, '2025-01-27', TIMESTAMP '2025-01-27 10:00:00', NULL),
(1044, 1009, '2025-01-27', TIMESTAMP '2025-01-27 09:00:00', NULL);


-- 데이터 입력 완료
-- ==========================================
-- 더미데이터: 1000번대 (직원), 2000번대 (크리에이터)
-- 포스트맨 테스트: 1번부터 자동 증가
-- ==========================================