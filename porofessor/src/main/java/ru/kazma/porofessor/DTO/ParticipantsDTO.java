package ru.kazma.porofessor.DTO;

import lombok.Data;

@Data
public class ParticipantsDTO {
    private int assists;
    private int deaths;
    private int kills;
    private  String championName;
    private  String summonerName;
    private Boolean win;
}
