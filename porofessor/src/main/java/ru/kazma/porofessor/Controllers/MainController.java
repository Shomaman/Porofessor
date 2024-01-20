package ru.kazma.porofessor.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.kazma.porofessor.DTO.*;

import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class MainController {
    @GetMapping
    public String hello(){
        return "main";
    }
    @PostMapping("check")
    public String check(@RequestParam String summonnerName, Model model){
        RestTemplate restTemplate = new RestTemplate();
        String urlInfoAboutAccount = "https://ru.api.riotgames.com/lol/summoner/v4/summoners/by-name/";
        String key = "?api_key=RGAPI-c528133e-6cec-4931-af7d-f6b9855cf283";
       SummonerDTO summonerDTO =  restTemplate.getForObject(urlInfoAboutAccount+summonnerName+key, SummonerDTO.class);
       String puuidCurrentSummoner = summonerDTO.getPuuid();
        String urlListGames = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/";
        String config = "/ids?startTime=1700410860&queue=420&type=ranked&start=0&count=20&api_key=RGAPI-c528133e-6cec-4931-af7d-f6b9855cf283";
      List listGamesSummoner =restTemplate.getForObject(urlListGames+puuidCurrentSummoner+config, List.class);
       String urlWithMatchStat = "https://europe.api.riotgames.com/lol/match/v5/matches/";
        double killsTotal = 0;
        double deathsTotal = 0;
        double assistsTotal = 0;
        int countGames = 0;
        double wins = 0;
        double winrate = 0d;
       for(Object game : listGamesSummoner){
            MatchDto matchDTO = restTemplate.getForObject(urlWithMatchStat+(String) game+key, MatchDto.class);

            InfoDto infoDTO = matchDTO.getInfo();
            List<ParticipantsDTO> participants = infoDTO.getParticipants();
            for(ParticipantsDTO participant:participants){
                if(participant.getSummonerName().equals(summonnerName)){
                    if(participant.getChampionName().equals("Trundle")){
                        countGames++;
                        if(participant.getWin()){
                           wins++;
                        }
                        killsTotal+= participant.getKills();
                        deathsTotal+= participant.getDeaths();
                        assistsTotal+= participant.getAssists();
                    }
                }
            }
       }
      log.info("killsTotal{} deathsTotal {} assistsTotal{}",killsTotal, deathsTotal, assistsTotal);
       double kills =  (double) Math.round((killsTotal / countGames) * 10) /10;
       double deaths = (double) Math.round((deathsTotal / countGames) * 10) /10;
       double assists = (double) Math.round((assistsTotal / countGames) * 10) /10;
       winrate = (Math.round(wins/countGames))*100;
       model.addAttribute("name",summonerDTO.getName());
       model.addAttribute("id",summonerDTO.getId());
       model.addAttribute("accountId",summonerDTO.getAccountId());
       model.addAttribute("puuid",summonerDTO.getPuuid());
       model.addAttribute("profileIconId",summonerDTO.getProfileIconId());
       model.addAttribute("revisionDate",summonerDTO.getRevisionDate());
       model.addAttribute("summonerLevel",summonerDTO.getSummonerLevel());
        model.addAttribute("kills", kills);
        model.addAttribute("deaths", deaths);
        model.addAttribute("assists", assists);
        model.addAttribute("winrate", winrate);
        model.addAttribute("countGames", countGames);
        model.addAttribute("summonnerName", summonnerName);
        return "info";
    }
}
