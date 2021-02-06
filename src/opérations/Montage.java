package opérations;

import film.Film;

import java.util.Arrays;

public interface Montage {
	char ENCADREMENT = '*';

	/**
	 * Méthode permettant d'encadrer un film.
	 *
	 * @param f Le film à encadrer
	 * @return Le film encadré par 4 lignes d'étoiles ('*').
	 */
	static Film encadrer(Film f) {
		return new Film() {

			@Override
			public int hauteur() {
				return f.hauteur() + 2;
			}

			@Override
			public int largeur() {
				return f.largeur() + 2;
			}

			@Override
			public boolean suivante(char[][] écran) {
				char[][] écranOrigine = new char[f.hauteur()][f.largeur()];
				boolean res = f.suivante(écranOrigine);

				for (int i = 0; i < écran.length; i++) {
					if (i == 0 || i == écran.length - 1) {
						Arrays.fill(écran[i], ENCADREMENT);
					} else {
						écran[i][0] = ENCADREMENT;
						écran[i][écran[i].length - 1] = ENCADREMENT;
					}
				}

				for (int i = 1; i <= écranOrigine.length; i++) {
					System.arraycopy(écranOrigine[i - 1], 0, écran[i],
					                 1, écranOrigine[i - 1].length);
				}

				return res;
			}

			@Override
			public void rembobiner() {
				f.rembobiner();
			}
		};
	}

	/**
	 * Méthode permettant de coller un film à la fin d'un autre.
	 *
	 * À noter : Si les deux films n'ont pas la même taille, le film résultant
	 * du collage doit être assez grand pour pouvoir contenir chacun des films.
	 *
	 * @param f1 Le film auquel on doit coller le second
	 * @param f2 Le film que l'on doit coller à la fin du premier
	 * @return La séquence des deux films.
	 */
	static Film coller(Film f1, Film f2) {
		return new Film() {
			boolean premierFini = false;

			@Override
			public int hauteur() {
				return Math.max(f1.hauteur(), f2.hauteur());
			}

			@Override
			public int largeur() {
				return Math.max(f1.largeur(), f2.largeur());
			}

			@Override
			public boolean suivante(char[][] écran) {
				boolean res = false;

				if (!premierFini) {
					res = f1.suivante(écran);
					premierFini = !res;
					if (premierFini) {
						f1.rembobiner();
					}
				}
				if (premierFini) {
					res = f2.suivante(écran);
				}

				return res;
			}

			@Override
			public void rembobiner() {
				if (!premierFini)
					f1.rembobiner();
				f2.rembobiner();
				premierFini = false;
			}
		};
	}

	/**
	 * Méthode permettant l'extraction d'un film à partir d'une image de début
	 * jusqu'à une image de fin.
	 *
	 * À noter : si le numéro de la dernière image d'un extrait de film est
	 * inférieur au numéro de la première image, l'extrait doit être un film
	 * vide.
	 *
	 * @param f     Le film dont on souhaite réaliser un extrait
	 * @param début Le numéro de la première image de l'extrait
	 * @param fin   Le numéro de la dernière image de l'extrait
	 * @return L'extrait désigné par les numéros de la première et la dernière
	 * image de l'extrait.
	 */
	static Film extraire(Film f, int début, int fin) {
		return new Film() {
			int cpt = 0;

			@Override
			public int hauteur() {
				return f.hauteur();
			}

			@Override
			public int largeur() {
				return f.largeur();
			}

			@Override
			public boolean suivante(char[][] écran) {
				if (fin < début) return false;

				while (cpt < début) {
					f.suivante(écran);
					cpt++;
				}

				if (cpt++ <= fin) {
					return f.suivante(écran);
				}
				return false;
			}

			@Override
			public void rembobiner() {
				f.rembobiner();
				cpt = 0;
			}
		};
	}

	/**
	 * Méthode permettant d'incruster un film dans un film. Le point
	 * d'incrustation est désigné par les numéros de ligne et de colonne que
	 * doit prendre le coin supérieur gauche du film devant être incrusté dans
	 * les images du film où il est incrusté.
	 *
	 * À noter : Si les images incrustées dépassent les images dans lesquelles
	 * elles sont incrustées, les images incrustées doivent automatiquement être
	 * tronquées de manière à tenir sur l'écran du film où elles sont incrustées
	 *
	 * @param f1         Le film dans lequel on incruste le second
	 * @param f2         Le film à incruster dans le premier
	 * @param ligne      Le numéro de ligne où incruster le film
	 * @param colonne    Le numéro de colonne où incruster le film
	 * @return Un film incrusté dans un autre film.
	 */
	static Film incruster(Film f1, Film f2, int ligne, int colonne) {
		if (ligne < 0)
			ligne = 0;
		if (colonne < 0)
			colonne = 0;

		final int finalLigne = ligne;
		final int finalColonne = colonne;

		return new Film() {

			@Override
			public int hauteur() {
				return f1.hauteur();
			}

			@Override
			public int largeur() {
				return f1.largeur();
			}

			@Override
			public boolean suivante(char[][] écran) {
				char[][] écranIncrusté = new char[f2.hauteur()][f2.largeur()];

				boolean res = f1.suivante(écran);

				if (f2.suivante(écranIncrusté)) {
					for (int i = 0; i < écranIncrusté.length; i++) {
						for (int j = 0; j < écranIncrusté[i].length; j++) {
							if ((i + finalLigne) >= 0 &&
							    (i + finalLigne) < écran.length &&
							    (j + finalColonne) >= 0 &&
							    (j + finalColonne) < (écran[i + finalLigne].length))

								écran[i + finalLigne][j + finalColonne] =
										écranIncrusté[i][j];
						}
					}
				}

				return res;
			}

			@Override
			public void rembobiner() {
				f1.rembobiner();
				f2.rembobiner();
			}
		};
	}

	/**
	 * Répète un film un nombre donné de fois.
	 *
	 * À noter : répéter un film un nombre de fois inférieur à 1 doit retourner
	 * un film vide.
	 *
	 * @param f             Le film à répéter
	 * @param nbRépétitions Le nombre de fois que le film doit être répété
	 * @return Le film une fois répété le nombre de fois demandé.
	 */
	static Film répéter(Film f, int nbRépétitions) {
		return new Film() {
			int cpt = 0;

			@Override
			public int hauteur() {
				return f.hauteur();
			}

			@Override
			public int largeur() {
				return f.largeur();
			}

			@Override
			public boolean suivante(char[][] écran) {
				if (cpt < nbRépétitions) {
					boolean res = f.suivante(écran);
					if (!res) {
						cpt++;
						f.rembobiner();
						if (cpt < nbRépétitions) res = f.suivante(écran);
					}
					return res;
				}
				return false;
			}

			@Override
			public void rembobiner() {
				f.rembobiner();
				cpt = 0;
			}
		};
	}
}