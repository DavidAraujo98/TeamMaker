public enum Graduacao {
    COMANDANTE("Comandante"), COMANDANTE2("2 Comandante"), ADJUNTO("Adjunto"), CHEFE("Chefe"), SUBCHEFE("Sub-Chefe"),
    BOMBEIRO1("Bombeiro 1"), BOMBEIRO2("Bombeiro 2"), BOMBEIRO3("Bombeiro 3"), ESTAGIARIO("Estagiario");

    private String x = null;

    public String getValue() {
        return this.x;
    }

    private Graduacao(String x) {
        this.x = x;
    }
}