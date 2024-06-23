package Client;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import produits.Produit;

public class Client {

	private String ID;
	private List<Produit> achats = new ArrayList<>();
	String cheminFacture;
	private HashMap<LocalDate, List<Produit>> produitParDateAchat = new HashMap<LocalDate, List<Produit>>();

	private static String DOSSIER_CHEMIN_FACTURE = "C:/Users/z00409zx/OneDrive - Siemens AG/projets_java/gestionDeStock/factures/";

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public List<Produit> getAchats() {
		return achats;
	}

//	public void ajouterProduits(List<Produit> achats) {
//		this.achats.addAll(achats);
//	}

	public void ajouterProduit(Produit p) {
		this.achats.add(p);
	}

	public void afficherAchatsClient() {

		for (int indexProduit = 0; indexProduit < this.getAchats().size(); indexProduit++) {

			Produit p = this.getAchats().get(indexProduit);

			System.out.println("le prix unitaire du produit : " + p.getNom() + " est " + p.getPrixUnitaire()
					+ " : quantité achetée  = " + p.getQuantite() + " date " + p.getDateAchat());

		}

	}

	public String getCheminFacture() {
		return DOSSIER_CHEMIN_FACTURE + this.ID.toString() + ".txt";
	}

	public void etablirFactureParDate() {

		for (Map.Entry<LocalDate, List<Produit>> entry : this.getProduitParDateAchat().entrySet()) {

			LocalDate date = entry.getKey();
			List<Produit> List = entry.getValue();

			try

			{
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu");
				int UnMois = date.getMonthValue();
				int annee = date.getYear();

				String directoryNameAnneePuisAnnee = DOSSIER_CHEMIN_FACTURE
						.concat(String.valueOf(annee) + "/" + String.valueOf(UnMois) + "/");
				String NomFactureParDate = directoryNameAnneePuisAnnee + ID.toString() + "_" + date.format(formatter)
						+ ".txt";
				processEcriture(date, List, formatter, NomFactureParDate);

			} catch (

			FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

	private void processEcriture(LocalDate date, List<Produit> List, DateTimeFormatter formatter,
			String NomFactureParDate) throws IOException {
		FileWriter writer = new FileWriter(NomFactureParDate);
		PrintWriter PrintWriter = new PrintWriter(writer);
		writer.append("facture du client : " + this.ID.toString() + " / Date : " + date.format(formatter));
		writer.append("\n");

		ecrireAchatsParDateDansFichier(writer, PrintWriter, List);

		writer.close();

		System.out.println("la facture de la date " + date.format(formatter) + " a été éditée dans le dossier : "
				+ DOSSIER_CHEMIN_FACTURE + " pour le client  : " + this.getID());
	}

	private void ecrireAchatsParDateDansFichier(FileWriter writer, PrintWriter PrintWriter, List<Produit> List)
			throws IOException {
		BigDecimal total_HT = new BigDecimal("0.00");

		BigDecimal total_TVA = new BigDecimal("0.00");

		for (Produit p : List) {
			writer.append("produit : " + p.getNom() + " : ");
			writer.append("pu : ");
			PrintWriter.printf("%.2f", p.getPrixUnitaire());
			writer.append(" : qt  = ");
			PrintWriter.printf("%.2f", p.getQuantite());
			writer.append(" : prix*quantié ");
			PrintWriter.printf("%.2f", p.getTotalAchatProduit());
			writer.append("\n");
			total_HT = total_HT.add(p.getTotalAchatProduit());
		}

		total_TVA = total_HT.multiply(new BigDecimal("1.2"));
		writer.append("total HT =  ");
		PrintWriter.printf("%.2f", total_HT);
		writer.append("\n");
		writer.append("total TVA =  ");
		PrintWriter.printf("%.2f", total_TVA);

	}

	public HashMap<LocalDate, List<Produit>> getProduitParDateAchat() {
		return produitParDateAchat;
	}

	public void grouperProduitsClientParDateAchat() {

		for (int indexProduit = 0; indexProduit < this.getAchats().size(); indexProduit++) {

			Produit p = this.getAchats().get(indexProduit);

			LocalDate dateAchatProduitcourant = p.getDateAchat();

			if (!produitParDateAchat.containsKey(dateAchatProduitcourant)) {
				List<Produit> listeProduitsParDate = new ArrayList<>();
				listeProduitsParDate.add(p);
				produitParDateAchat.put(dateAchatProduitcourant, listeProduitsParDate);
			} else {
				List<Produit> listeProduitsParDate = produitParDateAchat.get(dateAchatProduitcourant);
				listeProduitsParDate.add(p);
			}

		}

	}

	public void ajouterProduitAlisteAchatClient(String nomProduit, BigDecimal quantiteProduit, BigDecimal pu,
			LocalDate dateAchat) {
		Produit produit = new Produit();
		produit.setNom(nomProduit);
		produit.setQuantite(quantiteProduit);
		produit.setPrixUnitaire(pu);
		produit.setDateAchat(dateAchat);
		produit.setTotalAchatProduit();
		this.getAchats().add(produit);

	}

}
