package sofa.internetbankieren.repository;

/**
 * @author Taco Jongkind, 09-12-2020
 *
 * */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sofa.internetbankieren.model.Particulier;

import java.sql.*;
import java.util.List;

@Repository
public class ParticulierDAO {

    private JdbcTemplate jdbcTemplate;
    private PriverekeningDAO priverekeningDAO;
    private BedrijfsrekeningDAO bedrijfsrekeningDAO;

    public ParticulierDAO(JdbcTemplate jdbcTemplate, @Lazy PriverekeningDAO priverekeningDAO, @Lazy BedrijfsrekeningDAO bedrijfsrekeningDAO) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.priverekeningDAO = priverekeningDAO;
        this.bedrijfsrekeningDAO = bedrijfsrekeningDAO;
    }

    //get All
    public List<Particulier> getAll() {
        final String sql = "select * from particulier";
        return jdbcTemplate.query(sql, new ParticulierRowMapper(), null);
    }

    // get One by Id
    public Particulier getOneByID(int idKlant){
        final String sql = "select * from particulier where idparticulier=?";
        return jdbcTemplate.queryForObject(sql, new ParticulierRowMapper(), idKlant);
    }

    // get One by gebruikersnaam en wachtwoord
    public List<Particulier> getOneByGebruikersnaamWachtwoord(String gebruikersnaam, String wachtwoord){
        final String sql = "select * from particulier where gebruikersnaam=? and wachtwoord=?";
        return jdbcTemplate.query(sql, new ParticulierRowMapper(), gebruikersnaam, wachtwoord);
    }

    //get All by naam
    public Particulier getByNaam(String voornaam, String achternaam) {
        final String sql = "select * from particulier where voornaam=? and achternaam=?";
        return jdbcTemplate
                .queryForObject(sql,
                        new ParticulierRowMapper(), voornaam, achternaam);
    }
    //get All by BSN
    public Particulier getByBSN(int BSN) {
        final String sql = "select * from particulier where bsn=?";
        return jdbcTemplate
                .queryForObject(sql,
                        new ParticulierRowMapper(), BSN);
    }

    // store One
    public void storeOne(Particulier particulier) {
        final String sql = "insert into particulier(gebruikersnaam, wachtwoord, voornaam, tussenvoegsels, achternaam, " +
                "bsn, geboortedatum," +
                "straat, huisnummer, postcode, woonplaats ) values (?,?,?,?,?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"idparticulier"});
                ps.setString(1, particulier.getGebruikersnaam());
                ps.setString(2, particulier.getWachtwoord());
                ps.setString(3, particulier.getVoornaam());
                ps.setString(4, particulier.getTussenvoegsels());
                ps.setString(5, particulier.getAchternaam());
                ps.setInt(6, particulier.getBSN());
                ps.setDate(7, Date.valueOf(particulier.getGeboortedatum()));
                ps.setString(8, particulier.getStraat());
                ps.setInt(9, particulier.getHuisnummer());
                ps.setString(10, particulier.getPostcode());
                ps.setString(11, particulier.getWoonplaats());
                return ps;
            }
        }, keyHolder);
        particulier.setIdKlant(keyHolder.getKey().intValue());
    }
    //update one
    public int updateOne(Particulier particulier) {
        return jdbcTemplate.update("update particulier set gebruikersnaam=?, wachtwoord=?, voornaam=?, tussenvoegsels=?, achternaam=?, bsn=?," +
                        "geboortedatum=?, straat=?, huisnummer=?, postcode=?, woonplaats=? " +
                        "where idparticulier=?",
                particulier.getVoornaam(),
                particulier.getTussenvoegsels(),
                particulier.getAchternaam(),
                particulier.getBSN(),
                particulier.getGeboortedatum(), //todo Date.valueOf toevoegen? (Wendy)
                particulier.getStraat(),
                particulier.getHuisnummer(),
                particulier.getPostcode(),
                particulier.getWoonplaats(),
                particulier.getIdKlant());
    }

    public int deleteOne(Particulier particulier) {
        return jdbcTemplate.update("delete from particulier where idparticulier=?",
                particulier.getIdKlant());
    }

    //RowMapper

    public class ParticulierRowMapper implements RowMapper<Particulier> {

        @Override
        public Particulier mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Particulier(resultSet.getInt("idparticulier"),
                    resultSet.getString("gebruikersnaam"),
                    resultSet.getString("wachtwoord"),
                    resultSet.getString("straat"),
                    resultSet.getInt("huisnummer"),
                    resultSet.getString("postcode"),
                    resultSet.getString("woonplaats"),
                    resultSet.getString("voornaam"),
                    resultSet.getString("tussenvoegsels"),
                    resultSet.getString("achternaam"),
                    resultSet.getDate("geboortedatum").toLocalDate(),
                    resultSet.getInt("bsn"),
                    priverekeningDAO.getAllIDsByRekeninghouder(resultSet.getInt("idparticulier")),
                    bedrijfsrekeningDAO.getAllIDsByContactpersoon(resultSet.getInt("idparticulier")),
                    bedrijfsrekeningDAO,
                    priverekeningDAO
            );
        }
    }
}
