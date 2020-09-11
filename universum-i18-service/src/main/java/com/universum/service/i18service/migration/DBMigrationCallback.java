package com.universum.service.i18service.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class DBMigrationCallback implements Callback {
    public static final String COMMA_DELIMITER = ",";
    private static final String INSERT_AVAILABLE_LANGUAGE = "INSERT INTO public.available_language (code, \"label\", dir, \"default\") VALUES(?,?,?,?) ON CONFLICT (code) DO UPDATE SET last_update = now(), \"label\" = ?, dir = ?, \"default\" = ? ";
    private static final String INSERT_RESOURCE_MESSAGE = "INSERT INTO public.resource_message (resource_name, resource_value, available_language_fk) VALUES(?,?,(SELECT al.id FROM public.available_language al WHERE al.code = ?)) ON CONFLICT (resource_name,available_language_fk) DO UPDATE SET last_update = now(), resource_value = ?";

    @Override
    public boolean supports(Event event, Context context) {
        log.info("DBMigrationCallback only supported AFTER_MIGRATE.");
        return Event.AFTER_MIGRATE.equals(event);
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return false;
    }

    @Override
    public void handle(Event event, Context context) {
        if(Event.AFTER_MIGRATE.equals(event)) {
            processAvailableLanguage(context.getConnection());
            processResourceMessage(context.getConnection());
        }
    }

    private void processAvailableLanguage(final Connection dbConn) {
        try(Scanner scanner = readCSVFile("classpath:db/data/available_language.CSV", true)){
            while (scanner.hasNextLine()) {
                List<String> availableLanguageData = getRecordFromLine(scanner.nextLine());
                PreparedStatement preStmt = dbConn.prepareStatement(INSERT_AVAILABLE_LANGUAGE);
                preStmt.setString(1, availableLanguageData.get(0));
                preStmt.setString(2, availableLanguageData.get(1));
                preStmt.setString(3, availableLanguageData.get(2));
                preStmt.setBoolean(4, Boolean.getBoolean(availableLanguageData.get(3)));
                preStmt.setString(5, availableLanguageData.get(1));
                preStmt.setString(6, availableLanguageData.get(2));
                preStmt.setBoolean(7, Boolean.getBoolean(availableLanguageData.get(3)));
                preStmt.executeUpdate();
            }
        } catch (FileNotFoundException notFoundEx) {
            log.error("Unable to read default data from available_language.CSV", notFoundEx);
        } catch (SQLException sqlEx) {
            log.error("SQL exception occurred while inserting available language.", sqlEx);
        }
    }

    private void processResourceMessage(final Connection dbConn) {
        try(Scanner scanner = readCSVFile("classpath:db/data/resource_message.CSV", true)){
            while (scanner.hasNextLine()) {
                List<String> resourceMessageData = getRecordFromLine(scanner.nextLine());
                PreparedStatement preStmt = dbConn.prepareStatement(INSERT_RESOURCE_MESSAGE);
                preStmt.setString(1, resourceMessageData.get(0));
                preStmt.setString(2, resourceMessageData.get(1));
                preStmt.setString(3, resourceMessageData.get(2));
                preStmt.setString(4, resourceMessageData.get(1));
                preStmt.executeUpdate();
            }
        } catch (FileNotFoundException notFoundEx) {
            log.error("Unable to read default data from resource_message.CSV", notFoundEx);
        } catch (SQLException sqlEx) {
            log.error("SQL exception occurred while inserting resource message.", sqlEx);
        }
    }

    private Scanner readCSVFile(final String resourceLocation, final Boolean skipHeader) throws FileNotFoundException {
        Scanner scanner = null;
        if(!StringUtils.isEmpty(resourceLocation)) {
            scanner = new Scanner(ResourceUtils.getFile(resourceLocation));
            if(skipHeader) {
                if(scanner.hasNextLine()){
                    scanner.nextLine();
                }
            }
        }
        return scanner;
    }

    private List<String> getRecordFromLine(final String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
