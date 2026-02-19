package com.mcn.in4.domain.chat.repository;

import com.mcn.in4.domain.chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, String> {

    //특정 채팅방 전체 인원수 카운트 쿼리
    @Query("SELECT COUNT(cm) FROM ChatRoomMember cm WHERE cm.chatRoom.roomId = :roomId")
    Long countByRoomId(@Param("roomId") String roomId);

    //읽은 사람 수(맨 마지막 읽은 매세지 >= 현재 메세지)
    @Query("SELECT COUNT(cm) FROM ChatRoomMember cm WHERE cm.chatRoom.roomId = :roomId AND cm.lastReadMessageId >= :messageId")
    long countReadMembers(@Param("roomId") String roomId, @Param("messageId") Long messageId);

    //접속중인 즉 구독하고있는 사람들 마지막 읽음 업데이트 벌크 업데이트
    @Modifying(clearAutomatically = true) // 벌크 업데이트시 영속성 컨텍스트와 데이터가 다른 오류가 발생 가능 으로 클리어 하는 로직 추가
    @Query("UPDATE ChatRoomMember cm SET cm.lastReadMessageId = :lastReadMessageId WHERE cm.chatRoom.roomId = :roomId AND cm.member.memberId IN :memberIds")
    void updateLastReadMessageId(@Param("roomId") String roomId, @Param("memberIds") List<Long> memberIds, @Param("lastReadMessageId") Long lastReadMessageId);


    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChatRoomMember cm SET cm.lastReadMessageId = :lastReadMessageId WHERE cm.chatRoom.roomId = :roomId AND cm.member.memberId = :memberId")
    void updateMemberLastReadMessage(@Param("roomId") String roomId, @Param("memberId") Long memberId, @Param("lastReadMessageId") Long lastReadMessageId);
}
