package ru.alexey_ovcharov.rusfootballmanager.career;

import ru.alexey_ovcharov.rusfootballmanager.common.DataLoader;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Tournament;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Status;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alexey
 */
public class CareerSettings {

    private List<League> leagues;
    private List<Tournament> tournaments;
    private Market transferMarket;

    public boolean isNewCareer() {
        return leagues == null || transferMarket == null;
    }

    @Nonnull
    public List<League> getLeagues() throws Exception {
        if (leagues == null) {
            leagues = DataLoader.loadLeagues("config.xml");
        }
        return leagues;
    }

    public void createTransfers() {
        if (transferMarket == null) {
            transferMarket = Market.getInstance();
            leagues.stream()
                   .map(League::getTeams)
                   .parallel()
                   .flatMap(List::stream)
                   .forEach(team -> {
                       List<Player> players = team.getAllPlayers();
                       players.forEach(player -> transferMarket.addPlayer(player,
                               team, Status.ON_CONTRACT));
                   });
        }
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void createTournaments(Date date) {
        tournaments = new ArrayList<>();
        if (leagues != null) {
            leagues.forEach(league -> tournaments.add(new Tournament(date, league)));
        }
    }

    public void updateTournaments(Date date, Tournament exclude) {
        if (tournaments != null && !tournaments.isEmpty()) {
            for (Tournament tournament : tournaments) {
                if (tournament != exclude) {
                    tournament.updateToDate(date);
                }
            }
        }
    }

    public void simulateTransfers() {
        //TODO еще не реализовано
    }

}