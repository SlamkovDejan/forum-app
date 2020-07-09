package mk.ukim.finki.emt.forum.forummanagement.domain.value;

import lombok.Getter;
import mk.ukim.finki.emt.forum.forummanagement.domain.exception.PostContentTooLongException;
import mk.ukim.finki.emt.forum.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Getter
@Embeddable
public class Content implements ValueObject {

    @Transient
    private static final int MAX_WORD_COUNT = 1000;

    @Column(name = "content", nullable = false)
    private final String content;

    public Content(@NonNull String content) {
        content = content.trim();
        int wordCount = content.split("\\s+").length;
        if(wordCount > MAX_WORD_COUNT){
            throw new PostContentTooLongException("The contents of the post are too long!");
        }
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Content)) return false;
        Content content1 = (Content) o;
        return content.equals(content1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

}
