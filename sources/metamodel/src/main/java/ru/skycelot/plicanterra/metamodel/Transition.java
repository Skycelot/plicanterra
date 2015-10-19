package ru.skycelot.plicanterra.metamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import ru.skycelot.plicanterra.util.Argument;
import ru.skycelot.plicanterra.util.ArgumentsChecker;

/**
 *
 */
public class Transition {

    public Long id;
    public Status original;
    public Status outcome;
    public Map<String, Role> allowedFor;

    public static Map<Long, Transition> loadTransitions(Connection connection, Long projectId, Map<Long, Status> statuses) {
        ArgumentsChecker.notNull(new Argument("connection", connection), new Argument("projectId", projectId));
        String transitionsQuery = "select tr.ID, tr.SOURCE_STATUS_ID, tr.DESTINATION_STATUS_ID from TRANSITION tr inner join STATUS s1 on tr.SOURCE_STATUS_ID = s1.ID inner join STATUS s2 on tr.DESTINATION_STATUS_ID = s2.ID inner join TEMPLATE t1 on s1.TEMPLATE_ID = t1.ID inner join TEMPLATE t2 on s2.TEMPLATE_ID = t2.ID where t1.PROJECT_ID = ? and t2.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(transitionsQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            statement.setLong(2, projectId);
            ResultSet resultSet = statement.executeQuery();
            Map<Long, Transition> result;
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>((int) (count / 0.75) + 100);
                while (resultSet.next()) {
                    Transition transition = new Transition();
                    transition.id = resultSet.getLong("ID");
                    long sourceStatusId = resultSet.getLong("SOURCE_STATUS_ID");
                    if (statuses.containsKey(sourceStatusId)) {
                        transition.original = statuses.get(sourceStatusId);
                        long destinationStatusId = resultSet.getLong("DESTINATION_STATUS_ID");
                        if (statuses.containsKey(destinationStatusId)) {
                            transition.outcome = statuses.get(destinationStatusId);
                            result.put(transition.id, transition);
                        } else {
                            throw new IllegalStateException("Transition{id=" + transition.id + "}'s destination Status{id=" + destinationStatusId + "} is not loaded!");
                        }
                    } else {
                        throw new IllegalStateException("Transition{id=" + transition.id + "}'s source Status{id=" + sourceStatusId + "} is not loaded!");
                    }
                }
            } else {
                result = new HashMap<>();
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadRolePermissions(Connection connection, Long projectId, Map<Long, Transition> transitions, Map<Long, Role> roles) {
        String transitionsQuery = "select tp.TRANSITION_ID, tp.ROLE_ID from TRANSITION_PERMISSIONS tp inner join TRANSITION t on tp.TRANSITION_ID = t.ID inner join STATUS sd on t.DESTINATION_STATUS_ID = sd.ID inner join STATUS so on t.DESTINATION_STATUS_ID = so.ID inner join TEMPLATE tm on so.TEMPLATE_ID = tm.ID where tm.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(transitionsQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long transitionId = resultSet.getLong("TRANSITION_ID");
                long roleId = resultSet.getLong("ROLE_ID");
                if (transitions.containsKey(transitionId)) {
                    Transition transition = transitions.get(transitionId);
                    if (roles.containsKey(roleId)) {
                        Role role = roles.get(roleId);
                        if (transition.allowedFor == null) {
                            transition.allowedFor = new HashMap<>();
                        }
                        transition.allowedFor.put(role.code, role);
                    } else {
                        throw new IllegalStateException("TransitionPermission{transitionId=" + transitionId + ",roleId=" + roleId + "}'s role isn't loaded!");
                    }
                } else {
                    throw new IllegalStateException("TransitionPermission{transitionId=" + transitionId + ",roleId=" + roleId + "}'s transition isn't loaded!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return (this.original != null && this.outcome != null) ? (this.original.hashCode() + this.outcome.hashCode()) : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Transition) {
            Transition other = (Transition) obj;
            if (this.original != null && this.outcome != null) {
                equality = this.original.equals(other.original) && this.outcome.equals(other.outcome);
            } else {
                super.equals(obj);
            }
        }
        return equality;
    }
}
