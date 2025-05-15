package mathematik;

    /**
     * Ein mathematisches Intervall auf einer linear geordneten Menge T.
     *
     * @param <T> der Typ der Intervallgrenzen;
     * muss Comparable sein,damit sich z.B. Date, Time und Timestamp in einer
     * gemeinsamen Ordnung vergleichen lassen.
     */
    public class Intervall <T extends Comparable<? super T>>{

        private T untergrenze;
        private T obergrenze;

        /**
         *
         * @param untergrenze wird die Untergrenze über Setter gesetzt
         * @param obergrenze wird die Obergrenze über Setter gesetzt
         */
        public Intervall(T untergrenze, T obergrenze){
            setUntergrenze(untergrenze);
            setObergrenze(obergrenze);
        }

        // region SETTER

        /**
         * Setzt this.untergrenze auf den Wert des parameters
         * @param untergrenze wird this.untergrenze gesetzt
         */
        private void setUntergrenze(T untergrenze) {
            this.untergrenze = untergrenze;
        }

        /**
         * Setzt this.untergrenze auf den Wert des parameters
         * @param obergrenze wird this.obergrenze gesetzt
         */
        private void setObergrenze(T obergrenze) {
            this.obergrenze = obergrenze;
        }
        // endregion

        // region GETTER
        public T getUntergrenze() {
            return untergrenze;
        }
        public T getObergrenze() {
            return obergrenze;
        }
        // endregion

        /**
         * Prüft ob Obergrenze {@literal <} Untergrenze und somit das Intervall leer ist
         * @return true wenn ja und false wenn nein
         */
        public boolean isLeer() {
            return untergrenze.compareTo(obergrenze) > 0;
        }

        /**
         *
         * Intervalles ist und kleiner als die obere.
         * @param wert wert im Intervall enthalten ist, d.h. ob wert größer als die untere Grenze des
         * @return true wenn wert im Intervall enthalten ist
         */
        public <E extends T> boolean enthaelt(E wert){
            return wert.compareTo(untergrenze) >= 0 && wert.compareTo(obergrenze) <= 0;
        }

        /**
         * bildet ein Schnittmengen-Intervall von this und anderes.
         * @param anderes, mit dem der Schnitt gebildet werden soll
         * @return den Schnittmengen-Intervall von this und anderes.
         */
        public <A extends T> Intervall<T> schnitt(Intervall<A> anderes){
            T untergrenzeNeu = untergrenze.compareTo(anderes.getUntergrenze()) < 0 ? untergrenze : anderes.getUntergrenze();
            T obergrenzeNeu = obergrenze.compareTo(anderes.getObergrenze()) > 0 ? obergrenze : anderes.getObergrenze();
            return new Intervall<>(untergrenzeNeu, obergrenzeNeu);
        }

        /**
         *
         * @return String Repräsentation von Untergrenze und Obergrenze
         */
        @Override
        public String toString() {
            return "[" + untergrenze + " … " + obergrenze + "]";
        }
    }
