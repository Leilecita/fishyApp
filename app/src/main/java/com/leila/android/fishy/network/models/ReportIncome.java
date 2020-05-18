package com.leila.android.fishy.network.models;

import java.util.List;

public class ReportIncome {

    public Long id;
    public String created;

    public Double amountByMonth;
    public List<Income> listByMonth;
    public List<Income> personalIncomesList;

    public ReportIncome(){

    }
}
