/*
 * This file is generated by jOOQ.
 */
package nl.tudelft.simulation.housinggame.data;


import nl.tudelft.simulation.housinggame.data.tables.Bid;
import nl.tudelft.simulation.housinggame.data.tables.Community;
import nl.tudelft.simulation.housinggame.data.tables.Facilitator;
import nl.tudelft.simulation.housinggame.data.tables.Gamesession;
import nl.tudelft.simulation.housinggame.data.tables.Gameversion;
import nl.tudelft.simulation.housinggame.data.tables.Group;
import nl.tudelft.simulation.housinggame.data.tables.Groupround;
import nl.tudelft.simulation.housinggame.data.tables.House;
import nl.tudelft.simulation.housinggame.data.tables.Initialhousemeasure;
import nl.tudelft.simulation.housinggame.data.tables.Label;
import nl.tudelft.simulation.housinggame.data.tables.Language;
import nl.tudelft.simulation.housinggame.data.tables.Languages;
import nl.tudelft.simulation.housinggame.data.tables.Measure;
import nl.tudelft.simulation.housinggame.data.tables.Measuretype;
import nl.tudelft.simulation.housinggame.data.tables.Newseffects;
import nl.tudelft.simulation.housinggame.data.tables.Newsitem;
import nl.tudelft.simulation.housinggame.data.tables.Player;
import nl.tudelft.simulation.housinggame.data.tables.Playerround;
import nl.tudelft.simulation.housinggame.data.tables.Question;
import nl.tudelft.simulation.housinggame.data.tables.Questionscore;
import nl.tudelft.simulation.housinggame.data.tables.Round;
import nl.tudelft.simulation.housinggame.data.tables.Scenario;
import nl.tudelft.simulation.housinggame.data.tables.Scenarioparameters;
import nl.tudelft.simulation.housinggame.data.tables.Tax;
import nl.tudelft.simulation.housinggame.data.tables.User;
import nl.tudelft.simulation.housinggame.data.tables.Welfaretype;
import nl.tudelft.simulation.housinggame.data.tables.records.BidRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.CommunityRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.FacilitatorRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GamesessionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.InitialhousemeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LabelRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LanguageRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LanguagesRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewseffectsRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewsitemRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionscoreRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.RoundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioparametersRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.TaxRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.UserRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.WelfaretypeRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * housinggame.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<BidRecord> KEY_BID_ID_UNIQUE = Internal.createUniqueKey(Bid.BID, DSL.name("KEY_bid_id_UNIQUE"), new TableField[] { Bid.BID.ID }, true);
    public static final UniqueKey<BidRecord> KEY_BID_PRIMARY = Internal.createUniqueKey(Bid.BID, DSL.name("KEY_bid_PRIMARY"), new TableField[] { Bid.BID.ID }, true);
    public static final UniqueKey<CommunityRecord> KEY_COMMUNITY_ID_UNIQUE = Internal.createUniqueKey(Community.COMMUNITY, DSL.name("KEY_community_id_UNIQUE"), new TableField[] { Community.COMMUNITY.ID }, true);
    public static final UniqueKey<CommunityRecord> KEY_COMMUNITY_PRIMARY = Internal.createUniqueKey(Community.COMMUNITY, DSL.name("KEY_community_PRIMARY"), new TableField[] { Community.COMMUNITY.ID }, true);
    public static final UniqueKey<FacilitatorRecord> KEY_FACILITATOR_ID_UNIQUE = Internal.createUniqueKey(Facilitator.FACILITATOR, DSL.name("KEY_facilitator_Id_UNIQUE"), new TableField[] { Facilitator.FACILITATOR.ID }, true);
    public static final UniqueKey<FacilitatorRecord> KEY_FACILITATOR_PRIMARY = Internal.createUniqueKey(Facilitator.FACILITATOR, DSL.name("KEY_facilitator_PRIMARY"), new TableField[] { Facilitator.FACILITATOR.ID }, true);
    public static final UniqueKey<GamesessionRecord> KEY_GAMESESSION_ID_UNIQUE = Internal.createUniqueKey(Gamesession.GAMESESSION, DSL.name("KEY_gamesession_Id_UNIQUE"), new TableField[] { Gamesession.GAMESESSION.ID }, true);
    public static final UniqueKey<GamesessionRecord> KEY_GAMESESSION_NAME_UNIQUE = Internal.createUniqueKey(Gamesession.GAMESESSION, DSL.name("KEY_gamesession_name_UNIQUE"), new TableField[] { Gamesession.GAMESESSION.NAME }, true);
    public static final UniqueKey<GamesessionRecord> KEY_GAMESESSION_PRIMARY = Internal.createUniqueKey(Gamesession.GAMESESSION, DSL.name("KEY_gamesession_PRIMARY"), new TableField[] { Gamesession.GAMESESSION.ID }, true);
    public static final UniqueKey<GameversionRecord> KEY_GAMEVERSION_ID_UNIQUE = Internal.createUniqueKey(Gameversion.GAMEVERSION, DSL.name("KEY_gameversion_id_UNIQUE"), new TableField[] { Gameversion.GAMEVERSION.ID }, true);
    public static final UniqueKey<GameversionRecord> KEY_GAMEVERSION_NAME_UNIQUE = Internal.createUniqueKey(Gameversion.GAMEVERSION, DSL.name("KEY_gameversion_name_UNIQUE"), new TableField[] { Gameversion.GAMEVERSION.NAME }, true);
    public static final UniqueKey<GameversionRecord> KEY_GAMEVERSION_PRIMARY = Internal.createUniqueKey(Gameversion.GAMEVERSION, DSL.name("KEY_gameversion_PRIMARY"), new TableField[] { Gameversion.GAMEVERSION.ID }, true);
    public static final UniqueKey<GroupRecord> KEY_GROUP_ID_UNIQUE = Internal.createUniqueKey(Group.GROUP, DSL.name("KEY_group_Id_UNIQUE"), new TableField[] { Group.GROUP.ID }, true);
    public static final UniqueKey<GroupRecord> KEY_GROUP_PRIMARY = Internal.createUniqueKey(Group.GROUP, DSL.name("KEY_group_PRIMARY"), new TableField[] { Group.GROUP.ID }, true);
    public static final UniqueKey<GrouproundRecord> KEY_GROUPROUND_ID_GROUP_ROUND = Internal.createUniqueKey(Groupround.GROUPROUND, DSL.name("KEY_groupround_id_group_round"), new TableField[] { Groupround.GROUPROUND.GROUP_ID, Groupround.GROUPROUND.ROUND_ID }, true);
    public static final UniqueKey<GrouproundRecord> KEY_GROUPROUND_ID_UNIQUE = Internal.createUniqueKey(Groupround.GROUPROUND, DSL.name("KEY_groupround_id_UNIQUE"), new TableField[] { Groupround.GROUPROUND.ID }, true);
    public static final UniqueKey<GrouproundRecord> KEY_GROUPROUND_PRIMARY = Internal.createUniqueKey(Groupround.GROUPROUND, DSL.name("KEY_groupround_PRIMARY"), new TableField[] { Groupround.GROUPROUND.ID }, true);
    public static final UniqueKey<HouseRecord> KEY_HOUSE_ID_UNIQUE = Internal.createUniqueKey(House.HOUSE, DSL.name("KEY_house_id_UNIQUE"), new TableField[] { House.HOUSE.ID }, true);
    public static final UniqueKey<HouseRecord> KEY_HOUSE_PRIMARY = Internal.createUniqueKey(House.HOUSE, DSL.name("KEY_house_PRIMARY"), new TableField[] { House.HOUSE.ID }, true);
    public static final UniqueKey<InitialhousemeasureRecord> KEY_INITIALHOUSEMEASURE_ID_UNIQUE = Internal.createUniqueKey(Initialhousemeasure.INITIALHOUSEMEASURE, DSL.name("KEY_initialhousemeasure_id_UNIQUE"), new TableField[] { Initialhousemeasure.INITIALHOUSEMEASURE.ID }, true);
    public static final UniqueKey<InitialhousemeasureRecord> KEY_INITIALHOUSEMEASURE_PRIMARY = Internal.createUniqueKey(Initialhousemeasure.INITIALHOUSEMEASURE, DSL.name("KEY_initialhousemeasure_PRIMARY"), new TableField[] { Initialhousemeasure.INITIALHOUSEMEASURE.ID }, true);
    public static final UniqueKey<LabelRecord> KEY_LABEL_ID_UNIQUE = Internal.createUniqueKey(Label.LABEL, DSL.name("KEY_label_id_UNIQUE"), new TableField[] { Label.LABEL.ID }, true);
    public static final UniqueKey<LabelRecord> KEY_LABEL_PRIMARY = Internal.createUniqueKey(Label.LABEL, DSL.name("KEY_label_PRIMARY"), new TableField[] { Label.LABEL.ID }, true);
    public static final UniqueKey<LanguageRecord> KEY_LANGUAGE_CODE_UNIQUE = Internal.createUniqueKey(Language.LANGUAGE, DSL.name("KEY_language_code_UNIQUE"), new TableField[] { Language.LANGUAGE.CODE }, true);
    public static final UniqueKey<LanguageRecord> KEY_LANGUAGE_ID_UNIQUE = Internal.createUniqueKey(Language.LANGUAGE, DSL.name("KEY_language_id_UNIQUE"), new TableField[] { Language.LANGUAGE.ID }, true);
    public static final UniqueKey<LanguageRecord> KEY_LANGUAGE_NAME_UNIQUE = Internal.createUniqueKey(Language.LANGUAGE, DSL.name("KEY_language_name_UNIQUE"), new TableField[] { Language.LANGUAGE.NAME }, true);
    public static final UniqueKey<LanguageRecord> KEY_LANGUAGE_PRIMARY = Internal.createUniqueKey(Language.LANGUAGE, DSL.name("KEY_language_PRIMARY"), new TableField[] { Language.LANGUAGE.ID }, true);
    public static final UniqueKey<LanguagesRecord> KEY_LANGUAGES_ID_UNIQUE = Internal.createUniqueKey(Languages.LANGUAGES, DSL.name("KEY_languages_id_UNIQUE"), new TableField[] { Languages.LANGUAGES.ID }, true);
    public static final UniqueKey<LanguagesRecord> KEY_LANGUAGES_NAME_UNIQUE = Internal.createUniqueKey(Languages.LANGUAGES, DSL.name("KEY_languages_name_UNIQUE"), new TableField[] { Languages.LANGUAGES.NAME }, true);
    public static final UniqueKey<LanguagesRecord> KEY_LANGUAGES_PRIMARY = Internal.createUniqueKey(Languages.LANGUAGES, DSL.name("KEY_languages_PRIMARY"), new TableField[] { Languages.LANGUAGES.ID }, true);
    public static final UniqueKey<MeasureRecord> KEY_MEASURE_ID_UNIQUE = Internal.createUniqueKey(Measure.MEASURE, DSL.name("KEY_measure_id_UNIQUE"), new TableField[] { Measure.MEASURE.ID }, true);
    public static final UniqueKey<MeasureRecord> KEY_MEASURE_PRIMARY = Internal.createUniqueKey(Measure.MEASURE, DSL.name("KEY_measure_PRIMARY"), new TableField[] { Measure.MEASURE.ID }, true);
    public static final UniqueKey<MeasuretypeRecord> KEY_MEASURETYPE_ID_UNIQUE = Internal.createUniqueKey(Measuretype.MEASURETYPE, DSL.name("KEY_measuretype_id_UNIQUE"), new TableField[] { Measuretype.MEASURETYPE.ID }, true);
    public static final UniqueKey<MeasuretypeRecord> KEY_MEASURETYPE_PRIMARY = Internal.createUniqueKey(Measuretype.MEASURETYPE, DSL.name("KEY_measuretype_PRIMARY"), new TableField[] { Measuretype.MEASURETYPE.ID }, true);
    public static final UniqueKey<NewseffectsRecord> KEY_NEWSEFFECTS_ID_UNIQUE = Internal.createUniqueKey(Newseffects.NEWSEFFECTS, DSL.name("KEY_newseffects_id_UNIQUE"), new TableField[] { Newseffects.NEWSEFFECTS.ID }, true);
    public static final UniqueKey<NewseffectsRecord> KEY_NEWSEFFECTS_PRIMARY = Internal.createUniqueKey(Newseffects.NEWSEFFECTS, DSL.name("KEY_newseffects_PRIMARY"), new TableField[] { Newseffects.NEWSEFFECTS.ID }, true);
    public static final UniqueKey<NewsitemRecord> KEY_NEWSITEM_ID_UNIQUE = Internal.createUniqueKey(Newsitem.NEWSITEM, DSL.name("KEY_newsitem_id_UNIQUE"), new TableField[] { Newsitem.NEWSITEM.ID }, true);
    public static final UniqueKey<NewsitemRecord> KEY_NEWSITEM_PRIMARY = Internal.createUniqueKey(Newsitem.NEWSITEM, DSL.name("KEY_newsitem_PRIMARY"), new TableField[] { Newsitem.NEWSITEM.ID }, true);
    public static final UniqueKey<PlayerRecord> KEY_PLAYER_ID_UNIQUE = Internal.createUniqueKey(Player.PLAYER, DSL.name("KEY_player_id_UNIQUE"), new TableField[] { Player.PLAYER.ID }, true);
    public static final UniqueKey<PlayerRecord> KEY_PLAYER_PRIMARY = Internal.createUniqueKey(Player.PLAYER, DSL.name("KEY_player_PRIMARY"), new TableField[] { Player.PLAYER.ID }, true);
    public static final UniqueKey<PlayerroundRecord> KEY_PLAYERROUND_ID_PLAYER_GROUPROUND = Internal.createUniqueKey(Playerround.PLAYERROUND, DSL.name("KEY_playerround_id_player_groupround"), new TableField[] { Playerround.PLAYERROUND.PLAYER_ID, Playerround.PLAYERROUND.GROUPROUND_ID }, true);
    public static final UniqueKey<PlayerroundRecord> KEY_PLAYERROUND_ID_UNIQUE = Internal.createUniqueKey(Playerround.PLAYERROUND, DSL.name("KEY_playerround_id_UNIQUE"), new TableField[] { Playerround.PLAYERROUND.ID }, true);
    public static final UniqueKey<PlayerroundRecord> KEY_PLAYERROUND_PRIMARY = Internal.createUniqueKey(Playerround.PLAYERROUND, DSL.name("KEY_playerround_PRIMARY"), new TableField[] { Playerround.PLAYERROUND.ID }, true);
    public static final UniqueKey<QuestionRecord> KEY_QUESTION_ID_UNIQUE = Internal.createUniqueKey(Question.QUESTION, DSL.name("KEY_question_id_UNIQUE"), new TableField[] { Question.QUESTION.ID }, true);
    public static final UniqueKey<QuestionRecord> KEY_QUESTION_PRIMARY = Internal.createUniqueKey(Question.QUESTION, DSL.name("KEY_question_PRIMARY"), new TableField[] { Question.QUESTION.ID }, true);
    public static final UniqueKey<QuestionscoreRecord> KEY_QUESTIONSCORE_ID_PLAYERROUND_QUESTION = Internal.createUniqueKey(Questionscore.QUESTIONSCORE, DSL.name("KEY_questionscore_id_playerround_question"), new TableField[] { Questionscore.QUESTIONSCORE.PLAYERROUND_ID, Questionscore.QUESTIONSCORE.QUESTION_ID }, true);
    public static final UniqueKey<QuestionscoreRecord> KEY_QUESTIONSCORE_ID_UNIQUE = Internal.createUniqueKey(Questionscore.QUESTIONSCORE, DSL.name("KEY_questionscore_id_UNIQUE"), new TableField[] { Questionscore.QUESTIONSCORE.ID }, true);
    public static final UniqueKey<QuestionscoreRecord> KEY_QUESTIONSCORE_PRIMARY = Internal.createUniqueKey(Questionscore.QUESTIONSCORE, DSL.name("KEY_questionscore_PRIMARY"), new TableField[] { Questionscore.QUESTIONSCORE.ID }, true);
    public static final UniqueKey<RoundRecord> KEY_ROUND_ID_UNIQUE = Internal.createUniqueKey(Round.ROUND, DSL.name("KEY_round_id_UNIQUE"), new TableField[] { Round.ROUND.ID }, true);
    public static final UniqueKey<RoundRecord> KEY_ROUND_PRIMARY = Internal.createUniqueKey(Round.ROUND, DSL.name("KEY_round_PRIMARY"), new TableField[] { Round.ROUND.ID }, true);
    public static final UniqueKey<ScenarioRecord> KEY_SCENARIO_ID_UNIQUE = Internal.createUniqueKey(Scenario.SCENARIO, DSL.name("KEY_scenario_id_UNIQUE"), new TableField[] { Scenario.SCENARIO.ID }, true);
    public static final UniqueKey<ScenarioRecord> KEY_SCENARIO_PRIMARY = Internal.createUniqueKey(Scenario.SCENARIO, DSL.name("KEY_scenario_PRIMARY"), new TableField[] { Scenario.SCENARIO.ID }, true);
    public static final UniqueKey<ScenarioparametersRecord> KEY_SCENARIOPARAMETERS_ID_UNIQUE = Internal.createUniqueKey(Scenarioparameters.SCENARIOPARAMETERS, DSL.name("KEY_scenarioparameters_Id_UNIQUE"), new TableField[] { Scenarioparameters.SCENARIOPARAMETERS.ID }, true);
    public static final UniqueKey<ScenarioparametersRecord> KEY_SCENARIOPARAMETERS_NAME_UNIQUE = Internal.createUniqueKey(Scenarioparameters.SCENARIOPARAMETERS, DSL.name("KEY_scenarioparameters_name_UNIQUE"), new TableField[] { Scenarioparameters.SCENARIOPARAMETERS.NAME }, true);
    public static final UniqueKey<ScenarioparametersRecord> KEY_SCENARIOPARAMETERS_PRIMARY = Internal.createUniqueKey(Scenarioparameters.SCENARIOPARAMETERS, DSL.name("KEY_scenarioparameters_PRIMARY"), new TableField[] { Scenarioparameters.SCENARIOPARAMETERS.ID }, true);
    public static final UniqueKey<TaxRecord> KEY_TAX_ID_UNIQUE = Internal.createUniqueKey(Tax.TAX, DSL.name("KEY_tax_id_UNIQUE"), new TableField[] { Tax.TAX.ID }, true);
    public static final UniqueKey<TaxRecord> KEY_TAX_PRIMARY = Internal.createUniqueKey(Tax.TAX, DSL.name("KEY_tax_PRIMARY"), new TableField[] { Tax.TAX.ID }, true);
    public static final UniqueKey<UserRecord> KEY_USER_ID_UNIQUE = Internal.createUniqueKey(User.USER, DSL.name("KEY_user_id_UNIQUE"), new TableField[] { User.USER.ID }, true);
    public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = Internal.createUniqueKey(User.USER, DSL.name("KEY_user_PRIMARY"), new TableField[] { User.USER.ID }, true);
    public static final UniqueKey<UserRecord> KEY_USER_USERNAME_UNIQUE = Internal.createUniqueKey(User.USER, DSL.name("KEY_user_username_UNIQUE"), new TableField[] { User.USER.USERNAME }, true);
    public static final UniqueKey<WelfaretypeRecord> KEY_WELFARETYPE_ID_UNIQUE = Internal.createUniqueKey(Welfaretype.WELFARETYPE, DSL.name("KEY_welfaretype_id_UNIQUE"), new TableField[] { Welfaretype.WELFARETYPE.ID }, true);
    public static final UniqueKey<WelfaretypeRecord> KEY_WELFARETYPE_PRIMARY = Internal.createUniqueKey(Welfaretype.WELFARETYPE, DSL.name("KEY_welfaretype_PRIMARY"), new TableField[] { Welfaretype.WELFARETYPE.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<BidRecord, GrouproundRecord> FK_BID_GROUPROUND1 = Internal.createForeignKey(Bid.BID, DSL.name("fk_bid_groupround1"), new TableField[] { Bid.BID.GROUPROUND_ID }, Keys.KEY_GROUPROUND_PRIMARY, new TableField[] { Groupround.GROUPROUND.ID }, true);
    public static final ForeignKey<BidRecord, HouseRecord> FK_BID_HOUSE1 = Internal.createForeignKey(Bid.BID, DSL.name("fk_bid_house1"), new TableField[] { Bid.BID.HOUSE_ID }, Keys.KEY_HOUSE_PRIMARY, new TableField[] { House.HOUSE.ID }, true);
    public static final ForeignKey<CommunityRecord, GameversionRecord> FK_COMMUNITY_GAMEVERSION1 = Internal.createForeignKey(Community.COMMUNITY, DSL.name("fk_community_gameversion1"), new TableField[] { Community.COMMUNITY.GAMEVERSION_ID }, Keys.KEY_GAMEVERSION_PRIMARY, new TableField[] { Gameversion.GAMEVERSION.ID }, true);
    public static final ForeignKey<FacilitatorRecord, UserRecord> FK_FACILITATOR_USER1 = Internal.createForeignKey(Facilitator.FACILITATOR, DSL.name("fk_facilitator_user1"), new TableField[] { Facilitator.FACILITATOR.USER_ID }, Keys.KEY_USER_PRIMARY, new TableField[] { User.USER.ID }, true);
    public static final ForeignKey<GamesessionRecord, GameversionRecord> FK_GAMESESSION_GAMEVERSION1 = Internal.createForeignKey(Gamesession.GAMESESSION, DSL.name("fk_gamesession_gameversion1"), new TableField[] { Gamesession.GAMESESSION.GAMEVERSION_ID }, Keys.KEY_GAMEVERSION_PRIMARY, new TableField[] { Gameversion.GAMEVERSION.ID }, true);
    public static final ForeignKey<GameversionRecord, LanguagesRecord> FK_GAMEVERSION_LANGUAGES1 = Internal.createForeignKey(Gameversion.GAMEVERSION, DSL.name("fk_gameversion_languages1"), new TableField[] { Gameversion.GAMEVERSION.LANGUAGES_ID }, Keys.KEY_LANGUAGES_PRIMARY, new TableField[] { Languages.LANGUAGES.ID }, true);
    public static final ForeignKey<GroupRecord, FacilitatorRecord> FK_GROUP_FACILITATOR1 = Internal.createForeignKey(Group.GROUP, DSL.name("fk_group_facilitator1"), new TableField[] { Group.GROUP.FACILITATOR_ID }, Keys.KEY_FACILITATOR_PRIMARY, new TableField[] { Facilitator.FACILITATOR.ID }, true);
    public static final ForeignKey<GroupRecord, GamesessionRecord> FK_GROUP_GAMESESSION1 = Internal.createForeignKey(Group.GROUP, DSL.name("fk_group_gamesession1"), new TableField[] { Group.GROUP.GAMESESSION_ID }, Keys.KEY_GAMESESSION_PRIMARY, new TableField[] { Gamesession.GAMESESSION.ID }, true);
    public static final ForeignKey<GroupRecord, ScenarioRecord> FK_GROUP_SCENARIO1 = Internal.createForeignKey(Group.GROUP, DSL.name("fk_group_scenario1"), new TableField[] { Group.GROUP.SCENARIO_ID }, Keys.KEY_SCENARIO_PRIMARY, new TableField[] { Scenario.SCENARIO.ID }, true);
    public static final ForeignKey<GrouproundRecord, GroupRecord> FK_GROUPROUND_GROUP1 = Internal.createForeignKey(Groupround.GROUPROUND, DSL.name("fk_groupround_group1"), new TableField[] { Groupround.GROUPROUND.GROUP_ID }, Keys.KEY_GROUP_PRIMARY, new TableField[] { Group.GROUP.ID }, true);
    public static final ForeignKey<GrouproundRecord, RoundRecord> FK_GROUPROUND_ROUND1 = Internal.createForeignKey(Groupround.GROUPROUND, DSL.name("fk_groupround_round1"), new TableField[] { Groupround.GROUPROUND.ROUND_ID }, Keys.KEY_ROUND_PRIMARY, new TableField[] { Round.ROUND.ID }, true);
    public static final ForeignKey<HouseRecord, CommunityRecord> FK_HOUSE_COMMUNITY1 = Internal.createForeignKey(House.HOUSE, DSL.name("fk_house_community1"), new TableField[] { House.HOUSE.COMMUNITY_ID }, Keys.KEY_COMMUNITY_PRIMARY, new TableField[] { Community.COMMUNITY.ID }, true);
    public static final ForeignKey<InitialhousemeasureRecord, HouseRecord> FK_INITIALHOUSEMEASURE_HOUSE1 = Internal.createForeignKey(Initialhousemeasure.INITIALHOUSEMEASURE, DSL.name("fk_initialhousemeasure_house1"), new TableField[] { Initialhousemeasure.INITIALHOUSEMEASURE.HOUSE_ID }, Keys.KEY_HOUSE_PRIMARY, new TableField[] { House.HOUSE.ID }, true);
    public static final ForeignKey<InitialhousemeasureRecord, MeasuretypeRecord> FK_INITIALHOUSEMEASURE_MEASURETYPE1 = Internal.createForeignKey(Initialhousemeasure.INITIALHOUSEMEASURE, DSL.name("fk_initialhousemeasure_measuretype1"), new TableField[] { Initialhousemeasure.INITIALHOUSEMEASURE.MEASURETYPE_ID }, Keys.KEY_MEASURETYPE_PRIMARY, new TableField[] { Measuretype.MEASURETYPE.ID }, true);
    public static final ForeignKey<LabelRecord, GameversionRecord> FK_LABEL_GAMEVERSION1 = Internal.createForeignKey(Label.LABEL, DSL.name("fk_label_gameversion1"), new TableField[] { Label.LABEL.GAMEVERSION_ID }, Keys.KEY_GAMEVERSION_PRIMARY, new TableField[] { Gameversion.GAMEVERSION.ID }, true);
    public static final ForeignKey<LanguagesRecord, LanguageRecord> FK_LANGUAGES_LANGUAGE1 = Internal.createForeignKey(Languages.LANGUAGES, DSL.name("fk_languages_language1"), new TableField[] { Languages.LANGUAGES.LANGUAGE_ID1 }, Keys.KEY_LANGUAGE_PRIMARY, new TableField[] { Language.LANGUAGE.ID }, true);
    public static final ForeignKey<LanguagesRecord, LanguageRecord> FK_LANGUAGES_LANGUAGE2 = Internal.createForeignKey(Languages.LANGUAGES, DSL.name("fk_languages_language2"), new TableField[] { Languages.LANGUAGES.LANGUAGE_ID2 }, Keys.KEY_LANGUAGE_PRIMARY, new TableField[] { Language.LANGUAGE.ID }, true);
    public static final ForeignKey<LanguagesRecord, LanguageRecord> FK_LANGUAGES_LANGUAGE3 = Internal.createForeignKey(Languages.LANGUAGES, DSL.name("fk_languages_language3"), new TableField[] { Languages.LANGUAGES.LANGUAGE_ID3 }, Keys.KEY_LANGUAGE_PRIMARY, new TableField[] { Language.LANGUAGE.ID }, true);
    public static final ForeignKey<LanguagesRecord, LanguageRecord> FK_LANGUAGES_LANGUAGE4 = Internal.createForeignKey(Languages.LANGUAGES, DSL.name("fk_languages_language4"), new TableField[] { Languages.LANGUAGES.LANGUAGE_ID4 }, Keys.KEY_LANGUAGE_PRIMARY, new TableField[] { Language.LANGUAGE.ID }, true);
    public static final ForeignKey<MeasureRecord, MeasuretypeRecord> FK_MEASURE_MEASURETYPE1 = Internal.createForeignKey(Measure.MEASURE, DSL.name("fk_measure_measuretype1"), new TableField[] { Measure.MEASURE.MEASURETYPE_ID }, Keys.KEY_MEASURETYPE_PRIMARY, new TableField[] { Measuretype.MEASURETYPE.ID }, true);
    public static final ForeignKey<MeasureRecord, PlayerroundRecord> FK_MEASURE_PLAYERROUND1 = Internal.createForeignKey(Measure.MEASURE, DSL.name("fk_measure_playerround1"), new TableField[] { Measure.MEASURE.PLAYERROUND_ID }, Keys.KEY_PLAYERROUND_PRIMARY, new TableField[] { Playerround.PLAYERROUND.ID }, true);
    public static final ForeignKey<MeasuretypeRecord, GameversionRecord> FK_MEASURETYPE_GAMEVERSION1 = Internal.createForeignKey(Measuretype.MEASURETYPE, DSL.name("fk_measuretype_gameversion1"), new TableField[] { Measuretype.MEASURETYPE.GAMEVERSION_ID }, Keys.KEY_GAMEVERSION_PRIMARY, new TableField[] { Gameversion.GAMEVERSION.ID }, true);
    public static final ForeignKey<NewseffectsRecord, CommunityRecord> FK_NEWSEFFECTS_COMMUNITY1 = Internal.createForeignKey(Newseffects.NEWSEFFECTS, DSL.name("fk_newseffects_community1"), new TableField[] { Newseffects.NEWSEFFECTS.COMMUNITY_ID }, Keys.KEY_COMMUNITY_PRIMARY, new TableField[] { Community.COMMUNITY.ID }, true);
    public static final ForeignKey<NewseffectsRecord, NewsitemRecord> FK_NEWSEFFECTS_NEWSITEM1 = Internal.createForeignKey(Newseffects.NEWSEFFECTS, DSL.name("fk_newseffects_newsitem1"), new TableField[] { Newseffects.NEWSEFFECTS.NEWSITEM_ID }, Keys.KEY_NEWSITEM_PRIMARY, new TableField[] { Newsitem.NEWSITEM.ID }, true);
    public static final ForeignKey<NewsitemRecord, RoundRecord> FK_NEWSITEM_ROUND1 = Internal.createForeignKey(Newsitem.NEWSITEM, DSL.name("fk_newsitem_round1"), new TableField[] { Newsitem.NEWSITEM.ROUND_ID }, Keys.KEY_ROUND_PRIMARY, new TableField[] { Round.ROUND.ID }, true);
    public static final ForeignKey<PlayerRecord, GroupRecord> FK_PLAYER_GROUP1 = Internal.createForeignKey(Player.PLAYER, DSL.name("fk_player_group1"), new TableField[] { Player.PLAYER.GROUP_ID }, Keys.KEY_GROUP_PRIMARY, new TableField[] { Group.GROUP.ID }, true);
    public static final ForeignKey<PlayerRecord, UserRecord> FK_PLAYER_USER1 = Internal.createForeignKey(Player.PLAYER, DSL.name("fk_player_user1"), new TableField[] { Player.PLAYER.USER_ID }, Keys.KEY_USER_PRIMARY, new TableField[] { User.USER.ID }, true);
    public static final ForeignKey<PlayerRecord, WelfaretypeRecord> FK_PLAYER_WELFARETYPE1 = Internal.createForeignKey(Player.PLAYER, DSL.name("fk_player_welfaretype1"), new TableField[] { Player.PLAYER.WELFARETYPE_ID }, Keys.KEY_WELFARETYPE_PRIMARY, new TableField[] { Welfaretype.WELFARETYPE.ID }, true);
    public static final ForeignKey<PlayerroundRecord, GrouproundRecord> FK_PLAYERROUND_GROUPROUND1 = Internal.createForeignKey(Playerround.PLAYERROUND, DSL.name("fk_playerround_groupround1"), new TableField[] { Playerround.PLAYERROUND.GROUPROUND_ID }, Keys.KEY_GROUPROUND_PRIMARY, new TableField[] { Groupround.GROUPROUND.ID }, true);
    public static final ForeignKey<PlayerroundRecord, HouseRecord> FK_PLAYERROUND_HOUSE1 = Internal.createForeignKey(Playerround.PLAYERROUND, DSL.name("fk_playerround_house1"), new TableField[] { Playerround.PLAYERROUND.HOUSE_ID }, Keys.KEY_HOUSE_PRIMARY, new TableField[] { House.HOUSE.ID }, true);
    public static final ForeignKey<PlayerroundRecord, PlayerRecord> FK_PLAYERROUND_PLAYER1 = Internal.createForeignKey(Playerround.PLAYERROUND, DSL.name("fk_playerround_player1"), new TableField[] { Playerround.PLAYERROUND.PLAYER_ID }, Keys.KEY_PLAYER_PRIMARY, new TableField[] { Player.PLAYER.ID }, true);
    public static final ForeignKey<QuestionRecord, ScenarioRecord> FK_QUESTION_SCENARIO1 = Internal.createForeignKey(Question.QUESTION, DSL.name("fk_question_scenario1"), new TableField[] { Question.QUESTION.SCENARIO_ID }, Keys.KEY_SCENARIO_PRIMARY, new TableField[] { Scenario.SCENARIO.ID }, true);
    public static final ForeignKey<QuestionscoreRecord, PlayerroundRecord> FK_QUESTIONSCORE_PLAYERROUND1 = Internal.createForeignKey(Questionscore.QUESTIONSCORE, DSL.name("fk_questionscore_playerround1"), new TableField[] { Questionscore.QUESTIONSCORE.PLAYERROUND_ID }, Keys.KEY_PLAYERROUND_PRIMARY, new TableField[] { Playerround.PLAYERROUND.ID }, true);
    public static final ForeignKey<QuestionscoreRecord, QuestionRecord> FK_QUESTIONSCORE_QUESTION1 = Internal.createForeignKey(Questionscore.QUESTIONSCORE, DSL.name("fk_questionscore_question1"), new TableField[] { Questionscore.QUESTIONSCORE.QUESTION_ID }, Keys.KEY_QUESTION_PRIMARY, new TableField[] { Question.QUESTION.ID }, true);
    public static final ForeignKey<RoundRecord, ScenarioRecord> FK_ROUND_SCENARIO1 = Internal.createForeignKey(Round.ROUND, DSL.name("fk_round_scenario1"), new TableField[] { Round.ROUND.SCENARIO_ID }, Keys.KEY_SCENARIO_PRIMARY, new TableField[] { Scenario.SCENARIO.ID }, true);
    public static final ForeignKey<ScenarioRecord, GameversionRecord> FK_SCENARIO_GAMEVERSION1 = Internal.createForeignKey(Scenario.SCENARIO, DSL.name("fk_scenario_gameversion1"), new TableField[] { Scenario.SCENARIO.GAMEVERSION_ID }, Keys.KEY_GAMEVERSION_PRIMARY, new TableField[] { Gameversion.GAMEVERSION.ID }, true);
    public static final ForeignKey<ScenarioRecord, ScenarioparametersRecord> FK_SCENARIO_SCENARIOPARAMETERS1 = Internal.createForeignKey(Scenario.SCENARIO, DSL.name("fk_scenario_scenarioparameters1"), new TableField[] { Scenario.SCENARIO.SCENARIOPARAMETERS_ID }, Keys.KEY_SCENARIOPARAMETERS_PRIMARY, new TableField[] { Scenarioparameters.SCENARIOPARAMETERS.ID }, true);
    public static final ForeignKey<ScenarioparametersRecord, LanguageRecord> FK_SCENARIOPARAMETERS_LANGUAGE1 = Internal.createForeignKey(Scenarioparameters.SCENARIOPARAMETERS, DSL.name("fk_scenarioparameters_language1"), new TableField[] { Scenarioparameters.SCENARIOPARAMETERS.DEFAULT_LANGUAGE_ID }, Keys.KEY_LANGUAGE_PRIMARY, new TableField[] { Language.LANGUAGE.ID }, true);
    public static final ForeignKey<TaxRecord, CommunityRecord> FK_TAX_COMMUNITY1 = Internal.createForeignKey(Tax.TAX, DSL.name("fk_tax_community1"), new TableField[] { Tax.TAX.COMMUNITY_ID }, Keys.KEY_COMMUNITY_PRIMARY, new TableField[] { Community.COMMUNITY.ID }, true);
    public static final ForeignKey<WelfaretypeRecord, ScenarioRecord> FK_WELFARETYPE_SCENARIO1 = Internal.createForeignKey(Welfaretype.WELFARETYPE, DSL.name("fk_welfaretype_scenario1"), new TableField[] { Welfaretype.WELFARETYPE.SCENARIO_ID }, Keys.KEY_SCENARIO_PRIMARY, new TableField[] { Scenario.SCENARIO.ID }, true);
}
