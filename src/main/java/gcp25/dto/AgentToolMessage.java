package gcp25.dto;

import java.io.Serializable;
import java.util.Objects;

public class AgentToolMessage implements Serializable {

    private String report;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentToolMessage that = (AgentToolMessage) o;
        return Objects.equals(report, that.report);
    }

    @Override
    public int hashCode() {
        return Objects.hash(report);
    }

    @Override
    public String toString() {
        return "{" +
                "report='" + report + '\'' +
                '}';
    }
}
