package rusfootballmanager.represent;

import rusfootballmanager.Tournament;
import rusfootballmanager.User;
import rusfootballmanager.entities.Team;

/**
 *
 * @author Алексей
 */
public class ManageForm extends javax.swing.JDialog {

    private User user;

    public ManageForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setUser(User user) {
        this.user = user;
        Team team = user.getTeam();
        lBudget.setText("Бюджет: " + String.valueOf(team.getBudget()));
        lTrainerLevel.setText("Опыт тренера: " + String.valueOf(user.getExperience()));
        pbSupportLevel.setValue(team.getSupport());
        pbTeamwork.setValue(team.getTeamwork());
        beforeEvent(user, team);
    }

    private void beforeEvent(User user, Team team) {
        Tournament tournament = user.getTournament();
        if (tournament.isGameDays()) {
            bStartMatch.setText("Начать матч");
            lNextMatchInfo.setText("Следующий матч с командой: "
                    + tournament.getTeamWithPlaysNext(team));
        } else {
            lNextMatchInfo.setText("Период трансферов");
            bStartMatch.setText("Далее");
        }
    }

    private void nextEvent() {
        if (user.getTournament().isGameDays()) {

        } else {
            user.getSettings().simulateTransfers();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paneNextMatch = new javax.swing.JPanel();
        bStartMatch = new javax.swing.JButton();
        bTraining = new javax.swing.JButton();
        bTeamManagement = new javax.swing.JButton();
        lNextMatchInfo = new javax.swing.JLabel();
        lPlayedGames = new javax.swing.JLabel();
        lSeason = new javax.swing.JLabel();
        lTournamentPlace = new javax.swing.JLabel();
        lYear = new javax.swing.JLabel();
        paneMail = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lMessages = new javax.swing.JList<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        taDescriptions = new javax.swing.JTextArea();
        bDeleteMessage = new javax.swing.JButton();
        bClearPost = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pbSupportLevel = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        pbTeamwork = new javax.swing.JProgressBar();
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
        bStartMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bStartMatchActionPerformed(evt);
            }
        });

        bTraining.setText("Тренировка");

        bTeamManagement.setText("Руководство командой");

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
                            .addComponent(bTeamManagement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(bTeamManagement)
                .addGap(18, 18, 18)
                .addGroup(paneNextMatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bStartMatch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bTraining))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paneMail.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Почта", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel10.setText("Сообщения");

        lMessages.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "<html><font color=\"red\">Дата: 01.08.2016</font> Тема: Начало сезона От кого: Совет директоров", "Дата: 01.08.2016 Тема: Требования спонсора От кого: Спонсоры" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lMessages);

        jLabel11.setText("Подробнее");

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
                    .addComponent(jLabel11)
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
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Поддержка болельшиков");

        pbSupportLevel.setValue(50);

        jLabel2.setText("Сыгранность команды");

        pbTeamwork.setValue(50);

        lBudget.setText("Бюджет");

        lTrainerLevel.setText("Опыт тренера");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pbSupportLevel, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(lBudget)
                            .addComponent(lTrainerLevel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pbTeamwork, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(pbSupportLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pbTeamwork, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lBudget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(lTrainerLevel)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bCommandStructure.setText("Состав");
        bCommandStructure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCommandStructureActionPerformed(evt);
            }
        });

        bStaff.setText("Персонал");

        bSportSchool.setText("Спортивная школа");

        bPlayersMarket.setText("Трансферный рынок");
        bPlayersMarket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPlayersMarketActionPerformed(evt);
            }
        });

        bTournamentTable.setText("Турнирная таблица");

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

    private void bCommandStructureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCommandStructureActionPerformed
        PlayersForm playersForm = new PlayersForm(null, true);
        playersForm.setPlayers(user.getTeam().getAllPlayers());
        playersForm.setVisible(true);
    }//GEN-LAST:event_bCommandStructureActionPerformed

    private void bPlayersMarketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPlayersMarketActionPerformed
        TransferForm transferForm = new TransferForm(null, true);
        transferForm.setTeam(user.getTeam());
        transferForm.setVisible(true);
    }//GEN-LAST:event_bPlayersMarketActionPerformed

    private void bStartMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bStartMatchActionPerformed
        nextEvent();
    }//GEN-LAST:event_bStartMatchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClearPost;
    private javax.swing.JButton bCommandStructure;
    private javax.swing.JButton bDeleteMessage;
    private javax.swing.JButton bPlayersMarket;
    private javax.swing.JButton bSportSchool;
    private javax.swing.JButton bStaff;
    private javax.swing.JButton bStartMatch;
    private javax.swing.JButton bTeamManagement;
    private javax.swing.JButton bTournamentTable;
    private javax.swing.JButton bTraining;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lBudget;
    private javax.swing.JList<String> lMessages;
    private javax.swing.JLabel lNextMatchInfo;
    private javax.swing.JLabel lPlayedGames;
    private javax.swing.JLabel lSeason;
    private javax.swing.JLabel lTournamentPlace;
    private javax.swing.JLabel lTrainerLevel;
    private javax.swing.JLabel lYear;
    private javax.swing.JPanel paneMail;
    private javax.swing.JPanel paneNextMatch;
    private javax.swing.JProgressBar pbSupportLevel;
    private javax.swing.JProgressBar pbTeamwork;
    private javax.swing.JTextArea taDescriptions;
    // End of variables declaration//GEN-END:variables

}