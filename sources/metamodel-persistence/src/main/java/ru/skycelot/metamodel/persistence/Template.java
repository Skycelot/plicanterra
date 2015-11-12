package ru.skycelot.metamodel.persistence;

import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "template")
public class Template {

    @Id
    @Column(name = "")
    private Long id;
    @Column(name = "")
    private String code;
    @Column(name = "")
    private String name;
    @Column(name = "")
    private String description;
    @OneToMany(mappedBy = "template")
    private List<Attribute> attributes;
    @OneToMany(mappedBy = "template")
    private List<Link> links;
    @OneToMany(mappedBy = "template")
    private List<Status> statuses;
    @OneToMany(mappedBy = "template")
    private List<TemplatePermissions> permissions;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}
