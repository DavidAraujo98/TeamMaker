public enum Quadro {

    ATIVO("Ativo"), COMANDO("Comando"), HONRA("Honra");

    private String x = null;

    public String getValue() {
        return this.x;
    }

    private Quadro(String x) {
        this.x = x;
    }

}