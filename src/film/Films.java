package film;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Ensemble de méthodes de classe utilitaires pour la projection (sur System.in)
 * et la sauvegarde (dans un fichier) d'un film. Le format de fichier employé
 * pour la sauvegarde est compatible avec le player graphique.
 */
public class Films {
	/**
	 * Projette un film dans sa totalité sur System.in. Attention, la méthode ne
	 * se termine pas si le film est infini.
	 * 
	 * @param f
	 *            Le film devant être projeté.
	 */
	public static void projeter(Film f) {
		char[][] écran = getEcran(f);
		while (f.suivante(écran)) {
			System.out.println(toString(écran));
			pause(1. / 12);
			effacer(écran);
		}
	}

	/**
	 * Sauvegarder un film dans un fichier.
	 * 
	 * @param f
	 *            Le film à sauvegarder.
	 * @param nom
	 *            le nom du fichier où sauvegarder le film.
	 * @throws FileNotFoundException
	 *             Si le nom du fichier ne permet pas de le créer.
	 */
	public static void sauvegarder(Film f, String nom) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(nom)) {
			char[][] écran = getEcran(f);
			out.println(f.largeur() + " " + f.hauteur());
			while (f.suivante(écran)) {
				out.println(toString(écran));
				out.println("\\newframe");
				effacer(écran);
			}
		}
	}

	/**
	 * Construit un écran adapté à la projection d'un film.
	 * 
	 * @param f
	 *            Le film pour lequel un écran doit être constuit.
	 * @return L'écran adapté au film.
	 */
	public static char[][] getEcran(Film f) {
		char[][] écran = new char[f.hauteur()][f.largeur()];
		effacer(écran);
		return écran;
	}

	/**
	 * Efface un écran.
	 * 
	 * @param écran
	 *            L'écran à effacer
	 */
	public static void effacer(char[][] écran) {
		for (char[] ligne : écran)
			Arrays.fill(ligne, ' ');
	}

	/**
	 * Convertit en chaine de caractère un écran.
	 * 
	 * @param écran
	 *            L'écran à convertir
	 * @return La chaine correspondante à l'écran.
	 */
	public static String toString(char[][] écran) {
		StringBuilder s = new StringBuilder();
		for (char[] ligne : écran)
			s.append(new String(ligne) + System.lineSeparator());
		return s.toString();
	}

	/**
	 * Endort le programme pendant un temps donné.
	 * 
	 * @param d
	 *            Le temps d'endormissement exprimé en seconde.
	 */
	public static void pause(double d) {
		try {
			Thread.sleep((long) (d * 1000));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// Cette classe n'a pas vocation à être instanciée car elle ne contient que
	// des méthodes de classe (i.e. statiques).
	// Introduire un constructeur privé interdit toute instanciation (en dehors
	// de la classe elle même).
	private Films() {
	}
}
