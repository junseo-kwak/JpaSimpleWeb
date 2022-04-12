package jpabook.jpabookstore.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
               joinColumns = @JoinColumn(name = "category_id"),
               inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();
    @OneToMany(mappedBy = "parent")
    private List<Category> childList = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    public void addChildCategory(Category child){
        this.childList.add(child);
        child.setParent(this);
    }

}
