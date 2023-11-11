package nl.tudelft.simulation.housinggame.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewseffectsRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewsitemRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.RoundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioparametersRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.UserRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.WelfaretypeRecord;

public final class SqlUtils
{

    private SqlUtils()
    {
        // utility class
    }

    public static Connection dbConnection() throws SQLException, ClassNotFoundException
    {
        String jdbcURL = "jdbc:mysql://localhost:3306/housinggame";
        String dbUser = "housinggame";
        String dbPassword = "tudHouse#4";

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
    }

    public static RoundRecord readRoundFromRoundId(final AdminData data, final Integer roundId)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        return dslContext.selectFrom(Tables.ROUND).where(Tables.ROUND.ID.eq(UInteger.valueOf(roundId))).fetchAny();
    }

    public static UserRecord readUserFromUserId(final AdminData data, final Integer userId)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        return dslContext.selectFrom(Tables.USER).where(Tables.USER.ID.eq(UInteger.valueOf(userId))).fetchAny();
    }

    public static UserRecord readUserFromUsername(final AdminData data, final String username)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        return dslContext.selectFrom(Tables.USER).where(Tables.USER.USERNAME.eq(username)).fetchAny();
    }

    public static void loadAttributes(final HttpSession session)
    {
        AdminData data = SessionUtils.getData(session);
        data.setMenuChoice("");
    }

    public static <R extends org.jooq.UpdatableRecord<R>> R readRecordFromId(final AdminData data, final Table<R> table,
            final int recordId)
    {
        return readRecordFromId(data, table, UInteger.valueOf(recordId));
    }

    @SuppressWarnings("unchecked")
    public static <R extends org.jooq.UpdatableRecord<R>> R readRecordFromId(final AdminData data, final Table<R> table,
            final UInteger recordId)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        return dslContext.selectFrom(table).where(((TableField<R, UInteger>) table.field("id")).eq(recordId)).fetchOne();
    }

    /**
     * Clone the Scenario. Surround the code with try-catch to alert the user when something went wrong. The following steps
     * need to be taken:
     *
     * <pre>
     * 1. Clone scenario with new name. scenarioparametersId and gameversionId stay the same.
     * 2. For the scenario, clone the questions, using the new scenarioId.
     * 3. For the scenario, clone the welfaretypes, using the new scenarioId.
     * 4. For the scenario, clone the rounds using the new scenarioId.
     *    5. For each round, clone the newsitems using the new roundid.
     *       6. For each newsitem, clone the newseffects using the new newsitemId; link to the old communityId.
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param oldScenario ScenarioRecord
     * @param newScenarioName
     */
    public static void cloneScenario(final AdminData data, final ScenarioRecord oldScenario, final String newScenarioName)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);

        // 1. Clone scenario with new name. scenarioparametersId and gameversionId stay the same.
        ScenarioRecord newScenario = oldScenario.copy();
        newScenario.setName(newScenarioName);
        newScenario.store();
        UInteger newScenarioId = newScenario.getId();

        // 2. For the scenario, clone the questions, using the new scenarioId.
        List<QuestionRecord> questionList =
                dslContext.selectFrom(Tables.QUESTION).where(Tables.QUESTION.SCENARIO_ID.eq(oldScenario.getId())).fetch();
        for (QuestionRecord oldQuestion : questionList)
        {
            QuestionRecord newQuestion = oldQuestion.copy();
            newQuestion.setScenarioId(newScenarioId);
            newQuestion.store();
        }

        // 3. For the scenario, clone the welfaretypes, using the new scenarioId.
        List<WelfaretypeRecord> welfareTypeList =
                dslContext.selectFrom(Tables.WELFARETYPE).where(Tables.WELFARETYPE.SCENARIO_ID.eq(oldScenario.getId())).fetch();
        for (WelfaretypeRecord oldWelfareType : welfareTypeList)
        {
            WelfaretypeRecord newWelfareType = oldWelfareType.copy();
            newWelfareType.setScenarioId(newScenarioId);
            newWelfareType.store();
        }

        // 4. For the scenario, clone the rounds using the new scenarioId.
        List<RoundRecord> roundList =
                dslContext.selectFrom(Tables.ROUND).where(Tables.ROUND.SCENARIO_ID.eq(oldScenario.getId())).fetch();
        for (RoundRecord oldRound : roundList)
        {
            RoundRecord newRound = oldRound.copy();
            newRound.setScenarioId(newScenarioId);
            newRound.store();
            UInteger newRoundId = newRound.getId();

            // 5. For each round, clone the newsitems using the new roundid.
            List<NewsitemRecord> newsItemList =
                    dslContext.selectFrom(Tables.NEWSITEM).where(Tables.NEWSITEM.ROUND_ID.eq(oldRound.getId())).fetch();
            for (NewsitemRecord oldNewsitem : newsItemList)
            {
                NewsitemRecord newNewsItem = oldNewsitem.copy();
                newNewsItem.setRoundId(newRoundId);
                newNewsItem.store();
                UInteger newNewsItemId = newNewsItem.getId();

                // 6. For each newsitem, clone the newseffects using the new newsitemId; link to the old communityId.
                List<NewseffectsRecord> newsEffectsList = dslContext.selectFrom(Tables.NEWSEFFECTS)
                        .where(Tables.NEWSEFFECTS.NEWSITEM_ID.eq(oldNewsitem.getId())).fetch();
                for (NewseffectsRecord oldNewsEffects : newsEffectsList)
                {
                    NewseffectsRecord newNewsEffects = oldNewsEffects.copy();
                    newNewsEffects.setNewsitemId(newNewsItemId);
                    newNewsEffects.store();
                }
            }
        }
    }

    /**
     * Clone the Scenario. Surround the code with try-catch to alert the user when something went wrong.
     * @param data AdminData; record with all session relevant information
     * @param oldScenario ScenarioRecord
     */
    public static void cloneScenario(final AdminData data, final ScenarioRecord oldScenario)
    {
        cloneScenario(data, oldScenario, makeUniqueScenarioName(data, oldScenario));
    }

    /**
     * Clone the ScenarioParameters. Surround the code with try-catch to alert the user when something went wrong.
     * @param data AdminData; record with all session relevant information
     * @param oldScenarioParameters ScenarioparametersRecord
     * @param newScenarioParametersName
     */
    public static void cloneScenarioParameters(final AdminData data, final ScenarioparametersRecord oldScenarioParameters,
            final String newScenarioParametersName)
    {
        ScenarioparametersRecord newScenarioParameters = oldScenarioParameters.copy();
        newScenarioParameters.setName(newScenarioParametersName);
        newScenarioParameters.store();
    }

    /**
     * Clone the ScenarioParameters. Surround the code with try-catch to alert the user when something went wrong.
     * @param data AdminData; record with all session relevant information
     * @param oldScenarioParameters ScenarioparametersRecord
     */
    public static void cloneScenarioParameters(final AdminData data, final ScenarioparametersRecord oldScenarioParameters)
    {
        cloneScenarioParameters(data, oldScenarioParameters, makeUniqueScenarioParametersName(data, oldScenarioParameters));
    }

    /**
     * Clone the GameVersion. Surround the code with try-catch to alert the user when something went wrong. The following steps
     * need to be taken:
     *
     * <pre>
     * 1. Clone gameversion with new name. languagesId stays the same.
     * 2. For the gameversion, clone the labels, using the new gameversionId.
     * 3. For the gameversion, clone the measuretypes.
     *    - make a map of old measuretypeId to new measuretypeId.
     * 4. For the gameversion, clone the communities, using the new gameversionId.
     *    - make a map of old communityId to new communityId.
     *    5. For each community, clone the taxes, using the new communityId.
     *    6. For each community, clone the houses, using the new communityId.
     *       7. For each house, clone the initialhousemeasures, link to new communityId and new measuretypeId via the MAP.
     * 8. For the gameversion, clone each scenario with new name and new gameversionId. scenarioparametersId stays the same.
     *    9.  For each scenario, clone the questions, using the new scenarioId.
     *    10. For each scenario, clone the welfaretypes, using the new scenarioId.
     *    11. For each scenario, clone the rounds using the new scenarioId.
     *        12. For each round, clone the newsitems using the new roundid.
     *        13. For each newsitem, clone the newseffects using the new newsitemId; use the MAP to set new communityId.
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param oldGameVersion GameversionRecord
     * @param newGameVersionName
     */
    public static void cloneGameVersion(final AdminData data, final GameversionRecord oldGameVersion,
            final String newGameVersionName)
    {

    }

    /**
     * Clone the GameVersion. Surround the code with try-catch to alert the user when something went wrong.
     * @param data AdminData; record with all session relevant information
     * @param oldGameVersion GameversionRecord
     * @param newGameVersionName
     */
    public static void cloneGameVersion(final AdminData data, final GameversionRecord oldGameVersion)
    {
        cloneGameVersion(data, oldGameVersion, makeUniqueGameVersionName(data, oldGameVersion));
    }

    /**
     * Delete a Scenario that has NOT been played. Surround the code with try-catch to alert the user when something went wrong.
     * The following steps need to be taken:
     *
     * <pre>
     * 1. Check whether the scenario has an associated group; if yes, return false.
     * 2. For the scenario, for each round:
     *    3. For each round, for each newsitem:
     *       4. For each newsitem, for each newseffects:
     *          - delete newseffects
     *       - delete newsitem
     *    - delete round
     * 5. For the scenario, for each question:
     *    - delete the question
     * 6. For the scenario, for each welfaretype:
     *    - delete the welfaretype
     * - delete the scenario
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param scenario ScenarioRecord
     * @return whether the deletion was successful
     */
    public static void destroyScenario(final AdminData data, final ScenarioRecord scenario) throws HousingGameException
    {
        // 1. Check whether the scenario has an associated group; if yes, return false.
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<GroupRecord> groupList =
                dslContext.selectFrom(Tables.GROUP).where(Tables.GROUP.SCENARIO_ID.eq(scenario.getId())).fetch();
        if (groupList.size() > 0)
            throw new HousingGameException(
                    "Scenario had associated groups (gameplay) <br>and could therefore not be destroyed");

        // 2. For the scenario, for each round:
        List<RoundRecord> roundList =
                dslContext.selectFrom(Tables.ROUND).where(Tables.ROUND.SCENARIO_ID.eq(scenario.getId())).fetch();
        for (RoundRecord round : roundList)
        {
            // 3. For each round, for each newsitem:
            List<NewsitemRecord> newsItemList =
                    dslContext.selectFrom(Tables.NEWSITEM).where(Tables.NEWSITEM.ROUND_ID.eq(round.getId())).fetch();
            for (NewsitemRecord newsItem : newsItemList)
            {
                // 4. For each newsitem, for each newseffects:
                List<NewseffectsRecord> newsEffectsList = dslContext.selectFrom(Tables.NEWSEFFECTS)
                        .where(Tables.NEWSEFFECTS.NEWSITEM_ID.eq(newsItem.getId())).fetch();
                for (NewseffectsRecord newsEffects : newsEffectsList)
                {
                    newsEffects.delete();
                }
                newsItem.delete();
            }
            round.delete();
        }

        // For the scenario, for each question: delete the question
        List<QuestionRecord> questionList =
                dslContext.selectFrom(Tables.QUESTION).where(Tables.QUESTION.SCENARIO_ID.eq(scenario.getId())).fetch();
        for (QuestionRecord question : questionList)
        {
            question.delete();
        }

        // For the scenario, for each welfaretype:: delete the welfaretype
        List<WelfaretypeRecord> welfareTypeList =
                dslContext.selectFrom(Tables.WELFARETYPE).where(Tables.WELFARETYPE.SCENARIO_ID.eq(scenario.getId())).fetch();
        for (WelfaretypeRecord welfareType : welfareTypeList)
        {
            welfareType.delete();
        }

        // delete the scenario
        scenario.delete();
    }

    /**
     * Delete a GameVersion that has NOT been played, and without scenarios. Surround the code with try-catch to alert the user
     * when something went wrong. The following steps need to be taken:
     *
     * <pre>
     * 1. Check whether the gameVersion has an associated gamesession or scenario; if yes, return false.
     * 2. For the gameversion, for each label:
     *    - delete the label
     * 3. For the gameversion, for each community:
     *    4. For each community, for each tax:
     *       - delete the tax
     *    5. For each community, for each house:
     *       6. For each house, for each initialhousemeasure:
     *          - delete the initialhousemeasure
     *       - delete the house
     *    - delete the community
     * 7. For the gameversion, for each measuretype:
     *    - delete the measuretype
     * - delete the gameversion
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param gameVersion GameversionRecord
     * @return whether the deletion was successful
     */
    public static void destroyGameVersion(final AdminData data, final GameversionRecord gameVersion) throws HousingGameException
    {
        throw new HousingGameException(
                "Scenario had associated scenarios or game rounds <br>and could therefore not be destroyed");
    }

    private static String makeUniqueScenarioParametersName(final AdminData data,
            final ScenarioparametersRecord oldScenarioParameters)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<ScenarioparametersRecord> spList = dslContext.selectFrom(Tables.SCENARIOPARAMETERS).fetch();
        Set<String> nameSet = new HashSet<>();
        for (ScenarioparametersRecord record : spList)
            nameSet.add(record.getName());
        String oldName = oldScenarioParameters.getName().substring(0, Math.min(oldScenarioParameters.getName().length(), 250));
        int copyNr = 1;
        do
        {
            String newName = oldName + " (" + copyNr + ")";
            if (!nameSet.contains(newName))
                return newName;
            copyNr++;
        }
        while (copyNr < 99);
        return UUID.randomUUID().toString();
    }

    private static String makeUniqueScenarioName(final AdminData data, final ScenarioRecord oldScenario)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<ScenarioRecord> scenarioList = dslContext.selectFrom(Tables.SCENARIO).fetch();
        Set<String> nameSet = new HashSet<>();
        for (ScenarioRecord record : scenarioList)
            nameSet.add(record.getName());
        String oldName = oldScenario.getName().substring(0, Math.min(oldScenario.getName().length(), 40));
        int copyNr = 1;
        do
        {
            String newName = oldName + " (" + copyNr + ")";
            if (!nameSet.contains(newName))
                return newName;
            copyNr++;
        }
        while (copyNr < 99);
        return UUID.randomUUID().toString();
    }

    private static String makeUniqueGameVersionName(final AdminData data, final GameversionRecord oldGameVersion)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<GameversionRecord> scenarioList = dslContext.selectFrom(Tables.GAMEVERSION).fetch();
        Set<String> nameSet = new HashSet<>();
        for (GameversionRecord record : scenarioList)
            nameSet.add(record.getName());
        String oldName = oldGameVersion.getName().substring(0, Math.min(oldGameVersion.getName().length(), 250));
        int copyNr = 1;
        do
        {
            String newName = oldName + " (" + copyNr + ")";
            if (!nameSet.contains(newName))
                return newName;
            copyNr++;
        }
        while (copyNr < 99);
        return UUID.randomUUID().toString();
    }

}
