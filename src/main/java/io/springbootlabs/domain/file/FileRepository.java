package io.springbootlabs.domain.file;

import io.springbootlabs.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("select f from File f where f.user = ?1 and f.type = ?2 order by f.lastUpdated desc")
    List<File> findBy(User user, Type type);
}
