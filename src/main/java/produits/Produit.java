package produits;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Produit {

	private String nom;

	private BigDecimal quantite;

	private BigDecimal prixUnitaire;

	private LocalDate dateAchat;

	private BigDecimal totalAchatProduit;

	public String getNom() {
		return nom;
	}

	public void setNom(String nomParam) {
		nom = nomParam;
	}

	public BigDecimal getQuantite() {

		return quantite;
	}

	public void setQuantite(BigDecimal quantiteProduit) {
		quantite = quantiteProduit;
		quantite = quantite.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getPrixUnitaire() {

		return prixUnitaire;
	}

	public void setPrixUnitaire(BigDecimal pu) {
		prixUnitaire = pu;
		prixUnitaire = prixUnitaire.setScale(2, RoundingMode.HALF_UP);
	}

	public LocalDate getDateAchat() {
		return dateAchat;
	}

	public void setDateAchat(LocalDate dateAchatParam) {
		dateAchat = dateAchatParam;
	}

	public BigDecimal getTotalAchatProduit() {
		return totalAchatProduit;
	}

	public void setTotalAchatProduit() {
		totalAchatProduit = this.getQuantite().multiply(this.getPrixUnitaire());
		totalAchatProduit = totalAchatProduit.setScale(2, RoundingMode.HALF_UP);
	}

}
