package com.example.projekt_1.database;

import com.example.projekt_1.exceptions.BazaPodatakaException;
import com.example.projekt_1.main.Main;
import com.example.projekt_1.model.Applicants;
import com.example.projekt_1.model.Conference;
import com.example.projekt_1.model.ScientificWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    public static void insertScientificWork(ScientificWork scientificWork) throws BazaPodatakaException {
        logger.debug("Inserting scientific work...");

        try (Connection connection = DatabaseConnection.establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.INSERT_SCIENTIFIC_WORK);
            //PreparedStatement preparedStatement = tVeza.prepareStatement("invalid");

            preparedStatement.setString(1, scientificWork.getName());
            preparedStatement.setString(2, scientificWork.getDetails());
            preparedStatement.setString(3, scientificWork.getCategory());
            preparedStatement.setString(4, scientificWork.getMentor());
            preparedStatement.setObject(5, scientificWork.getDate());
            preparedStatement.setString(6, scientificWork.getPath());
            preparedStatement.setInt(7, scientificWork.getIdKorisnika());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            String errorMessage = "Error occurred while working with the database";
            logger.error(errorMessage, e);
            throw new BazaPodatakaException(errorMessage, e);
        }
    }

    public static void updateScientificWork(ScientificWork scientificWork) throws BazaPodatakaException {
        logger.debug("Updating scientific work...");

        try (Connection connection = DatabaseConnection.establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.UPDATE_SCIENTIFIC_WORK);

            preparedStatement.setString(1, scientificWork.getName());
            preparedStatement.setString(2, scientificWork.getDetails());
            preparedStatement.setString(3, scientificWork.getCategory());
            preparedStatement.setString(4, scientificWork.getMentor());
            preparedStatement.setObject(5, scientificWork.getDate());
            preparedStatement.setString(6, scientificWork.getPath());
            preparedStatement.setInt(7, scientificWork.getIdKorisnika());
            preparedStatement.setInt(8, scientificWork.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String errorMessage = "Error occurred while updating scientific work";
            logger.error(errorMessage, ex);
            throw new BazaPodatakaException(errorMessage, ex);
        }
    }

    public static void deleteScientificWork(int workId) throws BazaPodatakaException {
        logger.debug("Deleting scientific work...");

        try (Connection connection = DatabaseConnection.establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.DELETE_SCIENTIFIC_WORK);
            preparedStatement.setInt(1, workId);

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            String errorMessage = "Error occurred while deleting scientific work";
            logger.error(errorMessage, e);
            throw new BazaPodatakaException(errorMessage, e);
        }
    }

    public static ScientificWork getScientificWorkById(int workId) throws BazaPodatakaException {
        ScientificWork scientificWork = null;

        try (Connection connection = DatabaseConnection.establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.GET_SCIENTIFIC_WORK_WITH_ID);
            preparedStatement.setInt(1, workId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    Integer id = resultSet.getInt("ID");
                    String name = resultSet.getString("NASLOV");
                    String details = resultSet.getString("DETALJI");
                    String category = resultSet.getString("KATEGORIJE");
                    String mentor = resultSet.getString("MENTOR");
                    LocalDate date = resultSet.getObject("DATUM_OBRANE", LocalDate.class);
                    String path = resultSet.getString("DATOTEKA");
                    Integer userID = resultSet.getInt("ID_KORISNIKA");

                    scientificWork = new ScientificWork(id, name, details, category, mentor, date, path, userID);
                }
            }
        } catch (SQLException | IOException ex) {
            String errorMessage = "Error occurred while working with the database";
            logger.error(errorMessage, ex);
            throw new BazaPodatakaException(errorMessage, ex);
        }
        return scientificWork;
    }

    //CONFERENCES

    public static void insertConference(Conference conference) throws BazaPodatakaException {
        logger.debug("Inserting conference...");

        try (Connection connection = DatabaseConnection.establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.INSERT_CONFERENCE);

            preparedStatement.setString(1, conference.getName());
            preparedStatement.setString(2, conference.getDetails());
            preparedStatement.setString(3, conference.getPredmet());
            preparedStatement.setString(4, conference.getProfesor());
            preparedStatement.setObject(5, conference.getDate());
            preparedStatement.setInt(6, conference.getIdProfesora());

            preparedStatement.executeUpdate();

        } catch (SQLException | IOException e) {
            String errorMessage = "Error occurred while working with the database";
            logger.error(errorMessage, e);
            throw new BazaPodatakaException(errorMessage, e);
        }
    }

    public static void updateConference(Conference conference) throws BazaPodatakaException {
        logger.debug("Updating conference...");

        try (Connection connection = DatabaseConnection.establishConnection()) {
           PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.UPDATE_CONFERENCE);

            preparedStatement.setString(1, conference.getName());
            preparedStatement.setString(2, conference.getDetails());
            preparedStatement.setString(3, conference.getPredmet());
            preparedStatement.setString(4, conference.getProfesor());
            preparedStatement.setObject(5, conference.getDate());
            preparedStatement.setInt(6, conference.getIdProfesora());
            preparedStatement.setInt(7, conference.getIdKonferencije());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String errorMessage = "Error occurred while updating conference";
            logger.error(errorMessage, ex);
            throw new BazaPodatakaException(errorMessage, ex);
        }
    }

    public static void deleteConference(int conferenceId) throws BazaPodatakaException {
        logger.debug("Deleting conference...");

        try (Connection connection = DatabaseConnection.establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.DELETE_CONFERENCE);
            preparedStatement.setInt(1, conferenceId);

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String errorMessage = "Error occurred while deleting conference";
            logger.error(errorMessage, ex);
            throw new BazaPodatakaException(errorMessage, ex);
        }
    }

    public static Conference getConferenceById(int id) throws BazaPodatakaException {
        Conference conference = null;

        try (Connection connection = DatabaseConnection.establishConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.GET_CONFERENCE_WITH_ID);
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    int conferenceId = resultSet.getInt("ID_KONFERENCIJE");
                    String name = resultSet.getString("NASLOV");
                    String details = resultSet.getString("DETALJI");
                    String predmet = resultSet.getString("PREDMET");
                    String profesor = resultSet.getString("PROFESOR");
                    LocalDate date = resultSet.getObject("DATUM_KONFERENCIJE", LocalDate.class);
                    int idProfesora = resultSet.getInt("ID_PROFESORA");

                    conference = new Conference.Builder()
                            .setIdKonferencije(conferenceId)
                            .setName(name)
                            .setDetails(details)
                            .setPredmet(predmet)
                            .setProfesor(profesor)
                            .setDate(date)
                            .setIdProfesora(idProfesora)
                            .build();
                }
            }

        } catch (SQLException | IOException e) {
            String errorMessage = "Error occurred while working with the database";
            logger.error(errorMessage, e);
            throw new BazaPodatakaException(errorMessage, e);
        }
        return conference;
    }

    //APPLICANTS

    public static void applyConference(Applicants applicants) throws BazaPodatakaException {
        logger.debug("Inserting applicants...");

        try (Connection connection = DatabaseConnection.establishConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.INSERT_APPLICANTS);

            preparedStatement.setInt(1, applicants.idStudenta());
            preparedStatement.setInt(2, applicants.idKonferencije());
            preparedStatement.setString(3, applicants.name());

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String errorMessage = "Error occurred while working with the database";
            logger.error(errorMessage, ex);
            throw new BazaPodatakaException(errorMessage, ex);
        }
    }
}


