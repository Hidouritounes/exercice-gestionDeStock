package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import Client.Client;

public class MainStock {

	static HashMap<Integer, Set<Integer>> moisParAnnee = new HashMap<Integer, Set<Integer>>();

	static List<Client> listeClients = new ArrayList<Client>();

	private static void GrouperAchatsClientsParDate() {
		for (int idexClient = 0; idexClient < listeClients.size(); idexClient++) {

			listeClients.get(idexClient).grouperProduitsClientParDateAchat();

		}

	}

	private static void etablirFactureClientsParDate() {
		for (int idexClient = 0; idexClient < listeClients.size(); idexClient++) {

			listeClients.get(idexClient).etablirFactureParDate();

		}

	}

	private static Client RetournerClient(String idClient) {
		for (int idexClient = 0; idexClient < listeClients.size(); idexClient++) {

			if (listeClients.get(idexClient).getID().equals(idClient)) {
				return listeClients.get(idexClient);
			}

		}
		return null;

	}

	private static void initialiserDossier(String chemin) throws IOException {

		Path dirToDelete = Paths.get(chemin);

		try {
			// Delete all contents of the directory including subdirectories
			Files.walk(dirToDelete).sorted(Comparator.reverseOrder()) // Important to delete contents before the
																		// directory itself
					.map(Path::toFile).forEach(file -> {
						if (!file.delete()) {
							System.err.println("Failed to delete " + file.getAbsolutePath().toString());
						} else {
							System.out.println("Deleted: " + file.getAbsolutePath());
						}
					});
			System.out.println("All contents have been deleted.");
		} catch (IOException e) {
			System.err.println("Error occurred: " + e.getMessage());
		}

	}

	public static void main(String[] args) throws IOException {

		String DOSSIER_CHEMIN_FACTURE = "C:/Users/z00409zx/OneDrive - Siemens AG/projets_java/gestionDeStock/factures/";

		initialiserDossier(DOSSIER_CHEMIN_FACTURE);

		Scanner lecteurTxt = null;

		try {
			lecteurTxt = new Scanner(new File("inputs.txt"));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		lecteurTxt.nextLine();// skip premiere ligne

		while (lecteurTxt.hasNextLine()) {

			String line = lecteurTxt.nextLine();
			String[] data = line.split(";");
			String idClient = data[0];
			String nomProduit = data[1];
			BigDecimal quantiteProduit = new BigDecimal(data[2]);
			BigDecimal pu = new BigDecimal(data[3]);
			String dateAchat = data[4];
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

			LocalDate date = LocalDate.parse(dateAchat, formatter);
			int UnMois = date.getMonthValue();
			int annee = date.getYear();

			String dossierNomAnneePuisMois = DOSSIER_CHEMIN_FACTURE
					.concat(String.valueOf(annee) + "/" + String.valueOf(UnMois));

			Files.createDirectories(Paths.get(dossierNomAnneePuisMois));

			Client client = RetournerClient(idClient);// fonction qui retourne la référence du client ou null si il //
														// n'est pas encore créé
			if (client == null) {
				client = new Client();
				client.setID(idClient);
				listeClients.add(client);
			}

			client.ajouterProduitAlisteAchatClient(nomProduit, quantiteProduit, pu, date);
		}

		for (Integer name : moisParAnnee.keySet()) {
			String key = name.toString();
			String value = moisParAnnee.get(name).toString();
			System.out.println("key " + key + " " + "value " + value);
		}

		GrouperAchatsClientsParDate();
		etablirFactureClientsParDate();

	}

}
