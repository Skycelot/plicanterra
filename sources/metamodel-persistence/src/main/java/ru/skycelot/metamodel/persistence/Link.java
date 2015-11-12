package ru.skycelot.metamodel.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ru.skycelot.plicanterra.metamodel.LinkType;

/**
 *
 */
@Entity
@Table(name = "link")
public class Link {

    @Id
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "left_template_id")
    private Template leftTemplate;
    @ManyToOne
    @JoinColumn(name = "right_template_id")
    private Template rightTemplate;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private LinkType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Template getLeftTemplate() {
        return leftTemplate;
    }

    public void setLeftTemplate(Template leftTemplate) {
        this.leftTemplate = leftTemplate;
    }

    public Template getRightTemplate() {
        return rightTemplate;
    }

    public void setRightTemplate(Template rightTemplate) {
        this.rightTemplate = rightTemplate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkType getType() {
        return type;
    }

    public void setType(LinkType type) {
        this.type = type;
    }
}
