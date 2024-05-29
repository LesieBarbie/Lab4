package operations;

import models.*;

public class AddMetroCardOperation extends CardOperation {
    private MetroCard crd;

    public AddMetroCardOperation() {
        crd = new MetroCard();
    }

    public MetroCard getCrd() { return crd; }
}
