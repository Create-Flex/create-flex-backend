package com.mcn.in4.domain.creator.service;

import com.mcn.in4.domain.creator.dto.request.CreatorWorkRequestDTO;
import com.mcn.in4.domain.creator.dto.response.CreatorWorkResponseDTO;
import com.mcn.in4.domain.creator.entity.CreatorWork;
import com.mcn.in4.domain.creator.entity.creatorEnum.WorkStatus;
import com.mcn.in4.domain.creator.repository.CreatorRepository;
import com.mcn.in4.domain.creator.repository.CreatorWorkRepository;
import com.mcn.in4.domain.member.entity.Member;
import com.mcn.in4.domain.member.entity.memberEnum.MemberRole;
import com.mcn.in4.domain.member.entity.memberEnum.MemberStatus;
import com.mcn.in4.global.error.exception.CustomException;
import com.mcn.in4.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatorWorkServiceImpl implements CreatorWorkService {

    private final CreatorWorkRepository creatorWorkRepository;
    private final CreatorRepository creatorRepository;

    // 업무 목록 조회
    @Override
    public List<CreatorWorkResponseDTO.Info> getWorks(Long creatorId) {
        findCreator(creatorId);
        return creatorWorkRepository.findByCreatorIdWithWorker(creatorId).stream()
                .map(CreatorWorkResponseDTO.Info::from)
                .collect(Collectors.toList());
    }

    // 업무 추가
    @Override
    @Transactional
    public CreatorWorkResponseDTO.Info createWork(Long creatorId, Long workerId, CreatorWorkRequestDTO.Create request) {
        Member creator = findCreator(creatorId);
        Member worker = findMember(workerId);

        CreatorWork work = CreatorWork.builder()
                .memberCreator(creator)
                .workName(request.getWorkName())
                .workStatus(WorkStatus.WORKING)
                .memberWorker(worker)
                .build();

        return CreatorWorkResponseDTO.Info.from(creatorWorkRepository.save(work));
    }

    // 업무 상태 변경
    @Override
    @Transactional
    public CreatorWorkResponseDTO.Info updateWorkStatus(Long creatorId, Long workId, CreatorWorkRequestDTO.UpdateStatus request) {
        CreatorWork work = findWork(creatorId, workId);
        work.updateWorkStatus(WorkStatus.valueOf(request.getWorkStatus()));
        return CreatorWorkResponseDTO.Info.from(work);
    }

    // 업무 삭제
    @Override
    @Transactional
    public void deleteWork(Long creatorId, Long workId) {
        findWork(creatorId, workId);
        creatorWorkRepository.deleteById(workId);
    }

    // 크리에이터 조회
    private Member findCreator(Long creatorId) {
        return creatorRepository.findCreatorByIdWithDepartment(
                        creatorId, MemberRole.CREATOR, MemberStatus.WORKING)
                .orElseThrow(() -> new CustomException(ErrorCode.CREATOR_NOT_FOUND, "크리에이터 ID " + creatorId + "를 찾을 수 없습니다."));
    }

    // 회원 조회 (업무 작성자)
    private Member findMember(Long memberId) {
        return creatorRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND, "회원 ID " + memberId + "를 찾을 수 없습니다."));
    }

    // 업무 조회 (크리에이터 키 + 업무 키)
    private CreatorWork findWork(Long creatorId, Long workId) {
        return creatorWorkRepository.findByWorkIdAndCreatorId(workId, creatorId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND, "업무 ID " + workId + "를 찾을 수 없습니다."));
    }
}
