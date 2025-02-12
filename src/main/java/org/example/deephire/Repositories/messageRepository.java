package org.example.deephire.Repositories;

import org.example.deephire.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface messageRepository extends JpaRepository<Long, Message> {
}
