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
public class Status {

    public Long id;
    public Template template;
    public String code;
    public String name;
    public String desc;
    public StatusStage lifecyclePhase;
    public Map<String, Transition> outcomes;

    public static Map<Long, Status> loadStatuses(Connection connection, Long projectId, Map<Long, Template> templates) {
        ArgumentsChecker.notNull(new Argument("connection", connection), new Argument("projectId", projectId));
        Map<Long, Status> result = null;
        String statusesQuery = "select s.ID, s.TEMPLATE_ID, s.CODE, s.NAME, s.DESCRIPTION, ss.CODE as STAGE_CODE from STATUS s left join STATUS_STAGE ss on s.STATUS_STAGE_ID = ss.ID inner join TEMPLATE t on s.TEMPLATE_ID = t.ID where t.PROJECT_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(statusesQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);) {
            statement.setLong(1, projectId);
            ResultSet resultSet = statement.executeQuery();
            boolean notEmpty = resultSet.last();
            if (notEmpty) {
                int count = resultSet.getRow();
                resultSet.beforeFirst();
                result = new HashMap<>(count * 2);
                while (resultSet.next()) {
                    Status status = new Status();
                    status.id = resultSet.getLong("ID");
                    long templateId = resultSet.getLong("TEMPLATE_ID");
                    if (templates.containsKey(templateId)) {
                        status.template = templates.get(templateId);
                        status.code = resultSet.getString("CODE");
                        status.name = resultSet.getString("NAME");
                        status.desc = resultSet.getString("DESCRIPTION");
                        String stageCode = resultSet.getString("STAGE_CODE");
                        if (stageCode != null) {
                            status.lifecyclePhase = StatusStage.valueOf(stageCode);
                            result.put(status.id, status);
                        } else {
                            throw new IllegalStateException("Status{id=" + status.id + "}'s type doesn't have code!");
                        }
                    } else {
                        throw new IllegalArgumentException("Status{id=" + status.id + "}'s template isn't loaded!");
                    }
                }
            } else {
                result = new HashMap<>();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void gatherStatusesOutcomes(Map<Long, Transition> transitions) {
        for (Transition transition : transitions.values()) {
            Status srcStatus = transition.original;
            Status destStatus = transition.outcome;
            if (srcStatus.outcomes == null) {
                srcStatus.outcomes = new HashMap<>();
            }
            srcStatus.outcomes.put(destStatus.code, transition);
        }
    }

    public Status findStatus(String code) {
        Status result = null;
        if (code.equals(code)) {
            result = this;
        } else {
            for (Transition transition : outcomes.values()) {
                result = transition.outcome.findStatus(code);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        return (this.id != null ? this.id.hashCode() : super.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        boolean equality = false;
        if (obj instanceof Status) {
            final Status other = (Status) obj;
            if (this.id != null) {
                equality = this.id.equals(other.id);
            } else {
                equality = super.equals(obj);
            }
        }
        return equality;
    }

    @Override
    public String toString() {
        return "Status{" + "code=" + code + '}' + " of template{" + "code=" + template.code + "}";
    }
}
