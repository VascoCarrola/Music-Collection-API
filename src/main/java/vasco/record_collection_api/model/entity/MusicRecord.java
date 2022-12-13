package vasco.record_collection_api.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Table(name = "music_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String artist;
    private String label;
    private Integer year;
    private String pictureUrl;
    private String spotifyUrl;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
