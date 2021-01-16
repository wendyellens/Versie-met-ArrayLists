package sofa.internetbankieren.repository;

/**
 * @Author Wichert Tjerkstra aangemaakt op 8 dec
 */

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sofa.internetbankieren.model.Bedrijfsrekening;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Primary
public class BedrijfsrekeningDAO implements GenericDAO<Bedrijfsrekening> {

    private JdbcTemplate jdbcTemplate;
    private ParticulierDAO particulierDAO;
    private BedrijfDAO bedrijfDAO;
    private TransactieDAO transactieDAO;

    public BedrijfsrekeningDAO(JdbcTemplate jdbcTemplate, ParticulierDAO particulierDAO, BedrijfDAO bedrijfDAO,
                               @Lazy TransactieDAO transactieDAO) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.particulierDAO = particulierDAO;
        this.bedrijfDAO = bedrijfDAO;
        this.transactieDAO = transactieDAO;
    }

    // get One by Id
    public Bedrijfsrekening getOneByID(int idBedrijfsrekening) {
        final String sql = "select * from bedrijfsrekening where idbedrijfsrekening=?";
        return jdbcTemplate.queryForObject(sql, new BedrijfsrekeningRowMapper(), idBedrijfsrekening);
    }

    // toegevoegd door Wendy
    public List<Bedrijfsrekening> getOneByIban(String iban) {
        final String sql = "select * from internet_bankieren.bedrijfsrekening where iban=?";
        return jdbcTemplate.query(sql, new BedrijfsrekeningRowMapper(), iban);
    }

    // get All
    public List<Bedrijfsrekening> getAll() {
        final String sql = "select * from bedrijfsrekening";
        return jdbcTemplate.query(sql, new BedrijfsrekeningRowMapper(), null);
    }

    // get All By Rekeninghouder (Bedrijf)
    public List<Bedrijfsrekening> getAllByBedrijf(int idRekeninghouder) {
        final String sql = "select * from bedrijfsrekening where idbedrijf=?";
        return jdbcTemplate.query(sql, new BedrijfsrekeningRowMapper(), idRekeninghouder);
    }

    public List<Integer> getAllIDsByBedrijf(int idRekeninghouder) {
        final String sql = "select idbedrijfsrekening from bedrijfsrekening where idbedrijf=?";
        return jdbcTemplate.query(sql, new BedrijfsrekeningIDRowMapper(), idRekeninghouder);
    }

    // get All By Contactpersoon (Contactpersoon)
    public List<Bedrijfsrekening> getAllByContactpersoon(int idContactpersoon) {
        final String sql = "select * from bedrijfsrekening where idcontactpersoon=?";
        return jdbcTemplate.query(sql, new BedrijfsrekeningRowMapper(), idContactpersoon);
    }

    public List<Integer> getAllIDsByContactpersoon(int idContactpersoon) {
        final String sql = "select idbedrijfsrekening from bedrijfsrekening where idcontactpersoon=?";
        return jdbcTemplate.query(sql, new BedrijfsrekeningIDRowMapper(), idContactpersoon);
    }

    // update One
    public void updateOne(Bedrijfsrekening bedrijfsrekening) {
        jdbcTemplate.update("update bedrijfsrekening set idbedrijf=?, " +
                        " idcontactpersoon=?, saldo=?, IBAN=? where idbedrijfsrekening=?",
                bedrijfsrekening.getRekeninghouder().getIdKlant(),
                bedrijfsrekening.getContactpersoon().getIdKlant(),
                bedrijfsrekening.getSaldo(),
                bedrijfsrekening.getIBAN(),
                bedrijfsrekening.getIdRekening());
    }

    // store One
    public void storeOne(Bedrijfsrekening bedrijfsrekening) {
        final String sql = "insert into bedrijfsrekening (idbedrijf, idcontactpersoon, saldo, iban) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"idbedrijfsrekening"});
                ps.setInt(1, bedrijfsrekening.getRekeninghouder().getIdKlant());
                ps.setInt(2, bedrijfsrekening.getContactpersoon().getIdKlant());
                ps.setDouble(3, bedrijfsrekening.getSaldo());
                ps.setString(4, bedrijfsrekening.getIBAN());
                return ps;
            }
        }, keyHolder);
        bedrijfsrekening.setIdRekening(keyHolder.getKey().intValue());   // TODO wil jij dit uitleggen wat dit doet?
    }


    // delete One
    public void deleteOne(Bedrijfsrekening bedrijfsrekening) {
        jdbcTemplate.update("delete from bedrijfsrekening where idbedrijfsrekening=?",
                bedrijfsrekening.getIdRekening());
    }

    class BedrijfsrekeningRowMapper implements RowMapper<Bedrijfsrekening> {

        @Override
        public Bedrijfsrekening mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Bedrijfsrekening(resultSet.getInt("idbedrijfsrekening"),
                    resultSet.getString("iban"),
                    resultSet.getDouble("saldo"),
                    transactieDAO.getAllIdsByIdBedrijfsrekening(resultSet.getInt("idbedrijfsrekening")),
                    transactieDAO,
                    (particulierDAO.getOneByID(resultSet.getInt("idcontactpersoon"))),
                    (bedrijfDAO.getOneByID(resultSet.getInt("idbedrijf"))));
        }
    }

    class BedrijfsrekeningIDRowMapper implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("idbedrijfsrekening");
        }
    }
}



