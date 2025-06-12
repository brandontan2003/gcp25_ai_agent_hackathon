package gcp25.dto;


import java.io.Serializable;
import java.util.Objects;

public class AgentToolResponse implements Serializable {

    private String status;
    private AgentToolMessage result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AgentToolMessage getResult() {
        return result;
    }

    public void setResult(AgentToolMessage result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentToolResponse response = (AgentToolResponse) o;
        return Objects.equals(status, response.status) && Objects.equals(result, response.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, result);
    }

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", result=" + result +
                '}';
    }
}
