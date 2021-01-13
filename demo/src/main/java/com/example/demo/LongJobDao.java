package com.example.demo;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.sql.*;
import java.util.concurrent.TimeUnit;

@Component
public class LongJobDao {

    @PersistenceContext
    private EntityManager entityManager;

    public LongJobDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public boolean doLongJob(int taskCount) {
        Assert.state(taskCount > 0, "Parameter taskCount cannot be 0");
//        Query query = entityManager.createNativeQuery("select long_running(:taskCount)");
//        query.setParameter("taskCount", taskCount);
//        query.getSingleResult();
//        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery("long_running");
//        storedProcedureQuery.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
//        storedProcedureQuery.execute();

        final Boolean[] result = new Boolean[1];


        Session session = entityManager.unwrap(Session.class);
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                PreparedStatement st = connection.prepareStatement("select long_running(?)");
                st.setInt(1, taskCount);

                AdHocWarningReader warningReader = new AdHocWarningReader(st);
                warningReader.start();

                ResultSet rs = st.executeQuery();

                warningReader.interrupt();
                try {
                    warningReader.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(rs.next()){
                    result[0] = rs.getBoolean(1);
                }
            }
        });

        return result[0];
    }

    public static class AdHocWarningReader extends Thread {
        private PreparedStatement preparedStatement;
        private boolean shouldStop;
        private SQLWarning lastWarning;

        public AdHocWarningReader(PreparedStatement preparedStatement) {
            super();
            this.preparedStatement = preparedStatement;
        }

        @Override
        public void run() {
            while(true) {
                get();
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (shouldStop) {
                    get();
                    break;
                }
            }
        }

        private void get() {
            try {
                SQLWarning warning;
                if (lastWarning == null) {
                    warning = preparedStatement.getWarnings();
                } else {
                    warning = lastWarning.getNextWarning();
                }
                while (warning != null) {
                    lastWarning = warning;
                    System.out.println(warning.getMessage());
                    warning = warning.getNextWarning();
                }
            } catch(SQLException e) {
                //
            }
        }

        public void interrupt() {
            shouldStop = true;
        }
    }
}
