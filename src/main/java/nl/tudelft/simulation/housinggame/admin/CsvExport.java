package nl.tudelft.simulation.housinggame.admin;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void test(final AdminData data)
    {
        try
        {
            Path zipPath = Files.createTempFile("tables-", ".zip");

            try (OutputStream fos = Files.newOutputStream(zipPath); ZipCsvDumpSession session = new ZipCsvDumpSession(fos))
            {
                // welfare type
                CsvExport.dumpTableCsvHeader(data, session, Tables.WELFARETYPE);
                CsvExport.dumpTableCsvLines(data, session, Tables.WELFARETYPE, null, Tables.WELFARETYPE.NAME);
                CsvExport.closeTableCsv(Tables.WELFARETYPE, session);

                // measure type
                CsvExport.dumpTableCsvHeader(data, session, Tables.MEASURETYPE);
                CsvExport.dumpTableCsvLines(data, session, Tables.MEASURETYPE, null, Tables.MEASURETYPE.NAME);
                CsvExport.closeTableCsv(Tables.MEASURETYPE, session);
            }

            // Hand off zipPath to your servlet for streaming
            System.out.println("ZIP ready at: " + zipPath);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
