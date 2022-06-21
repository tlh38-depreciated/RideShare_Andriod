package Distance_Matrix;

import java.util.List;

public class DistanceMatrixResponseShell {

    private List<String> destination_addresses = null;
    private List<String> origin_addresses = null;
    private List<Row> rows = null;
    private String status;

    /**
     * No args constructor for use in serialization
     *
     */
    public DistanceMatrixResponseShell() {
    }

    /**
     *
     * @param destination_addresses
     * @param rows
     * @param origin_addresses
     * @param status
     */
    public DistanceMatrixResponseShell(List<String> destination_addresses, List<String> origin_addresses, List<Row> rows, String status) {
        super();
        this.destination_addresses = destination_addresses;
        this.origin_addresses = origin_addresses;
        this.rows = rows;
        this.status = status;
    }

    public List<String> getDestination_addresses() {
        return destination_addresses;
    }

    public void setDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
    }

    public DistanceMatrixResponseShell withDestination_addresses(List<String> destination_addresses) {
        this.destination_addresses = destination_addresses;
        return this;
    }

    public List<String> getOrigin_addresses() {
        return origin_addresses;
    }

    public void setOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
    }

    public DistanceMatrixResponseShell withOrigin_addresses(List<String> origin_addresses) {
        this.origin_addresses = origin_addresses;
        return this;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public DistanceMatrixResponseShell withRows(List<Row> rows) {
        this.rows = rows;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DistanceMatrixResponseShell withStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DistanceMatrixResponseShell.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("destination_addresses");
        sb.append('=');
        sb.append(((this.destination_addresses == null)?"<null>":this.destination_addresses));
        sb.append(',');
        sb.append("origin_addresses");
        sb.append('=');
        sb.append(((this.origin_addresses == null)?"<null>":this.origin_addresses));
        sb.append(',');
        sb.append("rows");
        sb.append('=');
        sb.append(((this.rows == null)?"<null>":this.rows));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
