package com.deephire.Repositories;

import com.deephire.Dto.ConversationPartnerDto;
import com.deephire.Models.Message;
import com.deephire.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
    SELECT m FROM Message m
    WHERE 
        (m.sender.username = :user1 AND m.receiver.username = :user2)
        OR
        (m.sender.username = :user2 AND m.receiver.username = :user1)
    ORDER BY m.timestamp ASC
""")
    List<Message> findMessagesBetweenUsers(@Param("user1") String user1, @Param("user2") String user2);




    @Query("""
    SELECT DISTINCT new com.deephire.Dto.ConversationPartnerDto(
        CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END,
        CASE WHEN m.sender.id = :userId THEN m.receiver.username ELSE m.sender.username END
    )
    FROM Message m
    WHERE m.sender.id = :userId OR m.receiver.id = :userId
""")
    List<ConversationPartnerDto> findConversationPartners(@Param("userId") Long userId);

}
