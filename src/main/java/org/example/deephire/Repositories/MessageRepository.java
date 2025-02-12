package org.example.deephire.Repositories;

import org.example.deephire.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

=======
import org.springframework.stereotype.Repository;

@Repository
>>>>>>> 20083e789c1e9823027e8725fdc7393f8864974f
public interface MessageRepository extends JpaRepository<Message,Long> {
}
