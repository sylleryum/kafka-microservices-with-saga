package com.sylleryum.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ItemDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String itemNumber;
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderDAO order;
    private long quantity;
}
