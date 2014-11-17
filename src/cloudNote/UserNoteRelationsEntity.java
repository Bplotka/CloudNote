package cloudNote;

import javax.persistence.*;

/**
 * Created by Piotr on 2014-11-04.
 */
@Entity
@Table(name = "user_note_relations", schema = "", catalog = "cloudnote")
public class UserNoteRelationsEntity {
    private int id;
    private Integer noteRight;
    private Integer userId;
    private Integer noteId;
    public UserEntity user;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "note_right")
    public Integer getNoteRight() {
        return noteRight;
    }

    public void setNoteRight(Integer noteRight) {
        this.noteRight = noteRight;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "note_id")
    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserNoteRelationsEntity that = (UserNoteRelationsEntity) o;

        if (id != that.id) return false;
        if (noteId != null ? !noteId.equals(that.noteId) : that.noteId != null) return false;
        if (noteRight != null ? !noteRight.equals(that.noteRight) : that.noteRight != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (noteRight != null ? noteRight.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (noteId != null ? noteId.hashCode() : 0);
        return result;
    }
}
