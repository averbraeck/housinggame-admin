package nl.tudelft.simulation.housinggame.admin;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectConditionStep;
import org.jooq.SelectSeekStep1;
import org.jooq.Table;
import org.jooq.impl.DSL;

import de.siegmar.fastcsv.writer.CsvWriter;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.CommunityRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GamesessionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousegroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasurecategoryRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewsitemRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;

/**
 * ExportUtils.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CsvExport
{

    public static <R extends Record, T extends Table<R>> void dumpTableCsvHeader(final AdminData data,
            final ZipCsvDumpSession session, final T table) throws IOException
    {
        session.openTable(table);
        session.writeHeaderIfNeeded(table);
    }

    public static <R extends Record, T extends Table<R>, S extends Comparable<? super S>> List<R> dumpTableCsvLines(
            final AdminData data, final ZipCsvDumpSession session, final T table, final Condition condition,
            final Field<S> sortField) throws IOException
    {
        DSLContext dsl = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        session.openTable(table);
        session.writeHeaderIfNeeded(table);

        SelectConditionStep<R> step = dsl.selectFrom(table).where(condition);
        SelectSeekStep1<R, S> ordered = (sortField != null) ? step.orderBy(sortField) : null;

        // Stream with a cursor to keep memory low on big tables
        List<R> written = new ArrayList<>();
        if (ordered != null)
        {
            try (Cursor<R> cur = ordered.fetchLazy())
            {
                for (R rec : cur)
                {
                    session.writeRow(table, rec);
                    written.add(rec);
                }
            }
        }
        else
        {
            try (Cursor<R> cur = step.fetchLazy())
            {
                for (R rec : cur)
                {
                    session.writeRow(table, rec);
                    written.add(rec);
                }
            }
        }

        // Flush after batch append (entry remains open)
        session.flushTable(table);
        return written;
    }

    /** Close the CSV entry for this table (call when done appending rows). */
    public static <R extends Record, T extends Table<R>> void closeTableCsv(final T table, final ZipCsvDumpSession session)
            throws IOException
    {
        session.closeTable(table);
    }

    public static void exportGameSession(final AdminData data, final File tempFile, final int gameSessionRecordId)
            throws Exception
    {
        try (OutputStream fos = Files.newOutputStream(tempFile.toPath());
                ZipCsvDumpSession session = new ZipCsvDumpSession(fos))
        {
            // game session
            dumpTableCsvHeader(data, session, Tables.GAMESESSION);
            List<GamesessionRecord> gameSessionList = dumpTableCsvLines(data, session, Tables.GAMESESSION,
                    Tables.GAMESESSION.ID.eq(gameSessionRecordId), Tables.GAMESESSION.NAME);
            var gameSession = gameSessionList.get(0);
            closeTableCsv(Tables.GAMESESSION, session);

            // group
            dumpTableCsvHeader(data, session, Tables.GROUP);
            List<GroupRecord> groupList = dumpTableCsvLines(data, session, Tables.GROUP,
                    Tables.GROUP.GAMESESSION_ID.eq(gameSessionRecordId), Tables.GROUP.NAME);
            closeTableCsv(Tables.GROUP, session);

            exportDetails(data, session, gameSession, groupList);
        }
    }

    public static void exportGroupResults(final AdminData data, final File tempFile, final int groupRecordId) throws Exception
    {
        try (OutputStream fos = Files.newOutputStream(tempFile.toPath());
                ZipCsvDumpSession session = new ZipCsvDumpSession(fos))
        {
            // group
            dumpTableCsvHeader(data, session, Tables.GROUP);
            List<GroupRecord> groupList =
                    dumpTableCsvLines(data, session, Tables.GROUP, Tables.GROUP.ID.eq(groupRecordId), Tables.GROUP.NAME);
            var group = groupList.get(0);
            closeTableCsv(Tables.GROUP, session);

            // game session
            dumpTableCsvHeader(data, session, Tables.GAMESESSION);
            List<GamesessionRecord> gameSessionList = dumpTableCsvLines(data, session, Tables.GAMESESSION,
                    Tables.GAMESESSION.ID.eq(group.getGamesessionId()), Tables.GAMESESSION.NAME);
            var gameSession = gameSessionList.get(0);
            closeTableCsv(Tables.GAMESESSION, session);

            exportDetails(data, session, gameSession, groupList);
        }
    }

    protected static void exportDetails(final AdminData data, final ZipCsvDumpSession session,
            final GamesessionRecord gameSession, final List<GroupRecord> groupList) throws Exception
    {

        // player
        List<PlayerRecord> playerList = new ArrayList<>();
        dumpTableCsvHeader(data, session, Tables.PLAYER);
        for (var group : groupList)
        {
            var players = dumpTableCsvLines(data, session, Tables.PLAYER, Tables.PLAYER.GROUP_ID.eq(group.getId()),
                    Tables.PLAYER.CODE);
            playerList.addAll(players);
        }
        closeTableCsv(Tables.PLAYER, session);

        // groupround
        List<GrouproundRecord> groupRoundList = new ArrayList<>();
        dumpTableCsvHeader(data, session, Tables.GROUPROUND);
        for (var group : groupList)
        {
            var groupRounds = dumpTableCsvLines(data, session, Tables.GROUPROUND, Tables.GROUPROUND.GROUP_ID.eq(group.getId()),
                    Tables.GROUPROUND.TIMESTAMP);
            groupRoundList.addAll(groupRounds);
        }
        closeTableCsv(Tables.GROUPROUND, session);

        // playerround
        List<PlayerroundRecord> playerRoundList = new ArrayList<>();
        dumpTableCsvHeader(data, session, Tables.PLAYERROUND);
        for (var player : playerList)
        {
            var playerRounds = dumpTableCsvLines(data, session, Tables.PLAYERROUND,
                    Tables.PLAYERROUND.PLAYER_ID.eq(player.getId()), Tables.PLAYERROUND.CREATE_TIME);
            playerRoundList.addAll(playerRounds);
        }
        closeTableCsv(Tables.PLAYERROUND, session);

        // groupstate
        dumpTableCsvHeader(data, session, Tables.GROUPSTATE);
        for (var groupRound : groupRoundList)
            dumpTableCsvLines(data, session, Tables.GROUPSTATE, Tables.GROUPSTATE.GROUPROUND_ID.eq(groupRound.getId()),
                    Tables.GROUPSTATE.TIMESTAMP);
        closeTableCsv(Tables.GROUPSTATE, session);

        // playerstate
        dumpTableCsvHeader(data, session, Tables.PLAYERSTATE);
        for (var playerRound : playerRoundList)
            dumpTableCsvLines(data, session, Tables.PLAYERSTATE, Tables.PLAYERSTATE.PLAYERROUND_ID.eq(playerRound.getId()),
                    Tables.PLAYERSTATE.TIMESTAMP);
        closeTableCsv(Tables.PLAYERSTATE, session);

        // housegroup
        List<HousegroupRecord> houseGroupList = new ArrayList<>();
        dumpTableCsvHeader(data, session, Tables.HOUSEGROUP);
        for (var group : groupList)
        {
            var houseGroups = dumpTableCsvLines(data, session, Tables.HOUSEGROUP, Tables.HOUSEGROUP.GROUP_ID.eq(group.getId()),
                    Tables.HOUSEGROUP.ID);
            houseGroupList.addAll(houseGroups);
        }
        closeTableCsv(Tables.HOUSEGROUP, session);

        // housetransaction
        dumpTableCsvHeader(data, session, Tables.HOUSETRANSACTION);
        for (var houseGroup : houseGroupList)
            dumpTableCsvLines(data, session, Tables.HOUSETRANSACTION,
                    Tables.HOUSETRANSACTION.HOUSEGROUP_ID.eq(houseGroup.getId()), Tables.HOUSETRANSACTION.TIMESTAMP);
        closeTableCsv(Tables.HOUSETRANSACTION, session);

        // housemeasure
        dumpTableCsvHeader(data, session, Tables.HOUSEMEASURE);
        for (var houseGroup : houseGroupList)
            dumpTableCsvLines(data, session, Tables.HOUSEMEASURE, Tables.HOUSEMEASURE.HOUSEGROUP_ID.eq(houseGroup.getId()),
                    Tables.HOUSEMEASURE.ID);
        closeTableCsv(Tables.HOUSEMEASURE, session);

        // personalmeasure
        dumpTableCsvHeader(data, session, Tables.PERSONALMEASURE);
        for (var playerRound : playerRoundList)
            dumpTableCsvLines(data, session, Tables.PERSONALMEASURE,
                    Tables.PERSONALMEASURE.PLAYERROUND_ID.eq(playerRound.getId()), Tables.PERSONALMEASURE.ID);
        closeTableCsv(Tables.PERSONALMEASURE, session);

        // questionscore
        dumpTableCsvHeader(data, session, Tables.QUESTIONSCORE);
        for (var playerRound : playerRoundList)
            dumpTableCsvLines(data, session, Tables.QUESTIONSCORE, Tables.QUESTIONSCORE.PLAYERROUND_ID.eq(playerRound.getId()),
                    Tables.QUESTIONSCORE.ID);
        closeTableCsv(Tables.QUESTIONSCORE, session);

        // ------------------------------------- DEFINITION TABLES -------------------------------------------

        // gameversion
        dumpTableCsvHeader(data, session, Tables.GAMEVERSION);
        List<GameversionRecord> gameVersionList = dumpTableCsvLines(data, session, Tables.GAMEVERSION,
                Tables.GAMEVERSION.ID.eq(gameSession.getGameversionId()), Tables.GAMEVERSION.NAME);
        var gameVersion = gameVersionList.get(0);
        closeTableCsv(Tables.GAMEVERSION, session);

        // scenario
        dumpTableCsvHeader(data, session, Tables.SCENARIO);
        List<ScenarioRecord> scenarioList = dumpTableCsvLines(data, session, Tables.SCENARIO,
                Tables.SCENARIO.GAMEVERSION_ID.eq(gameVersion.getId()), Tables.SCENARIO.NAME);
        closeTableCsv(Tables.SCENARIO, session);

        // scenarioparameters
        dumpTableCsvHeader(data, session, Tables.SCENARIOPARAMETERS);
        for (var scenario : scenarioList)
            dumpTableCsvLines(data, session, Tables.SCENARIOPARAMETERS,
                    Tables.SCENARIOPARAMETERS.ID.eq(scenario.getScenarioparametersId()), Tables.SCENARIOPARAMETERS.ID);
        closeTableCsv(Tables.SCENARIOPARAMETERS, session);

        // welfaretype
        dumpTableCsvHeader(data, session, Tables.WELFARETYPE);
        for (var scenario : scenarioList)
            dumpTableCsvLines(data, session, Tables.WELFARETYPE, Tables.WELFARETYPE.SCENARIO_ID.eq(scenario.getId()),
                    Tables.WELFARETYPE.ID);
        closeTableCsv(Tables.WELFARETYPE, session);

        // question
        dumpTableCsvHeader(data, session, Tables.QUESTION);
        List<QuestionRecord> questionList = new ArrayList<>();
        for (var scenario : scenarioList)
        {
            var questions = dumpTableCsvLines(data, session, Tables.QUESTION, Tables.QUESTION.SCENARIO_ID.eq(scenario.getId()),
                    Tables.QUESTION.ID);
            questionList.addAll(questions);
        }
        closeTableCsv(Tables.QUESTION, session);

        // questionitem
        dumpTableCsvHeader(data, session, Tables.QUESTIONITEM);
        for (var question : questionList)
            dumpTableCsvLines(data, session, Tables.QUESTIONITEM, Tables.QUESTIONITEM.QUESTION_ID.eq(question.getId()),
                    Tables.QUESTIONITEM.ID);
        closeTableCsv(Tables.QUESTIONITEM, session);

        // newsitem
        dumpTableCsvHeader(data, session, Tables.NEWSITEM);
        List<NewsitemRecord> newsItemList = new ArrayList<>();
        for (var scenario : scenarioList)
        {
            var newsItems = dumpTableCsvLines(data, session, Tables.NEWSITEM, Tables.NEWSITEM.SCENARIO_ID.eq(scenario.getId()),
                    Tables.NEWSITEM.ID);
            newsItemList.addAll(newsItems);
        }
        closeTableCsv(Tables.NEWSITEM, session);

        // newseffects
        dumpTableCsvHeader(data, session, Tables.NEWSEFFECTS);
        for (var newsItem : newsItemList)
            dumpTableCsvLines(data, session, Tables.NEWSEFFECTS, Tables.NEWSEFFECTS.NEWSITEM_ID.eq(newsItem.getId()),
                    Tables.NEWSEFFECTS.ID);
        closeTableCsv(Tables.NEWSEFFECTS, session);

        // measurecategory
        dumpTableCsvHeader(data, session, Tables.MEASURECATEGORY);
        List<MeasurecategoryRecord> measureCategoryList = new ArrayList<>();
        for (var scenario : scenarioList)
        {
            var mcs = dumpTableCsvLines(data, session, Tables.MEASURECATEGORY,
                    Tables.MEASURECATEGORY.SCENARIO_ID.eq(scenario.getId()), Tables.MEASURECATEGORY.ID);
            measureCategoryList.addAll(mcs);
        }
        closeTableCsv(Tables.MEASURECATEGORY, session);

        // measuretype
        dumpTableCsvHeader(data, session, Tables.MEASURETYPE);
        for (var measureCategory : measureCategoryList)
            dumpTableCsvLines(data, session, Tables.MEASURETYPE,
                    Tables.MEASURETYPE.MEASURECATEGORY_ID.eq(measureCategory.getId()), Tables.MEASURETYPE.ID);
        closeTableCsv(Tables.MEASURETYPE, session);

        // movingreason
        dumpTableCsvHeader(data, session, Tables.MOVINGREASON);
        dumpTableCsvLines(data, session, Tables.MOVINGREASON, Tables.MOVINGREASON.GAMEVERSION_ID.eq(gameVersion.getId()),
                Tables.MOVINGREASON.ID);
        closeTableCsv(Tables.MOVINGREASON, session);

        // community
        dumpTableCsvHeader(data, session, Tables.COMMUNITY);
        List<CommunityRecord> communityList = dumpTableCsvLines(data, session, Tables.COMMUNITY,
                Tables.COMMUNITY.GAMEVERSION_ID.eq(gameVersion.getId()), Tables.COMMUNITY.ID);
        closeTableCsv(Tables.COMMUNITY, session);

        // tax
        dumpTableCsvHeader(data, session, Tables.TAX);
        for (var community : communityList)
            dumpTableCsvLines(data, session, Tables.TAX, Tables.TAX.COMMUNITY_ID.eq(community.getId()), Tables.TAX.ID);
        closeTableCsv(Tables.TAX, session);

        // house
        dumpTableCsvHeader(data, session, Tables.HOUSE);
        List<HouseRecord> houseList = new ArrayList<>();
        for (var community : communityList)
        {
            var houses = dumpTableCsvLines(data, session, Tables.HOUSE, Tables.HOUSE.COMMUNITY_ID.eq(community.getId()),
                    Tables.HOUSE.ID);
            houseList.addAll(houses);
        }
        closeTableCsv(Tables.HOUSE, session);

        // initialhousemeasure
        dumpTableCsvHeader(data, session, Tables.INITIALHOUSEMEASURE);
        for (var house : houseList)
            dumpTableCsvLines(data, session, Tables.INITIALHOUSEMEASURE, Tables.INITIALHOUSEMEASURE.HOUSE_ID.eq(house.getId()),
                    Tables.INITIALHOUSEMEASURE.ID);
        closeTableCsv(Tables.INITIALHOUSEMEASURE, session);
    }

    /**
     * ZipCsvDumpSession is a helper class to write multiple table-based csv files into a zip file.
     */
    public static class ZipCsvDumpSession implements AutoCloseable
    {

        private final ZipOutputStream zip;

        private final Map<String, CsvWriter> csvWriters = new HashMap<>();

        private final Map<String, Writer> rawWriters = new HashMap<>();

        private final Set<String> headerWritten = new HashSet<>();

        public ZipCsvDumpSession(final OutputStream out)
        {
            this.zip = new ZipOutputStream(new BufferedOutputStream(out), StandardCharsets.UTF_8);
        }

        /** Open a CSV entry for a table if not already open. */
        public <R extends Record, T extends Table<R>> void openTable(final T table) throws IOException
        {
            String entryName = table.getName() + ".csv";
            if (this.csvWriters.containsKey(entryName))
                return;

            ZipEntry entry = new ZipEntry(entryName);
            this.zip.putNextEntry(entry);

            // Writer chain: ZipOutputStream -> OutputStreamWriter(UTF-8) -> BufferedWriter
            Writer osw = new BufferedWriter(new OutputStreamWriter(this.zip, StandardCharsets.UTF_8));
            CsvWriter csv = de.siegmar.fastcsv.writer.CsvWriter.builder().build(osw);

            this.rawWriters.put(entryName, osw);
            this.csvWriters.put(entryName, csv);
        }

        /** Write header once (derived from jOOQ table fields). */
        public <R extends Record, T extends Table<R>> void writeHeaderIfNeeded(final T table) throws IOException
        {
            String entryName = table.getName() + ".csv";
            if (this.headerWritten.contains(entryName))
                return;

            CsvWriter csv = getCsvWriter(table);
            List<String> headers = new ArrayList<>(table.fields().length);
            for (Field<?> f : table.fields())
                headers.add(f.getName());
            csv.writeRow(headers);

            this.headerWritten.add(entryName);
        }

        /** Get the CsvWriter for a table (must be opened). */
        public <R extends Record, T extends Table<R>> CsvWriter getCsvWriter(final T table)
        {
            String entryName = table.getName() + ".csv";
            CsvWriter csv = this.csvWriters.get(entryName);
            if (csv == null)
                throw new IllegalStateException("Table not opened: " + entryName);
            return csv;
        }

        /** Append a row (values aligned with table.fields() order). */
        public <R extends Record, T extends Table<R>> void writeRow(final T table, final R record) throws IOException
        {
            CsvWriter csv = getCsvWriter(table);
            Field<?>[] fields = table.fields();
            List<String> values = new ArrayList<>(fields.length);
            for (Field<?> f : fields)
            {
                Object v = record.get(f);
                values.add(v == null ? "" : String.valueOf(v));
            }
            csv.writeRow(values);
        }

        /** Flush of the raw writer. */
        public <R extends Record, T extends Table<R>> void flushTable(final T table) throws IOException
        {
            String entryName = table.getName() + ".csv";
            Writer osw = this.rawWriters.get(entryName);
            if (osw == null)
                throw new IllegalStateException("Table not opened: " + entryName);
            osw.flush(); // CsvWriter has no flush() in 2.2.2
        }

        /** Close a table’s CSV entry (after all appends). */
        public <R extends Record, T extends Table<R>> void closeTable(final T table) throws IOException
        {
            String entryName = table.getName() + ".csv";
            this.rawWriters.computeIfPresent(entryName, (k, osw) -> {
                try
                {
                    osw.flush();
                }
                catch (IOException e)
                {
                    throw new UncheckedIOException(e);
                }
                return osw;
            });
            this.csvWriters.remove(entryName);
            this.rawWriters.remove(entryName);
            this.zip.closeEntry();
        }

        @Override
        public void close() throws IOException
        {
            // Safety: close any still-open entries
            for (String entryName : new ArrayList<>(this.csvWriters.keySet()))
            {
                // best-effort: flush and close entry
                this.csvWriters.remove(entryName);
                Writer osw = this.rawWriters.remove(entryName);
                if (osw != null)
                    osw.flush();
                // We can’t call closeEntry() without knowing which entry is open,
                // but in our flow we always close entries explicitly via closeTable().
            }
            this.zip.finish();
            this.zip.close();
        }
    }
}
