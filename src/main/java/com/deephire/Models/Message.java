package com.deephire.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;
    private Date timestamp;

    @ManyToOne
    @JsonBackReference("user-sent")
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JsonBackReference("user-received")
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private boolean isRead;
}