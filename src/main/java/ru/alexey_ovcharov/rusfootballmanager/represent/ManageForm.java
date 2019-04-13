package ru.alexey_ovcharov.rusfootballmanager.represent;

import ru.alexey_ovcharov.rusfootballmanager.career.Message;
import ru.alexey_ovcharov.rusfootballmanager.entities.MoneyDay;
import ru.alexey_ovcharov.rusfootballmanager.entities.match.Match;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Opponents;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Table;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Tournament;
import ru.alexey_ovcharov.rusfootballmanager.career.User;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Алексей
 */
public class ManageForm extends javax.swing.JDialog {

    private static final Logger LOGGER = Logger.getLogger(ManageForm.class.getName());
    private static final int ONE_EVENT_TRAINING_HOURS = 10;
    private transient User user;
    private int availableHours = ONE_EVENT_TRAINING_HOURS;
    private Team team;

    public ManageForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initLocation();
    }

    public void setUser(User user) {
        this.user = user;
        this.team = user.getTeam();
        lSeason.setText(user.getSeason());
        lYear.setText(user.getCurrentDate().format(DateTimeFormatter.ISO_DATE));
        listMessages.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return user.getMessages().size();
            }

            @Override
            public String getElementAt(int index) {
                Message message = user.getMessages().get(index);
                return "От: " + message.getFrom() + ", Тема: " + message.getTheme();
            }
        });
        progressBarSupportLevel.setStringPainted(true);
        progressBarSupportLevel.setValue(team.getSupport());
        progressBarSupportLevel.setString(String.valueOf(team.getSupport()));

        progressBarTeamwork.setStringPainted(true);
        progressBarTeamwork.setValue(team.getTeamwork());
        progressBarTeamwork.setString(String.valueOf(team.getTeamwork()));
        Tournament tournament = user.getTournament();
        tournament.createSchedule();
        updateUserInterface(user.getCurrentDate());
    }

    private void updateUserInterface(LocalDate nextDate) {
        Tournament tournament = user.getTournament();
        updateBudgetLabel();
        lTrainerLevel.setText("Опыт тренера: " + user.getExperience());
        lPlayedGames.setText("Сыграно: " + (tournament.getMatchIndex() - 1) + "/" + tournament.getToursCount());
        Table tournamentTable = tournament.getTournamentTable();
        if (tournamentTable != null) {
            int placeNumberOfTeam = tournamentTable.getPlaceNumberOfTeam(team);
            if (placeNumberOfTeam != 0) {
                lTournamentPlace.setText("Место: " + placeNumberOfTeam + "/" + tournamentTable.getTeamsCount());
            } else {
                lTournamentPlace.setText("Место: ?/" + tournamentTable.getTeamsCount());
            }
        } else {
            lTournamentPlace.setText("");
        }
        lSeason.setText(user.getSeason());
        LocalDate currentDate = user.getCurrentDate();
        lYear.setText(currentDate.format(DateTimeFormatter.ISO_DATE));
        progressBarSupportLevel.setValue(team.getSupport());
        progressBarSupportLevel.setString(String.valueOf(team.getSupport()));
        progressBarTeamwork.setValue(team.getTeamwork());
        progressBarTeamwork.setString(String.valueOf(team.getTeamwork()));

        if (tournament.isGameDays(nextDate)) {
            bStartMatch.setText("Начать матч");
            Opponents opponents = tournament.getTeamWithPlaysNext(team);
            Team opponent = opponents.getOpponentOf(team);
            lNextMatchInfo.setText("Следующий матч с командой: " + opponent.getName());
        } else {
            bStartMatch.setText("Далее");
            if (tournament.isEnd()) {
                bStartMatch.setEnabled(false);
                lNextMatchInfo.setText("Турнир завершен");
            } else {
                lNextMatchInfo.setText("Период трансферов");
            }
        }
        listMessages.updateUI();
    }

    private void nextEvent() {
        LOGGER.info("nextEvent");
        Tournament tournament = user.getTournament();
        LocalDate currentDate = user.getCurrentDate();

        if (tournament.isGameDays(currentDate)) {
            LOGGER.info("game days");
            Optional<Match> matchResult = getMatchResult(tournament, currentDate, team);
            if (matchResult.isPresent()) {
                availableHours = ONE_EVENT_TRAINING_HOURS;
                Match match = matchResult.get();
                tournament.simulateTour(currentDate, match);
                LocalDate matchDate = match.getMatchDate();
                Optional<MoneyDay> moneyLogOpt = team.getMoneyLog(matchDate);
                moneyLogOpt.ifPresent(moneyLog -> user.addMessage(new Message("Бухгалтерия команды",
                        matchDate, "Финансовый отчет за " + matchDate.format(DateTimeFormatter.ISO_DATE),
                        moneyLog.report())));
            } else {
                return;
            }
        } else {
            LOGGER.info("holidays");
            user.getSettings().simulateTransfers();
            availableHours = ONE_EVENT_TRAINING_HOURS;
        }
        Optional<Tournament.Event> eventOpt = tournament.nextEvent();
        eventOpt.ifPresent(event -> user.setCurrentDate(event.getLocalDate()));
        if (tournament.isEnd()) {
            Table tournamentTable = tournament.getTournamentTable();
            if (tournamentTable != null) {
                JOptionPane.showMessageDialog(this, "Турнир завершен, ваша команда заняла место: "
                        + tournamentTable.getPlaceNumberOfTeam(team));
            }
        }
        updateUserInterface(user.getCurrentDate());
    }

    private Optional<Match> getMatchResult(Tournament tournament, LocalDate currentDate, Team team) {
        StartMatchForm startMatchForm = new StartMatchForm(null, true);
        Opponents opponents = tournament.getTeamWithPlaysNext(team);
        startMatchForm.setData(opponents, currentDate, tournament);
        startMatchForm.setLocationRelativeTo(this);
        startMatchForm.setVisible(true);
        return startMatchForm.getMatchResult();
    }

    private void showSportSchoolForm() {
        SportSchoolForm schoolForm = new SportSchoolForm(null, true);
        schoolForm.setTeam(team);
        schoolForm.setVisible(true);
    }

    private void showTrainingForm() {
        TrainingForm trainingForm = new TrainingForm(null, true);
        trainingForm.setLocationRelativeTo(this);
        trainingForm.init(team, availableHours);
        trainingForm.setVisible(true);
        availableHours = trainingForm.getAvailableHours();
    }

    private void showTeamSettingsForm() {
        TeamSettingsForm teamSettingsForm = new TeamSettingsForm(null, true);
        teamSettingsForm.setLocationRelativeTo(this);
        teamSettingsForm.setTeam(team);
        teamSettingsForm.setVisible(true);
    }

    private void showPlayersForm() {
        PlayersForm playersForm = new PlayersForm(null, true);
        playersForm.setTeam(team);
        playersForm.setVisible(true);
    }

    private void showTransferForm() {
        TransferForm transferForm = new TransferForm(null, true);
        transferForm.setLocationRelativeTo(this);
        transferForm.setTeam(team);
        transferForm.setVisible(true);
    }

    private void showPersonalForm() {
        PersonalForm personalForm = new PersonalForm(null, true);
        personalForm.init(team, user.getCurrentDate());
        personalForm.setVisible(true);
        updateBudgetLabel();
    }

    private void updateBudgetLabel() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
        String budgetPrinted = numberFormat.format(team.getBudget());
        lBudget.setText("Бюджет: " + budgetPrinted);
    }

    private void showTournamentForm() {
        TournamentForm tournamentForm = new TournamentForm(null, true);
        tournamentForm.setPreferredSize(this.getPreferredSize());
        tournamentForm.setSize(this.getSize());
        Tournament tournament = user.getTournament();
        tournamentForm.setTournament(tournament);
        tournamentForm.setLocationRelativeTo(this);
        tournamentForm.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paneNextMatch = new javax.swing.JPanel();
        bStartMatch = new javax.swing.JButton();
        bTraining = new javax.swing.JButton();
        bTeamSettings = new javax.swing.JButton();
        lNextMatchInfo = new javax.swing.JLabel();
        lPlayedGames = new javax.swing.JLabel();
        lSeason = new javax.swing.JLabel();
        lTournamentPlace = new javax.swing.JLabel();
        lYear = new javax.swing.JLabel();
        paneMail = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listMessages = new javax.swing.JList<>();
        lDetails = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDescriptions = new javax.swing.JTextArea();
        bDeleteMessage = new javax.swing.JButton();
        bClearPost = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        progressBarSupportLevel = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        progressBarTeamwork = new javax.swing.JProgressBar();
        lBudget = new javax.swing.JLabel();
        lTrainerLevel = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        bCommandStructure = new javax.swing.JButton();
        bStaff = new javax.swing.JButton();
        bSportSchool = new javax.swing.JButton();
        bPlayersMarket = new javax.swing.JButton();
        bTournamentTable = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Перед матчем");
        setResizable(false);
        setSize(new java.awt.Dimension(800, 500));

        paneNextMatch.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bStartMatch.setText("Начать матч");
        bStartMatch.addActionListener(this::bStartMatchActionPerformed);

        bTraining.setText("Тренировка");

        bTeamSettings.setText("Руководство командой");
        bTeamSettings.addActionListener(this::bTeamSettingsActionPerformed);

        lNextMatchInfo.setText("Следующий матч с командой");

        lPlayedGames.setText("Сыграно: 0/30");

        lSeason.setText("Сезон 1");

        lTournamentPlace.setText("Место: 1/16");

        lYear.setText("Год: 2016");

        javax.swing.GroupLayout paneNextMatchLayout = new javax.swing.GroupLayout(paneNextMatch);
        paneNextMatch.setLayout(paneNextMatchLayout);
        paneNextMatchLayout.setHorizontalGroup(
                paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                   .addGroup(paneNextMatchLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                             .addGroup(paneNextMatchLayout.createSequentialGroup()
                                                                                                                          .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                                                                                       .addComponent(bTeamSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                                                       .addComponent(bTraining, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                                                                          .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                          .addComponent(bStartMatch, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                             .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneNextMatchLayout.createSequentialGroup()
                                                                                                                                                                      .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                                   .addComponent(lYear)
                                                                                                                                                                                                   .addComponent(lSeason))
                                                                                                                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                                                                      .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                                   .addGroup(paneNextMatchLayout.createSequentialGroup()
                                                                                                                                                                                                                                .addComponent(lTournamentPlace)
                                                                                                                                                                                                                                .addGap(151, 151, 151)
                                                                                                                                                                                                                                .addComponent(lPlayedGames))
                                                                                                                                                                                                   .addComponent(lNextMatchInfo))))
                                                                .addContainerGap())
        );
        paneNextMatchLayout.setVerticalGroup(
                paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                   .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneNextMatchLayout.createSequentialGroup()
                                                                                                            .addContainerGap()
                                                                                                            .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                         .addComponent(lSeason)
                                                                                                                                         .addComponent(lPlayedGames)
                                                                                                                                         .addComponent(lTournamentPlace))
                                                                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                            .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                         .addComponent(lYear)
                                                                                                                                         .addComponent(lNextMatchInfo))
                                                                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                            .addComponent(bTeamSettings)
                                                                                                            .addGap(18, 18, 18)
                                                                                                            .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                                                                         .addComponent(bStartMatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                                         .addComponent(bTraining))
                                                                                                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paneMail.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Почта", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel10.setText("Сообщения");

        listMessages.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = {"<html><font color=\"red\">Дата: 01.08.2016</font> Тема: Начало сезона От кого: Совет директоров", "Дата: 01.08.2016 Тема: Требования спонсора От кого: Спонсоры"};

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        listMessages.addListSelectionListener(this::listMessagesValueChanged);
        jScrollPane1.setViewportView(listMessages);

        lDetails.setText("Подробнее");

        taDescriptions.setEditable(false);
        taDescriptions.setColumns(20);
        taDescriptions.setRows(5);
        jScrollPane2.setViewportView(taDescriptions);

        bDeleteMessage.setText("Удалить выбранное");

        bClearPost.setText("Очистить почту");

        javax.swing.GroupLayout paneMailLayout = new javax.swing.GroupLayout(paneMail);
        paneMail.setLayout(paneMailLayout);
        paneMailLayout.setHorizontalGroup(
                paneMailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addGroup(paneMailLayout.createSequentialGroup()
                                                      .addContainerGap()
                                                      .addGroup(paneMailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                              .addGroup(paneMailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                                                                                                      .addGroup(paneMailLayout.createSequentialGroup()
                                                                                                                              .addComponent(bDeleteMessage)
                                                                                                                              .addGap(18, 18, 18)
                                                                                                                              .addComponent(bClearPost))
                                                                                                      .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                                                                              .addComponent(lDetails)
                                                                              .addComponent(jLabel10))
                                                      .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        paneMailLayout.setVerticalGroup(
                paneMailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addGroup(paneMailLayout.createSequentialGroup()
                                                      .addComponent(jLabel10)
                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                      .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                      .addGap(9, 9, 9)
                                                      .addGroup(paneMailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                              .addGroup(paneMailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                      .addComponent(bDeleteMessage)
                                                                                                      .addComponent(bClearPost))
                                                                              .addComponent(lDetails))
                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                      .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                      .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Поддержка болельшиков");

        progressBarSupportLevel.setValue(50);

        jLabel2.setText("Сыгранность команды");

        progressBarTeamwork.setValue(50);

        lBudget.setText("Бюджет");

        lTrainerLevel.setText("Опыт тренера");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel3Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(progressBarSupportLevel, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                                                                           .addGroup(jPanel3Layout.createSequentialGroup()
                                                                                                  .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                         .addComponent(jLabel1)
                                                                                                                         .addComponent(jLabel2)
                                                                                                                         .addComponent(lBudget)
                                                                                                                         .addComponent(lTrainerLevel))
                                                                                                  .addGap(0, 0, Short.MAX_VALUE))
                                                                           .addComponent(progressBarTeamwork, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                    .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel3Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(jLabel1)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(progressBarSupportLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel2)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(progressBarTeamwork, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(lBudget)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                                    .addComponent(lTrainerLevel)
                                                    .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bCommandStructure.setText("Состав");
        bCommandStructure.addActionListener(this::bCommandStructureActionPerformed);

        bStaff.setText("Персонал");
        bStaff.addActionListener(this::bStaffActionPerformed);

        bSportSchool.setText("Спортивная школа");
        bSportSchool.addActionListener(this::bSportSchoolActionPerformed);

        bPlayersMarket.setText("Трансферный рынок");
        bPlayersMarket.addActionListener(this::bPlayersMarketActionPerformed);

        bTournamentTable.setText("Турнирная таблица");
        bTournamentTable.addActionListener(this::bTournamentTableActionPerformed);

        bTraining.addActionListener(this::bTrainingActionPerformed);
        bDeleteMessage.addActionListener(this::bDeleteMessageActionPerformed);
        bClearPost.addActionListener(this::bClearPostActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                           .addComponent(bCommandStructure, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                           .addComponent(bStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                           .addComponent(bSportSchool, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                           .addComponent(bPlayersMarket, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                           .addComponent(bTournamentTable, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                             .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(bCommandStructure)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(bStaff)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(bSportSchool)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(bPlayersMarket)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(bTournamentTable)
                                                    .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                      .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                      .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                      .addGap(18, 18, 18)
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addComponent(paneNextMatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                      .addComponent(paneMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                      .addGap(62, 62, 62))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                      .addGroup(layout.createSequentialGroup()
                                      .addContainerGap()
                                      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                      .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                      .addGroup(layout.createSequentialGroup()
                                                                      .addComponent(paneNextMatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                      .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                      .addComponent(paneMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                      .addContainerGap(93, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bClearPostActionPerformed(ActionEvent evt) {
        clearPost();
    }

    private void clearPost() {
        user.clearMessages();
        taDescriptions.setText("");
        listMessages.updateUI();
    }

    private void listMessagesValueChanged(ListSelectionEvent e) {
        showMessageBody();
    }

    private void showMessageBody() {
        int selectedIndex = listMessages.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < user.getMessages().size()) {
            Message message = user.getMessages().get(selectedIndex);
            String body = message.getBody();
            taDescriptions.setText(body);
        }
    }

    private void bTrainingActionPerformed(ActionEvent evt) {
        showTrainingForm();
    }

    private void bDeleteMessageActionPerformed(ActionEvent evt) {
        deleteSelectedMessage();
    }

    private void deleteSelectedMessage() {
        int selectedIndex = listMessages.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < user.getMessages().size()) {
            user.getMessages().remove(selectedIndex);
            taDescriptions.setText("");
            listMessages.updateUI();
        }
    }

    private void bTeamSettingsActionPerformed(ActionEvent evt) {
        showTeamSettingsForm();
    }

    private void bCommandStructureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCommandStructureActionPerformed
        showPlayersForm();
    }//GEN-LAST:event_bCommandStructureActionPerformed

    private void bPlayersMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPlayersMarketActionPerformed
        showTransferForm();
    }//GEN-LAST:event_bPlayersMarketActionPerformed

    private void bStartMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStartMatchActionPerformed
        nextEvent();
    }//GEN-LAST:event_bStartMatchActionPerformed

    private void bSportSchoolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSportSchoolActionPerformed
        showSportSchoolForm();
    }//GEN-LAST:event_bSportSchoolActionPerformed

    private void bStaffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStaffActionPerformed
        showPersonalForm();
    }//GEN-LAST:event_bStaffActionPerformed

    private void bTournamentTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTournamentTableActionPerformed
        showTournamentForm();
    }//GEN-LAST:event_bTournamentTableActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClearPost;
    private javax.swing.JButton bCommandStructure;
    private javax.swing.JButton bDeleteMessage;
    private javax.swing.JButton bPlayersMarket;
    private javax.swing.JButton bSportSchool;
    private javax.swing.JButton bStaff;
    private javax.swing.JButton bStartMatch;
    private javax.swing.JButton bTeamSettings;
    private javax.swing.JButton bTournamentTable;
    private javax.swing.JButton bTraining;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel lDetails;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lBudget;
    private javax.swing.JList<String> listMessages;
    private javax.swing.JLabel lNextMatchInfo;
    private javax.swing.JLabel lPlayedGames;
    private javax.swing.JLabel lSeason;
    private javax.swing.JLabel lTournamentPlace;
    private javax.swing.JLabel lTrainerLevel;
    private javax.swing.JLabel lYear;
    private javax.swing.JPanel paneMail;
    private javax.swing.JPanel paneNextMatch;
    private javax.swing.JProgressBar progressBarSupportLevel;
    private javax.swing.JProgressBar progressBarTeamwork;
    private javax.swing.JTextArea taDescriptions;
    // End of variables declaration//GEN-END:variables

    private void initLocation() {
        setLocationRelativeTo(null);
    }

}
