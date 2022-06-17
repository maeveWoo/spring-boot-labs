package io.springbootlabs.domain.file;

import io.springbootlabs.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Kind kind;

    /** byte size **/
    private Long size;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    protected File() {}

    private File(User user, Type type, String name, Kind kind, Long size) {
        this.user = user;
        this.type = type;
        this.name = name;
        this.size = size;
        this.kind = kind;
        lastUpdated = LocalDateTime.now();
    }

    /** TODO test용 표시하기 */
    public File(User user, Type type, String name, Kind kind, Long size, LocalDateTime lastUpdated) {
        this.user = user;
        this.type = type;
        this.name = name;
        this.size = size;
        this.kind = kind;
        this.lastUpdated = lastUpdated;
    }

    public static File createFile(User user, Type type, String name, Kind kind, Long size){
        return new File(user, type, name, kind, size);
    }
}
