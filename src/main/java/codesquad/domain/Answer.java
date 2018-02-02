package codesquad.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import codesquad.UnAuthorizedException;
import codesquad.dto.AnswerDto;
import support.domain.AbstractEntity;
import support.domain.UrlGeneratable;

import java.time.LocalDateTime;

@Entity
public class Answer extends AbstractEntity implements UrlGeneratable {
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Size(min = 5)
    @Lob
    private String contents;

    private boolean deleted = false;

    public Answer() {
    }

    public Answer(User writer, String contents) {
        this.writer = writer;
        this.contents = contents;
    }

    public Answer(long id, String contents, boolean deleted) {
        super(id);
        this.contents = contents;
        this.deleted = deleted;
    }

    public Answer(Long id, User writer, Question question, String contents) {
        super(id);
        this.writer = writer;
        this.question = question;
        this.contents = contents;
        this.deleted = false;
    }

    public void writeBy(User loginUser) {
        this.writer = loginUser;
    }

    public User getWriter() {
        return writer;
    }

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents;
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public boolean isOwner(User loginUser) {
        return writer.equals(loginUser);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public AnswerDto toAnswerDto() {
        if (isDeleted()) {
            return null;
        }
        return new AnswerDto(getId(), this.contents);
    }

    public void delete(User loginUser) {
        if (!isOwner(loginUser))
            throw new UnAuthorizedException();

        this.deleted = true;
    }

    @Override
    public String generateUrl() {
        return String.format("/api/%s/answers/%d", question.generateUrl(), getId());
    }

    @Override
    public String toString() {
        return "Answer [id=" + getId() + ", writer=" + writer + ", contents=" + contents + "]";
    }
}
