package com.epam.javacourse.hotelapp.dao;

import com.epam.javacourse.hotelapp.dao.interfaces.IUserDAO;
import com.epam.javacourse.hotelapp.exception.DBException;
import com.epam.javacourse.hotelapp.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends GenericDAO implements IUserDAO {

    private static final Logger logger = LogManager.getLogger(UserDAO.class);

//    @Override
//    public List<User> findAllUsers() throws DBException {
//
//        List<User> allUsersList = new ArrayList<>();
//        Connection con = null;
//        Statement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = ConnectionPool.getInstance().getConnection();
//            stmt = con.createStatement();
//            rs = stmt.executeQuery(DBConstatns.SQL_GET_ALL_USERS);
//            while (rs.next()) {
//                User user = mapResultSetToUser(rs);
//
//                allUsersList.add(user);
//            }
//        } catch (SQLException e) {
//            throw new DBException("Can't get all users", e);
//        } finally {
//            close(con);
//            close(stmt);
//            close(rs);
//        }
//
//        return allUsersList;
//    }
//
//    @Override
//    public void createUser(User user) throws DBException {
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//
//        try {
//            con = ConnectionPool.getInstance().getConnection();
//            con.setAutoCommit(false);
//
//            pstmt = con.prepareStatement(DBConstatns.SQL_INSERT_USER);
//            pstmt.setString(1, user.getFirstName());
//            pstmt.setString(2, user.getLastName());
//            pstmt.setString(3, user.getEmail());
//            pstmt.setString(4, user.getPassword());
//            pstmt.setString(5, user.getCountry());
//            pstmt.setString(6, user.getRole());
//
//            pstmt.executeUpdate();
//
//            con.commit();
//
//        } catch (SQLException e) {
//            rollback(con);
//            throw new DBException("Can't create a user", e);
//        } finally {
//            close(con);
//            close(pstmt);
//        }
//    }
//
//    @Override
//    public User findUserByEmail(String email) throws DBException {
//
//        User user = null;
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            con = ConnectionPool.getInstance().getConnection();
//            pstmt = con.prepareStatement(DBConstatns.SQL_GET_USER_BY_EMAIL);
//            pstmt.setString(1, email);
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                user = mapResultSetToUser(rs);
//            }
//
//        } catch (SQLException e) {
//            String errorMessage = "Can't get a user by email=" + email;
//            logger.error(errorMessage, e);
//            throw new DBException(errorMessage, e);
//        } finally {
//            close(con);
//            close(pstmt);
//            close(rs);
//        }
//
//        return user;
//    }
//
//    @Override
//    public List<User> findUsersByIds(List<Integer> ids) throws DBException {
//        List<User> users = new ArrayList<>();
//        String sql = String.format(DBConstatns.SQL_GET_USERS_BY_IDS, preparePlaceHolders(ids.size()));
//        Connection con = null;
//        PreparedStatement pStmt = null;
//
//        try {
//            con = ConnectionPool.getInstance().getConnection();
//            pStmt = con.prepareStatement(sql);
//
//            setValuesInPreparedStatement(pStmt, ids.toArray());
//
//            try (ResultSet resultSet = pStmt.executeQuery()) {
//                while (resultSet.next()) {
//                    users.add(mapResultSetToUser(resultSet));
//                }
//            }
//        } catch (SQLException e) {
//            throw new DBException("Can't get users by ids", e);
//        } finally {
//            close(con);
//            close(pStmt);
//        }
//
//        return users;
//    }
//
//    @Override
//    public User findUserById(int id) throws DBException {
//
//        User user = new User();
//        Connection con = null;
//        PreparedStatement pStmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = ConnectionPool.getInstance().getConnection();
//            pStmt = con.prepareStatement(DBConstatns.SQL_GET_USER_BY_ID);
//            pStmt.setInt(1, id);
//
//            rs = pStmt.executeQuery();
//            while (rs.next()) {
//                user.setId(id);
//                mapUserCommonProperties(user, rs);
//            }
//
//        } catch (SQLException e) {
//            String errorMessage = "Cannot get user by id=" + id;
//            logger.error(errorMessage, e);
//            throw new DBException(errorMessage, e);
//        } finally {
//            close(con);
//            close(pStmt);
//            close(rs);
//        }
//        return user;
//    }

    private static User mapResultSetToUser(ResultSet rs) throws SQLException{
        User user = new User();
        user.setId(rs.getInt("id"));
        mapUserCommonProperties(user, rs);

        return user;
    }

    private static void mapUserCommonProperties(User user, ResultSet rs) throws SQLException {
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setCountry(rs.getString("country"));
        user.setRole(rs.getString("role"));
    }

    private static void close(AutoCloseable itemToBeClosed) {
        close(itemToBeClosed, "DB close failed in UserDAO");
    }

    private static void rollback(Connection con) {
        rollback(con, "Cannot rollback transaction in UserDAO");
    }
}
