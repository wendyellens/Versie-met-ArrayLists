package sofa.internetbankieren.repository;

import java.util.List;

/**
 * @author Wendy Ellens
 *
 * Alle DAO's implementeren onderstaande methodes
 */
public interface GenericDAO<T> {
    List<T> getAll();
    T getOneByID(int id);
    void storeOne(T type);
    void updateOne(T type);
    void deleteOne(T type);
}