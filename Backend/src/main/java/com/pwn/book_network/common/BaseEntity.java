package com.pwn.book_network.common;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;


@Getter
@Setter
@SuperBuilder // for creating hierarchy - " book extends BaseEntity "
@MappedSuperclass // defines the base class for inheritance
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
/*
   @EntityListener used to specify listener class that should notify
   lifecycle events fot this entity

   AuditingEntityListener - Provided bu spring data JPA that acts as entity
   listener for auditing purposes,
 */
public class BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column( nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    /* Spring doesn't know how to get user by default so to audit " who did what ", have to implement different technique
       for that config.ApplicationAuditAware class is implemented -->
    */
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;
}
