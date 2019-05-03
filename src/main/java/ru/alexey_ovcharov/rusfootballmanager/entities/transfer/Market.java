package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ru.alexey_ovcharov.rusfootballmanager.entities.player.Contract;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 * Трансферный рынок
 *
 * @author Alexey
 */
public class Market {

    private static final Logger LOGGER = Logger.getLogger(Market.class.getName());
    private static final Market MARKET = new Market();
    private final List<Transfer> players = new ArrayList<>();
    private final Set<Offer> offers = new HashSet<>();
    private final Map<Team, List<Transfer>> cache = new HashMap<>();

    private Market() {
    }

    public static Market getInstance() {
        return MARKET;
    }

    public void clear() {
        players.clear();
    }

    public void addPlayer(Player player, Team team, TransferStatus transferStatus) {
        players.add(new Transfer(player, team, transferStatus));
    }

    public void removePlayer(Player player) {
        players.stream()
               .filter(transferPlayer -> transferPlayer.getPlayer() == player)
               .findFirst()
               .ifPresent(players::remove);
    }

    public List<Transfer> getTransfers() {
        return players;
    }

    public List<Transfer> getTransfers(Filter filter) {
        if (filter != null) {
            return players.stream()
                          .filter(filter::accept)
                          .collect(Collectors.toList());
        }
        return players;
    }

    public List<Transfer> getTransfers(TransferStatus transferStatus) {
        return players.stream()
                      .filter(tr -> tr.getTransferStatus() == transferStatus)
                      .collect(Collectors.toList());
    }

    public List<Transfer> getTransfers(Team team) {
        List<Transfer> playersFromTeam;
        if (cache.containsKey(team)) {
            playersFromTeam = cache.get(team);
        } else {
            playersFromTeam = players.stream()
                                     .filter(Objects::nonNull)
                                     .filter(player -> player.getTeam() == team)
                                     .collect(Collectors.toList());
            cache.put(team, playersFromTeam);
        }
        return playersFromTeam;
    }

    public List<Transfer> getTransfers(Team team, Filter filter) {
        List<Transfer> playersFromTeam = getTransfers(team);
        if (filter != null) {
            return playersFromTeam.stream()
                                  .filter(filter::accept)
                                  .collect(Collectors.toList());
        } else {
            return playersFromTeam;
        }
    }

    public List<Offer> getOffers(Team team) {
        return offers.stream()
                     .filter(offer -> offer.getFromTeam() == team || offer.getToTeam() == team)
                     .collect(Collectors.toList());
    }

    public void makeOffer(Offer offer) {
        this.offers.add(offer);
    }

    public void removeOffer(Offer selectedOffer) {
        this.offers.remove(selectedOffer);
    }

    public void processOffers(LocalDate date) {
        LOGGER.info("Start processOffers");
        Set<Player> accepted = new HashSet<>();
        for (Iterator<Offer> iterator = offers.iterator(); iterator.hasNext(); ) {
            Offer offer = iterator.next();
            Player player = offer.getPlayer();
            if (accepted.contains(player)) {
                offer.decline();
                iterator.remove();
            } else {
                TransferResult transferResult = offer.process(date);
                if (transferResult == TransferResult.ACCEPT) {
                    accepted.add(player);
                }
            }

        }
    }

    public void performTransfer(Offer offer) {
        Player player = offer.getPlayer();
        Team fromTeam = offer.getFromTeam();
        fromTeam.removePlayer(player);

        Team toTeam = offer.getToTeam();
        toTeam.addPlayer(player);
        player.setContract(new Contract(offer.getContractDuration(), offer.getFare()));
        removePlayer(player);
        addPlayer(player, toTeam, TransferStatus.ON_CONTRACT);
    }
}
