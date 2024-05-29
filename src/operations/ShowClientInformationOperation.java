package operations;

public class ShowClientInformationOperation extends CardOperation {
    private String serNum;

    public ShowClientInformationOperation(String serNum) {
        this.serNum = serNum;
    }

    public ShowClientInformationOperation() {}

    public String getSerNum() { return serNum; }
    public void setSerNum(String serNum) { this.serNum = serNum; }
}
