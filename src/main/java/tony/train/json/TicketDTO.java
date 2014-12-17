package tony.train.json;

import org.codehaus.jackson.annotate.JsonIgnore;

public class TicketDTO {
    QueryLeftNewDTO queryLeftNewDTO;

    String secretStr;

    String buttonTextInfo;

    @JsonIgnore
    /**
     * 3 --yw, 1--yz, 4--rw, O --erdeng, M --yideng
     */
    String seatType;

    public QueryLeftNewDTO getQueryLeftNewDTO() {
        return queryLeftNewDTO;
    }

    public void setQueryLeftNewDTO(QueryLeftNewDTO queryLeftNewDTO) {
        this.queryLeftNewDTO = queryLeftNewDTO;
    }

    public String getSecretStr() {
        return secretStr;
    }

    public void setSecretStr(String secretStr) {
        this.secretStr = secretStr;
    }

    public String getButtonTextInfo() {
        return buttonTextInfo;
    }

    public void setButtonTextInfo(String buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

}
