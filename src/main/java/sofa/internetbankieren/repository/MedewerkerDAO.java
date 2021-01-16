package sofa.internetbankieren.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sofa.internetbankieren.model.Medewerker;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Wendy Ellens
 *
 * Voegt medewerkergegevens toe aan de SQL-database of haalt deze eruit op.
 */
@Repository
public class MedewerkerDAO implements GenericDAO<Medewerker>{

    private final JdbcTemplate jdbcTemplate;
    private final BedrijfDAO bedrijfDAO;

    public MedewerkerDAO(JdbcTemplate jdbcTemplate, @Lazy BedrijfDAO bedrijfDAO) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.bedrijfDAO = bedrijfDAO;
    }

    @Override
    public List<Medewerker> getAll() {
        String sql = "select * from medewerker";
        return jdbcTemplate.query(sql, new MedewerkerRowMapper());
    }

    @Override
    public Medewerker getOneByID(int personeelsnummer) {
        String sql = "select * from medewerker where personeelsnummer = ?";
        return jdbcTemplate.queryForObject(sql, new MedewerkerRowMapper(), personeelsnummer);
    }

  public List<Medewerker> getOneByGebruikersnaamWachtwoord(String gebruikersnaam, String wachtwoord){
        final String sql = "select * from medewerker where gebruikersnaam=? and wachtwoord=?";
        return jdbcTemplate.query(sql, new MedewerkerDAO.MedewerkerRowMapper(), gebruikersnaam, wachtwoord);
    }

    @Override
    public void storeOne(Medewerker medewerker) {
        String sql = "insert into medewerker (gebruikersnaam, wachtwoord, voornaam, " +
                "tussenvoegsels, achternaam, rol) values (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"personeelsnummer"});
            ps.setString(1, medewerker.getGebruikersnaam());
            ps.setString(2, medewerker.getWachtwoord());
            ps.setString(3, medewerker.getVoornaam());
            ps.setString(4, medewerker.getTussenvoegsels());
            ps.setString(5, medewerker.getAchternaam());
            ps.setString(6, medewerker.getRol().name());
            return ps;
        }, keyHolder);
        medewerker.setPersoneelsnummer(keyHolder.getKey().intValue());
    }

    @Override
    public void updateOne(Medewerker medewerker) {
        jdbcTemplate.update("update medewerker set gebruikersnaam = ?, wachtwoord = ?, " +
                        "voornaam = ?, tussenvoegsels = ?, achternaam = ?, rol = ? where personeelsnummer=?",
            medewerker.getGebruikersnaam(),
            medewerker.getWachtwoord(),
            medewerker.getVoornaam(),
            medewerker.getTussenvoegsels(),
            medewerker.getAchternaam(),
            medewerker.getRol().name(),
            medewerker.getPersoneelsnummer()
        );
    }

    @Override
    public void deleteOne(Medewerker medewerker) {
        jdbcTemplate.update("delete from medewerker where personeelsnummer = ?",
                medewerker.getPersoneelsnummer());
    }

    private final class MedewerkerRowMapper implements RowMapper<Medewerker> {
        @Override
        public Medewerker mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Medewerker(
                resultSet.getInt("personeelsnummer"),
                resultSet.getString("gebruikersnaam"),
                resultSet.getString("wachtwoord"),
                resultSet.getString("voornaam"),
                resultSet.getString("tussenvoegsels"),
                resultSet.getString("achternaam"),
                Medewerker.Rol.valueOf(resultSet.getString("rol")),
                // TODO IllegalArgumentException afvangen?
                bedrijfDAO.getAllIDsByIdAccountmanager(resultSet.getInt("personeelsnummer")),
                bedrijfDAO
            );
        }
    }
}


