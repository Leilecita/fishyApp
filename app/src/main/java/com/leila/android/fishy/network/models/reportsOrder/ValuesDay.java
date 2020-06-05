package com.leila.android.fishy.network.models.reportsOrder;

public class ValuesDay {
    public Double sumTot;

    public Double sumTotCard;
    public Double sumTotCash;
    public Double sumTotTrans;

    public Double sumPendient;
    public Double sumDone;

    public ValuesDay(Double sumDone,Double sumPendient, Double sumTot ){
        this.sumDone=sumDone;
        this.sumPendient=sumPendient;
        this.sumTot=sumTot;
    }
}
