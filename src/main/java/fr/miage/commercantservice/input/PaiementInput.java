package fr.miage.commercantservice.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PaiementInput {

    @NotNull
    @Min(0)
    private double montant;

    @NotNull
    private String pays;

    @NotNull
    private String ibanCrediteur;

    @NotNull
    private double taux;

    @NotNull
    @Pattern(regexp = "([0-9]{16})")
    private String numCarte;

    @NotNull
    @Pattern(regexp = "([0-9]{3})")
    private String cryptoCarte;

    @NotNull
    @Size(min = 3)
    private String nomUser;

    @NotNull
    private String label;

    @NotNull
    private String categ;

    public PaiementInput(String label){
        this.label = label;
    }
}
