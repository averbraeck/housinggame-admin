package nl.tudelft.simulation.housinggame.admin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.common.SqlUtils;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.CommunityRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GamesessionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupstateRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousegroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousemeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousetransactionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.InitialhousemeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasurecategoryRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MovingreasonRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewseffectsRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewsitemRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PersonalmeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerstateRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionitemRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionscoreRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioparametersRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.TaxRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.WelfaretypeRecord;

public final class AdminUtils extends SqlUtils
{

    public static void loadAttributes(final HttpSession session)
    {
        AdminData data = SessionUtils.getData(session);
        data.setMenuChoice("");
    }

    /**
     * Clone the Scenario. The following steps need to be taken:
     *
     * <pre>
     * 1. Clone scenario with new name. scenarioparametersId and gameversionId stay the same.
     * 2. For the scenario, clone the questions, using the new scenarioId.
     *    3. For each question, clone the questionitems, using the new questionId.
     * 4. For the scenario, clone the welfaretypes, using the new scenarioId.
     * 5. For the scenario, clone the newsitems using the new scenarioId.
     *    6. For each newsitem, clone the newseffects using the new newsitemId; link to the old communityId.
     * 7. For the scenario, clone the measurecategories using the new scenarioId.
     *    8. For each measurecategory, clone the measuretypes using the new measurecategoryId.
     *       9. For each measuretype, clone the initialhousemeasures using the new measuretypeId (and the same houseId).
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param oldScenario ScenarioRecord
     * @param newScenarioName
     */
    public static void cloneScenario(final AdminData data, final ScenarioRecord oldScenario, final String newScenarioName)
    {
        // 1. Clone scenario with new name. scenarioparametersId and gameversionId stay the same.
        ScenarioRecord newScenario = oldScenario.copy();
        newScenario.setName(newScenarioName);
        newScenario.store();
        int newScenarioId = newScenario.getId();

        cloneScenarioTables(data, oldScenario.getId(), newScenarioId);
    }

    private static void cloneScenarioTables(final AdminData data, final int oldScenarioId, final int newScenarioId)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);

        // 2. For the scenario, clone the questions, using the new scenarioId.
        List<QuestionRecord> questionList =
                dslContext.selectFrom(Tables.QUESTION).where(Tables.QUESTION.SCENARIO_ID.eq(oldScenarioId)).fetch();
        for (QuestionRecord oldQuestion : questionList)
        {
            QuestionRecord newQuestion = oldQuestion.copy();
            newQuestion.setScenarioId(newScenarioId);
            newQuestion.store();
            int newQuestionId = newQuestion.getId();

            // 3. For each question, clone the questionitems, using the new questionId.
            List<QuestionitemRecord> questionItemList = dslContext.selectFrom(Tables.QUESTIONITEM)
                    .where(Tables.QUESTIONITEM.QUESTION_ID.eq(oldQuestion.getId())).fetch();
            for (QuestionitemRecord oldQuestionItem : questionItemList)
            {
                QuestionitemRecord newQuestionItem = oldQuestionItem.copy();
                newQuestionItem.setQuestionId(newQuestionId);
                newQuestionItem.store();
            }
        }

        // 4. For the scenario, clone the welfaretypes, using the new scenarioId.
        List<WelfaretypeRecord> welfareTypeList =
                dslContext.selectFrom(Tables.WELFARETYPE).where(Tables.WELFARETYPE.SCENARIO_ID.eq(oldScenarioId)).fetch();
        for (WelfaretypeRecord oldWelfareType : welfareTypeList)
        {
            WelfaretypeRecord newWelfareType = oldWelfareType.copy();
            newWelfareType.setScenarioId(newScenarioId);
            newWelfareType.store();
        }

        // 5. For the scenario, clone the newsitems using the new scenarioId.
        List<NewsitemRecord> newsItemList =
                dslContext.selectFrom(Tables.NEWSITEM).where(Tables.NEWSITEM.SCENARIO_ID.eq(oldScenarioId)).fetch();
        for (NewsitemRecord oldNewsitem : newsItemList)
        {
            NewsitemRecord newNewsItem = oldNewsitem.copy();
            newNewsItem.setScenarioId(newScenarioId);
            newNewsItem.store();
            int newNewsItemId = newNewsItem.getId();

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

        // 7. For the scenario, clone the measurecategories using the new scenarioId.
        List<MeasurecategoryRecord> measureCategoryList = dslContext.selectFrom(Tables.MEASURECATEGORY)
                .where(Tables.MEASURECATEGORY.SCENARIO_ID.eq(oldScenarioId)).fetch();
        for (MeasurecategoryRecord oldMeasureCategory : measureCategoryList)
        {
            MeasurecategoryRecord newMeasureCategory = oldMeasureCategory.copy();
            newMeasureCategory.setScenarioId(newScenarioId);
            newMeasureCategory.store();
            int newMeasureCategoryId = newMeasureCategory.getId();

            // 8. For each measurecategory, clone the measuretypes using the new measurecategoryId.
            List<MeasuretypeRecord> measureTypeList = dslContext.selectFrom(Tables.MEASURETYPE)
                    .where(Tables.MEASURETYPE.MEASURECATEGORY_ID.eq(oldMeasureCategory.getId())).fetch();
            for (MeasuretypeRecord oldMeasureType : measureTypeList)
            {
                MeasuretypeRecord newMeasureType = oldMeasureType.copy();
                newMeasureType.setMeasurecategoryId(newMeasureCategoryId);
                newMeasureType.store();
                int newMeasureTypeId = newMeasureType.getId();

                // 9. For each measuretype, clone the initialhousemeasures using the new measuretypeId (and the same houseId).
                List<InitialhousemeasureRecord> initialHouseMeasureList = dslContext.selectFrom(Tables.INITIALHOUSEMEASURE)
                        .where(Tables.INITIALHOUSEMEASURE.MEASURETYPE_ID.eq(oldMeasureType.getId())).fetch();
                for (InitialhousemeasureRecord oldInitialHouseMeasure : initialHouseMeasureList)
                {
                    InitialhousemeasureRecord newInitialHouseMeasure = oldInitialHouseMeasure.copy();
                    newInitialHouseMeasure.setMeasuretypeId(newMeasureTypeId);
                    newInitialHouseMeasure.store();
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
     * Clone the GameVersion. The following steps need to be taken:
     *
     * <pre>
     * 1. Clone gameversion with new name. languagesId stays the same.
     * 2. For the gameversion, clone the communities, using the new gameversionId.
     *    - make a map of old communityId to new communityId for updating the newseffects.
     *    3. For each community, clone the taxes, using the new communityId.
     *    4. For each community, clone the houses, using the new communityId.
     *       - make a map of old houseId to new houseId for updating the initialhousemeasures.
     * 5. For the gameversion, clone the movingreasons.
     * 6. For the gameversion, clone the scenarios.
     *    7. for each cloned newseffects record with a communityId, update the communityId.
     *    8. for each cloned initialhousemeasure record, update the houseId.
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param oldGameVersion GameversionRecord
     * @param newGameVersionName
     */
    public static void cloneGameVersion(final AdminData data, final GameversionRecord oldGameVersion,
            final String newGameVersionName)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);

        // 1. Clone gameversion with new name. languagesId stays the same.
        GameversionRecord newGameVersion = oldGameVersion.copy();
        newGameVersion.setName(newGameVersionName);
        newGameVersion.store();
        int newGameVersionId = newGameVersion.getId();

        // 2. For the gameversion, clone the communities, using the new gameversionId;
        // make a map of old communityId to new communityId.
        List<CommunityRecord> communityList = dslContext.selectFrom(Tables.COMMUNITY)
                .where(Tables.COMMUNITY.GAMEVERSION_ID.eq(oldGameVersion.getId())).fetch();
        Map<Integer, Integer> communityMap = new HashMap<>();
        Map<Integer, Integer> houseMap = new HashMap<>();
        for (CommunityRecord oldCommunity : communityList)
        {
            CommunityRecord newCommunity = oldCommunity.copy();
            newCommunity.setGameversionId(newGameVersionId);
            newCommunity.store();
            int newCommunityId = newCommunity.getId();
            communityMap.put(oldCommunity.getId(), newCommunity.getId());

            // 3. For each community, clone the taxes, using the new communityId.
            List<TaxRecord> taxList =
                    dslContext.selectFrom(Tables.TAX).where(Tables.TAX.COMMUNITY_ID.eq(oldCommunity.getId())).fetch();
            for (TaxRecord oldTax : taxList)
            {
                TaxRecord newTax = oldTax.copy();
                newTax.setCommunityId(newCommunityId);
                newTax.store();
            }

            // 4. For each community, clone the houses, using the new communityId.
            List<HouseRecord> houseList =
                    dslContext.selectFrom(Tables.HOUSE).where(Tables.HOUSE.COMMUNITY_ID.eq(oldCommunity.getId())).fetch();
            for (HouseRecord oldHouse : houseList)
            {
                HouseRecord newHouse = oldHouse.copy();
                newHouse.setCommunityId(newCommunityId);
                newHouse.store();
                houseMap.put(oldHouse.getId(), newHouse.getId());
            }
        }

        // 5. For the gameversion, clone the movingreasons.
        List<MovingreasonRecord> movingReasonList = dslContext.selectFrom(Tables.MOVINGREASON)
                .where(Tables.MOVINGREASON.GAMEVERSION_ID.eq(oldGameVersion.getId())).fetch();
        for (MovingreasonRecord oldMovingReason : movingReasonList)
        {
            MovingreasonRecord newMovingReason = oldMovingReason.copy();
            newMovingReason.setGameversionId(newGameVersionId);
            newMovingReason.store();
        }

        // 6. For the gameversion, clone the scenarios.
        // scenarioparametersId stays the same.
        List<ScenarioRecord> scenarioList =
                dslContext.selectFrom(Tables.SCENARIO).where(Tables.SCENARIO.GAMEVERSION_ID.eq(oldGameVersion.getId())).fetch();
        for (ScenarioRecord oldScenario : scenarioList)
        {
            ScenarioRecord newScenario = oldScenario.copy();
            newScenario.setGameversionId(newGameVersionId);
            newScenario.store();
            int newScenarioId = newScenario.getId();
            cloneScenarioTables(data, oldScenario.getId(), newScenarioId);

            // 7. for each cloned newseffects record with a communityId, update the communityId.
            List<NewsitemRecord> newsItemList =
                    dslContext.selectFrom(Tables.NEWSITEM).where(Tables.NEWSITEM.SCENARIO_ID.eq(newScenario.getId())).fetch();
            for (NewsitemRecord newsitem : newsItemList)
            {
                // use the MAP to set new communityId.
                List<NewseffectsRecord> newsEffectsList = dslContext.selectFrom(Tables.NEWSEFFECTS)
                        .where(Tables.NEWSEFFECTS.NEWSITEM_ID.eq(newsitem.getId())).fetch();
                for (NewseffectsRecord newsEffects : newsEffectsList)
                {
                    newsEffects.setCommunityId(communityMap.get(newsEffects.getCommunityId()));
                    newsEffects.store();
                }
            }

            // 8. for each cloned initialhousemeasure record, update the houseId.
            List<MeasurecategoryRecord> measureCategoryList = dslContext.selectFrom(Tables.MEASURECATEGORY)
                    .where(Tables.MEASURECATEGORY.SCENARIO_ID.eq(newScenario.getId())).fetch();
            for (MeasurecategoryRecord measureCategory : measureCategoryList)
            {
                List<MeasuretypeRecord> measureTypeList = dslContext.selectFrom(Tables.MEASURETYPE)
                        .where(Tables.MEASURETYPE.MEASURECATEGORY_ID.eq(measureCategory.getId())).fetch();
                for (MeasuretypeRecord measureType : measureTypeList)
                {
                    List<InitialhousemeasureRecord> ihmList = dslContext.selectFrom(Tables.INITIALHOUSEMEASURE)
                            .where(Tables.INITIALHOUSEMEASURE.MEASURETYPE_ID.eq(measureType.getId())).fetch();
                    for (InitialhousemeasureRecord ihm : ihmList)
                    {
                        ihm.setHouseId(houseMap.get(ihm.getHouseId()));
                        ihm.store();
                    }
                }
            }
        }
    }

    /**
     * Clone the GameVersion.
     * @param data AdminData; record with all session relevant information
     * @param oldGameVersion GameversionRecord
     * @param newGameVersionName
     */
    public static void cloneGameVersion(final AdminData data, final GameversionRecord oldGameVersion)
    {
        cloneGameVersion(data, oldGameVersion, makeUniqueGameVersionName(data, oldGameVersion));
    }

    /**
     * Delete a Scenario that has NOT been played. The following steps need to be taken:
     *
     * <pre>
     * 1. Check whether the scenario has an associated group; if yes, throw exception.
     * 2. For the scenario, for each newsitem
     *    3. For the newsitem, for each newseffects:
     *       - delete the newseffects
     *    - delete the newsitem
     * 4. For the scenario, for each question:
     *    5. For the question, for each questionitem:
     *       - delete the questionitem
     *    - delete the question
     * 6. For the scenario, for each welfaretype:
     *    - delete the welfaretype
     * 7. For the scenario, for each measurecategory:
     *    8. For the measurecategory, for each measuretype:
     *       9. For the measuretype, for each initialhousescenario:
     *          - delete the initialhousescenario
     *       - delete the measuretype
     *    - delete the measurecategory
     * - delete the scenario
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param scenario ScenarioRecord
     * @return whether the deletion was successful
     */
    public static void destroyScenario(final AdminData data, final ScenarioRecord scenario) throws HousingGameException
    {
        // 1. Check whether the scenario has an associated group; if yes, throw exception.
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<GroupRecord> groupList =
                dslContext.selectFrom(Tables.GROUP).where(Tables.GROUP.SCENARIO_ID.eq(scenario.getId())).fetch();
        if (groupList.size() > 0)
            throw new HousingGameException(
                    "Scenario had associated groups (gameplay) <br>and could therefore not be destroyed");

        // 2. For the scenario, for each newsitem:
        List<NewsitemRecord> newsItemList =
                dslContext.selectFrom(Tables.NEWSITEM).where(Tables.NEWSITEM.SCENARIO_ID.eq(scenario.getId())).fetch();
        for (NewsitemRecord newsItem : newsItemList)
        {
            // 3. For the newsitem, for each newseffects:
            List<NewseffectsRecord> newsEffectsList = dslContext.selectFrom(Tables.NEWSEFFECTS)
                    .where(Tables.NEWSEFFECTS.NEWSITEM_ID.eq(newsItem.getId())).fetch();
            for (NewseffectsRecord newsEffects : newsEffectsList)
            {
                newsEffects.delete();
            }
            newsItem.delete();
        }

        // 4. For the scenario, for each question:
        List<QuestionRecord> questionList =
                dslContext.selectFrom(Tables.QUESTION).where(Tables.QUESTION.SCENARIO_ID.eq(scenario.getId())).fetch();
        for (QuestionRecord question : questionList)
        {
            // 5. For the question, for each questionitem:
            List<QuestionitemRecord> questionItemList = dslContext.selectFrom(Tables.QUESTIONITEM)
                    .where(Tables.QUESTIONITEM.QUESTION_ID.eq(question.getId())).fetch();
            for (QuestionitemRecord questionItem : questionItemList)
            {
                questionItem.delete();
            }
            question.delete();
        }

        // 6. For the scenario, for each welfaretype:: delete the welfaretype
        List<WelfaretypeRecord> welfareTypeList =
                dslContext.selectFrom(Tables.WELFARETYPE).where(Tables.WELFARETYPE.SCENARIO_ID.eq(scenario.getId())).fetch();
        for (WelfaretypeRecord welfareType : welfareTypeList)
        {
            welfareType.delete();
        }

        // 7. For the scenario, for each measurecategory:
        List<MeasurecategoryRecord> measureCategoryList = dslContext.selectFrom(Tables.MEASURECATEGORY)
                .where(Tables.MEASURECATEGORY.SCENARIO_ID.eq(scenario.getId())).fetch();
        for (MeasurecategoryRecord measureCategory : measureCategoryList)
        {
            // 8. For the measurecategory, for each measuretype:
            List<MeasuretypeRecord> measureTypeList = dslContext.selectFrom(Tables.MEASURETYPE)
                    .where(Tables.MEASURETYPE.MEASURECATEGORY_ID.eq(measureCategory.getId())).fetch();
            for (MeasuretypeRecord measureType : measureTypeList)
            {
                // 9. For the measuretype, for each initialhousescenario:
                List<InitialhousemeasureRecord> ihmList = dslContext.selectFrom(Tables.INITIALHOUSEMEASURE)
                        .where(Tables.INITIALHOUSEMEASURE.MEASURETYPE_ID.eq(measureType.getId())).fetch();
                for (InitialhousemeasureRecord ihm : ihmList)
                {
                    ihm.delete();
                }
                measureType.delete();
            }
            measureCategory.delete();
        }

        // delete the scenario
        scenario.delete();
    }

    /**
     * Delete a GameVersion that has NOT been played, and WITHOUT scenarios. The following steps need to be taken:
     *
     * <pre>
     * 1. Check whether the gameVersion has an associated gamesession or scenario; if yes, throw exception.
     * 2. For the gameversion, for each community:
     *    3. For the community, for each tax:
     *       - delete the tax
     *    4. For each community, for each house:
     *       - delete the house
     *    - delete the community
     * 5. For the gameversion, for each movingreason:
     *    - delete the movingreason
     * - delete the gameversion
     * </pre>
     *
     * @param data AdminData; record with all session relevant information
     * @param gameVersion GameversionRecord
     * @return whether the deletion was successful
     */
    public static void destroyGameVersion(final AdminData data, final GameversionRecord gameVersion) throws HousingGameException
    {
        // 1. Check whether the gameVersion has an associated gamesession or scenario; if yes, throw exception.
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<ScenarioRecord> scenarioList =
                dslContext.selectFrom(Tables.SCENARIO).where(Tables.SCENARIO.GAMEVERSION_ID.eq(gameVersion.getId())).fetch();
        List<GamesessionRecord> gameSessionList = dslContext.selectFrom(Tables.GAMESESSION)
                .where(Tables.GAMESESSION.GAMEVERSION_ID.eq(gameVersion.getId())).fetch();
        if (scenarioList.size() > 0 || gameSessionList.size() > 0)
            throw new HousingGameException(
                    "GameVersion had associated scenarios or game sessions<br>and could therefore not be destroyed");

        // 2. For the gameversion, for each community:
        List<CommunityRecord> communityList =
                dslContext.selectFrom(Tables.COMMUNITY).where(Tables.COMMUNITY.GAMEVERSION_ID.eq(gameVersion.getId())).fetch();
        for (CommunityRecord community : communityList)
        {
            // 3. For each community, for each tax: delete the tax
            List<TaxRecord> taxList =
                    dslContext.selectFrom(Tables.TAX).where(Tables.TAX.COMMUNITY_ID.eq(community.getId())).fetch();
            for (TaxRecord tax : taxList)
            {
                tax.delete();
            }

            // 4. For each community, for each house:
            List<HouseRecord> houseList =
                    dslContext.selectFrom(Tables.HOUSE).where(Tables.HOUSE.COMMUNITY_ID.eq(community.getId())).fetch();
            for (HouseRecord house : houseList)
            {
                house.delete();
            }
            community.delete();
        }

        // 5. For the gameversion, for each movingreason: delete the movingreason
        List<MovingreasonRecord> movingReasonList = dslContext.selectFrom(Tables.MOVINGREASON)
                .where(Tables.MOVINGREASON.GAMEVERSION_ID.eq(gameVersion.getId())).fetch();
        for (MovingreasonRecord movingReason : movingReasonList)
        {
            movingReason.delete();
        }

        gameVersion.delete();
    }

    public static void destroyGamePlay(final AdminData data, final int groupId)
    {
        var dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        var group = AdminUtils.readRecordFromId(data, Tables.GROUP, groupId);

        // The gamesession, group and players stay. We delete groupround, playerround, measure, bid, questionscore,
        // as well as groupstate and playerstate

        // remove house ownership of player
        List<PlayerRecord> playerList =
                dslContext.selectFrom(Tables.PLAYER).where(Tables.PLAYER.GROUP_ID.eq(group.getId())).fetch();
        for (var player : playerList)
        {
            List<HousegroupRecord> hgList =
                    dslContext.selectFrom(Tables.HOUSEGROUP).where(Tables.HOUSEGROUP.OWNER_ID.eq(player.getId())).fetch();
            for (var houseGroup : hgList)
            {
                houseGroup.setOwnerId(null);
                houseGroup.store();
            }
        }

        List<GrouproundRecord> groupRoundList =
                dslContext.selectFrom(Tables.GROUPROUND).where(Tables.GROUPROUND.GROUP_ID.eq(group.getId())).fetch();
        for (var groupRound : groupRoundList)
        {
            List<PlayerroundRecord> playerRoundList = dslContext.selectFrom(Tables.PLAYERROUND)
                    .where(Tables.PLAYERROUND.GROUPROUND_ID.eq(groupRound.getId())).fetch();
            for (var playerRound : playerRoundList)
            {
                playerRound.setStartHousegroupId(null); // avoid circular reference
                playerRound.setFinalHousegroupId(null); // avoid circular reference
                playerRound.setActiveTransactionId(null); // avoid circular reference
                playerRound.store();
            }

            List<HousetransactionRecord> transactionList = dslContext.selectFrom(Tables.HOUSETRANSACTION)
                    .where(Tables.HOUSETRANSACTION.GROUPROUND_ID.eq(groupRound.getId())).fetch();
            for (var transaction : transactionList)
                transaction.delete();

            List<GroupstateRecord> groupStateList = dslContext.selectFrom(Tables.GROUPSTATE)
                    .where(Tables.GROUPSTATE.GROUPROUND_ID.eq(groupRound.getId())).fetch();
            for (var groupState : groupStateList)
                groupState.delete();

            for (var playerRound : playerRoundList)
            {
                List<QuestionscoreRecord> questionScoreList = dslContext.selectFrom(Tables.QUESTIONSCORE)
                        .where(Tables.QUESTIONSCORE.PLAYERROUND_ID.eq(playerRound.getId())).fetch();
                for (var questionScore : questionScoreList)
                    questionScore.delete();
                List<PlayerstateRecord> playerStateList = dslContext.selectFrom(Tables.PLAYERSTATE)
                        .where(Tables.PLAYERSTATE.PLAYERROUND_ID.eq(playerRound.getId())).fetch();
                for (var playerState : playerStateList)
                    playerState.delete();
                List<PersonalmeasureRecord> personalMeasureList = dslContext.selectFrom(Tables.PERSONALMEASURE)
                        .where(Tables.PERSONALMEASURE.PLAYERROUND_ID.eq(playerRound.getId())).fetch();
                for (var personalMeasure : personalMeasureList)
                    personalMeasure.delete();
                playerRound.delete();
            }
        }

        for (var groupRound : groupRoundList)
        {
            List<HousegroupRecord> houseGroupList =
                    dslContext.selectFrom(Tables.HOUSEGROUP).where(Tables.HOUSEGROUP.GROUP_ID.eq(group.getId())).fetch();
            for (var houseGroup : houseGroupList)
            {
                List<HousemeasureRecord> measureList = dslContext.selectFrom(Tables.HOUSEMEASURE)
                        .where(Tables.HOUSEMEASURE.HOUSEGROUP_ID.eq(houseGroup.getId())).fetch();
                for (var measure : measureList)
                    measure.delete();
                houseGroup.delete();
            }
            groupRound.delete();
        }
    }

    public static void destroyGroup(final AdminData data, final int groupId)
    {
        var dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        var group = AdminUtils.readRecordFromId(data, Tables.GROUP, groupId);
        destroyGamePlay(data, groupId);
        List<PlayerRecord> playerList =
                dslContext.selectFrom(Tables.PLAYER).where(Tables.PLAYER.GROUP_ID.eq(group.getId())).fetch();
        for (var player : playerList)
            player.delete();
        group.delete();
    }

    public static void destroyGameSession(final AdminData data, final int gameSessionId)
    {
        var dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        var gameSession = AdminUtils.readRecordFromId(data, Tables.GAMESESSION, gameSessionId);
        List<GroupRecord> groupList =
                dslContext.selectFrom(Tables.GROUP).where(Tables.GROUP.GAMESESSION_ID.eq(gameSession.getId())).fetch();
        for (var group : groupList)
            destroyGroup(data, group.getId());
        gameSession.delete();
    }

    public static void destroyPlayerPlay(final AdminData data, final int playerId)
    {
        var dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        var player = AdminUtils.readRecordFromId(data, Tables.PLAYER, playerId);

        // remove house ownership of player
        List<HousegroupRecord> hgList =
                dslContext.selectFrom(Tables.HOUSEGROUP).where(Tables.HOUSEGROUP.OWNER_ID.eq(player.getId())).fetch();
        for (var houseGroup : hgList)
        {
            houseGroup.setOwnerId(null);
            houseGroup.store();
        }

        // delete play rounds and associated data
        List<PlayerroundRecord> playerRoundList =
                dslContext.selectFrom(Tables.PLAYERROUND).where(Tables.PLAYERROUND.PLAYER_ID.eq(player.getId())).fetch();
        for (var playerRound : playerRoundList)
        {
            playerRound.setStartHousegroupId(null); // avoid circular reference
            playerRound.setFinalHousegroupId(null); // avoid circular reference
            playerRound.setActiveTransactionId(null); // avoid circular reference
            playerRound.store();

            List<HousetransactionRecord> transactionList = dslContext.selectFrom(Tables.HOUSETRANSACTION)
                    .where(Tables.HOUSETRANSACTION.PLAYERROUND_ID.eq(playerRound.getId())).fetch();
            for (var transaction : transactionList)
                transaction.delete();
            List<QuestionscoreRecord> questionScoreList = dslContext.selectFrom(Tables.QUESTIONSCORE)
                    .where(Tables.QUESTIONSCORE.PLAYERROUND_ID.eq(playerRound.getId())).fetch();
            for (var questionScore : questionScoreList)
                questionScore.delete();
            List<PlayerstateRecord> playerStateList = dslContext.selectFrom(Tables.PLAYERSTATE)
                    .where(Tables.PLAYERSTATE.PLAYERROUND_ID.eq(playerRound.getId())).fetch();
            for (var playerState : playerStateList)
                playerState.delete();
            List<PersonalmeasureRecord> personalMeasureList = dslContext.selectFrom(Tables.PERSONALMEASURE)
                    .where(Tables.PERSONALMEASURE.PLAYERROUND_ID.eq(playerRound.getId())).fetch();
            for (var personalMeasure : personalMeasureList)
                personalMeasure.delete();
            playerRound.delete();
        }
    }

    public static void destroyPlayerPlusPlay(final AdminData data, final int playerId)
    {
        var player = AdminUtils.readRecordFromId(data, Tables.PLAYER, playerId);
        destroyPlayerPlay(data, playerId);
        player.delete();
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
