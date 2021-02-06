package film;

/**
 * Interface devant être respectée par tout film.
 * 
 * Attention, pour qu'un film puisse être projeté ou sauvergardé (voir la classe
 * film.Outils), il doit être composé d'un nombre fini d'images. En conséquence,
 * la méthode suivante() doit nécessairement retourner false au bout d'un nombre
 * fini d'appels.
 */
public interface Film {
	/**
	 * Indique la hauteur des images de ce film (en nombre de caractères).
	 * 
	 * @return Hauteur minimale de l'écran pour pouvoir afficher les images de
	 *         ce film.
	 */
	int hauteur();

	/**
	 * Indique la largeur des images de ce film (en nombre de caractères).
	 * 
	 * @return largeur minimale de l'écran pour pouvoir afficher les images de
	 *         ce film.
	 */
	int largeur();

	/**
	 * Obtenir l'image suivante (s'il y en a une).
	 * 
	 * @param écran
	 *            L'écran où afficher l'image
	 * @return vrai Si l'image suivante a été affichée sur l'écran et faux si le
	 *         film est terminé
	 */
	boolean suivante(char[][] écran);

	/**
	 * Rembobine le film en permettant de rejouer le film dans sa totalité (via
	 * des appels successifs à la méthode suivante()).
	 */
	void rembobiner();
}
