package sofa.internetbankieren.repository;

/**
 * @Author Wichert Tjerkstra aangemaakt op 9 dec
 */

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sofa.internetbankieren.model.Priverekening;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Primary
public class PriverekeningDAO implements GenericDAO<Priverekening> {

    private JdbcTemplate jdbcTemplate;
    private ParticulierDAO particulierDAO;
    private TransactieDAO transactieDAO;

    public PriverekeningDAO(JdbcTemplate jdbcTemplate, ParticulierDAO particulierDAO, @Lazy TransactieDAO transactieDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.particulierDAO = particulierDAO;
        this.transactieDAO = transactieDAO;
    }

    // get One by Id
    public Priverekening getOneByID(int idPriverekening){
        final String sql = "select * from priverekening where idpriverekening = ?";
        return jdbcTemplate.queryForObject(sql, new PriverekeningRowMapper(), idPriverekening);
    }

    // toegevoegd door Wendy
    public List<Priverekening> getOneByIban(String iban) {
        final String sql = "select * from priverekening where iban=?";
        return jdbcTemplate.query(sql, new PriverekeningRowMapper(), iban);
    }

        // get All
    public List<Priverekening> getAll() {
        final String sql = "select * from priverekening";
        return jdbcTemplate.query(sql, new PriverekeningRowMapper(), null);
    }

    // get All By Rekeninghouder
    public List<Priverekening> getAllByRekeninghouder(int idRekeninghouder) {
        final String sql = "select * from priverekening where idrekeninghouder=?";
        return jdbcTemplate.query(sql, new PriverekeningRowMapper(), idRekeninghouder);
    }

    public List<Integer> getAllIDsByRekeninghouder(int idRekeninghouder) {
        final String sql = "select idpriverekening from priverekening where idrekeninghouder=?";
        return jdbcTemplate.query(sql, new PriverekeningIDRowMapper(), idRekeninghouder);

    }

    // update One
    public void updateOne(Priverekening priverekening) {
         jdbcTemplate.update("update priverekening set idrekeninghouder=?, " +
                        " saldo=?, iban=? where idpriverekening=?",
                priverekening.getRekeninghouder().getIdKlant(),
                priverekening.getSaldo(),
                priverekening.getIBAN(),
                priverekening.getIdRekening());
    }

    // store One
    public void storeOne(Priverekening priverekening) {
        final String sql = "insert into priverekening (idrekeninghouder, saldo, iban) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"idpriverekening"});
                ps.setInt(1, priverekening.getRekeninghouder().getIdKlant());
                ps.setDouble(2, priverekening.getSaldo());
                ps.setString(3, priverekening.getIBAN());
                return ps;
            }
        }, keyHolder);
        priverekening.setIdRekening(keyHolder.getKey().intValue());
    }

    // delete One
    public void deleteOne(Priverekening priverekening) {
        jdbcTemplate.update("delete from priverekening where idpriverekening=?",
                priverekening.getIdRekening());
    }

    class PriverekeningRowMapper implements RowMapper<Priverekening> {

        @Override
        public Priverekening mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Priverekening(resultSet.getInt("idpriverekening"),
                    resultSet.getString("iban"),
                    resultSet.getDouble("saldo"),
                    transactieDAO.getAllIdsByIdPriverekening(resultSet.getInt("idpriverekening")),
                    transactieDAO,
                    (particulierDAO.getOneByID(resultSet.getInt("idrekeninghouder"))));
        }
    }

    class PriverekeningIDRowMapper implements RowMapper<Integer> {

        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("idpriverekening");
        }
    }
}



