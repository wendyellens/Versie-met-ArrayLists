package sofa.internetbankieren.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import sofa.internetbankieren.model.*;

import java.sql.*;
import java.util.List;

/**
 * @author Wendy Ellens
 *
 * Voegt transactiegegevens toe aan de SQL-database of haalt deze eruit op.
 */
@Repository
public class TransactieDAO implements GenericDAO<Transactie> {

    private final JdbcTemplate jdbcTemplate;
    private final PriverekeningDAO priverekeningDAO;
    private final BedrijfsrekeningDAO bedrijfsrekeningDAO;

    public TransactieDAO(JdbcTemplate jdbcTemplate, PriverekeningDAO priverekeningDAO,
                         BedrijfsrekeningDAO bedrijfsrekeningDAO) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.priverekeningDAO = priverekeningDAO;
        this.bedrijfsrekeningDAO = bedrijfsrekeningDAO;
    }

    @Override
    public List<Transactie> getAll() {
        String sql = "select * from transactie";
        return jdbcTemplate.query(sql, new TransactieRowMapper());
    }

    public List<Transactie> getAllByIDPriverekening(int idRekening) {
        String sql = "select * from transactie where idpriverekening = ?";
        return jdbcTemplate.query(sql, new TransactieRowMapper(), idRekening);
    }

    public List<Integer> getAllIdsByIdPriverekening(int idRekening) {
        String sql = "select idtransactie from transactie where idpriverekening = ?";
        return jdbcTemplate.query(sql, new TransactieIdRowMapper(), idRekening);
    }

    public List<Transactie> getAllByIDBedrijfsrekening(int idRekening) {
        String sql = "select * from transactie where idbedrijfsrekening = ?";
        return jdbcTemplate.query(sql, new TransactieRowMapper(), idRekening);
    }

    public List<Integer> getAllIdsByIdBedrijfsrekening(int idRekening) {
        String sql = "select idtransactie from transactie where idbedrijfsrekening = ?";
        return jdbcTemplate.query(sql, new TransactieIdRowMapper(), idRekening);
    }

    @Override
    public Transactie getOneByID(int idTransactie) {
        String sql = "select * from transactie where idtransactie = ?";
        return jdbcTemplate.queryForObject(sql, new TransactieRowMapper(), idTransactie);
    }

    private Rekening getRekeningByID(int idTransactie) {
        String sql = "select * from transactie where idtransactie = ?";
        return jdbcTemplate.queryForObject(sql, new TransactieRekeningRowMapper(), idTransactie);
    }

    @Override
    public void storeOne(Transactie transactie) {
        // in de database wordt de transactie opgeslagen als een aparte bij- en afschrijving
        Transactie transactie2 = new Transactie(transactie);
        transactie2.setBijschrijving(!transactie.isBijschrijving());
        transactie2.setRekening(transactie.getTegenRekening());
        transactie2.setTegenRekening(transactie.getRekening());
        // eerst de afschrijving, dan de bijschrijving
        if (transactie.isBijschrijving()) {
            storePart(transactie2);
            storePart(transactie);
        }
        else {
            storePart(transactie);
            storePart(transactie2);
        }
    }

    private void storePart(Transactie transactie) {
        String sql = "insert into transactie (bedrag, transactiebeschrijving, datum, bijschrijving, " +
                "idbedrijfsrekening, idpriverekening) values (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"idtransactie"});
            ps.setDouble(1, transactie.getBedrag());
            ps.setString(2, transactie.getOmschrijving());
            ps.setTimestamp(3, Timestamp.valueOf(transactie.getDatum()));
            ps.setBoolean(4, transactie.isBijschrijving());
            if (transactie.getRekening() instanceof Bedrijfsrekening) {
                ps.setInt(5, transactie.getRekening().getIdRekening());
                ps.setNull(6, Types.INTEGER);
            }
            else {
                ps.setNull(5, Types.INTEGER);
                ps.setInt(6, transactie.getRekening().getIdRekening());
            }
            return ps;
        }, keyHolder);
        transactie.setIdTransactie(keyHolder.getKey().intValue());
    }

    @Override
    public void updateOne(Transactie transactie) {
        // todo: bij- en afschrijvingen tegelijk updaten
        jdbcTemplate.update("update transactie set bedrag = ?, transactiebeschrijving = ?, datum = ?, " +
                        "bijschrijving = ?, idbedrijfsrekening = ?, idpriverekening = ? where idtransactie = ?",
            transactie.getBedrag(),
            transactie.getOmschrijving(),
            Timestamp.valueOf(transactie.getDatum()),
            transactie.isBijschrijving(),
            transactie.getRekening() instanceof Bedrijfsrekening ?
                transactie.getRekening().getIdRekening() : null,
            transactie.getRekening() instanceof Priverekening ?
                transactie.getRekening().getIdRekening() : null,
            transactie.getIdTransactie()
            );
    }

    @Override
    public void deleteOne(Transactie transactie) {
        // de bij- en afschrijving worden verwijderd
        deletePart(transactie.getIdTransactie());
        deletePart(transactie.getIdTransactie() + 1);
    }

    private void deletePart(int idTransactie) {
        jdbcTemplate.update("delete from transactie where idtransactie = ?", idTransactie);
    }

    private final class TransactieRowMapper implements RowMapper<Transactie> {
        @Override
        public Transactie mapRow(ResultSet resultSet, int i) throws SQLException {
            Transactie transactie = new Transactie(
                resultSet.getInt("idtransactie"),
                getRekening(resultSet),
                resultSet.getDouble("bedrag"),
                resultSet.getTimestamp("datum").toLocalDateTime(),
                resultSet.getString("transactiebeschrijving"),
                // voor bijschrijvingen staat de tegenrekening in de vorige transactie, anders in de volgende
                        resultSet.getBoolean("bijschrijving")
                        ? getRekeningByID(resultSet.getInt("idtransactie") - 1)
                        : getRekeningByID(resultSet.getInt("idtransactie") + 1)
                    // todo controleren of afschrijving inderdaad eerst staat?
            );
            transactie.setBijschrijving(resultSet.getBoolean("bijschrijving"));
            // todo controleren of bijschrijving 0 of 1 is?
            return transactie;
        }
    }

    private final class TransactieIdRowMapper implements RowMapper<Integer> {
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("idtransactie");
        }
    }

    private final class TransactieRekeningRowMapper implements RowMapper<Rekening> {
        @Override
        public Rekening mapRow(ResultSet resultSet, int i) throws SQLException {
            return getRekening(resultSet);
        }
    }

    // retourneert het gevulde veld: priverekening of bedrijfsrekening
    private Rekening getRekening(ResultSet resultSet) throws SQLException {
        return resultSet.getInt("idbedrijfsrekening") == 0 ?
                priverekeningDAO.getOneByID(resultSet.getInt("idpriverekening")) :
                bedrijfsrekeningDAO.getOneByID(resultSet.getInt("idbedrijfsrekening"));
        // todo controleren of precies een van beide rekeningvelden is gevuld?
    }
}
