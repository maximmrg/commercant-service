package fr.miage.commercantservice.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PaiementInput {

    private double montant;

    private String pays;

    private String accountCreditorIban;

    private double taux;

    private String numCarte;

    private Date expCarte;

    private String crypto;

    private String nomCarte;

    private String label;

    private String categ;

    public PaiementInput(String label){
        this.label = label;
    }
}
