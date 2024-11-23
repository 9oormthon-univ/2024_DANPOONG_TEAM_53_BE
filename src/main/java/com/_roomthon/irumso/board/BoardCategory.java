package com._roomthon.irumso.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BoardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // 주거, 의료, 학업, 기타
    /* db상에서 id 값
       1 = 주거
       2 = 의료
       3 = 학업
       4 = 기타
     */
}
